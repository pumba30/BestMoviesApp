package com.pundroid.bestmoviesapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.activity.BiographyActorActivity;
import com.pundroid.bestmoviesapp.activity.DetailMovieActivity;
import com.pundroid.bestmoviesapp.adapters.CastListAdapter;
import com.pundroid.bestmoviesapp.objects.Actor;
import com.pundroid.bestmoviesapp.service.DownloadHelperService;

import java.util.ArrayList;

/**
 * Created by pumba30 on 23.08.2015.
 */
public class CastFragment extends Fragment {

    private static final String TAG = CastFragment.class.getSimpleName();
    public static final String ACTOR_ID = "actor_id";
    public static final String ACTOR = "actor";
    public static final String ACTORS_DATA = "actors_data";
    public static final String ACTION_SEND_ACTORS_DATA = "action_send_actors_data";
    private static CastFragment instance;
    private ArrayList<Actor> actors = new ArrayList<>();
    private ListView listView;
    private ResultActorsDataReceiver mReceiver = new ResultActorsDataReceiver();


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
        DownloadHelperService helperService = new DownloadHelperService(getActivity());
        helperService.downloadActors(movieId, isConnected(), ACTOR);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // get out ads from last list's item
                int lastVisiblePosition = view.getLastVisiblePosition() + 1;
                if (lastVisiblePosition == (totalItemCount - 1)) {
                    DetailMovieActivity.adView.setVisibility(View.INVISIBLE);
                } else if (lastVisiblePosition < (totalItemCount - 1)) {
                    DetailMovieActivity.adView.setVisibility(View.VISIBLE);
                }

            }
        });
        return view;
    }

    //check internet connection
    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, new IntentFilter(ACTION_SEND_ACTORS_DATA));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    public class ResultActorsDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                actors = (ArrayList<Actor>) intent.getSerializableExtra(ACTORS_DATA);
                listView.setAdapter(new CastListAdapter(getActivity(), actors));
            } else {
                listView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Actors loading failed",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}

