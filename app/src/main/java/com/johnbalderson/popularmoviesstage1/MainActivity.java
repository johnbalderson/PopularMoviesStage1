package com.johnbalderson.popularmoviesstage1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import com.johnbalderson.popularmoviesstage1.model.Movie;
import com.johnbalderson.popularmoviesstage1.utils.MovieAdapter;
import com.johnbalderson.popularmoviesstage1.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> mMovieList;
    private GridView movieListView;
    private String sort_filter;

    String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    String mApiKey = "YOUR API KEY";

    /**
     *
     * Logo used for Picasso placeholder and launcher icon used with permission from themoviedb.org
     * https://www.themoviedb.org/about/logos-attribution
     *
     */

    public boolean isOnline() {
        // test for online connectivity
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting() ;

    }



    private ArrayList<Movie> extractDataFromJSON(String MoviesJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(MoviesJSON)) {
            return null;
        }

        // Create an empty ArrayList , then start adding movies
        final ArrayList<Movie> movieList = new ArrayList<>();
        movieList.clear();

        // Parse the JSON response string, throw JSONException if there's a problem.

        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(MoviesJSON);

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of movies.

            JSONArray moviesArray = baseJsonResponse.getJSONArray("results");

            // For each Movie in the MovieArray, create an Movie object
            for (int i = 0; i < moviesArray.length(); i++) {

                // Get a single movie at position i within the list of Movies
                JSONObject currentMovie = moviesArray.getJSONObject(i);

                // get the relevant information about the movie
                String original_title = currentMovie.getString("original_title");
                String plot_synopsis = currentMovie.getString("overview");
                String release_date = currentMovie.getString("release_date");
                String poster_path = currentMovie.getString("poster_path");
                String poster_url = BASE_POSTER_URL + poster_path;
                double user_rating = currentMovie.getDouble("vote_average");
                int id = currentMovie.getInt("id");

                // Create a new Movie object with the information
                Movie movie = new Movie(original_title, poster_url,
                        plot_synopsis, user_rating, release_date, id);

                // Add the new movie to the list of movies
                movieList.add(movie);

            }

        } catch (JSONException e) {

            Log.e("MainActivity", "Problem parsing JSON", e);
            e.printStackTrace();

        }

        // Return the list of movies
        return movieList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isOnline()) {
            // if not online, show dialog box and end app
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.online_error)
                    .setCancelable(false)
                    .setTitle(R.string.online_error_title)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                            System.exit(0);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }


        /** if API key has not been changed from the default string (user's API key not provided)
         *   show dialog box and end app
         *   */

        if (mApiKey == "YOUR API KEY") {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.api_error)
                    .setCancelable(false)
                    .setTitle(R.string.api_error_title)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                            System.exit(0);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {


            mMovieList = new ArrayList<>();

            // Get a reference to the GridView, and attach the MovieAdapter to it.
            movieListView = findViewById(R.id.movies_gridview);
            //default sort_filter
            sort_filter = getResources().getString(R.string.popular_filter);
            movieSearch(sort_filter, mApiKey);



            movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // Find the movie that was clicked on
                    Movie selectedMovie = mMovieAdapter.getItem(position);

                    // pass movie info via intent to details activity
                    Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                    intent.putExtra("TITLE", selectedMovie.getOriginalTitle());
                    intent.putExtra("SYNOPSIS", selectedMovie.getPlotSynopsis());
                    intent.putExtra("POSTER", selectedMovie.getPosterImage());
                    intent.putExtra("RELEASE_DATE", selectedMovie.getReleaseDate());
                    intent.putExtra("USER_RATING", String.valueOf(selectedMovie.getUserRating()));
                    intent.putExtra("ID", String.valueOf(selectedMovie.getMovieId()));

                    startActivity(intent);
                }
            });

        }
    }

    private void movieSearch(String filter, String apiKey) {
        // search for movies using sort filter and API key
        URL movieURL = NetworkUtils.buildUrl(filter, apiKey);
        new searchMovies().execute(movieURL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int option_id = item.getItemId();

        switch (option_id) {

            // sort movies by popularity
            case R.id.popular_sort:
                //popular
                sort_filter = getResources().getString(R.string.popular_filter);
                movieSearch(sort_filter, mApiKey);
                return true;

            // sort movies by top rating
            case R.id.top_rated_sort:
                //user rating
                sort_filter = getResources().getString(R.string.rating_filter);
                movieSearch(sort_filter, mApiKey);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private class searchMovies extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieSearchResults;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                mMovieList = extractDataFromJSON(movieSearchResults);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            mMovieAdapter = new MovieAdapter(MainActivity.this, mMovieList);
            movieListView.setAdapter(mMovieAdapter);

        }

    }


}
