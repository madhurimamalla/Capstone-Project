package mmalla.android.com.whatnext;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import mmalla.android.com.whatnext.features.FeatureActivity;
import mmalla.android.com.whatnext.model.Movie;
import mmalla.android.com.whatnext.recommendations.engine.DatabaseUtils;

public class SplashActivity extends BaseActivity implements View.OnClickListener {

    /**
     * Creating instance of DatabaseUtils for interacting with DB
     */
    private DatabaseUtils databaseUtils = new DatabaseUtils();
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private static final String ARE_THERE_MOVIES_UNDER_USER = "ARE_THERE_MOVIES_UNDER_USER";
    private static final String MOVIE_WISHLIST_PARCELED = "MOVIE_WISHLIST_PARCELED";
    private static final String MOVIE_DISCOVER_FEATURE = "MOVIE_DISCOVER_FEATURE";
    private static final String MOVIE_HISTORY_PARCELED = "MOVIE_HISTORY_PARCELED";
    private static final String MOVIES_DISLIKED = "MOVIES_DISLIKED";
    private static final String MOVIES_LIKED = "MOVIES_LIKED";

    private int num = 0;
    private Long[] size;

    private List<Movie> presentLikedMovies;
    private List<Movie> dislikedMovies;


    private final static String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.wishlist).setOnClickListener(this);
        findViewById(R.id.history).setOnClickListener(this);
        findViewById(R.id.discover).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        /*
          Check if there are any movies under the authenticated user, if so, send a message to the next
          activity so the discovery process can be easy.
         */

        this.presentLikedMovies = new ArrayList<Movie>();
        this.dislikedMovies = new ArrayList<Movie>();

        this.size = databaseUtils.checkIfMoviesExist(mAuth.getCurrentUser().getUid());
        this.presentLikedMovies = databaseUtils.getHistory(mAuth.getCurrentUser().getUid());
        this.dislikedMovies = databaseUtils.getDislikedMovies(mAuth.getCurrentUser().getUid());
    }

    /**
     * Creates a menu for various options
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit_account_details:
                Toast.makeText(getApplicationContext(), R.string.Edit_account_details_clicked, Toast.LENGTH_LONG).show();
                return true;
            case R.id.clear_movie_list:
                if (databaseUtils.removeFullMoviesListFromTheUser(mAuth.getCurrentUser().getUid())) {
                    Toast.makeText(getApplicationContext(), R.string.Cleared_movie_list, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.wishlist) {
            Intent wishlistIntent = new Intent(this, FeatureActivity.class);
            wishlistIntent.putExtra(MOVIE_WISHLIST_PARCELED, "");
            startActivity(wishlistIntent);

        } else if (i == R.id.discover) {
            Movie movie = new Movie();
            Movie movie1 = new Movie();
            ArrayList<Movie> movies = new ArrayList<Movie>();
            movies.add(movie);
            movies.add(movie1);

            /*
              Initiating the Discover fragment via Feature Activity
             */
            Intent discoverIntent = new Intent(this, FeatureActivity.class);
            discoverIntent.putExtra(MOVIE_DISCOVER_FEATURE, movies);
            discoverIntent.putExtra(ARE_THERE_MOVIES_UNDER_USER, size[0]);
            discoverIntent.putParcelableArrayListExtra(MOVIES_LIKED, (ArrayList<? extends Parcelable>) this.presentLikedMovies);
            discoverIntent.putParcelableArrayListExtra(MOVIES_DISLIKED, (ArrayList<? extends Parcelable>) this.dislikedMovies);
            startActivity(discoverIntent);

        } else if (i == R.id.history) {

            Intent historyIntent = new Intent(this, FeatureActivity.class);
            historyIntent.putExtra(MOVIE_HISTORY_PARCELED, "");
            startActivity(historyIntent);

            /**
             * TODO Implement the search feature for future extension
             */
//        } else if( i == R.id.search){
//            /**
//             * Implement the search overlay here enquiring
//             */
//            SearchView searchView = (SearchView) findViewById(R.id.search);
//
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Timber.d(TAG, "Text entered : " + query);
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    Timber.d(TAG, "Text changed : " + newText);
//                    return true;
//                }
//            });
//
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }
}
