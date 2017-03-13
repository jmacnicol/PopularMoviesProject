package com.macnicol.popularmoviesproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static com.macnicol.popularmoviesproject.Constants.API_KEY;

public class MainActivity
        extends AppCompatActivity
        implements MainGridAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<MovieItem[]> {
    //---key for savedInstanceState bundle
    private final static String DATA_MOVIE_ITEMS = "DATA_MOVIE_ITEMS";
    //---unique identifier for loader
    private static final int MOVIE_ITEMS_LOADER = 22;
    //---class views
    private ArrayList<MovieItem> mMovieItems;
    private ProgressBar mProgBar;
    private TextView mErrorView;
    //---URLs endpoints
    private static final String mPopularMovies = Constants.BASE_URL + Constants.SORT_SELECTION_POPULAR + Constants.KEY_PROMPT + API_KEY;
    private static final String mTopRatedMovies = Constants.BASE_URL + Constants.SORT_SELECTION_TOP_RATED + Constants.KEY_PROMPT + API_KEY;
    private String mMovieUrl = mPopularMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //---init views
        mProgBar = (ProgressBar) findViewById(R.id.progbar_main);
        mErrorView = (TextView) findViewById(R.id.text_error);

        loadMovies();
    }

    public void loadMovies() {
        Bundle movieBundle = new Bundle();
        movieBundle.putString(DATA_MOVIE_ITEMS, mMovieUrl);

        if (MovieUtils.isOnline(this)) {
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<MovieItem[]> loader = loaderManager.getLoader(MOVIE_ITEMS_LOADER);
            if (loader == null) {
                loaderManager.initLoader(MOVIE_ITEMS_LOADER, movieBundle, this);
            } else {
                loaderManager.restartLoader(MOVIE_ITEMS_LOADER, movieBundle, this);
            }
        } else {
            mErrorView.setVisibility(View.VISIBLE);
        }
    }

    //---Callback for grid adapter's interface
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(Constants.POSITION, mMovieItems.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //---Change title on action bar and fetch data based on menu selection
        switch(item.getItemId()) {
            case R.id.action_sort_popular:
                mMovieUrl = mPopularMovies;
                setTitle(getString(R.string.movies_popular));
                loadMovies();
                return true;
            case R.id.action_sort_rated:
                mMovieUrl = mTopRatedMovies;
                setTitle(getString(R.string.movies_top_rated));
                loadMovies();
                return true;
            default:
                Toast.makeText(this, getString(R.string.menu_error), Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //---Save data if state loss occurs (i.e. device configuration change, etc)
        outState.putParcelableArrayList(DATA_MOVIE_ITEMS, mMovieItems);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<MovieItem[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieItem[]>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null) {
                    return;
                }
                mProgBar.setVisibility(View.VISIBLE);
                onForceLoad();
            }

            @Override
            public MovieItem[] loadInBackground() {
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
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieItem[]> loader, MovieItem[] data) {
        if (data != null) {
            //---Hide error and initialize main views when data is received
            mMovieItems = new ArrayList<>(Arrays.asList(data));
            initGridProperties(mMovieItems);
            mErrorView.setVisibility(View.INVISIBLE);
            mProgBar.setVisibility(View.INVISIBLE);
        } else {
            //---error view
            mErrorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieItem[]> loader) {
        //---stub method
    }

    //---Helper function implemented to handle grid, adapter, data
    public void initGridProperties(ArrayList<MovieItem> movieItems) {
        RecyclerView mainGrid = (RecyclerView) findViewById(R.id.main_grid);
        MainGridAdapter adapter = new MainGridAdapter(MainActivity.this, movieItems);
        adapter.setClickListener(this);
        int numOfColumns = MovieUtils.getNumOfColumns(this);
        mainGrid.setLayoutManager(new GridLayoutManager(MainActivity.this, numOfColumns));
        mainGrid.setAdapter(adapter);
    }
}