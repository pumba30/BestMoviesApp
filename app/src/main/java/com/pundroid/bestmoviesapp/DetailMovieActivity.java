package com.pundroid.bestmoviesapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.pundroid.bestmoviesapp.adapters.PagerTabSlideAdapter;
import com.pundroid.bestmoviesapp.fragments.CastFragment;
import com.pundroid.bestmoviesapp.fragments.CrewFragment;
import com.pundroid.bestmoviesapp.fragments.DetailMovieActivityFragment;
import com.pundroid.bestmoviesapp.fragments.GridMovieFragment;

public class DetailMovieActivity extends ActionBarActivity {
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //передали  idMovie для дальнейшей обработки

        movieId = getIntent().getExtras().getInt(GridMovieFragment.MOVIE_ID);
        setTitle(getIntent().getExtras().getString(GridMovieFragment.MOVIE_TITLE));

        Bundle args = new Bundle();
        args.putInt(GridMovieFragment.MOVIE_ID, movieId);
        DetailMovieActivityFragment.newInstance().setArguments(args);
        CrewFragment.newInstance().setArguments(args);
        CastFragment.newInstance().setArguments(args);


        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        PagerTabSlideAdapter movieDetailPagerAdapter
                = new PagerTabSlideAdapter(getSupportFragmentManager());


        pager.setAdapter(movieDetailPagerAdapter);


        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
