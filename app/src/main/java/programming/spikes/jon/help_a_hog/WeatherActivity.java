package programming.spikes.jon.help_a_hog;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class WeatherActivity extends AppCompatActivity {

    TextView cityField, detailsField, currentTemperatureField, humidity_field, wind_field, weatherIcon, weatherTitle,
    sunrise, sunset;
    ProgressBar loader;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1000;
    private long FASTEST_INTERVAL = 1000;
    LatLng userLatLng;
    Typeface weatherFont;
    String userCity = "Default City";
    String userState = "Default State";
    boolean titleSet = false;
    String OPEN_WEATHER_MAP_API = "c615a3f0b0187d384a08dbdd52d32caf";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherTitle = findViewById(R.id.weatherTitle);
        loader = findViewById(R.id.loader);
        cityField = findViewById(R.id.city_field);
        detailsField = findViewById(R.id.details_field);
        currentTemperatureField = findViewById(R.id.current_temperature_field);
        humidity_field = findViewById(R.id.humidity_field);
        wind_field = findViewById(R.id.wind_field);
        weatherIcon = findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon.setTypeface(weatherFont);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        //find users city for lookup
        startLocationUpdates();

    }

    //tutorial from https://androstock.com/tutorials/create-a-weather-app-on-android-android-studio.html
    public static String excuteGet(String targetURL)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("content-type", "application/json;  charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
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


    //https://github.com/pjwelcome/GoogleMapsDirections/blob/master/app/src/main/java/com/multimeleon/android/googlemapsdirections/MapsActivity.java
    public void onLocationChanged(Location location) {

        userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        Geocoder geocoder;
        List<Address> userAddress;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {

            userAddress = geocoder.getFromLocation(userLatLng.latitude, userLatLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            userCity = userAddress.get(0).getLocality();
            userState = userAddress.get(0).getAdminArea();
            String userCountry = userAddress.get(0).getCountryName();
            String userPostalCode = userAddress.get(0).getPostalCode();
            String knownName = userAddress.get(0).getFeatureName();

            if(titleSet == false){
                taskLoadUp(userCity + ", US");
                titleSet = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void taskLoadUp(String query) {

            DownloadWeather task = new DownloadWeather();
            task.execute(query);
    }

    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch(id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }

    class DownloadWeather extends AsyncTask< String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... args) {
            String xml = excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=imperial&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    JSONObject wind = json.getJSONObject("wind");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    cityField.setText(json.getString("name") + ", " + userState);
                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.0f", main.getDouble("temp")) + "°");
                    humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                    wind_field.setText("Wind: " + String.format("%.0f", wind.getDouble("speed")) + " mph");
                    String sunr = new SimpleDateFormat("hh:mm a").format(new Date(json.getJSONObject("sys").getInt("sunrise")*1000L));
                    sunrise.setText("Sunrise: " +sunr);
                    String suns = new SimpleDateFormat("hh:mm a").format(new Date(json.getJSONObject("sys").getInt("sunset")*1000L));
                    sunset.setText("Sunset: " +suns);

                    weatherIcon.setText(Html.fromHtml(setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));
                    loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }


        }

    }
}


