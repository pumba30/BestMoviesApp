package com.pundroid.bestmoviesapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.fragments.CastFragment;
import com.pundroid.bestmoviesapp.fragments.CrewFragment;
import com.pundroid.bestmoviesapp.fragments.DetailMovieActivityFragment;

/**
 * Created by pumba30 on 27.08.2015.
 */
public class PagerTabSlideAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = PagerTabSlideAdapter.class.getSimpleName();
    private final String[] TITLES;

    public PagerTabSlideAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLES = context.getResources()
                .getStringArray(R.array.titles_pager_tab);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return DetailMovieActivityFragment.newInstance();
            case 1:
                return CastFragment.newInstance();
            case 2:
                return CrewFragment.newInstance();
        }
        return null;
    }


}

