package mmalla.android.com.whatnext.features;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import mmalla.android.com.whatnext.BaseActivity;
import mmalla.android.com.whatnext.R;
import mmalla.android.com.whatnext.features.discovery.DiscoverPagerAdapter;
import mmalla.android.com.whatnext.features.history.HistoryFragment;
import mmalla.android.com.whatnext.features.wishlist.WishlistFragment;
import mmalla.android.com.whatnext.model.Movie;
import mmalla.android.com.whatnext.moviedbclient.MovieDBClient;
import mmalla.android.com.whatnext.moviedbclient.MovieDBClientException;
import mmalla.android.com.whatnext.recommendations.engine.DatabaseUtils;
import timber.log.Timber;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FeatureActivity extends BaseActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    DatabaseUtils databaseUtils;
    FirebaseAuth mAuth;

    Long num;

    private ViewPager mViewPager;
    private List<Movie> discoveredMovies;
    private List<Movie> dislikedMovies;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private static final String ARE_THERE_MOVIES_UNDER_USER = "ARE_THERE_MOVIES_UNDER_USER";
    private static final String MOVIE_WISHLIST_PARCELED = "MOVIE_WISHLIST_PARCELED";
    private static final String MOVIE_DISCOVER_FEATURE = "MOVIE_DISCOVER_FEATURE";
    private static final String MOVIE_HISTORY_PARCELED = "MOVIE_HISTORY_PARCELED";
    private static final String MOVIES_DISLIKED = "MOVIES_DISLIKED";
    private static final String MOVIES_LIKED = "MOVIES_LIKED";
    private static final String MOVIES_DISCOVERED = "MOVIES_DISCOVERED";

    private final static String TAG = FeatureActivity.class.getSimpleName();

    /**
     * Store the state if the Activity was showing Discover, Wishlist or History feature
     * Wishlist : 0
     * Discover : 1
     * History : 2
     */
    private int state;

    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private TextView mFeatureTitle;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseUtils = new DatabaseUtils();
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_feature);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mFeatureTitle = findViewById(R.id.feature_title);

        /**
         * ViewPager to display the various movies in the discover feature
         */
        mViewPager = (ViewPager) findViewById(R.id.container);

        discoveredMovies = new ArrayList<Movie>();
        dislikedMovies = new ArrayList<Movie>();

