package com.pundroid.bestmoviesapp.fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.activity.MainActivity;
import com.pundroid.bestmoviesapp.objects.Genres;
import com.pundroid.bestmoviesapp.objects.MovieDetails;
import com.pundroid.bestmoviesapp.objects.ProductionCompanies;
import com.pundroid.bestmoviesapp.objects.ProductionCountries;
import com.pundroid.bestmoviesapp.utils.RestClient;
import com.pundroid.bestmoviesapp.utils.ScrollViewExt;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailMovieActivityFragment extends Fragment {
    public static final String TAG = DetailMovieActivityFragment.class.getSimpleName();
    public static final int POSTER_HEIGHT = 300;
    public static final int POSTER_WIDTH = 200;
    public static final int END_SCROLLING = 17;
    private AdView adView;

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

        Bundle args = getArguments();
        int movieId = args.getInt(GridMovieFragment.MOVIE_ID);
        downloadMovieDetail(movieId);

    }

    private void downloadMovieDetail(int movieId) {
        RestClient.get().getDetailMovieById(movieId, new Callback<MovieDetails>() {
            @Override
            public void success(MovieDetails movieDetail, Response response) {
                if (movieDetail != null) {
                    fillLayout(getView(), movieDetail);

                    //send data to activity
                    sendDetailMovie.onDataSendDetailMovie(movieDetail);
                } else {
                    Toast.makeText(getActivity(), "Load movie failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Load movie failed");
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        // Google Ads
        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("adMob").build();
        adView.loadAd(adRequest);
        //********

        ScrollViewExt scrollView = (ScrollViewExt) view.findViewById(R.id.scrollView_detail_movie);
        // get out ads from end scrollView
        scrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                Log.d(TAG, "DIFF= :" + diff);
                if (diff < END_SCROLLING) {
                    adView.setVisibility(View.INVISIBLE);
                } else if (diff > END_SCROLLING) {
                    adView.setVisibility(View.VISIBLE);
                }
            }
        });

        Log.d(TAG, "onCreateView");
        return view;
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
