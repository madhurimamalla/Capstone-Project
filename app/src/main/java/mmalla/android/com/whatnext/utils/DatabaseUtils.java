package mmalla.android.com.whatnext.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private final static String USERS = "users";

    public DatabaseReference database;

    public DatabaseUtils() {
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    public void writeNewUser(String userId, User user){
        //database = FirebaseDatabase.getInstance().getReference();
        database.child(USERS).child(userId).setValue(user);
    }

    /**
     * TODO Implement a method to add movies to a user
     */
    public void addMoviesSetToUser(List<Movie> movieList, String userId){
        // Check if the movies are already present, update the list or create the new list
        // TODO
    }

    /**
     * TODO Implement a method to remove the movies from a user
     */

    /**
     * TODO Implement a method to update a particular movies data of a user
     */


}
