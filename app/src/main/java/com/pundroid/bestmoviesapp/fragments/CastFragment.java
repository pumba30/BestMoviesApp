package com.pundroid.bestmoviesapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pundroid.bestmoviesapp.R;

/**
 * Created by pumba30 on 23.08.2015.
 */
public class CastFragment extends Fragment {

    private static CastFragment instance;

    public CastFragment() {
    }

    public static CastFragment newInstance() {
        if (instance == null) {
            instance = new CastFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_tab_cast,container,false);
    }
}
