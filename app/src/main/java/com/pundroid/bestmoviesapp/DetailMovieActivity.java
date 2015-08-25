package com.pundroid.bestmoviesapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.pundroid.bestmoviesapp.adapters.MovieDetailPagerAdapter;
import com.pundroid.bestmoviesapp.fragments.DetailMovieActivityFragment;
import com.pundroid.bestmoviesapp.object.Movie;

public class DetailMovieActivity extends ActionBarActivity {
    public Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //передали  объект Movie для дальнейшей обработки

        movie = getIntent().getExtras().getParcelable(Movie.MOVIE_OBJECT);
        if (movie.getOriginalTitle() != null) {
            getSupportActionBar().setTitle(movie.getOriginalTitle());
        }


        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new MovieDetailPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        DetailMovieActivityFragment  detailMovieActivityFragment
                = DetailMovieActivityFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Movie.MOVIE_OBJECT, movie);
        detailMovieActivityFragment.setArguments(bundle);





//        Fragment fragmentDetail = new DetailMovieActivityFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.container_detail_activity, fragmentDetail)
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .commit();


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
