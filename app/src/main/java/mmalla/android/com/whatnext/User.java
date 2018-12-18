package mmalla.android.com.whatnext;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {

   // public String username;
    public String email;
    public List<Movie> movieList;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(/*String username,*/ String email) {
        // TODO Add support for username too later
        //this.username = username;
        this.email = email;
        this.movieList = new ArrayList<Movie>();
    }

}

