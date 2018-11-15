package programming.spikes.jon.help_a_hog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;

import android.os.Handler;
import android.os.Looper;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
    HashMap<String, String> factsFromFile;
    HashMap<String, String> foodFromFile;
    boolean focusedUser = false;
    boolean debugging = false;
    MarkerOptions markerListener = null;
    CustomInfoWindow custom;

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
        readGPSFromFile();
        readFactsFromFile();
        readFoodFromFile();
        //gets users location and updates it, zooms camera to location
        startLocationUpdates();

    }


    //suppressing permission checks since permission has been granted in main activity
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //allows for clicking to see buildings at a new location
        mMap.setOnMapLongClickListener(this);
        googleMap.setMyLocationEnabled(true);
        //used to set camera to union initially
        LatLng union = new LatLng(36.068679, -94.175759);
        //moves camera to campus
        //10=city 15=streets 20=buildings
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(union, 15);
        mMap.moveCamera(cameraUpdate);
        custom = new CustomInfoWindow(this, gpsFromFile);
        mMap.setInfoWindowAdapter(custom);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener( ) {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent building = new Intent(getApplicationContext(), BuildingDisplay.class);
                building.putExtra("title", marker.getTitle());
                building.putExtra("facts", factsFromFile);
                building.putExtra("food", foodFromFile);
                startActivity(building);
            }});

        //printAllMarkers();
    }


    //used to set location on long click, for debugging
    @Override
    public void onMapLongClick(LatLng point){

        //resets map, needed to draw accurate markers
        mMap.clear();
        //updates latlng to long press location
        userLatLng = point;
        //places visual marker there
        MarkerOptions at = new MarkerOptions().position(userLatLng).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("manwalking",100,100))).title("User Location");
        markerListener = at;
        mMap.addMarker(at);
        //moves camera to users location

        Toast resetMsg = Toast.makeText(getApplicationContext(),"Tab blue marker to turn location tracking back on.", Toast.LENGTH_SHORT);
        resetMsg.show();
        //turns off updating user position automatically
        debugging = true;

        //listener to turn back on location checking
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            public boolean onMarkerClick(Marker marker){
                    //see's if the user has clicked on the blue marker, if they have clear map and turn back on location tracking
                    if(marker.getTitle().equals(markerListener.getTitle())){
                        debugging = false;
                        mMap.clear();
                    }
                    else
                    //shows the building info window
                        marker.showInfoWindow();
                    return true;
                }
            });

    }

    //for debugging purposes, will print all markers for verification
    void printAllMarkers(){

        Iterator it = gpsFromFile.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, LatLng> pair = (Map.Entry<String, LatLng>) it.next();
            mMap.addMarker(new MarkerOptions().position(pair.getValue()).title(pair.getKey()));
        }
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

    //solution from https://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
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
        }
        //iteartes over all buildings and puts a marker on the map
        Iterator it = gpsFromFile.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, LatLng> pair = (Map.Entry<String, LatLng>) it.next();

            if(Math.abs(pair.getValue().latitude-userLatLng.latitude) < 0.0009 &&
               Math.abs(pair.getValue().longitude-userLatLng.longitude) < 0.0009) {
                mMap.addMarker(new MarkerOptions().position(pair.getValue()).title(pair.getKey()));
            }
        }


    }
    void readGPSFromFile(){
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
            gpsFromFile.put(items.get(i), location);
        }

    }

    void readFactsFromFile(){

        String data = null;
        AssetManager am = this.getAssets();
        factsFromFile = new HashMap<>();
        try{
            InputStream is = am.open("facts.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            data = new String(buffer);
        } catch(Exception e){
            e.printStackTrace();
        }

        String lines[] = data.split("\\n");

        String building = "";
        int i = 0;
        while(i < lines.length){
            String fact = "";
            if(!lines[i].contains("\t")) {
                building = lines[i];
                i++;
            }
            //is a line with fact text
            while(lines[i].contains("\t")){
                fact += lines[i];
                i++;
                if(i == lines.length)
                    break;
            }
        building = building.replaceAll("(\r\t|\r|\t)", "");

        fact = fact.replaceAll("(\r\t|\r|\t)","");
        factsFromFile.put(building, fact);

        }
    }

    void readFoodFromFile() {

        String data = null;
        AssetManager am = this.getAssets();
        foodFromFile = new HashMap<>();
        try {
            InputStream is = am.open("food.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            data = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String lines[] = data.split("\\n");
        String building = "";
        int i = 0;
        while (i < lines.length) {
            String fact = "";
            if (!lines[i].contains("\t")) {
                building = lines[i];
                i++;
            }
            //is a line with fact text
            while (lines[i].contains("\t")) {
                fact += lines[i];
                i++;
                if (i == lines.length)
                    break;
            }
            building = building.replaceAll("(\r\t|\r|\t)", "");

            foodFromFile.put(building, fact);

        }
    }
}


