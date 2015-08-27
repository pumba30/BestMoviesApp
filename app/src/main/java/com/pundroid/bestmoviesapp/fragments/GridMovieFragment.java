package com.pundroid.bestmoviesapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.pundroid.bestmoviesapp.interfaces.AsyncResponse;
import com.pundroid.bestmoviesapp.objects.Movie;
import com.pundroid.bestmoviesapp.slidingmenu.NavDrawerItem;
import com.pundroid.bestmoviesapp.utils.DataFromJSON;
import com.pundroid.bestmoviesapp.utils.DownloadAsyncTask;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by pumba30 on 19.08.2015.
 */
public class GridMovieFragment extends Fragment implements AsyncResponse {
    public static final String TAG = GridMovieFragment.class.getSimpleName();
    public static final String TOP_RATED = "top_rated";
    private static final String REQUIRED_MOVIE_ID = "required_movie_id";
    private static final String GET_TOP_RATED_MOVIE = "get_top_rated_movie";
    private GridView gridView;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ActionBarDrawerToggle drawerToggle;
    private int number_page = 1;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter navDrawerListAdapter;

    private DataFromJSON dataFromJSON;

    private enum DownloadAction {
        TOP_RATED_MOVIE, MOVIE_BY_ID
    }

    private DownloadAction currentAction;

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
        try {
            dataFromJSON = new DataFromJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //первый параметр запрос, второй - номер страницы (page)
        String[] query = new String[]{GET_TOP_RATED_MOVIE, TOP_RATED, String.valueOf(number_page)};
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

        AppCompatActivity activity = (AppCompatActivity) getActivity();
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
                getDownloadMovieDetail(position);
            }
        });
        return view;
    }

    private void getDownloadMovieDetail(int position) {
        currentAction = DownloadAction.MOVIE_BY_ID;
        int movieId = dataFromJSON.getIdMovie(position);
        String[] query = new String[]{REQUIRED_MOVIE_ID, String.valueOf(movieId)};
        newDownloadTask(query);
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
                downloadMoviePoster(number_page);
                return true;
            case R.id.action_back:
                number_page--;
                if (number_page <= 0) number_page = 1;
                downloadMoviePoster(number_page);
                return true;
            case R.id.action_search:
                Log.d(TAG, "SEARCH!");
                Toast.makeText(getActivity(), "SEARCH", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //загрузим с параметрами query постеры
    private void downloadMoviePoster(int number_page) {
        currentAction = DownloadAction.TOP_RATED_MOVIE;
        String[] query = new String[]{GET_TOP_RATED_MOVIE, TOP_RATED, String.valueOf(number_page)};
        navDrawerItems.clear();
        navDrawerListAdapter.notifyDataSetChanged();
        newDownloadTask(query);
        toastShowPageNumber();
    }

    private void toastShowPageNumber() {
        Toast.makeText(getActivity(), "Page " + String.valueOf(number_page), Toast.LENGTH_SHORT).show();
    }

    private void newDownloadTask(String[] query) {
        DownloadAsyncTask task = new DownloadAsyncTask();
        task.execute(query);
        task.response = this;
    }

    //загрузим JSON запрос
    public void updateMovie(String[] query) throws JSONException {
        if (isConnected()) {
            currentAction = DownloadAction.TOP_RATED_MOVIE;
            //вызов загрузки через параметр:"latest"|"top_rated" - запросы
            newDownloadTask(query);
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

    @Override
    public void processFinish(String result) throws JSONException {


        switch (currentAction) {
            case TOP_RATED_MOVIE:
                try {
                    dataFromJSON.setMovieJSONString(result);
                    ArrayList<String> listPathToPoster = dataFromJSON.getPosterPathFromJSON();
                    gridView.setAdapter(new ImageGridAdapter(getActivity(), listPathToPoster));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MOVIE_BY_ID:
                dataFromJSON.setMovieJSONString(result);

                try {
                    Movie movie = dataFromJSON.getMovie();
                    Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                    intent.putExtra(Movie.MOVIE_OBJECT, movie);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
