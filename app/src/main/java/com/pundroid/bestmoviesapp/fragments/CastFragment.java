package com.pundroid.bestmoviesapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.pundroid.bestmoviesapp.DetailMovieActivity;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.adapters.CastListAdapter;
import com.pundroid.bestmoviesapp.objects.Actor;
import com.pundroid.bestmoviesapp.objects.Credits;
import com.pundroid.bestmoviesapp.objects.Movie;
import com.pundroid.bestmoviesapp.utils.RestClient;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pumba30 on 23.08.2015.
 */
public class CastFragment extends Fragment {

    private static final String TAG = CastFragment.class.getSimpleName();
    public static final String API_ENDPOINT = "http://api.themoviedb.org/3";
    public static final String ALL_CAST_ACTORS = "com.pundroid.bestmovieapp.all_cast_actors";
    private static CastFragment instance;
    private ArrayList<Actor> actors = new ArrayList<>();
    private ListView listView;

    //  private int idMovie;

    public static CastFragment newInstance() {
        if (instance == null) {
            instance = new CastFragment();
        }
        return instance;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DetailMovieActivity detailMovieActivity = (DetailMovieActivity) getActivity();
        Movie movie = detailMovieActivity.movie;
        int idMovie = movie.getId();

        loadActorByMovieId(idMovie);

    }


    private void loadActorByMovieId(int idMovie) {
        //Use RestClient for each request
        RestClient.get().getActorOfMovie(idMovie, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                actors = credits.getActors();
                listView.setAdapter(new CastListAdapter(getActivity(), actors));

                if (actors.size() == 0) {
                    listView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Actors loading failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Actors loading failed");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_tab_cast, container, false);


        listView = (ListView) view.findViewById(R.id.list_cast_actors);

        Log.d(TAG, "onCreateView");
        return view;
    }


}

