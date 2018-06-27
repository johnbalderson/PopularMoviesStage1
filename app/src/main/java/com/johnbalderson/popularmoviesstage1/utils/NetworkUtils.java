/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.johnbalderson.popularmoviesstage1.utils;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Networking Utilities
 *
 * buildUrl - build movie poster URL
 * getResponsefromHTTPUrl - build page stream
 *
 */
public class NetworkUtils extends AppCompatActivity {

    final static String BASE_MOVIEDB_URL = "http://api.themoviedb.org/3/movie/";
    final static String API_KEY_PARAM = "api_key";

    public static URL buildUrl(String filter, String apiKey) {

        /**
         *  Build URL to movie from data passed from MainActivity as well as API Key (apiKey)
         *  MainActivity checked to see if API Key was passed before getting here
         *  filter (filter) passed will be either popularity or top rating
        */

        Uri builtUri = Uri.parse(BASE_MOVIEDB_URL).buildUpon().appendPath(filter)
                .appendQueryParameter(API_KEY_PARAM, apiKey).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
}