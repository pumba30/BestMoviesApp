package com.pundroid.bestmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.pundroid.bestmoviesapp.adapters.PagerTabSlideAdapter;
import com.pundroid.bestmoviesapp.fragments.CastFragment;
import com.pundroid.bestmoviesapp.fragments.CrewFragment;
import com.pundroid.bestmoviesapp.fragments.DetailMovieActivityFragment;
import com.pundroid.bestmoviesapp.fragments.GridMovieFragment;
import com.pundroid.bestmoviesapp.objects.MovieDetail;

public class DetailMovieActivity extends ActionBarActivity implements DetailMovieActivityFragment.IDataSendDetailMovie {
    private MovieDetail movieDetail;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // idMovie transferred for further processing
        int movieId = getIntent().getExtras().getInt(GridMovieFragment.MOVIE_ID);
        setTitle(getIntent().getExtras().getString(GridMovieFragment.MOVIE_TITLE));
        isLogin = getIntent().getExtras().getBoolean(GridMovieFragment.IS_LOGIN);


        Bundle args = new Bundle();
        args.putInt(GridMovieFragment.MOVIE_ID, movieId);


        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        PagerTabSlideAdapter movieDetailPagerAdapter
                = new PagerTabSlideAdapter(getSupportFragmentManager());


        pager.setAdapter(movieDetailPagerAdapter);


        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        DetailMovieActivityFragment.newInstance().setArguments(args);
        CrewFragment.newInstance().setArguments(args);
        CastFragment.newInstance().setArguments(args);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_movie, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add_to_favorites) {
            if (isLogin) {
                Toast.makeText(getApplicationContext(),
                        "Add to favorites", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getApplicationContext(),
                        "Please,  login!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (item.getItemId() == R.id.action_search_detail_movie) {
            startSearch();
            return true;
        }

        if (item.getItemId() == R.id.action_share_movie) {
            startShare();
        }

        return super.onOptionsItemSelected(item);
    }

    private void startSearch() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
        finish();
    }

    private void startShare() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        String title = movieDetail.getTitle();
        String overview = movieDetail.getOverview();
        StringBuilder stringBuilder = new StringBuilder();

        String res = stringBuilder.append(title)
                .append("\n")
                .append("\n")
                .append(overview).toString();

        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, res);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));


    }

    // get data from fragment
    @Override
    public void onDataSendDetailMovie(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
    }

}
