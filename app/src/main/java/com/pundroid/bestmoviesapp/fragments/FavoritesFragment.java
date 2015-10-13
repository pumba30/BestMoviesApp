package com.pundroid.bestmoviesapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pundroid.bestmoviesapp.activity.DetailMovieActivity;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.adapters.SearchMovieAdapter;
import com.pundroid.bestmoviesapp.objects.Favorite;
import com.pundroid.bestmoviesapp.objects.MovieDetail;
import com.pundroid.bestmoviesapp.objects.QueryResultMovies;
import com.pundroid.bestmoviesapp.objects.Status;
import com.pundroid.bestmoviesapp.utils.PrefUtils;
import com.pundroid.bestmoviesapp.utils.RestClient;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pumba30 on 02.09.2015.
 */
public class FavoritesFragment extends Fragment {
    private static final String TAG = FavoritesFragment.class.getSimpleName();
    private ListView listView;
    private ArrayList<MovieDetail> listFavorites = new ArrayList<>();
    private SearchMovieAdapter adapter;
    private String sessionId;
    private int userId;
    private int page = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getActivity()
                .getSharedPreferences(PrefUtils.KEY_SHARED_PREF, Context.MODE_PRIVATE);


        userId = preferences.getInt(PrefUtils.KEY_SESSION_USER_ID, 0);// !!!!
        sessionId = preferences.getString(PrefUtils.KEY_SESSION_ID, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);


        listView = (ListView) view.findViewById(R.id.listView_favorites);


        getFavoritesList(userId, sessionId, page);

        return view;
    }

    private void showDetailMovie(ArrayList<MovieDetail> listFavorites, int position) {
        int movieId = listFavorites.get(position).getId();

        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra(GridMovieFragment.MOVIE_ID, movieId);
        startActivity(intent);
        // getActivity().finish();
    }

    public void getFavoritesList(int userId, String sessionId, int page) {
        RestClient.get().getFavoritesMovies(userId, sessionId, page, new Callback<QueryResultMovies>() {
            @Override
            public void success(QueryResultMovies queryResultMovies, Response response) {
                listFavorites = queryResultMovies.getResults();
                adapter = new SearchMovieAdapter(getActivity(), listFavorites);
                listView.setAdapter(adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        showDetailMovie(listFavorites, position);
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        dialogDeleteMovie(listFavorites, position);
                        return true;
                    }
                });
            }


            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Favorites list fail");
            }
        });
    }

    private void dialogDeleteMovie(final ArrayList<MovieDetail> listFavorites, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to remove this movie?")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int movieId = listFavorites.get(position).getId();
                                deleteMovie(movieId);
                                Toast.makeText(getActivity(), "Delete!", Toast.LENGTH_SHORT).show();
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteMovie(int movieId) {

        Favorite favorite = new Favorite("movie", movieId, false);

        RestClient.get().addMovieToFavorites(userId, sessionId, favorite, new Callback<Status>() {
            @Override
            public void success(Status status, Response response) {
                Toast.makeText(getActivity(), "Movie deleted", Toast.LENGTH_SHORT).show();
                getFavoritesList(userId, sessionId, page);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Failed to remove");
            }
        });

    }
}
