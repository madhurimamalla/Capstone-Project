package mmalla.android.com.whatnext.utils;

/**
 * @author mmalla
 */

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mmalla.android.com.whatnext.Movie;

/**
 * Description: This class is created to assist in parsing and understanding the JSON response given by APIs from TMDB
 * This parses data from the The Movie DataBase APIs explained in:  https://developers.themoviedb.org/3/movies/get-top-rated-movies &
 * https://developers.themoviedb.org/3/movies/get-popular-movies & https://developers.themoviedb.org/3/search/search-movies
 * having by paths /movie/popular & /movie/top_rated & /search/movie.
 */
public class MoviesListJsonUtils {

    private static final String PM_RESULTS = "results";

    private static final String PM_MOVIE_ID = "id";

    private static final String PM_MOVIE_TITLE = "title";

    private static final String PM_IMG_PATH = "poster_path";

    public static List<Movie> getSimpleMoviesInformationFromJson(String moviesJsonStr) throws JSONException {

        List<Movie> parsedMovieResults;

        JSONObject movieJson = new JSONObject(moviesJsonStr);

        JSONArray movielist = movieJson.getJSONArray(PM_RESULTS);

        parsedMovieResults = new ArrayList<>();

        /**
         * Run through the results list and get all the information needed
         */
        for (int i = 0; i < movielist.length(); i++) {
            JSONObject movieData = movielist.getJSONObject(i);

            Movie movie_object = new Movie(movieData.getString(PM_MOVIE_TITLE), movieData.getString(PM_IMG_PATH), movieData.getString(PM_MOVIE_ID));
            parsedMovieResults.add(movie_object);
        }
        return parsedMovieResults;
    }

    public static ContentValues[] getMovieContentValuesFromJson(String moviesJsonStr) throws JSONException {

        JSONObject movieJson = new JSONObject(moviesJsonStr);

        JSONArray movielistArray = movieJson.getJSONArray(PM_RESULTS);

        ContentValues[] movielistValues = new ContentValues[movielistArray.length()];

        for(int i = 0; i < movielistArray.length(); i++){

            JSONObject movieData = movielistArray.getJSONObject(i);

            ContentValues movieContentValue = new ContentValues();

            // TODO Fix this after deciding where to put the data
//            movieContentValue.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieData.getString(PM_MOVIE_ID));
//            movieContentValue.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieData.getString(PM_MOVIE_TITLE));
//            movieContentValue.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH, movieData.getString(PM_IMG_PATH));
//            movieContentValue.put(MovieContract.MovieEntry.COLUMN_FAVORITE, false);
//            movielistValues[i] = movieContentValue;
        }
        return movielistValues;
    }
}
