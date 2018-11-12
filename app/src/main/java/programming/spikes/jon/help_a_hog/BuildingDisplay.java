package programming.spikes.jon.help_a_hog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
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
    TextView buildingFood;
    HashMap<String, String> fromFile;
    HashMap<String, String> foodFromFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_display);
        //handles passing hashmap between intents
        Intent get = getIntent();
        fromMaps =  get.getStringExtra("title");
        fromFile = (HashMap<String, String>) get.getSerializableExtra("facts");
        foodFromFile = (HashMap<String, String>) get.getSerializableExtra("food");
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
        if(fromFile.containsKey(fromMaps))
            funBody.setText(fromFile.get(fromMaps));
        else
            funBody.setText("Check back shortly for new fun facts!");
        foodBody = (TextView) findViewById(R.id.foodBody);
        foodBody.setMovementMethod(new ScrollingMovementMethod());
        if(foodFromFile.containsKey(fromMaps))
            foodBody.setText(foodFromFile.get(fromMaps));
        else{
            foodBody.setVisibility(View.INVISIBLE);
            buildingFood = (TextView) findViewById(R.id.buildingFood);
            buildingFood.setVisibility(View.INVISIBLE);
        }

    }
}
