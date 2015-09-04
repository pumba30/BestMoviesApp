package com.pundroid.bestmoviesapp;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pundroid.bestmoviesapp.fragments.GridMovieFragment;

public class MainActivity extends ActionBarActivity {
    /**
     * IMPORTANT: DO NOT FORGET PLEASE REMOVE YOUR API KEY WHEN SHARING CODE PUBLICALLY!
     */

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String TAG_GRID_MOVIE_FRAGMENT = "com.pundroid.bestmoviesapp.gridMovieFragment";
    private AdView adView;
    private boolean isTabletModeDetermined = false;
    private boolean isTabletMode = false;
    public static boolean isLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Google Ads
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(android_id)
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        //********

        isLarge = isTablet(getApplicationContext());


        GridMovieFragment gridMovieFragment = new GridMovieFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_lin_lay, gridMovieFragment)
                .addToBackStack(TAG_GRID_MOVIE_FRAGMENT)
                .commit();


    }

    private boolean isTablet(Context context) {
        if (!isTabletModeDetermined) {
            if (context.getResources().getConfiguration().smallestScreenWidthDp >= 600)
                isTabletMode = true;
            isTabletModeDetermined = true;
        }
        return isTabletMode;
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
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }


    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


}
