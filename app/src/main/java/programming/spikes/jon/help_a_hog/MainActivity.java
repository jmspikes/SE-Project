package programming.spikes.jon.help_a_hog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //used for main page popup, user clicks and popup starts
        buildDropDown();

    }

    void buildDropDown(){

        //drop down menu for user to make selection of where to go
        Spinner dropDown = findViewById(R.id.menuNav);

        String[] options = {"What's Nearby?","Food","Directions","Help","Resources"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);

    }
}
