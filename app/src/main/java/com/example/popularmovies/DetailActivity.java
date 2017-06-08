package com.example.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Tayyab on 6/5/2017.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView titleView, ratingView, dateView, overviewView;
        ImageView posterView;

        titleView = (TextView) findViewById(R.id.detail_title_view);
        ratingView = (TextView) findViewById(R.id.detail_rating);
        dateView = (TextView) findViewById(R.id.detail_date);
        overviewView = (TextView) findViewById(R.id.detail_overview);
        posterView = (ImageView) findViewById(R.id.detail_poster_view);

        Bundle bundle = getIntent().getExtras();

        titleView.setText(bundle.getString("title"));
        String ratingText = getResources().getString(R.string.rating_txt)+bundle.getString("rating");
        ratingView.setText(ratingText);
        String releaseDateText = getResources().getString(R.string.date_txt)+bundle.getString("release_date");
        dateView.setText(releaseDateText);
        overviewView.setText(bundle.getString("overview"));

        final String URL = "http://image.tmdb.org/t/p/w185";
        String path = URL + bundle.getString("poster_path");

        Picasso.with(DetailActivity.this).load(path).into(posterView);

    }
}
