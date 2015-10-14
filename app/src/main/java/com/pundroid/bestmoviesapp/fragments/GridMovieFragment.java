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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.activity.DetailMovieActivity;
import com.pundroid.bestmoviesapp.activity.FavoritesActivity;
import com.pundroid.bestmoviesapp.activity.LoginActivity;
import com.pundroid.bestmoviesapp.activity.MainActivity;
import com.pundroid.bestmoviesapp.adapters.GridMovieFragmentAdapter;
import com.pundroid.bestmoviesapp.adapters.NavDrawerListAdapter;
import com.pundroid.bestmoviesapp.databases.DataModel.Poster;
import com.pundroid.bestmoviesapp.objects.MovieDetail;
import com.pundroid.bestmoviesapp.objects.QueryResultMovies;
import com.pundroid.bestmoviesapp.slidingmenu.NavDrawerItem;
import com.pundroid.bestmoviesapp.utils.PrefUtils;
import com.pundroid.bestmoviesapp.utils.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pumba30 on 19.08.2015.
 */
public class GridMovieFragment extends Fragment {
    public static final String TAG = GridMovieFragment.class.getSimpleName();
    public static final String MOVIE_TITLE = "com.pundroid.bestmoviesapp.movie_title";
    public static String MOVIE_ID = "com.pundroid.bestmoviesapp.movie_id";
    private GridView mGridView;
    private String[] mNavMenuTitles;
    private TypedArray mNavMenuIcons;
    private ActionBarDrawerToggle mDrawerToggle;
    private int numPage = 1;
    private ArrayList<MovieDetail> mMovieDetails = new ArrayList<>();
    private String mTypeMovies = RestClient.TOP_RATED_MOVIES;
    private ProgressBar mProgressBar;
    // при getString(R.string.guest) вылетает!!
    private String mUserName =  " guest!";
    private boolean mIsLogin;
    private DrawerLayout mDrawerLayout;

    public GridMovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SharedPreferences preferences = getActivity()
                .getSharedPreferences(PrefUtils.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        mIsLogin = preferences.getBoolean(PrefUtils.KEY_USER_IS_IN_ACCOUNT, false);

        if (mIsLogin) {
            mUserName = " " + preferences.getString(PrefUtils.KEY_SESSION_USER_USERNAME,
                    " guest") + "!";
        }

        // load slide menu items
        mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        mNavMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isConnected()) {
            // here we pass different types of films
            downloadMovies(numPage, mTypeMovies);
            toastShowPageNumber();
        } else {
            Toast.makeText(getActivity(),
                    R.string.internet_connection_failed, Toast.LENGTH_SHORT).show();
        }

    }

    private void downloadMovies(int numPage, String typeMovies) {
        mProgressBar.setVisibility(View.VISIBLE);

        if (typeMovies.equals(RestClient.TOP_RATED_MOVIES)) {
            RestClient.get().getMoviesByType(numPage, typeMovies,
                    new Callback<QueryResultMovies>() {
                        @Override
                        public void success(QueryResultMovies queryResultMovies, Response response) {
                            getMoviesByType(queryResultMovies);

                            mProgressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(TAG, getString(R.string.download_failed));
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }

        if (typeMovies.equals(RestClient.POPULAR_MOVIES)) {
            RestClient.get().getPopularMovies(numPage, new Callback<QueryResultMovies>() {
                @Override
                public void success(QueryResultMovies queryResultMovies, Response response) {
                    getMoviesByType(queryResultMovies);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, "Download failed");
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        }

        if (typeMovies.equals(RestClient.UPCOMING_MOVIES)) {
            RestClient.get().getUpcomingMovies(numPage, new Callback<QueryResultMovies>() {
                @Override
                public void success(QueryResultMovies queryResultMovies, Response response) {
                    getMoviesByType(queryResultMovies);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, "Download failed");
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        }


    }

    private void getMoviesByType(QueryResultMovies queryResultMovies) {
        if (queryResultMovies != null) {
            mMovieDetails = queryResultMovies.getResults();
            List<String> pathPoster = new ArrayList<>();
            List<Poster> posters = new ArrayList<>();

            for (MovieDetail item : mMovieDetails) {
                pathPoster.add(item.getPosterPath());
//                Poster poster = new Poster(item.getId(), item.getPosterPath());
//                Log.d(TAG, "item get poster_path " + poster.getMovieId());
//                posters.add(poster);
            }

//            //Check this
//            AsyncTask asyncTask = new DownloadPoster();
//            asyncTask.execute(posters);


            mGridView.setAdapter(new GridMovieFragmentAdapter(getActivity(),
                    pathPoster));
        } else {
            mGridView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), R.string.poster_loading_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }

//    private class DownloadPoster extends AsyncTask<List<Poster>, Void, Long> {
//
//        private Bitmap bitmap;
//
//
//        @SafeVarargs
//        @Override
//        protected final Long doInBackground(List<Poster>... params) {
//            Log.d(TAG, "doInBackground");
//
//            List<Poster> posters = params[0];
//            for (Poster poster : posters) {
//                File file = new File(getActivity().getCacheDir(), String.valueOf(poster.getMovieId()) + ".png");
//                OutputStream outputStream;
//                if (file.exists()) {
//                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                    return 0L;
//                }
//
//                try {
//                    file.createNewFile();
//                    URL urlPoster = new URL(RestClient.BASE_PATH_TO_IMAGE_W342 + poster.getPathToPoster());
//                    URLConnection connection = urlPoster.openConnection();
//                    connection.connect();
//
//                    InputStream inputStream = connection.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(inputStream);
//                    outputStream = new BufferedOutputStream(new FileOutputStream(file));
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                    outputStream.flush();
//                    outputStream.close();
//                    return 0L;
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    return 1L;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return 1L;
//                }
//            }
//            return 1L;
//        }
//
//        @Override
//        protected void onPostExecute(Long aLong) {
//            if (aLong == 0) {
//                Log.d(TAG, "Failed to save");
//            } else {
//                Log.d(TAG, "Done!");
//            }
//
//
//        }
//    }

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
                int movieId = mMovieDetails.get(position).getId();
                String movieTitle = mMovieDetails.get(position).getTitle();
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                // intent.putExtra(IS_LOGIN, mIsLogin);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle actionbar actions click by arrows
        switch (item.getItemId()) {
            case R.id.action_refresh:
                numPage++;
                downloadMovies(numPage, mTypeMovies);
                toastShowPageNumber();
                return true;
            case R.id.action_back:
                numPage--;
                if (numPage <= 0) numPage = 1;
                downloadMovies(numPage, mTypeMovies);
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
            startAction(position);
        }

        private void startAction(int position) {
            switch (position) {
                case 1:
                    mTypeMovies = RestClient.TOP_RATED_MOVIES;
                    downloadMovies(numPage, mTypeMovies);
                    mDrawerLayout.closeDrawers();
                    break;
                case 2:
                    mTypeMovies = RestClient.POPULAR_MOVIES;
                    numPage = 1;
                    downloadMovies(numPage, mTypeMovies);
                    mDrawerLayout.closeDrawers();
                    break;
                case 3:
                    mTypeMovies = RestClient.UPCOMING_MOVIES;
                    numPage = 1;
                    downloadMovies(numPage, mTypeMovies);
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

}
