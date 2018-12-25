package mmalla.android.com.whatnext.recommendations.engine;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mmalla.android.com.whatnext.model.Movie;
import mmalla.android.com.whatnext.model.User;
import mmalla.android.com.whatnext.moviedbclient.MovieDBClient;
import timber.log.Timber;

/**
 * Description: DatabaseUtils is created to perform operations on the database
 *
 * @author mmalla
 */
public class DatabaseUtils {

    private final static String TAG = DatabaseUtils.class.getSimpleName();

    private final static String USERS = "users";
    private final static String MOVIES = "movies";
    private final static String WISHLIST = "wishlist";
    private final static String HISTORY = "history";

    public DatabaseReference database;

    public DatabaseUtils() {
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    public void writeNewUser(String userId, User user) {
        database.child(USERS).child(userId).setValue(user);
    }


    /**
     * This method can be used for Create/Update on the Firebase realtime DB
     */
    /**
     * @param movies
     * @param userID
     */
    public void addMovies(List<Movie> movies, String userID) {
        DatabaseReference userWishlistRef = database.getRef().child(USERS).child(userID);

        for (Movie movie : movies) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(movie.getmId(), movie);
            /**
             * Updates only unique ids which works for me so that way more than once we can add the same
             * movie into the wishlist and it will add only once
             */
            userWishlistRef.child(MOVIES).updateChildren(map);
        }
    }

    /**
     * Update any movie in Firebase realtime movies table
     *
     * @param movie
     * @param userID
     * @param preference
     */
    public void updateMovie(String userID, Movie movie, Movie.PREFERENCE preference) {
        DatabaseReference userWishlistRef = database.getRef().child(USERS).child(userID);

        movie.setmPref(preference);
        HashMap<String, Object> map = new HashMap<>();
        map.put(movie.getmId(), movie);
        userWishlistRef.child(MOVIES).updateChildren(map);

    }

    /**
     * General method to get any list from Firebase
     *
     * @param userID
     * @param preference
     * @return
     */
    private List<Movie> getList(String userID, final Movie.PREFERENCE preference) {
        MovieDBClient client = new MovieDBClient();
        FirebaseDatabase moviesList = FirebaseDatabase.getInstance();
        DatabaseReference moviesListRef = moviesList.getReference().child(USERS).child(userID).child(MOVIES);
        final List<Movie> list = new ArrayList<Movie>();

        /**
         * Get list of movie id from the Firebase
         */

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Movie movie = ds.getValue(Movie.class);
                    if (movie.getmPref().equals(preference)) {
                        list.add(movie);
                        Log.d(TAG, list.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.e(TAG, databaseError.getMessage());
            }
        };

        moviesListRef.addValueEventListener(valueEventListener);

        return list;
    }

    /**
     * Method to retrieve the wishlist
     * @param userID
     * @return
     */
    public List<Movie> getWishlist(String userID) {

        List<Movie> movieList = getList(userID, Movie.PREFERENCE.WISHLISTED);

        return movieList;
    }


    /**
     * Method to retrieve the movies seen and liked!
     *
     * @param userID
     * @return
     */
    public List<Movie> getHistory(String userID) {
        List<Movie> movieList = getList(userID, Movie.PREFERENCE.LIKED);
        return movieList;
    }

    /**
     * Method to retrieve the movies seen and disliked
     */
    public List<Movie> getDislikedMovies(String userID) {
        List<Movie> movieList = getList(userID, Movie.PREFERENCE.DISLIKED);
        return movieList;
    }

    /**
     * Description:
     * Usage guide: du.removeFullMoviesListFromTheUser(mAuth.getCurrentUser().getUid()); where du
     * is the instance of the Database
     * A Method to clean up the whole movies list under the user
     *
     * @param userId
     */
    public boolean removeFullMoviesListFromTheUser(String userId) {
        try {
            /**
             * Removes the whole movies list from the user
             */
            database.getRef().child(USERS).child(userId).child(MOVIES).removeValue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Check if there is movies child in the DB
     *
     * @param userID
     * @return
     */
    public Long[] checkIfMoviesExist(String userID) {
        final Long[] size2 = new Long[1];

        FirebaseDatabase moviesList = FirebaseDatabase.getInstance();
        DatabaseReference moviesListRef = moviesList.getReference().child(USERS).child(userID).child(MOVIES);
        boolean isExists = false;
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                size2[0] = (dataSnapshot.getChildrenCount());
                Log.d(TAG, "Size: " + size2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.e(TAG, databaseError.getMessage());
            }
        };
        moviesListRef.addValueEventListener(valueEventListener);

        return size2;
    }


}
