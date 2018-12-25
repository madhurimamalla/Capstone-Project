package mmalla.android.com.whatnext.features.discovery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import mmalla.android.com.whatnext.model.Movie;

public class DiscoverPagerAdapter extends FragmentPagerAdapter {

    private List<Movie> movieList;

    public DiscoverPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setList(List<Movie> list){
        this.movieList = list;
    }

    @Override
    public Fragment getItem(int i) {
        /**
         * Send the ith movies mId
         */
        return DiscoverFragment.newInstance(movieList.get(i));
    }

    @Override
    public int getCount() {
        return movieList.size();
    }
}
