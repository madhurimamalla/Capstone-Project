package mmalla.android.com.whatnext.features.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mmalla.android.com.whatnext.R;
import mmalla.android.com.whatnext.model.Movie;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Movie}.
 */
public class MyHistoryItemRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoryItemRecyclerViewAdapter.HistoryItemViewHolder> {

    private final static String TAG = MyHistoryItemRecyclerViewAdapter.class.getSimpleName();

    private final List<Movie> mValues;

    public MyHistoryItemRecyclerViewAdapter(List<Movie> movies) {
        mValues = movies;
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history, parent, false);
        return new HistoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HistoryItemViewHolder holder, int position) {
        holder.mName.setText(mValues.get(position).getmTitle());
        holder.mYear.setText(mValues.get(position).getmReleaseYear());

        /**
         * Render the movie poster
         */
        try{
            String imgPath = mValues.get(position).getmPoster();
            String IMAGE_MOVIE_URL = "http://image.tmdb.org/t/p/w185/";
            Picasso.get().setLoggingEnabled(true);
            Picasso.get().load(IMAGE_MOVIE_URL + imgPath).into(holder.mImgPath);
        }catch (IllegalStateException e){
            holder.mImgPath.setImageResource(R.drawable.baseline_movie_filter_black_48dp);
        }

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class HistoryItemViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImgPath;
        public final TextView mName;
        public final TextView mYear;

        public HistoryItemViewHolder(View view) {
            super(view);
            mImgPath = (ImageView) view.findViewById(R.id.movie_poster);
            mName = (TextView) view.findViewById(R.id.movie_name);
            mYear = (TextView) view.findViewById(R.id.movie_year);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
