package com.pundroid.bestmoviesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pundroid.bestmoviesapp.BiographyActorActivity;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.adapters.CastListAdapter;
import com.pundroid.bestmoviesapp.objects.Actor;
import com.pundroid.bestmoviesapp.objects.Credits;
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
    public static final String ACTOR_ID = "actor_id";
    private static CastFragment instance;
    private ArrayList<Actor> actors = new ArrayList<>();
    private ListView listView;
    private int movieId;


    public static CastFragment newInstance() {
        if (instance == null) {
            instance = new CastFragment();
        }
        return instance;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Bundle args = getArguments();
        int movieId = args.getInt(GridMovieFragment.MOVIE_ID);
        loadActorByMovieId(movieId);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void loadActorByMovieId(final int idMovie) {
        //Use RestClient for each request
        RestClient.get().getCreditsOfMovie(idMovie, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                if (credits != null) {
                    actors = credits.getActors();
                    listView.setAdapter(new CastListAdapter(getActivity(), actors));
                } else {
                    listView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Actors loading failed",
                            Toast.LENGTH_SHORT).show();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BiographyActorActivity.class);
                long actorId = actors.get(position).getId();
                intent.putExtra(ACTOR_ID, actorId);
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

    }


}

