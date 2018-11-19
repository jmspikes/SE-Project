package programming.spikes.jon.help_a_hog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.android.PolyUtil;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class DirectionsDisplay extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng userLatLng;
    LatLng destination;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1000;
    private long FASTEST_INTERVAL = 1000;
    boolean focusedUser = false;
    private static final int overview = 0;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_display);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent get = getIntent();
        destination = get.getParcelableExtra("gps");


        startLocationUpdates();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        //mMap.setTrafficEnabled(false);
        LatLng union = new LatLng(36.068679, -94.175759);
        //moves camera to campus
        //10=city 15=streets 20=buildings
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(union, 15);
        mMap.moveCamera(cameraUpdate);

    }

    // Trigger new location updates at interval
    //https://github.com/codepath/android_guides/wiki/Retrieving-Location-with-LocationServices-API
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    //https://github.com/pjwelcome/GoogleMapsDirections/blob/master/app/src/main/java/com/multimeleon/android/googlemapsdirections/MapsActivity.java
    public void onLocationChanged(Location location) {

        userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        //will move camera to user exactly once, used to give scale of reference for user by moving camera to them
        if(focusedUser == false) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatLng, 15);
            mMap.moveCamera(cameraUpdate);
            focusedUser = true;
        }


        Geocoder geocoder;
        List<Address> userAddress;
        List<Address> destAddress;
        geocoder = new Geocoder(this, Locale.getDefault());


        setupGoogleMapScreenSettings(mMap);
        DirectionsResult results = null;
        try {

            userAddress = geocoder.getFromLocation(userLatLng.latitude, userLatLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            destAddress = geocoder.getFromLocation(destination.latitude, destination.longitude, 1);

            String userAdd = userAddress.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String userCity = userAddress.get(0).getLocality();
            String userState = userAddress.get(0).getAdminArea();
            String userCountry = userAddress.get(0).getCountryName();
            String userPostalCode = userAddress.get(0).getPostalCode();
            String knownName = userAddress.get(0).getFeatureName();

            String destAdd = destAddress.get(0).getAddressLine(0);
            String destCity = destAddress.get(0).getLocality();
            String destState = destAddress.get(0).getAdminArea();
            String destCountry = destAddress.get(0).getCountryName();
            String destPostalCode = destAddress.get(0).getPostalCode();
            String destName = destAddress.get(0).getFeatureName();

            String uLoc = userAdd + " " + userCity + " " + userState;
            String dLoc = destAdd + " " + destCity + " " + destState;

            results = getDirectionsDetails( uLoc ,dLoc,TravelMode.WALKING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (results != null) {
            addPolyline(results, mMap);
            positionCamera(results.routes[overview], mMap);
            addMarkersToMap(results, mMap);
        }


    }

    private DirectionsResult getDirectionsDetails(String origin, String destination, TravelMode mode) throws Exception {
        DateTime now = new DateTime();
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
    }

    private void setupGoogleMapScreenSettings(GoogleMap mMap) {
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[overview].legs[overview].startLocation.lat,results.routes[overview].legs[overview].startLocation.lng)).title(results.routes[overview].legs[overview].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[overview].legs[overview].endLocation.lat,results.routes[overview].legs[overview].endLocation.lng)).title(results.routes[overview].legs[overview].startAddress).snippet(getEndLocationTitle(results)));
    }

    private void positionCamera(DirectionsRoute route, GoogleMap mMap) {
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(route.legs[overview].startLocation.lat, route.legs[overview].startLocation.lng), 12));
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[overview].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.argb(85, 47, 170, 212)));
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  "Time: "+ results.routes[overview].legs[overview].duration.humanReadable + " Distance: " + results.routes[overview].legs[overview].distance.humanReadable;
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.directionsApiKey))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

}
