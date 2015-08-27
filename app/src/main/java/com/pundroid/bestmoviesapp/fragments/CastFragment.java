package com.pundroid.bestmoviesapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pundroid.bestmoviesapp.DetailMovieActivity;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.adapters.CastListAdapter;
import com.pundroid.bestmoviesapp.object.Actor;
import com.pundroid.bestmoviesapp.object.Movie;
import com.pundroid.bestmoviesapp.utils.DownloadAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pumba30 on 23.08.2015.
 */
public class CastFragment extends Fragment  {

    private static final String TAG = CastFragment.class.getSimpleName();
    public static final String ALL_CAST_ACTORS = "com.pundroid.bestmovieapp.all_cast_actors";
    private static CastFragment instance;
    private ArrayList<Actor> actors = new ArrayList<>();
  //  private int idMovie;

    public static  CastFragment newInstance () {
        if (instance == null) {
            instance = new CastFragment();
        }
        return instance;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        DetailMovieActivity detailMovieActivity = (DetailMovieActivity)activity;
        Movie movie = detailMovieActivity.movie;
        int  idMovie = movie.getId();
        String[] query = new String[]{ALL_CAST_ACTORS, String.valueOf(idMovie)};
        DownloadAsyncTask task = new DownloadAsyncTask();
        task.execute(query);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_tab_cast, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list_cast_actors);
        listView.setAdapter(new CastListAdapter(getActivity(),
                actors));

        Log.d(TAG, "onCreateView");
        return view;
    }

    private  ArrayList<Actor> getActors(String result) throws JSONException {
        ArrayList<Actor> actors = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.optJSONArray("cast");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject actorObject = (JSONObject) jsonArray.get(i);

            Actor actor = new Actor();
            actor.setCharacterActor(actorObject.optString("character"));
            actor.setNameActor(actorObject.optString("name"));
            actor.setPathToImageActor(actorObject.optString("profile_path"));
            actors.add(actor);
        }
        return actors;
    }



}

