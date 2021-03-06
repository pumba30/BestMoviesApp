package com.pundroid.bestmoviesapp.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by pumba30 on 14.10.2015.
 */
public class DownloadHelperService {
    public static final String TAG = DownloadHelperService.class.getSimpleName();
    public static final String NUM_PAGE = "num_page";
    public static final String TYPE_MOVIES = "type_movies";
    public static final String IS_CONNECTED = "is_connected";
    public static final String MOVIE_ID = "movie_id";
    public static final String ACTOR = "actor";

    private Context mContext;

    public DownloadHelperService(Context context) {
        mContext = context;
    }

    public void downloadMoviesIntent(int numPage, String typeMovies, boolean isConnected) {
        Log.d(TAG, " downloadMoviesIntent");
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(NUM_PAGE, numPage);
        intent.putExtra(TYPE_MOVIES, typeMovies);
        intent.putExtra(IS_CONNECTED, isConnected);
        mContext.startService(intent);
    }

    public void downloadDetailMovie(int movieId, boolean isConnected){
        Log.d(TAG, " downloadDetailMovie");
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(MOVIE_ID, movieId);
        intent.putExtra(IS_CONNECTED, isConnected);
        mContext.startService(intent);
    }

    public void downloadActors(int movieId, boolean isConnected, String actor){
        Log.d(TAG, " downloadActors");
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(MOVIE_ID, movieId);
        intent.putExtra(IS_CONNECTED, isConnected);
        intent.putExtra(ACTOR, actor);
        mContext.startService(intent);

    }
}
