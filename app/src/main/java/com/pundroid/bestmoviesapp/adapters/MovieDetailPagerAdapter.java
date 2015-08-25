package com.pundroid.bestmoviesapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pundroid.bestmoviesapp.fragments.CastFragment;
import com.pundroid.bestmoviesapp.fragments.CrewFragment;
import com.pundroid.bestmoviesapp.fragments.DetailMovieActivityFragment;
import com.pundroid.bestmoviesapp.fragments.SimilarMovieFragment;

/**
 * Created by pumba30 on 23.08.2015.
 */
public class MovieDetailPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Movie", "Cast", "Crew", "Similar movie"};
    public static final int MOVIE = 0;
    public static final int CAST = 1;
    public static final int CREW = 2;
    public static final int SIMILAR_MOVIE = 3;

    public MovieDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if (position == MOVIE) {
            return DetailMovieActivityFragment.newInstance(); //todo  newInstance дожен быть один экземпляр
        }
        if (position == CAST) {
            return CastFragment.newInstance();
        }
        if (position == CREW) {
            return CrewFragment.newInstance();
        } else
            return SimilarMovieFragment.newInstance();
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
