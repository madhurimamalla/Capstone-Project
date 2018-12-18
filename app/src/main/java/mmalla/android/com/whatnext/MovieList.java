package mmalla.android.com.whatnext;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Description: Use this class to add and retrieve data into Firebase against the user
 *
 * @author mmalla
 */
public class MovieList implements Parcelable{

    public ArrayList<MovieList> movieList;

    protected MovieList(Parcel in) {
        movieList = in.createTypedArrayList(MovieList.CREATOR);
    }

    public static final Creator<MovieList> CREATOR = new Creator<MovieList>() {
        @Override
        public MovieList createFromParcel(Parcel in) {
            return new MovieList(in);
        }

        @Override
        public MovieList[] newArray(int size) {
            return new MovieList[size];
        }
    };

    public List<MovieList> getMovieList() {
        return movieList;
    }

    public void setMovieList(ArrayList<MovieList> movieList) {
        this.movieList = movieList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(movieList);
    }
}
