package programming.spikes.jon.help_a_hog;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;
    HashMap<String, LatLng> fromFile;



    public CustomInfoWindow(Context context, HashMap<String, LatLng> fromFile){
        this.fromFile = fromFile;
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        //allows view to be edited
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custinfo, null);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(500,500));
        //buildling information
        TextView buildingName = view.findViewById(R.id.buildingName);
        ImageView buildingPic = view.findViewById(R.id.buildingPic);
        buildingName.setText(marker.getTitle());

        //gets markers name and trims it to match picture names
        String pic = marker.getTitle().toLowerCase();
        pic = pic.replaceAll("\\s","");
        //gets picture name and provides it to info window
        if(fromFile.containsKey(marker.getTitle())){
            int imageId = context.getResources().getIdentifier(pic, "drawable", context.getPackageName());
            buildingPic.setImageResource(imageId);
        }

        return view;
    }
}