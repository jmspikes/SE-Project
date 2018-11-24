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


public class TrackFieldmActivity extends FragmentActivity {
    private TextView textView15;
    private StringBuilder text = new StringBuilder();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackfieldm);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("trackFieldm.txt")));
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

        TextView output = (TextView) findViewById(R.id.textView15);
        output.setText((CharSequence) text);
    }
}