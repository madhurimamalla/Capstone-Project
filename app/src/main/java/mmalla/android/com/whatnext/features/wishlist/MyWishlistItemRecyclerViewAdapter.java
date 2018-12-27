package mmalla.android.com.whatnext.features.wishlist;

import android.content.Context;
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
 * {@link RecyclerView.Adapter} that can display a {@link Movie} and makes a call to the
 * specified
 * TODO: Replace the implementation with code for your data type.
 */
public class MyWishlistItemRecyclerViewAdapter extends RecyclerView.Adapter<MyWishlistItemRecyclerViewAdapter.WishlistViewHolder> {

    private final static String TAG = MyWishlistItemRecyclerViewAdapter.class.getSimpleName();

    private final List<Movie> mValues;


    public MyWishlistItemRecyclerViewAdapter(List<Movie> items, Context mContext) {
        mValues = items;
        Context mContext1 = mContext;
    }

    @Override
    public WishlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_wishlist_item, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WishlistViewHolder holder, int position) {
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

    public class WishlistViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImgPath;
        final TextView mName;
        final TextView mYear;

        WishlistViewHolder(View view) {
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
