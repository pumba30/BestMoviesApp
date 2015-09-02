package com.pundroid.bestmoviesapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pundroid.bestmoviesapp.DetailMovieActivity;
import com.pundroid.bestmoviesapp.LoginActivity;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.adapters.GridMovieFragmentAdapter;
import com.pundroid.bestmoviesapp.adapters.NavDrawerListAdapter;
import com.pundroid.bestmoviesapp.objects.MovieDetail;
import com.pundroid.bestmoviesapp.objects.QueryResultMovies;
import com.pundroid.bestmoviesapp.slidingmenu.NavDrawerItem;
import com.pundroid.bestmoviesapp.utils.PrefUtils;
import com.pundroid.bestmoviesapp.utils.RestClient;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pumba30 on 19.08.2015.
 */
public class GridMovieFragment extends Fragment {
    public static final String TAG = GridMovieFragment.class.getSimpleName();
    public static final String MOVIE_TITLE = "com.pundroid.bestmoviesapp.movie_title";
    public static final String IS_LOGIN = "com.pundroid.bestmoviesapp_isLogin";
    public static String MOVIE_ID = "com.pundroid.bestmoviesapp.movie_id";
    private GridView gridView;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ActionBarDrawerToggle drawerToggle;
    private int numPage = 1;
    private ArrayList<MovieDetail> movieDetails = new ArrayList<>();
    private String typeMovies = RestClient.TOP_RATED_MOVIES;
    private ProgressBar progressBar;
    private String userName = " guest!";
    private boolean isLogin;

    public GridMovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SharedPreferences preferences = getActivity()
                .getSharedPreferences(PrefUtils.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        isLogin = preferences.getBoolean(PrefUtils.KEY_USER_IN_ACCOUNT, false);

        if (isLogin) {
            userName = " " + preferences.getString(PrefUtils.KEY_SESSION_USER_USERNAME, " guest!") + "!";
        }


        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isConnected()) {
            // сюда передаем различные типы фильмов
            downloadMovies(numPage, typeMovies);
            toastShowPageNumber();
        } else {
            Toast.makeText(getActivity(),
                    "Internet connection failed", Toast.LENGTH_SHORT).show();
        }

    }

    private void downloadMovies(int numPage, String typeMovies) {
        progressBar.setVisibility(View.VISIBLE);

        if (typeMovies.equals(RestClient.TOP_RATED_MOVIES)) {
            RestClient.get().getMoviesByType(numPage, typeMovies,
                    new Callback<QueryResultMovies>() {
                        @Override
                        public void success(QueryResultMovies queryResultMovies, Response response) {
                            getMoviesByType(queryResultMovies);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(TAG, "Download failed");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }

        if (typeMovies.equals(RestClient.POPULAR_MOVIES)) {
            RestClient.get().getPopularMovies(numPage, new Callback<QueryResultMovies>() {
                @Override
                public void success(QueryResultMovies queryResultMovies, Response response) {
                    getMoviesByType(queryResultMovies);
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, "Download failed");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }

        if (typeMovies.equals(RestClient.UPCOMING_MOVIES)) {
            RestClient.get().getUpcomingMovies(numPage, new Callback<QueryResultMovies>() {
                @Override
                public void success(QueryResultMovies queryResultMovies, Response response) {
                    getMoviesByType(queryResultMovies);
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, "Download failed");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }


    }

    private void getMoviesByType(QueryResultMovies queryResultMovies) {
        if (queryResultMovies != null) {
            movieDetails = queryResultMovies.getResults();
            ArrayList<String> pathPoster = new ArrayList<>();
            for (MovieDetail item : movieDetails) {
                pathPoster.add(item.getPosterPath());
            }

            gridView.setAdapter(new GridMovieFragmentAdapter(getActivity(),
                    pathPoster));
        } else {
            gridView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Poster loading failed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_layout, container, false);
        gridView = (GridView) view.findViewById(R.id.gridViewMovieItem);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        CharSequence drawerTitle;
        CharSequence title = drawerTitle = activity.getTitle();


        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) view.findViewById(R.id.left_drawer);
        drawerList.setOnItemClickListener(new SlideMenuClickListener());


        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<>();

        //Title user name, if user log in account
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0] + " " + userName, navMenuIcons.getResourceId(0, -1)));
        // Top rated movie
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Popular movie
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Upcoming
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Favorites
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // Login
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        // About
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        // заполним ListView айтемами

        NavDrawerListAdapter navDrawerListAdapter = new NavDrawerListAdapter(getActivity(), navDrawerItems, isLogin);
        drawerList.setAdapter(navDrawerListAdapter);


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int movieId = movieDetails.get(position).getId();
                String movieTitle = movieDetails.get(position).getTitle();
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra(IS_LOGIN, isLogin);
                intent.putExtra(MOVIE_ID, movieId);
                intent.putExtra(MOVIE_TITLE, movieTitle);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_grid, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle actionbar actions click by arrows
        switch (item.getItemId()) {
            case R.id.action_refresh:
                numPage++;
                downloadMovies(numPage, typeMovies);
                toastShowPageNumber();
                return true;
            case R.id.action_back:
                numPage--;
                if (numPage <= 0) numPage = 1;
                downloadMovies(numPage, typeMovies);
                toastShowPageNumber();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

    //check internet connection
    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void toastShowPageNumber() {
        Toast.makeText(getActivity(), "Page " + String.valueOf(numPage), Toast.LENGTH_SHORT).show();
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startAction(position, view);
        }

        private void startAction(int position, View view) {
            switch (position) {
                case 1:
                    typeMovies = RestClient.TOP_RATED_MOVIES;
                    downloadMovies(numPage, typeMovies);
                    break;
                case 2:
                    typeMovies = RestClient.POPULAR_MOVIES;
                    numPage = 1;
                    downloadMovies(numPage, typeMovies);
                    break;
                case 3:
                    typeMovies = RestClient.UPCOMING_MOVIES;
                    numPage = 1;
                    downloadMovies(numPage, typeMovies);
                    break;

                case 4:
                    if (!isLogin) {
                        Toast.makeText(getActivity(), "Please,  login!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Enabled", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
                case 6:
                    startAbout();
                    break;

            }
        }

        private void startAbout() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("About")
                    .setCancelable(false)
                    .setView(R.layout.about)
                    .setPositiveButton("Close",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

}
