package com.johnbalderson.popularmoviesstage1.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnbalderson.popularmoviesstage1.R;
import com.johnbalderson.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();


    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the movie from the ArrayAdapter at the selected position
        Movie movie = getItem(position);

        //reuse the old views if you have them
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, parent, false);
        }

        //display the movie box art
        ImageView iconView = convertView.findViewById(R.id.image_iv);
        Picasso.get().load(movie.getPosterImage())
                .placeholder(R.drawable.tmdb)
                .error(R.drawable.tmdb)
                .into(iconView);

        //display the movie name
        TextView versionNameView = convertView.findViewById(R.id.tv_original_title);
        versionNameView.setText(movie.getOriginalTitle());


        return convertView;
    }
}