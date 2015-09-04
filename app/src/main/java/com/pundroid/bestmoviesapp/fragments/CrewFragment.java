package com.pundroid.bestmoviesapp.fragments;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.adapters.CrewListAdapter;
import com.pundroid.bestmoviesapp.objects.Credits;
import com.pundroid.bestmoviesapp.objects.CrewMember;
import com.pundroid.bestmoviesapp.utils.RestClient;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pumba30 on 23.08.2015.
 */
public class CrewFragment extends Fragment  {
    private static final String TAG = CrewFragment.class.getSimpleName();
    private static CrewFragment instance;
    private int movieId;
    private AdView adView;

    private ListView listViewCrew;
    private ArrayList<CrewMember> crewMembers = new ArrayList<>();

    public static CrewFragment newInstance() {
        if (instance == null) {
            instance = new CrewFragment();
        }
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        int movieId = args.getInt(GridMovieFragment.MOVIE_ID);

        loadCrew(movieId);

    }

    private void loadCrew(int idMovie) {
        RestClient.get().getCreditsOfMovie(idMovie, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                if (credits != null) {
                    crewMembers = credits.getCrew();
                    listViewCrew.setAdapter(new CrewListAdapter(getActivity(), crewMembers));
                } else {
                    listViewCrew.setVisibility(View.GONE);
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
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_detail_tab_crew, container, false);

        // Google Ads
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(android_id)
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        //********

        listViewCrew = (ListView) view.findViewById(R.id.list_crew_movie);

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