//        // Set up the user interaction to manually show or hide the system UI.
//        mContentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggle();
//            }
//        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        // TODO Later surround this by a check if the saveInstanceState is there, then load the saved instance
        // For saved instance, keep the screen details too of the fragment

        // TODO Add the logic which kicks off the Wishlist fragment here

        Intent previousIntent = getIntent();

        if (previousIntent.getExtras().containsKey(MOVIE_WISHLIST_PARCELED)) {

            WishlistFragment wishlistFragment = (WishlistFragment) getFragmentManager().findFragmentById(R.id.feature_container);
            state = 0;
            mFeatureTitle.setText(R.string.wishlist);
            if (wishlistFragment == null) {
                wishlistFragment = new WishlistFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.feature_container, wishlistFragment);
                fragmentTransaction.commit();
            }

        } else if (previousIntent.getExtras().containsKey(MOVIE_DISCOVER_FEATURE)) {
            state = 1;
            mFeatureTitle.setText(R.string.discover);

            if (savedInstanceState != null) {
                num = savedInstanceState.getLong(ARE_THERE_MOVIES_UNDER_USER);
            } else if (previousIntent.getExtras().containsKey(ARE_THERE_MOVIES_UNDER_USER)) {
                num = previousIntent.getExtras().getLong(ARE_THERE_MOVIES_UNDER_USER);
            }

            if (savedInstanceState != null) {
                discoveredMovies.clear();
                discoveredMovies = savedInstanceState.getParcelableArrayList(MOVIES_DISCOVERED);
            } else if (previousIntent.getExtras().containsKey(MOVIES_LIKED)) {
                discoveredMovies = previousIntent.getExtras().getParcelableArrayList(MOVIES_LIKED);
            }

            if (savedInstanceState != null) {
                dislikedMovies.clear();
                dislikedMovies = savedInstanceState.getParcelableArrayList(MOVIES_DISLIKED);
            } else if (previousIntent.getExtras().containsKey(MOVIES_DISLIKED)) {
                this.dislikedMovies = previousIntent.getExtras().getParcelableArrayList(MOVIES_DISLIKED);
            }

            if (num == 0) {

                final MovieDBClient movieDBClient = new MovieDBClient();

                class fetchMovies extends AsyncTask<String, Void, List<Movie>> {

                    @Override
                    protected List<Movie> doInBackground(String... strings) {
                        List<Movie> movielist = new ArrayList<Movie>();

                        try {
                            movielist = movieDBClient.getSomePopularMovies(10);
                        } catch (MovieDBClientException e) {
                            e.printStackTrace();
                        }
                        return movielist;
                    }
                }

                try {
                    discoveredMovies = new fetchMovies().execute("").get();
                    Timber.d(TAG, "Discovered movies are here!");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {

                /**
                 * Fetch the liked movies, choose a movie randomly in that
                 * and send that to fetch similar movies from TMDB
                 * After fetching, remove the ones which are disliked and update the discovered list
                 */

                final MovieDBClient client = new MovieDBClient();

                int luckyNum = client.getRandomNumber(0, discoveredMovies.size());

                class fetchInterestingMovies extends AsyncTask<String, Void, List<Movie>> {

                    @Override
                    protected List<Movie> doInBackground(String... strings) {
                        List<Movie> movielist = new ArrayList<Movie>();

                        try {
                            movielist = client.getSimilarMovies(strings[0]);
                        } catch (MovieDBClientException e) {
                            e.printStackTrace();
                        }
                        return movielist;
                    }
                }

                try {
                    Movie luckyMovie = discoveredMovies.get(luckyNum - 1);
                    discoveredMovies.clear();
                    discoveredMovies = new fetchInterestingMovies().execute(luckyMovie.getmId()).get();
                    Timber.d(TAG, "Discovered movies are here!");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /**
             * TODO Refine the discovered movies to remove any disliked movies
             */


            /**
             * Get the discovered movie list and set the adapter list so it can display in the viewpager
             */
            DiscoverPagerAdapter discoverPagerAdapter = new DiscoverPagerAdapter(getSupportFragmentManager());
            if (discoveredMovies.size() > 10) {
                discoverPagerAdapter.setList(discoveredMovies.subList(0, 9));
            }
            discoverPagerAdapter.setList(discoveredMovies);
            mViewPager.setAdapter(discoverPagerAdapter);

        } else if (previousIntent.getExtras().containsKey(MOVIE_HISTORY_PARCELED)) {
            state = 2;
            mFeatureTitle.setText(R.string.history);
            List<Movie> movies = previousIntent.getParcelableArrayListExtra(MOVIE_HISTORY_PARCELED);

            HistoryFragment historyFragment = (HistoryFragment) getFragmentManager().findFragmentById(R.id.feature_container);
            if (historyFragment == null) {
                historyFragment = new HistoryFragment();
                historyFragment.setMoviesList(movies);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.feature_container, historyFragment);
                fragmentTransaction.commit();
            }

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);

//        /**
//         * Show the bottom modal fragment to display the plot summary
//         * TODO Figure out how to send data to the modal because we need to send the plot summary to it
//         */
//        PlotSummaryModalSheet plotSummaryModalSheet = new PlotSummaryModalSheet();
//        plotSummaryModalSheet.show(getSupportFragmentManager(), "Opening PlotSummaryModalSheet");

    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Timber.d("Inside onSaveInstanceState() Saving state ....");
        super.onSaveInstanceState(outState);
        outState.putLong(ARE_THERE_MOVIES_UNDER_USER, num);
        outState.putParcelableArrayList(MOVIES_DISLIKED, (ArrayList<? extends Parcelable>) dislikedMovies);
        outState.putParcelableArrayList(MOVIES_DISCOVERED, (ArrayList<? extends Parcelable>) discoveredMovies);

    }
}
