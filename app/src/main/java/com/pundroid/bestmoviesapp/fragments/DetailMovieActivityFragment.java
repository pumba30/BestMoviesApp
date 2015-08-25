package com.pundroid.bestmoviesapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.object.Movie;
import com.squareup.picasso.Picasso;

public class DetailMovieActivityFragment extends Fragment {
    public static final String TAG = DetailMovieActivityFragment.class.getSimpleName();


    private static DetailMovieActivityFragment instance;
    private Activity activity;


    public DetailMovieActivityFragment() {
    }


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        fillLayout(view);


        return view;
    }

    private void fillLayout(View view) {
        Movie movie = getArguments().getParcelable(Movie.MOVIE_OBJECT);
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

        TextView genre = (TextView) view.findViewById(R.id.tv_genre_title);// // FIXME: 25.08.2015 null
        genre.setText(movie.getGenres());

        TextView releaseDate = (TextView) view.findViewById(R.id.tv_release_date_title);
        releaseDate.setText(movie.getReleaseDate());

        TextView voteAverage = (TextView) view.findViewById(R.id.tv_vote_average_title);
        voteAverage.setText(String.valueOf(movie.getVoteAverage()));

        TextView voteCount = (TextView) view.findViewById(R.id.tv_vote_count_header_title);
        voteCount.setText(String.valueOf(movie.getVoteCount()));

        TextView tagLine = (TextView) view.findViewById(R.id.tv_tag_line_title);//// FIXME: 25.08.2015  null
        tagLine.setText(movie.getTagLine());


        TextView overview = (TextView) view.findViewById(R.id.tv_overview_title);
        overview.setText(movie.getOverview());

        TextView budget = (TextView) view.findViewById(R.id.cell_budget);// FIXME: 25.08.2015  null
        budget.setText(String.valueOf(movie.getBudget()));

        TextView runtime = (TextView) view.findViewById(R.id.cell_runtime);// FIXME: 25.08.2015  null
        runtime.setText(String.valueOf(movie.getRuntime()));

        TextView revenue = (TextView) view.findViewById(R.id.cell_revenue);// FIXME: 25.08.2015  null
        revenue.setText(String.valueOf(movie.getRuntime()));

        TextView homePage = (TextView) view.findViewById(R.id.tv_homepage_description);
        homePage.setText(movie.getHomePage());
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
