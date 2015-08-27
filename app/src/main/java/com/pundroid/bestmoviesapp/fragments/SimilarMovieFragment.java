package com.pundroid.bestmoviesapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pundroid.bestmoviesapp.R;

/**
 * Created by pumba30 on 23.08.2015.
 */
public class SimilarMovieFragment extends Fragment {
    private static final String TAG =SimilarMovieFragment.class.getSimpleName();
    private static SimilarMovieFragment instance;

//    public SimilarMovieFragment() {
//    }

        public static SimilarMovieFragment newInstance() {
        if (instance == null) {
            instance = new SimilarMovieFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_detail_tab_similar_movie
                ,container,false);
    }
}
