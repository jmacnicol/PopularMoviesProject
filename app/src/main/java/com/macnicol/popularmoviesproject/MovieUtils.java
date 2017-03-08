package com.macnicol.popularmoviesproject;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

//--Helper class used for various tasks
class MovieUtils {

    private MovieUtils() {
        //---do not instantiate
    }

    //---establish connection and open input stream - reusing method from Udacity's "Sunshine" project
    static String getResponseFromHttpUrl(URL url) throws IOException {
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

    //---encode URL - basic idea is from Udacity's "Sunshine" project
    static URL buildMovieUrl(String selection) {
        Uri movieUrl;
        movieUrl = Uri.parse(selection).buildUpon().build();

        URL url = null;
        try {
            url = new URL(movieUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //---serialize data from JSON
    static MovieItem[] processJsonData(String jsonString) {
        try {
            JSONObject baseJsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = baseJsonObject.getJSONArray("results");
            MovieItem[] movieItems = new MovieItem[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String posterPath = jsonObject.getString("poster_path");
                String overview = jsonObject.getString("overview");
                String releaseDate = jsonObject.getString("release_date");
                String title = jsonObject.getString("title");
                String voteAvg = jsonObject.getString("vote_average");
                MovieItem item = new MovieItem(posterPath, overview, releaseDate, title, voteAvg);
                movieItems[i] = item;
            }
            return movieItems;
        } catch (JSONException e) {
            e.getMessage();
            return null;
        }
    }

    static int getNumOfColumns(Context context) {
        final int MIN_NUM_COLS = 2;
        final int gridViewEntrySize = (int)context.getResources().getDimension(R.dimen.poster_dimens);
        final int gridViewSpacing = (int)context.getResources().getDimension(R.dimen.grid_spacing);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int num = size.x;

        int column = (num - gridViewSpacing) / (gridViewEntrySize + gridViewSpacing);
        if (column < MIN_NUM_COLS) return MIN_NUM_COLS; //---make sure that at least two columns are being displayed
        return column;
    }


    static String formatDate (String rawDate) {
        //---Strip out the hyphens in the returned date value
        String[] partitionStr = rawDate.split("-");

        //---Get integer values from the string types
        int year = Integer.parseInt(partitionStr[0]);
        int month = Integer.parseInt(partitionStr[1]);
        int day = Integer.parseInt(partitionStr[2]);

        //---Setup the Java calendar instance
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        //---Format as per the text parameter
        return new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(calendar.getTime());
    }

    //---Helper method to check device connectivity
    static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
