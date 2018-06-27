package com.johnbalderson.popularmoviesstage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        // allow for back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(MovieDetailsActivity.this, "No movie data",
                    Toast.LENGTH_SHORT).show();

        } else {


            String poster_image = extras.getString("POSTER");
            if (poster_image != null) {

                ImageView iconView = findViewById(R.id.iv_poster);
                Picasso.get()
                        .load(poster_image)
                        .placeholder(R.drawable.tmdb)
                        .error(R.drawable.tmdb)
                        .into(iconView);


            }


            // get data passed via intent extras

            // get title
            String name = extras.getString("TITLE");
            if (name != null) {

                TextView nameTextView = findViewById(R.id.tv_original_title);
                nameTextView.setText(name);

            }

            // get release date
            String release_date = extras.getString("RELEASE_DATE");
            if (release_date != null) {

                TextView releaseDateTextView = findViewById(R.id.tv_release_date);
                releaseDateTextView.setText(Html.fromHtml(formatDate(release_date)));
               }

            // get plot (synopsis)
            String plot_synopsis = extras.getString("SYNOPSIS");
            if (plot_synopsis != null) {

                TextView synopsisTextView = findViewById(R.id.tv_synopsis);
                synopsisTextView.setText(plot_synopsis);
            }

            // get user rating
            String user_rating = extras.getString("USER_RATING");
            if (user_rating != null) {

                TextView ratingTextView = findViewById(R.id.tv_user_rating);
                ratingTextView.setText(user_rating + " /10");
            }

            // get movie Id
            String movie_id = extras.getString("ID");
            Log.i("movie_id", movie_id);

        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // check to see if back arrow pressed
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * formatDate
     *
     * Input parameter - default format from themoviedb as yyyy-mm-dd
     * Output parameter - "normal" format of MMMM dd, yyyy such as July 4, 2018
     *
     */
    private String formatDate(String dateStr){
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMMM d, yyyy";
        SimpleDateFormat simpleDateInputFormat, simpleDateOutputFormat;
        try{
            simpleDateInputFormat = new SimpleDateFormat(inputPattern);
            Date date = simpleDateInputFormat.parse(dateStr);
            simpleDateOutputFormat = new SimpleDateFormat(outputPattern);
            dateStr = simpleDateOutputFormat.format(date);
            return dateStr;
        }catch (ParseException pe){

        }
        return null;
    }
}