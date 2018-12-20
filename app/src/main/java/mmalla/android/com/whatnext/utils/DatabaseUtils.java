package mmalla.android.com.whatnext.utils;

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

import mmalla.android.com.whatnext.Movie;
import mmalla.android.com.whatnext.User;

/**
 *
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

    public void writeNewUser(String userId, User user){
        //database = FirebaseDatabase.getInstance().getReference();
        database.child(USERS).child(userId).setValue(user);
    }

    /**
     * A method to add a list of movies to the user's wishlist
     * @param movieList
     * @param userId
     */
    public void addMoviesListToUsersWishlist(List<Movie> movieList, String userId){

        DatabaseReference userWishlistRef = database.getRef().child(USERS).child(userId);

        for(Movie movie: movieList){
            HashMap<String, Object> map = new HashMap<>();
            map.put(movie.getMovieId(), movie);
            /**
             * Updates only unique ids which works for me so that way more than once we can add the same
             * movie into the wishlist and it will add only once
             */
            userWishlistRef.child(MOVIES).child(WISHLIST).updateChildren(map);
        }
    }

    /**
     *
     * Description:
     * Usage guide: du.removeFullMoviesListFromTheUser(mAuth.getCurrentUser().getUid()); where du
     * is the instance of the Database
     * A Method to clean up the whole movies list under the user
     * @param userId
     */
    public void removeFullMoviesListFromTheUser(String userId){
        /**
         * Removes the whole movies list from the user
         */
        database.getRef().child(USERS).child(userId).child(MOVIES).removeValue();

    }

    /**
     * A method to add a list of movies to the user's history
     * Usage guide: du.addMoviesListToUsersHistory(movies, mAuth.getCurrentUser().getUid());
     * @param movieList
     * @param userId
     */
    public void addMoviesListToUsersHistory(List<Movie> movieList, String userId){

        DatabaseReference userWishlistRef = database.getRef().child(USERS).child(userId);

        for(Movie movie: movieList){
            HashMap<String, Object> map = new HashMap<>();
            map.put(movie.getMovieId(), movie);
            /**
             * Updates only unique ids which works for me so that way more than once we can add the same
             * movie into the wishlist and it will add only once
             */
            userWishlistRef.child(MOVIES).child(HISTORY).updateChildren(map);
        }
    }


    /**
     * TODO Implement a method to update a particular movies data of a user
     */

    public List<Movie> getTheMoviesInTheUsersWishlist(String userId){


        return null;
    }

    /**
     * TODO Retrieve the wishlist list from the user
     */
    public List<Movie> readWishListFromUser(String userID){
        final List<Movie> wishlistMovies = new ArrayList<Movie>();

        FirebaseDatabase moviesList = FirebaseDatabase.getInstance();
        DatabaseReference moviesListRef = moviesList.getReference().child(USERS);

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

        return wishlistMovies;
    }


    /**
     * TODO Retrieve the history list from the user
     */


}
