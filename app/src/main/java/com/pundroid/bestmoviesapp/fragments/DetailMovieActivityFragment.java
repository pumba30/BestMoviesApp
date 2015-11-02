package com.pundroid.bestmoviesapp.fragments;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.activity.DetailMovieActivity;
import com.pundroid.bestmoviesapp.activity.MainActivity;
import com.pundroid.bestmoviesapp.objects.Genres;
import com.pundroid.bestmoviesapp.objects.MovieDetails;
import com.pundroid.bestmoviesapp.objects.ProductionCompanies;
import com.pundroid.bestmoviesapp.objects.ProductionCountries;
import com.pundroid.bestmoviesapp.service.DownloadHelperService;
import com.pundroid.bestmoviesapp.utils.RestClient;
import com.pundroid.bestmoviesapp.utils.ScrollViewExt;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailMovieActivityFragment extends Fragment {
    public static final String TAG = DetailMovieActivityFragment.class.getSimpleName();
    public static final int POSTER_HEIGHT = 300;
    public static final int POSTER_WIDTH = 200;
    public static final int END_SCROLLING = 100;
    public static final String ACTION_SEND_MOVIE_DETAIL = "com.pundroid.bestmoviesapp.send_movie_detail";
    public static final String MOVIE_DETAIL = "movie_detail";
    private DownloadHelperService mDownloadHelperService;
    private ResultDetailMovieReceiver mReceiver = new ResultDetailMovieReceiver();
    private boolean mIsConnected;
    private int mMovieId;
    private View mMainView;

    // interface for transmission data from this fragment to DetailActivity
    public interface IDataSendDetailMovie {
        void onDataSendDetailMovie(MovieDetails movieDetail);
    }

    public interface ScrollViewListener {
        void onScrollChanged(ScrollViewExt scrollView,
                             int x, int y, int oldx, int oldy);
    }

    IDataSendDetailMovie sendDetailMovie;
    private static DetailMovieActivityFragment instance;

    public static DetailMovieActivityFragment newInstance() {
        if (instance == null) {
            instance = new DetailMovieActivityFragment();
        }
        return instance;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // init interface
        sendDetailMovie = (IDataSendDetailMovie) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mIsConnected = isConnected();

        Bundle args = getArguments();
        mMovieId = args.getInt(GridMovieFragment.MOVIE_ID);
        mDownloadHelperService = new DownloadHelperService(getActivity());
        // downloadMovieDetail(mMovieId);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDownloadHelperService.downloadDetailMovie(mMovieId, mIsConnected);
    }

    //check internet connection
    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        ScrollViewExt scrollView = (ScrollViewExt) mMainView.findViewById(R.id.scrollView_detail_movie);
        // get out ads from end scrollView
        scrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                Log.d(TAG, "DIFF= :" + diff);
                if (diff < END_SCROLLING) {
                    DetailMovieActivity.adView.setVisibility(View.INVISIBLE);
                } else if (diff > END_SCROLLING) {
                    DetailMovieActivity.adView.setVisibility(View.VISIBLE);
                }
            }
        });

        Log.d(TAG, "onCreateView");
        return mMainView;
    }

    private void fillLayout(View view, MovieDetails movie) {
        assert movie != null;

        // if use a tablet, download large size image
        String pathBackdrop;
        if (MainActivity.sIsLarge) {
            pathBackdrop = RestClient.BASE_PATH_TO_IMAGE_W780 + movie.getBackdropPath();
        } else {
            pathBackdrop = RestClient.BASE_PATH_TO_IMAGE_W342 + movie.getBackdropPath();
        }
        ImageView imageViewBackDrop = (ImageView) view.findViewById(R.id.image_view_backdrop);
        Picasso.with(getActivity()).load(pathBackdrop).into(imageViewBackDrop);


        ImageView imagePoster = (ImageView) view.findViewById(R.id.imageView_poster_w154);
        String pathPoster = RestClient.BASE_PATH_TO_IMAGE_W154 + movie.getPosterPath();
        imagePoster.getLayoutParams().height = POSTER_HEIGHT;
        imagePoster.getLayoutParams().width = POSTER_WIDTH;
        Picasso.with(getActivity()).load(pathPoster).into(imagePoster);

        TextView origTitle = (TextView) view.findViewById(R.id.tv_original_title);
        origTitle.setText(movie.getOriginalTitle());

        TextView genre = (TextView) view.findViewById(R.id.tv_genre_title);
        List<Genres> genresList = movie.getGenres();
        if (genresList.size() == 0) {
            genre.setText(R.string.nothing_found);
        } else {
            StringBuilder builder = new StringBuilder();
            for (Genres item : genresList) {
                builder.append(item.getName()).append(" / ");
            }
            genre.setText(builder.toString()
                    .substring(0, builder.toString().length() - 3));//удалим последний слеш
        }


        TextView releaseDate = (TextView) view.findViewById(R.id.tv_release_date_actor);
        releaseDate.setText(movie.getReleaseDate());

        TextView voteAverage = (TextView) view.findViewById(R.id.tv_vote_average_title);
        voteAverage.setText(String.valueOf(movie.getVoteAverage()));

        TextView voteCount = (TextView) view.findViewById(R.id.tv_vote_count_header_title);
        voteCount.setText(String.valueOf(movie.getVoteCount()));

        TextView tagLine = (TextView) view.findViewById(R.id.tv_tag_line_title);
        if (movie.getTagline() == null || movie.getTagline().equals("")) {
            tagLine.setText(R.string.nothing_found);
        } else {
            tagLine.setText(movie.getTagline());
        }


        TextView prodComp = (TextView) view.findViewById(R.id.tv_production_companies_description);
        List<ProductionCompanies> prodCompList = movie.getProductionCompanies();
        if (prodCompList.size() == 0) {
            prodComp.setText(R.string.nothing_found);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (ProductionCompanies companies : prodCompList) {
                stringBuilder.append(companies.getName()).append(" / ");
            }
            prodComp.setText(stringBuilder.toString()
                    .substring(0, stringBuilder.toString().length() - 3));
        }


        TextView prodCountries = (TextView) view.findViewById(R.id.tv_production_countries_description);
        List<ProductionCountries> prodCountriesList = movie.getProductionCountries();
        if (prodCountriesList.size() == 0) {
            prodCountries.setText(R.string.nothing_found);
        } else {
            StringBuilder builderCountries = new StringBuilder();
            for (ProductionCountries countries : prodCountriesList) {
                builderCountries.append(countries.getName()).append(" / ");
            }
            prodCountries.setText(builderCountries.toString().substring(0, builderCountries.toString().length() - 3));
        }


        TextView overview = (TextView) view.findViewById(R.id.tv_overview_title);
        overview.setText(movie.getOverview());

        TextView budget = (TextView) view.findViewById(R.id.cell_budget);
        String budgetStr = String.valueOf(movie.getBudget()) + " $";
        budget.setText(budgetStr);

        TextView runtime = (TextView) view.findViewById(R.id.cell_runtime);
        String runtimeStr = String.valueOf(movie.getRuntime()) + " min";
        runtime.setText(runtimeStr);

        TextView revenue = (TextView) view.findViewById(R.id.cell_revenue);
        revenue.setText(String.valueOf(movie.getRuntime()));

        TextView homePage = (TextView) view.findViewById(R.id.tv_homepage_description);
        homePage.setText(movie.getHomepage());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_detail_movie, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, new IntentFilter(ACTION_SEND_MOVIE_DETAIL));
    }


    public class ResultDetailMovieReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = isConnected();
            MovieDetails details;
            if (intent != null) {
                details = (MovieDetails) intent.getSerializableExtra(MOVIE_DETAIL);
                fillLayout(mMainView, details);
                sendDetailMovie.onDataSendDetailMovie(details);
            }
        }
    }

}
