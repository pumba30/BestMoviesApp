package com.pundroid.bestmoviesapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pundroid.bestmoviesapp.fragments.CastFragment;
import com.pundroid.bestmoviesapp.fragments.CrewFragment;
import com.pundroid.bestmoviesapp.fragments.DetailMovieActivityFragment;
import com.pundroid.bestmoviesapp.fragments.SimilarMovieFragment;

/**
 * Created by pumba30 on 27.08.2015.
 */
public class PagerTabSlideAdapter extends FragmentPagerAdapter {


    private final String[] TITLES = {"Movie", "Cast", "Crew", "Similar movie"};


    public PagerTabSlideAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }


    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return DetailMovieActivityFragment.newInstance();
            case 1:
                return  CastFragment.newInstance();
            case 2:
                return CrewFragment.newInstance();
            case 3:
                return SimilarMovieFragment.newInstance();
        }
        return null;
    }


}
