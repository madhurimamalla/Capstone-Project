package mmalla.android.com.whatnext.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mmalla.android.com.whatnext.R;
import mmalla.android.com.whatnext.model.Movie;
import mmalla.android.com.whatnext.moviedbclient.MovieDBClient;
import mmalla.android.com.whatnext.recommendations.engine.DatabaseUtils;
import timber.log.Timber;

class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = WidgetDataProvider.class.getSimpleName();
    private static final String MOVIE_DISCOVER_FEATURE = "MOVIE_DISCOVER_FEATURE";
    private static final String MOVIE_PARCELED = "MOVIE_PARCELED";
    private static final String MOVIES = "movies";
    private static final String USERS = "users";

    private Context mContext;

    private List<Movie> movieList;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public WidgetDataProvider(Context mContext, Intent mIntent) {
        this.mContext = mContext;
        Intent mIntent1 = mIntent;
        movieList = new ArrayList<Movie>();
    }

    private void initData() throws NullPointerException {
        try {
            DatabaseUtils databaseUtils = new DatabaseUtils();
            this.mAuth = FirebaseAuth.getInstance();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate() {
        Timber.d(TAG, "Starting onCreate() in WidgetDataProvider.......");
        initData();

        MovieDBClient client = new MovieDBClient();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference moviesListRef = firebaseDatabase.getReference().child(USERS)
                .child(mAuth.getCurrentUser().getUid()).child(MOVIES);

        /**
         * Get list of movie id from the Firebase
         */

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Movie movie = ds.getValue(Movie.class);
                    if (movie.getmPref().equals(Movie.PREFERENCE.WISHLISTED)) {
                        movieList.add(movie);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.e(TAG, databaseError.getMessage());
            }
        };

        moviesListRef.addValueEventListener(valueEventListener);
    }

    /**
     * TODO Check why the movies aren't getting displayed in the widget
     */
    @Override
    public void onDataSetChanged() {
        Timber.d(TAG, "Starting onDataSetChanged() in WidgetDataProvider.......");
        initData();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference moviesListRef = firebaseDatabase.getReference().child(USERS)
                .child(mAuth.getCurrentUser().getUid()).child(MOVIES);

        /**
         * Get list of movie id from the Firebase
         */

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Movie movie = ds.getValue(Movie.class);
                    if (movie.getmPref().equals(Movie.PREFERENCE.WISHLISTED)) {
                        movieList.add(movie);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.e(TAG, databaseError.getMessage());
            }
        };

        moviesListRef.addValueEventListener(valueEventListener);

        Timber.d(TAG, "Completed onDataSetChanged() in WidgetDataProvider.......");
    }

    @Override
    public void onDestroy() {
        Timber.d(TAG, "Starting onDestroy() in WidgetDataProvider.......");

    }

    @Override
    public int getCount() {
        Timber.d(TAG, "Starting onCount() in WidgetDataProvider.......");
        return movieList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Timber.d(TAG, "Starting getViewAt() in WidgetDataProvider.......");

        Movie movie = movieList.get(position);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.wishlist_widget_item_row);
        remoteViews.setTextViewText(R.id.widget_movie_title, movie.getmTitle());
        remoteViews.setTextViewText(R.id.widget_movie_year, movie.getmReleaseYear());

        Bundle extras = new Bundle();
        extras.putParcelable(MOVIE_PARCELED, movie);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(MOVIE_DISCOVER_FEATURE, movie);
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widget_item_row, fillInIntent);

        Timber.d(TAG, "Completing getViewAt() in WidgetDataProvider.......");
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Timber.d(TAG, "Starting getViewTypeCount() in WidgetDataProvider.......");
        return 1;
    }

    @Override
    public long getItemId(int position) {
        Timber.d(TAG, "Starting getItemId() in WidgetDataProvider.......");
        return position;
    }

    @Override
    public boolean hasStableIds() {
        Timber.d(TAG, "Starting hasStableIds() in WidgetDataProvider.......");
        return true;
    }
}
