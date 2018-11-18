package programming.spikes.jon.help_a_hog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.InputStream;

public class SportsActivity extends FragmentActivity  {
    String items[] = new String [] {"Football", "Basketball", "Track", "Golf"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
    }

    ListView listView = (ListView) findViewById(R.id.ListView);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
    listView.setAdapter(adapter);
}
