package com.macnicol.popularmoviesproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static final String BASE_IMG_URL = Constants.BASE_IMG_URL + Constants.SIZE_185;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //---Retrieve meta-data from intent
        Intent detailsIntent = getIntent();
        MovieItem movieItem = detailsIntent.getParcelableExtra(Constants.POSITION);

        //---Init views
        //---TODO: Include Butter Knife to inject views
        TextView titleCard = (TextView) findViewById(R.id.text_movie_title);
        ImageView thumbnail = (ImageView) findViewById(R.id.img_poster_thumbnail);
        TextView releaseDate = (TextView) findViewById(R.id.text_year);
        TextView rating = (TextView) findViewById(R.id.text_usr_rating);
        TextView synopsis = (TextView) findViewById(R.id.text_synopsis);

        //---Set views
        if (movieItem != null) {
            titleCard.setText(movieItem.getMovieTitle());
            Picasso.with(this)
                    .load(BASE_IMG_URL + movieItem.getPosterPath())
                    .placeholder(R.color.colorPrimary)
                    .error(R.mipmap.ic_launcher)
                    .into(thumbnail);
            releaseDate.setText(MovieUtils.formatDate(movieItem.getReleaseDate()));
            rating.setText(movieItem.getVoteAverage().concat("/10"));
            synopsis.setText(movieItem.getOverview());
        }
    }
}
