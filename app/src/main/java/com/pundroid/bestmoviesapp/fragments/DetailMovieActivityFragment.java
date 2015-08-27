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

import com.pundroid.bestmoviesapp.DetailMovieActivity;
import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.object.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailMovieActivityFragment extends Fragment {
    public static final String TAG = DetailMovieActivityFragment.class.getSimpleName();
    private Movie movie;


    private static DetailMovieActivityFragment instance;
        public static DetailMovieActivityFragment newInstance() {
        if (instance == null) {
            instance = new DetailMovieActivityFragment();
        }
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DetailMovieActivity detailMovieActivity = (DetailMovieActivity) activity;
        movie = detailMovieActivity.movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        Log.d(TAG, "onCreateView");
        fillLayout(view);


        return view;
    }

    private void fillLayout(View view) {
       // Movie movie = getArguments().getParcelable(Movie.MOVIE_OBJECT);
        assert movie != null;
        String pathBackdrop = Movie.BASE_BACKDROP_PATH + movie.getBackdropPath();
        ImageView imageViewBackDrop = (ImageView) view.findViewById(R.id.image_view_backdrop);
        Picasso.with(getActivity()).load(pathBackdrop).into(imageViewBackDrop);

        ImageView imagePoster = (ImageView) view.findViewById(R.id.imageView_poster_w154);
        String pathPoster = Movie.BASE_POSTER_PATH_W154 + movie.getPosterPath();
        imagePoster.getLayoutParams().height = 300;
        imagePoster.getLayoutParams().width = 200;
        Picasso.with(getActivity()).load(pathPoster).into(imagePoster);

        TextView origTitle = (TextView) view.findViewById(R.id.tv_original_title);
        origTitle.setText(movie.getOriginalTitle());

        TextView genre = (TextView) view.findViewById(R.id.tv_genre_title);
        ArrayList<String> genresList = movie.getGenres();
        if (genresList == null) {
            genresList = new ArrayList<>();
            genresList.add("Nothing found");
        }
        genre.setText(getStrings(genresList));

        TextView releaseDate = (TextView) view.findViewById(R.id.tv_release_date_title);
        releaseDate.setText(movie.getReleaseDate());

        TextView voteAverage = (TextView) view.findViewById(R.id.tv_vote_average_title);
        voteAverage.setText(String.valueOf(movie.getVoteAverage()));

        TextView voteCount = (TextView) view.findViewById(R.id.tv_vote_count_header_title);
        voteCount.setText(String.valueOf(movie.getVoteCount()));

        TextView tagLine = (TextView) view.findViewById(R.id.tv_tag_line_title);
        tagLine.setText(movie.getTagLine());

        TextView prodComp = (TextView) view.findViewById(R.id.tv_production_companies_description);
        ArrayList<String> prodCompList = movie.getProductionCompanies();
        if (prodCompList == null) {
            prodCompList = new ArrayList<>();
            prodCompList.add("Nothing found");
        }
        prodComp.setText(getStrings(prodCompList));

        TextView prodCountries = (TextView) view.findViewById(R.id.tv_production_countries_description);
        ArrayList<String> prodCountriesList = movie.getProductionCountries();
        if (prodCountriesList == null) {
            prodCountriesList = new ArrayList<>();
            prodCountriesList.add("Nothing found");
        }
        prodCountries.setText(getStrings(prodCountriesList));

        TextView overview = (TextView) view.findViewById(R.id.tv_overview_title);
        overview.setText(movie.getOverview());

        TextView budget = (TextView) view.findViewById(R.id.cell_budget);
        budget.setText(String.valueOf(movie.getBudget()));

        TextView runtime = (TextView) view.findViewById(R.id.cell_runtime);
        runtime.setText(String.valueOf(movie.getRuntime()));

        TextView revenue = (TextView) view.findViewById(R.id.cell_revenue);
        revenue.setText(String.valueOf(movie.getRuntime()));

        TextView homePage = (TextView) view.findViewById(R.id.tv_homepage_description);
        homePage.setText(movie.getHomePage());
    }


    //удалим в строке последний слеш
    private String getStrings(ArrayList<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder = stringBuilder.append(s + " ");
        }
        String s = stringBuilder.toString();
        return s.substring(0, s.length() - 3);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_detail_movie, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

    }


}
