package programming.spikes.jon.help_a_hog;

import android.content.Context;
import android.net.ConnectivityManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import programming.spikes.jon.help_a_hog.Weather;
import programming.spikes.jon.help_a_hog.WeatherLocation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.OnMapReadyCallback;

public class WeatherActivity extends FragmentActivity
{

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;

    private TextView hum;
    private ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String city = "London,UK";

        cityText =  findViewById(R.id.cityText);
        condDescr =  findViewById(R.id.condDescr);
        temp =  findViewById(R.id.temp);
        hum =  findViewById(R.id.hum);
        windSpeed =  findViewById(R.id.windSpeed);
        windDeg =  findViewById(R.id.windDeg);
        imgView =  findViewById(R.id.condIcon);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});
    }


    private class JSONWeatherTask extends AsyncTask<String, Void, Weather>
    {
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                //retrieve the icon
                weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }




        @Override
        protected void onPostExecute(Weather weather)
        {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescription() + ")");
            temp.setText("" + Math.round(weather.temperature.getTemp()) + "�F");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + "�");

        }
    }

}
