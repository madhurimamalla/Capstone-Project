package mmalla.android.com.whatnext.moviedbclient;

import android.net.Uri;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import mmalla.android.com.whatnext.model.Movie;
import timber.log.Timber;

public class MovieDBClient {

    private static final String TAG = MovieDBClient.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";

    private static final String SEARCH_URL = "https://api.themoviedb.org/3/search/movie";

    private static final String PATH_POPULAR_PARAM = "popular";

    private static final String SEARCH_PARAM = "similar";

    private final static String QUERY_PARAM = "api_key";

    private final static String LANG_PARAM = "language";

    private final static String PAGE_PARAM = "page";

    private final static String LANG_VALUE = "en_US";

    private final static String PAGE_VALUE = "1";

    /**
     * TODO Add your API key here
     */
    private final static String API_Key = "<<Insert-key>>";

    public MovieDBClient() {
    }

    /**
     * General method needed to get response from any API URL built above
     *
     * @param url
     * @return
     * @throws IOException
     */
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static URL buildUrl(String path) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(path)
                .appendQueryParameter(QUERY_PARAM, API_Key)
                .appendQueryParameter(LANG_PARAM, LANG_VALUE)
                .appendQueryParameter(PAGE_PARAM, PAGE_VALUE)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Timber.v(TAG, "Built URL " + url);
        return url;
    }

    /**
     * Description: https://api.themoviedb.org/3/movie/19404?api_key=<<api-key>></>&language=en-US
     *
     * @param id
     * @return
     */
    private static URL buildGetMovieDetailsUrl(String id) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter(QUERY_PARAM, API_Key)
                .appendQueryParameter(LANG_PARAM, LANG_VALUE)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Timber.v(TAG, "Built URL " + url);
        return url;
    }


    /**
     * Description: https://api.themoviedb.org/3/movie/338952/similar?api_key=<<api-key>></>&language=en-US&page=1
     *
     * @param id
     * @return
     */
    private static URL buildGetSimilarMoviesUrl(String id) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(id)
                .appendPath(SEARCH_PARAM)
                .appendQueryParameter(QUERY_PARAM, API_Key)
                .appendQueryParameter(LANG_PARAM, LANG_VALUE)
                .appendQueryParameter(PAGE_PARAM, PAGE_VALUE)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Timber.v(TAG, "Built URL " + url);

        return url;
    }

    /**
     * Method returns details of a movie by id
     *
     * @param id
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Movie getMovieDetailsById(String id) throws MovieDBClientException {

        Movie movieDetails = null;
        URL movieDetailUrl = buildGetMovieDetailsUrl(id);
        Timber.d(TAG, movieDetailUrl);
        try {
            String jsonMovieDetailsResponse = getResponseFromHttpUrl(movieDetailUrl);
            movieDetails = MovieDetailsJsonUtils.getMovieInformationFromJson(jsonMovieDetailsResponse);
        } catch (IOException | JSONException e) {
            throw new MovieDBClientException(e);
        }

        return movieDetails;
    }

    /**
     * Method returns all popular movies
     *
     * @return
     * @throws MovieDBClientException
     */
    public List<Movie> getPopularMovies() throws MovieDBClientException {
        List<Movie> movieList = null;

        URL moviesListUrl = buildUrl(PATH_POPULAR_PARAM);
        Timber.d(TAG, moviesListUrl);

        try {
            String jsonPopularMoviesResponse = getResponseFromHttpUrl(moviesListUrl);
            return MoviesListJsonUtils.getSimpleMoviesInformationFromJson(jsonPopularMoviesResponse);
        } catch (IOException | JSONException e) {
            throw new MovieDBClientException(e);
        }
    }


    /**
     * Method returns all similar movies compared to the movie id sent as the parameter
     *
     * @param id
     * @return
     * @throws MovieDBClientException
     */
    public List<Movie> getSimilarMovies(String id) throws MovieDBClientException {
        List<Movie> movieList = new ArrayList<Movie>();

        URL moviesListUrl = buildGetSimilarMoviesUrl(id);
        Timber.d(TAG, moviesListUrl);

        try {
            String jsonSimilarMoviesResponse = getResponseFromHttpUrl(moviesListUrl);
            return MoviesListJsonUtils.getSimpleMoviesInformationFromJson(jsonSimilarMoviesResponse);
        } catch (IOException | JSONException e) {
            throw new MovieDBClientException(e);
        }

    }

    /**
     *
     * @param length
     * @return
     * @throws MovieDBClientException
     */
    public List<Movie> getSomePopularMovies(int length) throws MovieDBClientException {

        List<Movie> movieList = new ArrayList<Movie>();
        movieList = getPopularMovies();

        return movieList.subList(0, length - 1);
    }

    /**
     * Get random number between a range
     */

    public int getRandomNumber(int fromNum, int toNum){

        final int min = fromNum + 1;
        final int max = toNum;
        final int random = new Random().nextInt((max - min) + 1) + min;

        return random;
    }

    /**
     * Get the wishlist of the user and pick a random id
     */




}
