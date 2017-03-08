package com.macnicol.popularmoviesproject;

public interface AsyncTaskCompleteListener {
    void onFetchMovieTaskPreExecute();
    void onFetchMovieTaskComplete(MovieItem[] movieItems);
}
