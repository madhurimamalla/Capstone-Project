package mmalla.android.com.whatnext;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mmalla.android.com.recommendationengine.RecommendationEngine;
import mmalla.android.com.whatnext.features.FeatureActivity;
import mmalla.android.com.whatnext.utils.DatabaseUtils;
import mmalla.android.com.whatnext.utils.MoviesListJsonUtils;
import mmalla.android.com.whatnext.utils.NetworkUtils;

public class SplashActivity extends BaseActivity implements View.OnClickListener{

    private final static String PATH_POPULAR_PARAM = "popular";

    /**
     * Creating instance of DatabaseUtils for interacting with DB
     */
    private DatabaseUtils du = new DatabaseUtils();
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private static final String MOVIE_WISHLIST_PARCELED = "MOVIE_WISHLIST_PARCELED";
    private static final String MOVIE_DISCOVER_FEATURE = "MOVIE_DISCOVER_FEATURE";
    private static final String MOVIE_HISTORY_PARCELED = "MOVIE_HISTORY_PARCELED";

    private final static String TAG = SplashActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.wishlist).setOnClickListener(this);
        findViewById(R.id.history).setOnClickListener(this);
        findViewById(R.id.discover).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        class FetchMoviesList extends AsyncTask<String, Void, List<Movie>> {

            @Override
            protected void onPreExecute() {
               /* recyclerView.setVisibility(View.INVISIBLE);
                mLoadingIcon.setVisibility(View.VISIBLE);*/
            }

            @Override
            protected List<Movie> doInBackground(String... params) {

                if (params.length == 0) {
                    return null;
                }

                String path = params[0];

                /**
                 * Get the URL to fetch the Popular movies
                 */
                // URL moviesListUrl = NetworkUtils.buildSearchMoviesUrl(path);
                /**
                 * For Search movie API fetch
                 */
                URL moviesListUrl = NetworkUtils.buildSearchMoviesUrl("Matrix");
                try {
                    String jsonPopularMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesListUrl);
                    return MoviesListJsonUtils.getSimpleMoviesInformationFromJson(jsonPopularMoviesResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Movie> mList) {
                if (mList != null) {
//                    mErrorMessage.setVisibility(View.INVISIBLE);
//                    showMovieThumbnails();
//                    moviesList.clear();
//                    moviesList.addAll(mList);
//                    moviesAdapter.notifyDataSetChanged();
                } else {
//                    showLoading();
                }
            }
        }

        new FetchMoviesList().execute(PATH_POPULAR_PARAM);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.wishlist){
            Intent wishlistIntent = new Intent(this, FeatureActivity.class);
            /**
             * TODO Retrieve and pass on the list of movies that this user wants to see
             */
            FirebaseUser currentUser = mAuth.getCurrentUser();
            /**
             * Parcel the MovieList and send it
             */
            // MovieList movieList = du.getTheMoviesInTheUsersWishlist(currentUser.getUid());
            /**
             * Creating dummy list now
              */
            Movie movie = new Movie("Name of the movie", "/ieuryiwyriuwe.jpg", "23423");
            Movie movie1 = new Movie("Terminal", "/erwerwerwerwerwerw.jpg", "234232423");
            Movie movie2 = new Movie("Terminal copy", "/eroewiuroewurowieur.jpg", "34534523");
            Movie movie3 = new Movie("New movie", "lfisdlfusdflds.jpg", "wo2340293");
            wishlistIntent.putExtra("Movie1", movie);
            ArrayList<Movie> movies = new ArrayList<Movie>();
            movies.add(movie);
            movies.add(movie1);
            movies.add(movie2);

            //List<Movie> movies2 = du.readWishListFromUser(mAuth.getCurrentUser().getUid());
            mAuth = FirebaseAuth.getInstance();
            //du.readWishListFromUser(mAuth.getCurrentUser().getUid());

            final List<Movie> wishlistMovies = new ArrayList<Movie>();
            FirebaseDatabase moviesList = FirebaseDatabase.getInstance();
            DatabaseReference moviesListRef = moviesList.getReference().child("users");

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        //Loop 1 to go through all the child nodes of users.
                        Log.d(TAG, "Unique key: " + uniqueKeySnapshot.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            moviesListRef.addValueEventListener(postListener);

            movies.add(movie3);
            du.addMoviesListToUsersWishlist(movies, mAuth.getCurrentUser().getUid());
            /**
             * Adding to Intent
             */
            wishlistIntent.putParcelableArrayListExtra(MOVIE_WISHLIST_PARCELED, movies);
            startActivity(wishlistIntent);

        } else if(i == R.id.discover){
            // TODO Launch the Feature Activity with the fragment of discover in it
            String recommendations = RecommendationEngine.getRecommendations();
            Log.d(TAG, recommendations);

            Movie movie = new Movie("Name of the movie 2", "/ieuryiwyriuwe.jpg", "23423");
            Movie movie1 = new Movie("Terminal 2", "/erwerwerwerwerwerw.jpg", "234232423");
            ArrayList<Movie> movies = new ArrayList<Movie>();
            movies.add(movie);
            movies.add(movie1);

            Intent discoverIntent = new Intent(this, FeatureActivity.class);
            discoverIntent.putExtra(MOVIE_DISCOVER_FEATURE, movies);
            startActivity(discoverIntent);


        } else if(i == R.id.history){
            // Testing Feature Activity launch from here
            Intent historyIntent = new Intent(this, FeatureActivity.class);
            /**
             * TODO Retrieve and pass on the list of movies that this user has already seen
             */
            FirebaseUser currentUser = mAuth.getCurrentUser();
            /**
             * Parcel the MovieList and send it
             */
            // MovieList movieList = du.getTheMoviesFromTheUsersHistory(currentUser.getUid());
            /**
             * Creating dummy list now
             */
            Movie movie = new Movie("Name of the movie 2", "/ieuryiwyriuwe.jpg", "23423");
            Movie movie1 = new Movie("Terminal 2", "/erwerwerwerwerwerw.jpg", "234232423");
            historyIntent.putExtra("Movie1", movie);
            ArrayList<Movie> movies = new ArrayList<Movie>();
            movies.add(movie);
            movies.add(movie1);
            /**
             * Adding to Intent
             */
            historyIntent.putParcelableArrayListExtra(MOVIE_HISTORY_PARCELED, movies);
            startActivity(historyIntent);
        } else if( i == R.id.search){
            /**
             * Implement the search overlay here enquiring
             */
            SearchView searchView = (SearchView) findViewById(R.id.search);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d(TAG, "Text entered : " + query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d(TAG, "Text changed : " + newText);
                    return true;
                }
            });

        }
    }
}
