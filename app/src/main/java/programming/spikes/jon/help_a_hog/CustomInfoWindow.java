package programming.spikes.jon.help_a_hog;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindow(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custinfo, null);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(500,500));

        TextView buildingName = view.findViewById(R.id.buildingName);
        ImageView buildingPic = view.findViewById(R.id.buildingPic);



        buildingName.setText(marker.getTitle());

        int imageId = context.getResources().getIdentifier("union",
                "drawable", context.getPackageName());
        buildingPic.setImageResource(imageId);


        return view;
    }
}