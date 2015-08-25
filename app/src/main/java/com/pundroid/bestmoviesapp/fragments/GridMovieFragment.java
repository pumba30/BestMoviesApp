package com.pundroid.bestmoviesapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.Toast;

import com.pundroid.bestmoviesapp.DetailMovieActivity;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.adapters.ImageGridAdapter;
import com.pundroid.bestmoviesapp.adapters.NavDrawerListAdapter;
import com.pundroid.bestmoviesapp.object.Movie;
import com.pundroid.bestmoviesapp.slidingmenu.NavDrawerItem;
import com.pundroid.bestmoviesapp.utils.DataFromJSON;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pumba30 on 19.08.2015.
 */
public class GridMovieFragment extends Fragment {
    public static final String TAG = GridMovieFragment.class.getSimpleName();
    public static final String TOP_RATED = "top_rated";
    private static final String REQUIRED_MOVIE_ID = "required_movie_id";
    public static final String SCHEME = "http";
    public static final String AUTHORITY = "api.themoviedb.org";
    public static final String SEGMENT_NUMBER_THREE = "3";
    public static final String SEGMENT_DISCOVER = "discover";
    public static final String SEGMENT_MOVIE = "movie";
    public static final String PARAMETER_APY_KEY = "api_key";
    public static final String PARAMETER_PAGE = "page";
    public static final String PARAMETER_QUERY = "query";
    private GridView gridView;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ActionBarDrawerToggle drawerToggle;
    private int number_page = 1;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter navDrawerListAdapter;
    private DataFromJSON dataFromJSON;


    public GridMovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
    }

    @Override
    public void onStart() {
        super.onStart();
        //первый параметр запрос, второй - номер страницы (page)
        String[] query = new String[]{TOP_RATED, String.valueOf(number_page)};
        toastShowPageNumber();
        try {
            updateMovie(query);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_layout, container, false);
        gridView = (GridView) view.findViewById(R.id.gridViewMovieItem);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        CharSequence drawerTitle;
        CharSequence title = drawerTitle = activity.getTitle();


        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) view.findViewById(R.id.left_drawer);


        navDrawerItems = new ArrayList<>();
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));

        // Recycle the typed array
        navMenuIcons.recycle();

        // заполним ListView айтемами

        navDrawerListAdapter = new NavDrawerListAdapter(getActivity(), navDrawerItems);
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

                //  Movie movie = dataFromJSON.getMovie(position);
                int movieId = dataFromJSON.getIdMovie(position);
                String[] query = new String[]{REQUIRED_MOVIE_ID, String.valueOf(movieId)};
                DownloadAsyncTask asyncTask = new DownloadAsyncTask();
                asyncTask.execute(query);
                Log.d(TAG, asyncTask.getStatus().toString());
                //используется тот же dataFromJSON

                try {
                    Movie movie = dataFromJSON.getMovie();
                    Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                    intent.putExtra(Movie.MOVIE_OBJECT, movie);//TODO здесь уже ложиться фильм без параметров
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                number_page++;
                downloadMoviePoster(TOP_RATED, number_page);
                return true;
            case R.id.action_back:
                number_page--;
                if (number_page <= 0) number_page = 1;
                downloadMoviePoster(TOP_RATED, number_page);
                return true;
            case R.id.action_search:
                Log.d(TAG, "SEARCH!");
                Toast.makeText(getActivity(), "SEARCH", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //загрузим с параметрами query постеры
    private void downloadMoviePoster(String kindOfMovie, int number_page) {
        String[] query = new String[]{kindOfMovie, String.valueOf(number_page)};
        navDrawerItems.clear();
        navDrawerListAdapter.notifyDataSetChanged();
        new DownloadAsyncTask().execute(query);
        toastShowPageNumber();
    }

    private void toastShowPageNumber() {
        Toast.makeText(getActivity(), "Page " + String.valueOf(number_page), Toast.LENGTH_SHORT).show();
    }

    //загрузим JSON запрос
    public void updateMovie(String[] query) throws JSONException {
        if (isConnected()) {
            //вызов загрузки через параметр:"latest"|"top_rated" - запросы
            DownloadAsyncTask task = new DownloadAsyncTask();
            task.execute(query);
        } else {
            Toast.makeText(getActivity(),
                    "You do not have Internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    private class DownloadAsyncTask extends AsyncTask<String, Integer, DataFromJSON> {
        public static final String API_KEY = "d1a2f8dc42f6388052172df57a6aba41";


        // params - запрос latest, popular, similar и т.д.
        @Override
        protected DataFromJSON doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            Log.d(TAG, "Build URL");
            //   http://api.themoviedb.org/3/discover/movie?api_key=d1a2f8dc42f6388052172df57a6aba41&page=1&query=top_rated
            //   http://api.themoviedb.org/3/movie/116741?api_key=d1a2f8dc42f6388052172df57a6aba41
            Log.d(TAG, "prePARAMS [0]= " + params[0]);

            // params[0] - REQUIRED_MOVIE_ID, params[1] - ID required film
            if (params[0] == REQUIRED_MOVIE_ID) {
                Log.d(TAG, "PARAMS [0]= " + params[0]);
                Uri.Builder builderUri = new Uri.Builder();
                builderUri.scheme(SCHEME).authority(AUTHORITY)
                        .appendPath(SEGMENT_NUMBER_THREE)
                        .appendPath(SEGMENT_MOVIE)
                        .appendPath(params[1])
                        .appendQueryParameter(PARAMETER_APY_KEY, API_KEY);
                String myUrl = builderUri.toString();
                try {

                    URL urlMovie = new URL(myUrl);
                    HttpURLConnection connection = (HttpURLConnection) urlMovie.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int status = connection.getResponseCode();
                    Log.d(TAG, "Connection status " + status);

                    InputStream inputStream = connection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();

                    if (inputStream == null) {
                        return null;
                    }

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line).append("/n");
                    }

                    if (stringBuffer.length() == 0) {
                        return null;
                    }

                    String movieJSONString1 = stringBuffer.toString();
                    dataFromJSON.setMovieJSONStringOnce(movieJSONString1);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return dataFromJSON;

            } else {
                // params[0] - query (top_rated и т.д.) params[1] - number page
                Uri.Builder builderUri = new Uri.Builder();
                builderUri.scheme(SCHEME).authority(AUTHORITY)
                        .appendPath(SEGMENT_NUMBER_THREE)
                        .appendPath(SEGMENT_DISCOVER)
                        .appendPath(SEGMENT_MOVIE)
                        .appendQueryParameter(PARAMETER_APY_KEY, API_KEY)
                        .appendQueryParameter(PARAMETER_PAGE, params[1])
                        .appendQueryParameter(PARAMETER_QUERY, params[0]);
                String myUrl = builderUri.toString();
                Log.d(TAG, "MY URL: " + myUrl);

                try {
                    URL urlMovie = new URL(myUrl);
                    HttpURLConnection connection = (HttpURLConnection) urlMovie.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int status = connection.getResponseCode();
                    Log.d(TAG, "Connection status " + status);

                    InputStream inputStream = connection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();

                    if (inputStream == null) {
                        return null;
                    }

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line).append("/n");
                    }

                    if (stringBuffer.length() == 0) {
                        return null;
                    }

                    String movieJSONString = stringBuffer.toString();
                    dataFromJSON = new DataFromJSON(movieJSONString);
                    Log.d(TAG, movieJSONString);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return dataFromJSON;
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(DataFromJSON data) {
            try {
                ArrayList<String> listPathToPoster = data.getPosterPathFromJSON();

                gridView.setAdapter(new ImageGridAdapter(getActivity(), listPathToPoster));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
