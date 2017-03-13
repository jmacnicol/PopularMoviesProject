package com.macnicol.popularmoviesproject;


import android.os.AsyncTask;

import java.net.URL;

//---asynchronous instructions to get data based on URL
class FetchMovieTask extends AsyncTask<String, Void, MovieItem[]> {

    private String mMovieUrl;
    private AsyncTaskCompleteListener mTaskCompleteListener;

    FetchMovieTask(String movieUrl, AsyncTaskCompleteListener listener) {
        mMovieUrl = movieUrl;
        mTaskCompleteListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mTaskCompleteListener.onFetchMovieTaskPreExecute();
    }

    @Override
    protected MovieItem[] doInBackground(String... params) {
        URL movieDataUrl = MovieUtils.buildMovieUrl(mMovieUrl);

        try {
            String jsonMovieResponse = MovieUtils.getResponseFromHttpUrl(movieDataUrl);
            //---Process the JSON data and serialize the results
            return MovieUtils.processJsonData(jsonMovieResponse);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieItem[] movieItems) {
        mTaskCompleteListener.onFetchMovieTaskComplete(movieItems);
    }
}

