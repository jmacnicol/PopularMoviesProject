package com.macnicol.popularmoviesproject;

import android.os.Parcel;
import android.os.Parcelable;

//---parcelable class intended to hold metadata of movie objects
class MovieItem implements Parcelable {

    private String mPosterPath;
    private String mOverview;
    private String mReleaseDate;
    private String mMovieTitle;
    private String mVoteAverage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mMovieTitle);
        dest.writeString(mVoteAverage);
    }

    //---Initialize the MovieItem object
    MovieItem(String posterPath, String overview, String releaseDate, String movieTitle, String voteAverage) {
        mPosterPath = posterPath;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mMovieTitle = movieTitle;
        mVoteAverage = voteAverage;
    }

    private MovieItem(Parcel in) {
        mPosterPath = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mMovieTitle = in.readString();
        mVoteAverage = in.readString();
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {

        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    String getPosterPath() {
        return mPosterPath;
    }

    String getOverview() {
        return mOverview;
    }

    String getReleaseDate() {
        return mReleaseDate;
    }

    String getMovieTitle() {
        return mMovieTitle;
    }

    String getVoteAverage() {
        return mVoteAverage;
    }
}
