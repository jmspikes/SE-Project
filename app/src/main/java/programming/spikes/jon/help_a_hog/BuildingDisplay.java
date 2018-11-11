package programming.spikes.jon.help_a_hog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

public class BuildingDisplay extends AppCompatActivity {

    String fromMaps;
    ImageView buildingPic;
    TextView buildingName;
    TextView funBody;
    TextView foodBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_display);
        //handles passing hashmap between intents
        Intent get = getIntent();
        fromMaps =  get.getStringExtra("title");
        String pic = fromMaps.toLowerCase();
        pic = pic.replaceAll("\\s","");
        buildingPic = (ImageView) findViewById(R.id.buildingPic);
        int imageId = this.getResources().getIdentifier(pic, "drawable", this.getPackageName());
        //sets a default image if no image is found
        if(imageId == 0)
            imageId = this.getResources().getIdentifier("defaultpic", "drawable", this.getPackageName());
        buildingPic.setImageResource(imageId);
        buildingName = (TextView) findViewById(R.id.buildingName);
        buildingName.setText(fromMaps);
        funBody = (TextView) findViewById(R.id.funBody);
        funBody.setMovementMethod(new ScrollingMovementMethod());
        foodBody = (TextView) findViewById(R.id.foodBody);
        foodBody.setMovementMethod(new ScrollingMovementMethod());

    }
}
