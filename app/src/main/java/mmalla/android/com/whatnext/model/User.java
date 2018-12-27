package mmalla.android.com.whatnext.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(/*String username,*/ String email) {
        // TODO Add support for username too later
        //this.username = username;
        String email1 = email;
        List<Movie> movieList = new ArrayList<Movie>();
    }

}

