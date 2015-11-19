package com.pundroid.bestmoviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.adapters.PagerTabSlideAdapter;
import com.pundroid.bestmoviesapp.fragments.CastFragment;
import com.pundroid.bestmoviesapp.fragments.CrewFragment;
import com.pundroid.bestmoviesapp.fragments.DetailMovieActivityFragment;
import com.pundroid.bestmoviesapp.fragments.GridMovieFragment;
import com.pundroid.bestmoviesapp.objects.AccountState;
import com.pundroid.bestmoviesapp.objects.Favorite;
import com.pundroid.bestmoviesapp.objects.MovieDetails;
import com.pundroid.bestmoviesapp.objects.Status;
import com.pundroid.bestmoviesapp.utils.PrefUtils;
import com.pundroid.bestmoviesapp.utils.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailMovieActivity extends ActionBarActivity implements DetailMovieActivityFragment.IDataSendDetailMovie {
    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    public static final String KEY_IS_LOGIN = "com.pundroid.bestmoviesapp.key_is_login";
    private MovieDetails mMovieDetail;
    private boolean mIsLogin;
    private int mMovieId;
    private String mSessionId;
    public static AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(PrefUtils.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        mSessionId = preferences.getString(PrefUtils.KEY_SESSION_ID, null);
        mIsLogin = preferences.getBoolean(PrefUtils.KEY_USER_IS_IN_ACCOUNT, false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include_my_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled((true));
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Google Ads
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("adMob").build();
        adView.loadAd(adRequest);
        //********

        // idMovie transferred for further processing
        mMovieId = getIntent().getExtras().getInt(GridMovieFragment.MOVIE_ID);
        setTitle(getIntent().getExtras().getString(GridMovieFragment.MOVIE_TITLE));
        setTitle(getIntent().getExtras().getString(GridMovieFragment.MOVIE_TITLE));
        String s = getIntent().getExtras().getString(GridMovieFragment.MOVIE_TITLE);


        Log.d(TAG, "TITLE " + s);
        Bundle args = new Bundle();
        args.putInt(GridMovieFragment.MOVIE_ID, mMovieId);

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        PagerTabSlideAdapter movieDetailPagerAdapter
                = new PagerTabSlideAdapter(getSupportFragmentManager(), getApplicationContext());
        pager.setAdapter(movieDetailPagerAdapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        DetailMovieActivityFragment.newInstance().setArguments(args);
        CastFragment.newInstance().setArguments(args);
        CrewFragment.newInstance().setArguments(args);

    }



    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "On SaveInstanceState");
        outState.putBoolean(KEY_IS_LOGIN, mIsLogin);
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
            if (mIsLogin) {
                Toast.makeText(getApplicationContext(),
                        R.string.add_to_favorites, Toast.LENGTH_SHORT).show();
                checkIsAddedToFavorList(mMovieId, mSessionId);

            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.please_login, Toast.LENGTH_SHORT).show();
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

    private void checkIsAddedToFavorList(final int movieId, final String sessionId) {
        RestClient.get().getAccountStates(movieId, sessionId, new Callback<AccountState>() {
            @Override
            public void success(AccountState accountState, Response response) {
                if (!accountState.isFavorite()) {
                    addMovieToFavorite(movieId, sessionId);
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.film_added, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, getString(R.string.added_fail));
            }
        });
    }

    private void addMovieToFavorite(int movieId, String sessionId) {
        RestClient.get().addMovieToFavorites(movieId, sessionId,
                new Favorite("movie", movieId, true), new Callback<Status>() {
                    @Override
                    public void success(Status status, Response response) {
                        Toast.makeText(getApplicationContext(),
                                "Successful added to favorites", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "An error occurred while adding movie to  favorites");
                    }
                });
    }

    private void startSearch() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
        finish();
    }

    // share title of movies and overview
    private void startShare() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        String title = mMovieDetail.getTitle();
        String overview = mMovieDetail.getOverview();
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
    public void onDataSendDetailMovie(MovieDetails movieDetail) {
        this.mMovieDetail = movieDetail;
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
