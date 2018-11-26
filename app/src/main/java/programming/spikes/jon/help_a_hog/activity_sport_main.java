package programming.spikes.jon.help_a_hog;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class activity_sport_main extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SportsAdapter adapter;
    private List<SportPic> sportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        sportsList = new ArrayList<>();
        adapter = new SportsAdapter(this, sportsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.razorbackback).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.razorbackfootball,
                R.drawable.razorbackbasketball,
                R.drawable.razorbackbaseball,
                R.drawable.razorbacktrack,
                R.drawable.razorbackcrosscountry,
                R.drawable.razorbackgym,
                R.drawable.razorbacklacrosse,
                R.drawable.razorbacksoccer,
                R.drawable.razorbackrugby,
                R.drawable.razorbackgolf,
                R.drawable.razorbackvolley,
                R.drawable.razorbacktennis
        };

        SportPic a = new SportPic("Football", "7th in NCAA 0-8", covers[0]);
        sportsList.add(a);

        a = new SportPic("Basketball", "34th in NCAA 23-12", covers[1]);
        sportsList.add(a);

        a = new SportPic("Baseball", "8th in NCAA 37-17", covers[2]);
        sportsList.add(a);

        a = new SportPic("Track and Field", "4th in NCAA", covers[3]);
        sportsList.add(a);

        a = new SportPic("Cross Country", "19th in NCAA", covers[4]);
        sportsList.add(a);

        a = new SportPic("Gymnastics", "10th in NCAA", covers[5]);
        sportsList.add(a);

        a = new SportPic("Lacrosse", "Not Ranked", covers[6]);
        sportsList.add(a);

        a = new SportPic("Soccer", "3rd in SEC 14-5-4", covers[7]);
        sportsList.add(a);

        a = new SportPic("Rugby", "Not Ranked", covers[8]);
        sportsList.add(a);

        a = new SportPic("Golf", "14th in NCAA", covers[9]);
        sportsList.add(a);

        a = new SportPic("Volleyball", "14th in NCAA", covers[10]);
        sportsList.add(a);

        a = new SportPic("Tennis", "9th in NCAA 4-8", covers[11]);
        sportsList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
