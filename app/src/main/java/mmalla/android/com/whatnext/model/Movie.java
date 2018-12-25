package mmalla.android.com.whatnext.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author mmalla
 */

public class Movie implements Parcelable {

    protected Movie(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mPoster = in.readString();
        mReleaseYear = in.readString();
        mRating = in.readString();
        mOverview = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mReleaseYear);
        dest.writeString(mRating);
        dest.writeString(mOverview);
    }

    public enum PREFERENCE {
        WISHLISTED, DISLIKED, LIKED, IGNORED
    }

    private String mId;
    private PREFERENCE mPref;
    private String mTitle;
    private String mPoster;
    private String mReleaseYear;
    private String mOverview;
    private String mRating;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPoster() {
        return mPoster;
    }

    public void setmPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public String getmReleaseYear() {
        return mReleaseYear;
    }

    public void setmReleaseYear(String mReleaseYear) {
        this.mReleaseYear = mReleaseYear;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public Movie() {
    }

    public Movie(String mId, String mTitle) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mPref = PREFERENCE.IGNORED;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public PREFERENCE getmPref() {
        return mPref;
    }

    public void setmPref(PREFERENCE mPref) {
        this.mPref = mPref;
    }
}
