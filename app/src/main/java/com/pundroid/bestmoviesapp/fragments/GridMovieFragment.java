package com.pundroid.bestmoviesapp.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.activity.DetailMovieActivity;
import com.pundroid.bestmoviesapp.activity.FavoritesActivity;
import com.pundroid.bestmoviesapp.activity.LoginActivity;
import com.pundroid.bestmoviesapp.activity.MainActivity;
import com.pundroid.bestmoviesapp.adapters.GridMovieFragmentAdapter;
import com.pundroid.bestmoviesapp.adapters.NavDrawerListAdapter;
import com.pundroid.bestmoviesapp.objects.Movie;
import com.pundroid.bestmoviesapp.service.DownloadHelperService;
import com.pundroid.bestmoviesapp.slidingmenu.NavDrawerItem;
import com.pundroid.bestmoviesapp.utils.PrefUtils;
import com.pundroid.bestmoviesapp.utils.RestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumba30 on 19.08.2015.
 */
public class GridMovieFragment extends Fragment {
    public static final String TAG = GridMovieFragment.class.getSimpleName();
    public static final String MOVIE_TITLE = "com.pundroid.bestmoviesapp.movie_title";
    public static final String ACTION_SEND_MOVIE = "com.pundroid.bestmoviesapp.send_movie";
    public static final String MOVIE = "com.pundroid.bestmoviesapp.send_movie";
    public static String MOVIE_ID = "com.pundroid.bestmoviesapp.movie_id";
    private GridView mGridView;
    private String[] mNavMenuTitles;
    private TypedArray mNavMenuIcons;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNumPage = 1;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private String mTypeMovies = RestClient.TOP_RATED_MOVIES;
    private ResultReceiver mResultReceiver = new ResultReceiver();
    private ProgressBar mProgressBar;
    private String mUserName = " guest!";
    private boolean mIsLogin;
    private boolean mIsConnected;
    private DrawerLayout mDrawerLayout;
    private DownloadHelperService mHelperService;

    public GridMovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        //check internet connection
        mIsConnected = isConnected();

        SharedPreferences preferences = getActivity()
                .getSharedPreferences(PrefUtils.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        mIsLogin = preferences.getBoolean(PrefUtils.KEY_USER_IS_IN_ACCOUNT, false);

        if (mIsLogin) {
            mUserName = " " + preferences.getString(PrefUtils.KEY_SESSION_USER_USERNAME,
                    getResources().getString(R.string.guest));
        }

        mHelperService = new DownloadHelperService(getActivity().getApplicationContext());

        // load slide menu items
        mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        mNavMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
    }


    //check internet connection
    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // here we pass different types of films
        mProgressBar.setVisibility(View.VISIBLE);
        mHelperService.downloadMoviesIntent(mNumPage, mTypeMovies, isConnected());
        toastShowPageNumber();
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_layout, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (!MainActivity.sIsLarge) {
            mGridView = (GridView) view.findViewById(R.id.gridViewMovieItem);
        } else {
            mGridView = (GridView) view.findViewById(R.id.gridViewMovieItem_large);
        }

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        CharSequence drawerTitle;
        CharSequence title = drawerTitle = activity.getTitle();

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) view.findViewById(R.id.left_drawer);
        drawerList.setOnItemClickListener(new SlideMenuClickListener());

        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<>();

        //Title user name, if user log in account
        navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0] + " " + mUserName, mNavMenuIcons.getResourceId(0, -1)));
        // Top rated movie
        navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1], mNavMenuIcons.getResourceId(1, -1)));
        // Popular movie
        navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2], mNavMenuIcons.getResourceId(2, -1)));
        // Upcoming
        navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3], mNavMenuIcons.getResourceId(3, -1)));
        // Favorite
        navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[4], mNavMenuIcons.getResourceId(4, -1)));
        // Login
        navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[5], mNavMenuIcons.getResourceId(5, -1)));
        // About
        navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[6], mNavMenuIcons.getResourceId(6, -1)));

        // Recycle the typed array
        mNavMenuIcons.recycle();

        // заполним ListView айтемами
        NavDrawerListAdapter navDrawerListAdapter = new NavDrawerListAdapter(getActivity(), navDrawerItems, mIsLogin);
        drawerList.setAdapter(navDrawerListAdapter);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(toolbar);

        // enabling action bar app icon and behaving it as toggle button
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int movieId = mMovies.get(position).getId();
                String movieTitle = mMovies.get(position).getTitle();
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra(MOVIE_ID, movieId);
                intent.putExtra(MOVIE_TITLE, movieTitle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mResultReceiver, new IntentFilter(ACTION_SEND_MOVIE));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mResultReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_grid, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle actionbar actions click by arrows
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mNumPage++;
                mHelperService.downloadMoviesIntent(mNumPage, mTypeMovies, mIsConnected);
                toastShowPageNumber();
                return true;
            case R.id.action_back:
                mNumPage--;
                if (mNumPage <= 0) mNumPage = 1;
                mHelperService.downloadMoviesIntent(mNumPage, mTypeMovies, mIsConnected);
                toastShowPageNumber();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


    private void toastShowPageNumber() {
        Toast.makeText(getActivity(),
                getActivity().getString(R.string.page)
                        + String.valueOf(mNumPage), Toast.LENGTH_SHORT).show();
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startAction(position);
        }

        private void startAction(int position) {
            switch (position) {
                case 1:
                    mTypeMovies = RestClient.TOP_RATED_MOVIES;
                    mHelperService.downloadMoviesIntent(mNumPage, mTypeMovies, mIsConnected);
                    mDrawerLayout.closeDrawers();
                    break;
                case 2:
                    mTypeMovies = RestClient.POPULAR_MOVIES;
                    mNumPage = 1;
                    mHelperService.downloadMoviesIntent(mNumPage, mTypeMovies, mIsConnected);
                    mDrawerLayout.closeDrawers();
                    break;
                case 3:
                    mTypeMovies = RestClient.UPCOMING_MOVIES;
                    mNumPage = 1;
                    mHelperService.downloadMoviesIntent(mNumPage, mTypeMovies, mIsConnected);
                    mDrawerLayout.closeDrawers();
                    break;

                case 4:
                    if (!mIsLogin) {
                        Toast.makeText(getActivity(), "Please,  login!", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intent = new Intent(getActivity(), FavoritesActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                    }
                    break;
                case 5:
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    mDrawerLayout.closeDrawers();
                    break;
                case 6:
                    startAbout();
                    break;
            }
        }

        private void startAbout() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.about))
                    .setCancelable(false)
                    .setView(R.layout.alertdialog_about)
                    .setPositiveButton(getString(R.string.close),
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

    public class ResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> pathPoster = new ArrayList<>();
            boolean isConnected = isConnected();
            if (intent != null) {
                mMovies = (ArrayList<Movie>) intent.getSerializableExtra(MOVIE);
                for (int i = 0; i < mMovies.size(); i++) {
                    if (isConnected) {
                        pathPoster.add(mMovies.get(i).getPosterPath());
                    }
                    if (!isConnected) {
                        pathPoster.add(mMovies.get(i).getPosterPathStorage());
                    }
                }
            }
            mGridView.setSmoothScrollbarEnabled(true);
            mGridView.setAdapter(new GridMovieFragmentAdapter(pathPoster, getActivity()));
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
