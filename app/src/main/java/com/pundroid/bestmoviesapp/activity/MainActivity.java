package com.pundroid.bestmoviesapp.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.fragments.GridMovieFragment;

public class MainActivity extends ActionBarActivity {
    /**
     * IMPORTANT: DO NOT FORGET PLEASE REMOVE YOUR API KEY WHEN SHARING CODE PUBLICALLY!
     */

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String TAG_GRID_MOVIE_FRAGMENT = "com.pundroid.bestmoviesapp.gridMovieFragment";
    public static final String AD_MOB = "adMob";
    private AdView mAdView;
    private boolean mIsTabletModeDetermined = false;
    private boolean mIsTabletMode = false;
    public static boolean sIsLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Google Ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent(AD_MOB).build();
        mAdView.loadAd(adRequest);
        //********

        sIsLarge = isTablet(getApplicationContext());

        GridMovieFragment gridMovieFragment = new GridMovieFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_lin_lay, gridMovieFragment)
                .addToBackStack(TAG_GRID_MOVIE_FRAGMENT)
                .commit();
    }

    private boolean isTablet(Context context) {
        if (!mIsTabletModeDetermined) {
            if (context.getResources().getConfiguration().smallestScreenWidthDp >= 600)
                mIsTabletMode = true;
            mIsTabletModeDetermined = true;
        }
        return mIsTabletMode;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}
