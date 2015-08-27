package com.pundroid.bestmoviesapp.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.pundroid.bestmoviesapp.fragments.CastFragment;
import com.pundroid.bestmoviesapp.interfaces.AsyncResponse;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pumba30 on 25.08.2015.
 */
public class DownloadAsyncTask extends AsyncTask<String, Integer, String> {

    public static final String TAG = DownloadAsyncTask.class.getSimpleName();
    public static final String API_KEY = "d1a2f8dc42f6388052172df57a6aba41";
    private static final String REQUIRED_MOVIE_ID = "required_movie_id";
    public static final String SCHEME = "http";
    public static final String AUTHORITY = "api.themoviedb.org";
    public static final String SEGMENT_NUMBER_THREE = "3";
    public static final String SEGMENT_DISCOVER = "discover";
    public static final String SEGMENT_MOVIE = "movie";
    public static final String PARAMETER_APY_KEY = "api_key";
    public static final String PARAMETER_PAGE = "page";
    public static final String PARAMETER_QUERY = "query";
    public static final String GET_TOP_RATED_MOVIE = "get_top_rated_movie";
    public static final String CREDITS = "credits";

    public AsyncResponse response = null;

    // params - запрос latest, popular, similar и т.д.
    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        String movieJSONString = null;


        if (params.length == 0) {
            return null;
        }
        Log.d(TAG, "Build URL");
        //   http://api.themoviedb.org/3/discover/movie?api_key=d1a2f8dc42f6388052172df57a6aba41&page=1&query=top_rated
        //   http://api.themoviedb.org/3/movie/116741?api_key=d1a2f8dc42f6388052172df57a6aba41

        Uri.Builder builderUri = new Uri.Builder();


        // params[0] - REQUIRED_MOVIE_ID, params[1] - ID required film
        if (params[0].equals(REQUIRED_MOVIE_ID)) {
            builderUri.scheme(SCHEME).authority(AUTHORITY)
                    .appendPath(SEGMENT_NUMBER_THREE)
                    .appendPath(SEGMENT_MOVIE)
                    .appendPath(params[1])
                    .appendQueryParameter(PARAMETER_APY_KEY, API_KEY);
        }
        if(params[0].equals(GET_TOP_RATED_MOVIE)){
            // params[0] -  kind of query (top_rated и т.д.) params[1] - query, params[2]- page
            builderUri.scheme(SCHEME).authority(AUTHORITY)
                    .appendPath(SEGMENT_NUMBER_THREE)
                    .appendPath(SEGMENT_DISCOVER)
                    .appendPath(SEGMENT_MOVIE)
                    .appendQueryParameter(PARAMETER_APY_KEY, API_KEY)
                    .appendQueryParameter(PARAMETER_QUERY, params[1])
                    .appendQueryParameter(PARAMETER_PAGE, params[2]);
        }
        // http://api.themoviedb.org/3/movie/135397/credits?api_key=d1a2f8dc42f6388052172df57a6aba41
        if(params[0].equals(CastFragment.ALL_CAST_ACTORS)){
            builderUri.scheme(SCHEME).authority(AUTHORITY)
                    .appendPath(SEGMENT_NUMBER_THREE)
                    .appendPath(SEGMENT_MOVIE)
                    .appendPath(params[1])
                    .appendPath(CREDITS)
                    .appendQueryParameter(PARAMETER_APY_KEY, API_KEY);
        }

        String myUrl = builderUri.toString();
        Log.d(TAG, "MY URL: " + myUrl);

        try {
            URL urlMovie = new URL(myUrl);
            connection = (HttpURLConnection) urlMovie.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int status = connection.getResponseCode();
            Log.d(TAG, "Connection status " + status);

            InputStream inputStream = connection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line).append("/n");
            }

            if (stringBuffer.length() == 0) {
                return null;
            }

            movieJSONString = stringBuffer.toString();

            Log.d(TAG, movieJSONString);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        return movieJSONString;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            try {
                response.processFinish(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


