package mmalla.android.com.whatnext.features.discovery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import mmalla.android.com.whatnext.R;
import mmalla.android.com.whatnext.model.Movie;
import mmalla.android.com.whatnext.recommendations.engine.DatabaseUtils;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    private final static String TAG = DiscoverFragment.class.getSimpleName();

    private DatabaseUtils databaseUtils;
    private FirebaseAuth mAuth;

    /**
     * Using this for rendering images
     */
    private String IMAGE_MOVIE_URL = "https://image.tmdb.org/t/p/w780/";

    private Movie movie;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DiscoverFragment.
     */
    public static DiscoverFragment newInstance(Movie movie) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putParcelable("DISCOVERED_MOVIE", movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            this.movie = savedInstanceState.getParcelable(getString(R.string.PARCELED_MOVIE));
        } else if (getArguments() != null) {
            this.movie = getArguments().getParcelable(getString(R.string.DISCOVERED_MOVIE));
        }

        mAuth = FirebaseAuth.getInstance();
        databaseUtils = new DatabaseUtils();
        Log.d(TAG, movie.toString());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_poster_discover_screen);
        String mImgPath = movie.getmPoster();

        /**
         * Glide the image rendering!
         */
        Glide.with(getActivity().getApplicationContext()).load(IMAGE_MOVIE_URL + mImgPath).into(imageView);

        /**
         * TODO Still need to add the plot summary of the movie
         */
        //        /**
//         * Show the bottom modal fragment to display the plot summary
//         * TODO Figure out how to send data to the modal because we need to send the plot summary to it
//         */
//        PlotSummaryModalSheet plotSummaryModalSheet = new PlotSummaryModalSheet();
//        plotSummaryModalSheet.setMovie(movie);
//        plotSummaryModalSheet.show(getFragmentManager(), "Opening PlotSummaryModalSheet");

        /**
         * The button clicks need to be recorded here
         */
        final ImageView watchlistView = (ImageView) rootView.findViewById(R.id.add_to_watchlist);
        final ImageView likedMovieView = (ImageView) rootView.findViewById(R.id.like_movie_button);
        final ImageView dontlikeMovieView = (ImageView) rootView.findViewById(R.id.dislike_movie_button);

        watchlistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUtils.updateMovie(mAuth.getCurrentUser().getUid(), movie, Movie.PREFERENCE.WISHLISTED);
                Toast.makeText(getContext(), "The movie is added to your wishlist", Toast.LENGTH_LONG).show();
                watchlistView.setEnabled(false);
                likedMovieView.setAlpha(1);
                dontlikeMovieView.setAlpha(1);
                likedMovieView.setEnabled(false);
                dontlikeMovieView.setEnabled(false);
                Log.d(TAG, "Disabling the liked & dislike buttons");
            }
        });

        likedMovieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUtils.updateMovie(mAuth.getCurrentUser().getUid(), movie, Movie.PREFERENCE.LIKED);
                Toast.makeText(getContext(), "The movie is added to your liked movie/history list", Toast.LENGTH_LONG).show();
                watchlistView.setAlpha(1);
                dontlikeMovieView.setAlpha(1);
                watchlistView.setEnabled(false);
                dontlikeMovieView.setEnabled(false);
                watchlistView.setEnabled(false);
                Log.d(TAG, "Disabling the watchlist & dislike buttons");
            }
        });

        dontlikeMovieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUtils.updateMovie(mAuth.getCurrentUser().getUid(), movie, Movie.PREFERENCE.DISLIKED);
                Toast.makeText(getContext(), "The movie is added to your dislike list", Toast.LENGTH_LONG).show();
                dontlikeMovieView.setEnabled(false);
                likedMovieView.setAlpha(1);
                watchlistView.setAlpha(1);
                likedMovieView.setEnabled(false);
                watchlistView.setEnabled(false);
                Log.d(TAG, "Disabling the liked & watchlist buttons");
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("MOVIE_PARCELED", movie);
    }
}
