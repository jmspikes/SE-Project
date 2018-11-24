package programming.spikes.jon.help_a_hog;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.InputStream;
import android.content.Intent;

public class SportsActivity extends FragmentActivity {
    String items[] = new String [] {"Baseball", "Basketball (M)", "Basketball (W)",
    "Cross Country (M)", "Cross Country (W)", "Football", "Golf (M)", "Golf (W)", "Gymnastics",
    "Soccer", "Softball", "Swimming & Diving", "Tennis (M)", "Tennis (W)", "Track & Field (M)",
    "Track & Field (W)", "Volleyball"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent baseball = new Intent(getApplicationContext(), BaseballActivity.class);
                    startActivity(baseball);
                }
                if (position == 1) {
                    Intent basketballM = new Intent(getApplicationContext(), BasketballmActivity.class);
                    startActivity(basketballM);
                }
                if (position == 2) {
                    Intent basketballW = new Intent(getApplicationContext(), BasketballwActivity.class);
                    startActivity(basketballW);
                }
                if (position == 3) {
                    Intent crossCountryM = new Intent(getApplicationContext(), CrossCountrymActivity.class);
                    startActivity(crossCountryM);
                }
                if (position == 4) {
                    Intent crossCountryW = new Intent(getApplicationContext(), CrossCountrywActivity.class);
                    startActivity(crossCountryW);
                }
                if (position == 5) {
                    Intent football = new Intent(getApplicationContext(), FootballActivity.class);
                    startActivity(football);
                }
                if (position == 6) {
                    Intent golfM = new Intent(getApplicationContext(), GolfmActivity.class);
                    startActivity(golfM);
                }
                if (position == 7) {
                    Intent golfW = new Intent(getApplicationContext(), GolfwActivity.class);
                    startActivity(golfW);
                }
                if (position == 8) {
                    Intent gym = new Intent(getApplicationContext(), GymActivity.class);
                    startActivity(gym);
                }
                if (position == 9) {
                    Intent soccer = new Intent(getApplicationContext(), SoccerActivity.class);
                    startActivity(soccer);
                }
                if (position == 10) {
                    Intent softball = new Intent(getApplicationContext(), SoftballActivity.class);
                    startActivity(softball);
                }
                if (position == 11) {
                    Intent swimming = new Intent(getApplicationContext(), SwimmingActivity.class);
                    startActivity(swimming);
                }
                if (position == 12) {
                    Intent tennisM = new Intent(getApplicationContext(), TennismActivity.class);
                    startActivity(tennisM);
                }
                if (position == 13) {
                    Intent tennisW = new Intent(getApplicationContext(), TenniswActivity.class);
                    startActivity(tennisW);
                }
                if (position == 14) {
                    Intent trackFieldm = new Intent(getApplicationContext(), TrackFieldmActivity.class);
                    startActivity(trackFieldm);
                }
                if (position == 15) {
                    Intent trackFieldw = new Intent(getApplicationContext(), TrackFieldwActivity.class);
                    startActivity(trackFieldw);
                }
                if (position == 16) {
                    Intent volleyball = new Intent(getApplicationContext(), VolleyballActivity.class);
                    startActivity(volleyball);
                }
            }
        });
    }
}
