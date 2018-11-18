package programming.spikes.jon.help_a_hog;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    Button direction;
    Button goBack;

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
        //used to parse picture name
        String pic = fromMaps.toLowerCase();
        pic = pic.replaceAll("\\s","");
        //gets building container
        buildingPic = findViewById(R.id.buildingPic);
        //sets picture
        int imageId = this.getResources().getIdentifier(pic, "drawable", this.getPackageName());
        //sets a default image if no image is found
        if(imageId == 0)
            imageId = this.getResources().getIdentifier("defaultpic", "drawable", this.getPackageName());
        //applies the image
        buildingPic.setImageResource(imageId);
        //gets building name container
        buildingName = findViewById(R.id.buildingName);
        //sets the text name of building
        buildingName.setText(fromMaps);
        //gets fun text container
        funBody = findViewById(R.id.funBody);
        //sets fun text information
        funBody.setMovementMethod(new ScrollingMovementMethod());
        //sets default text if there isn't a fun fact
        if(fromFile.containsKey(fromMaps))
            funBody.setText(fromFile.get(fromMaps));
        else
            funBody.setText("Check back shortly for new fun facts!");
        //gets food body dialog
        foodBody = findViewById(R.id.foodBody);
        //allows scrolling
        foodBody.setMovementMethod(new ScrollingMovementMethod());
        //gets the text information, sets invisible if not
        if(foodFromFile.containsKey(fromMaps))
            foodBody.setText(foodFromFile.get(fromMaps));
        else{
            //sets items to not be seen
            foodBody.setVisibility(View.INVISIBLE);
            buildingFood = findViewById(R.id.buildingFood);
            buildingFood.setVisibility(View.INVISIBLE);
        }

        direction = findViewById(R.id.directions);
        goBack = findViewById(R.id.goBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast placeholder = Toast.makeText(getApplicationContext(), "Make map!", Toast.LENGTH_SHORT);
                placeholder.show();
            }
        });

    }
}
