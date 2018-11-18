package programming.spikes.jon.help_a_hog;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    Button directions;
    Button nearby;
    Button food;
    Button sports;
    Button weather;
    HashMap<String, LatLng> gpsFromFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        //sets on click listeners for each button
        setOnClicks();
    }

    private void requestPermission(){ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1); }

    void setOnClicks() {

        //directions on click
        directions = findViewById(R.id.directions);
        directions.setOnClickListener(new View.OnClickListener() {

            //on click need to go to the Maps activity
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Directions", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        //nearby on click
        nearby = findViewById(R.id.nearby);
        nearby.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                Intent mapActivity = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(mapActivity);
            }
        });
        //food on click
        food = findViewById(R.id.fact2);
        food.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                Intent food = new Intent(getApplicationContext(), CampusFood.class);
                startActivity(food);
            }
        });

        sports = findViewById(R.id.sports);
        sports.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Sports", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        weather = findViewById(R.id.weather);
        weather.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Weather", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


}
