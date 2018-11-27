package programming.spikes.jon.help_a_hog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.MyViewHolder> {

    public Context mContext;
    public List<SportPic> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, count;
        ImageView thumbnail, overflow;

        public MyViewHolder(View view){
            super(view);

            title =  view.findViewById(R.id.title);
            count =  view.findViewById(R.id.count);
            thumbnail = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);

        }

    }

    public SportsAdapter(Context mContext, List<SportPic> albumList){

        this.mContext = mContext;
        this.albumList = albumList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SportPic sport = albumList.get(position);
        holder.title.setText(sport.getName());
        holder.count.setText("Record: " + sport.getRecord());

        // loading album cover using Glide library
        Glide.with(mContext).load(sport.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Under Construction", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Under Construction", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
