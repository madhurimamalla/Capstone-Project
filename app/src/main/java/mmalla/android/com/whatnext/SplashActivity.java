package mmalla.android.com.whatnext;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import mmalla.android.com.whatnext.utils.MoviesListJsonUtils;
import mmalla.android.com.whatnext.utils.NetworkUtils;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String PATH_POPULAR_PARAM = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.wishlist).setOnClickListener(this);


        class FetchMoviesList extends AsyncTask<String, Void, List<Movie>> {

            @Override
            protected void onPreExecute() {
               /* recyclerView.setVisibility(View.INVISIBLE);
                mLoadingIcon.setVisibility(View.VISIBLE);*/
            }

            @Override
            protected List<Movie> doInBackground(String... params) {

                if (params.length == 0) {
                    return null;
                }

                String path = params[0];

                /**
                 * Get the URL to fetch the Popular movies
                 */
                // URL moviesListUrl = NetworkUtils.buildSearchMoviesUrl(path);
                /**
                 * For Search movie API fetch
                 */
                URL moviesListUrl = NetworkUtils.buildSearchMoviesUrl("Matrix");
                try {
                    String jsonPopularMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesListUrl);
                    return MoviesListJsonUtils.getSimpleMoviesInformationFromJson(jsonPopularMoviesResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Movie> mList) {
                if (mList != null) {
//                    mErrorMessage.setVisibility(View.INVISIBLE);
//                    showMovieThumbnails();
//                    moviesList.clear();
//                    moviesList.addAll(mList);
//                    moviesAdapter.notifyDataSetChanged();
                } else {
//                    showLoading();
                }
            }
        }

        new FetchMoviesList().execute(PATH_POPULAR_PARAM);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.wishlist){
            // Testing Feature Activity launch from here
            Intent intent = new Intent(this, FeatureActivity.class);
            startActivity(intent);
            // TODO Launch the Feature Activity with the fragment of wishlist in it
        } else if(i == R.id.discover){
            // TODO Launch the Feature Activity with the fragment of discover in it
        } else if(i == R.id.history){
            // TODO Launch the History Activity with the fragment of discover in it
        }
    }
}
