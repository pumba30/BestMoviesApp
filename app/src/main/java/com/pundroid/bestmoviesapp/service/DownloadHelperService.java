package com.pundroid.bestmoviesapp.service;

import android.content.Context;
import android.content.Intent;

/**
 * Created by pumba30 on 14.10.2015.
 */
public class DownloadHelperService {
    public static final String TAG = DownloadHelperService.class.getCanonicalName();
    public static final String NUM_PAGE = "num_page";
    public static final String TYPE_MOVIES = "type_movies";
    public static final String IS_CONNECTED = "is_connected";

    private Context mContext;
    private boolean mIsConnected;


    public DownloadHelperService(Context context, boolean isConnected) {
        mContext = context;
        mIsConnected = isConnected;
    }

    public void downloadMovieIntent(int numPage, String typeMovies) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(NUM_PAGE, numPage);
        intent.putExtra(TYPE_MOVIES, typeMovies);
        intent.putExtra(IS_CONNECTED, mIsConnected);
        mContext.startService(intent);
    }
}
