package mmalla.android.com.whatnext.moviedbclient;

/**
 * @author mmalla
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mmalla.android.com.whatnext.model.Movie;

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

    private static final String PM_YEAR_OF_RELEASE = "release_date";

    private static final String PM_OVERVIEW = "overview";

    private static final String PM_RATING = "vote_average";

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

            Movie movie_object = new Movie(movieData.getString(PM_MOVIE_ID), movieData.getString(PM_MOVIE_TITLE));
            movie_object.setmPoster(movieData.getString(PM_IMG_PATH));
            movie_object.setmReleaseYear(movieData.getString(PM_YEAR_OF_RELEASE));
            movie_object.setmOverview(movieData.getString(PM_OVERVIEW));
            movie_object.setmRating(movieData.getString(PM_RATING));
            parsedMovieResults.add(movie_object);
        }
        return parsedMovieResults;
    }
}
