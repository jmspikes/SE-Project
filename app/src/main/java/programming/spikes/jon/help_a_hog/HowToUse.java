package programming.spikes.jon.help_a_hog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class HowToUse extends AppCompatActivity {

    TextView food, sport, weather, nearby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);

        food = findViewById(R.id.foodBody);
        food.setMovementMethod(new ScrollingMovementMethod());
        sport = findViewById(R.id.sportBody);
        sport.setMovementMethod(new ScrollingMovementMethod());
        weather = findViewById(R.id.weatherBody);
        weather.setMovementMethod(new ScrollingMovementMethod());
        nearby = findViewById(R.id.nearbyBody);
        nearby.setMovementMethod(new ScrollingMovementMethod());

    }
}
