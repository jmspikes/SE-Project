package programming.spikes.jon.help_a_hog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.StringBuilder;
import android.view.View;
import android.widget.Toast;


public class TenniswActivity extends FragmentActivity {
    private TextView textView14;
    private StringBuilder text = new StringBuilder();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tennisw);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("tennisw.txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append(mLine);
                text.append('\n');
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error reading file!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }

        TextView output = (TextView) findViewById(R.id.textView14);
        output.setText((CharSequence) text);
    }
}