package programming.spikes.jon.help_a_hog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;

import android.os.Looper;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    //declarations
    private GoogleMap mMap;
    LatLng userLatLng;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1000;
    private long FASTEST_INTERVAL = 1000;
    SupportMapFragment mapFragment;
    HashMap<String, LatLng> gpsFromFile;
    boolean focusedUser = false;
    boolean debugging = false;

    //suppressing permission checks since permission has been granted in main activity
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //reads GPS coordinates from file
        readFromFile();
        //gets users location and updates it, zooms camera to location
        startLocationUpdates();

    }


    //suppressing permission checks since permission has been granted in main activity
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        googleMap.setMyLocationEnabled(true);
        //used to set camera to union initially
        LatLng union = new LatLng(36.068679, -94.175759);
        //moves camera to campus
        //10=city 15=streets 20=buildings
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(union, 15);
        mMap.moveCamera(cameraUpdate);

        /*
        //iteartes over all buildings and puts a marker on the map
        Iterator it = gpsFromFile.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, LatLng> pair = (Map.Entry<String, LatLng>) it.next();
            mMap.addMarker(new MarkerOptions().position(pair.getValue()).title(pair.getKey()));
        }
        */

    }

    //used to set location on long click, for debugging
    @Override
    public void onMapLongClick(LatLng point){
        mMap.clear();
        userLatLng = point;
        mMap.addMarker(new MarkerOptions().position(userLatLng).icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE)));

        debugging = true;
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


    public void onLocationChanged(Location location) {

        //updates users location
        if(!debugging)
        userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        //will move camera to user exactly once, used to give scale of reference for user by moving camera to them
        if(focusedUser == false) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatLng, 15);
            mMap.moveCamera(cameraUpdate);
            focusedUser = true;

            /*markerOptions.position(userLatLng).title("Test").snippet("This is a test").icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));
            CustomInfoWindow custom = new CustomInfoWindow(this);
            mMap.setInfoWindowAdapter(custom);
            Marker m = mMap.addMarker(markerOptions);
            m.setTag("tester");
            m.showInfoWindow();*/

        }

        //iteartes over all buildings and puts a marker on the map
        Iterator it = gpsFromFile.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, LatLng> pair = (Map.Entry<String, LatLng>) it.next();

            double v = Math.abs(pair.getValue().latitude-userLatLng.latitude);
            double y = Math.abs(pair.getValue().longitude-userLatLng.longitude);
            if(Math.abs(pair.getValue().latitude-userLatLng.latitude) < 0.0009 &&
               Math.abs(pair.getValue().longitude-userLatLng.longitude) < 0.0009)
                mMap.addMarker(new MarkerOptions().position(pair.getValue()).title(pair.getKey()));
            else
                Log.i("not", "in");
        }


    }
    void readFromFile(){
        String data = null;
        AssetManager am = this.getAssets();
        gpsFromFile = new HashMap<>();
        try{
            InputStream is = am.open("GPS_Mid.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            data = new String(buffer);
        } catch(Exception e){
            e.printStackTrace();
        }
        //format text to be able to be put into hashmap
        data = data.replaceAll("(\\r|\\t|)", "");
        String[] split = data.split("\n");
        ArrayList<String> items = new ArrayList<String>();
        for(int i = 0; i < split.length; i++){
            if(!split[i].isEmpty() && split[i].length() != 1)
                items.add(split[i]);
        }
        //takes formatted text and puts longlat as value and building name as key
        for(int i = 0; i < items.size()-1; i+=2){
            String lstring = items.get(i+1);
            String[] two = lstring.split("\\s+");
            LatLng location = new LatLng(Double.parseDouble(two[0]), Double.parseDouble(two[1]));
            Log.i("loc:",location.toString());
            gpsFromFile.put(items.get(i), location);
        }

    }

}


