package com.pundroid.bestmoviesapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.pundroid.bestmoviesapp.fragments.GridMovieFragment;

public class MainActivity extends ActionBarActivity {
    //https://www.themoviedb.org/talk/55d2cd9192514170bd0004f8
    /**
     * IMPORTANT: DO NOT FORGET PLEASE REMOVE YOUR API KEY WHEN SHARING CODE PUBLICALLY!
     */

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String TAG_GRID_MOVIE_FRAGMENT = "gridMovieFragment";
    GridMovieFragment gridMovieFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hideStatusBar();
        setContentView(R.layout.activity_main);



        gridMovieFragment = new GridMovieFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_lin_lay, gridMovieFragment)
                .addToBackStack(TAG_GRID_MOVIE_FRAGMENT)
                .commit();


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }



        @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



}
