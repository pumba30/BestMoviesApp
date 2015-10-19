package com.pundroid.bestmoviesapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.objects.Movie;
import com.pundroid.bestmoviesapp.utils.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pumba30 on 31.08.2015.
 */
public class SearchMovieAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Movie> result;

    public SearchMovieAdapter(Context context, ArrayList<Movie> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_search_movie, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageViewMovie = (ImageView) convertView.findViewById(R.id.imageView_search_movie);
            viewHolder.textViewSearch = (TextView) convertView.findViewById(R.id.tv_search_name_movie);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar_search);
            viewHolder.overview = (TextView)convertView.findViewById(R.id.tv_mini_overview_search);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

       // get the details description of the film and fill item ListView
        Movie movie = result.get(position);

        viewHolder.textViewSearch.setText(movie.getTitle());
        viewHolder.overview.setText(movie.getOverview());

        String path = movie.getPosterPath();
        Picasso.with(context).load(RestClient.BASE_PATH_TO_IMAGE_W154 + path).into(viewHolder.imageViewMovie);

//        LayerDrawable stars = (LayerDrawable) viewHolder.ratingBar.getProgressDrawable();
//        stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

        // change color ratingBar
        Drawable progress = viewHolder.ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.parseColor("#857FC03A"));

        viewHolder.ratingBar.setIsIndicator(true);
        int rating = (int) (movie.getVoteAverage() * 10 - 1)%4;
        viewHolder.ratingBar.setRating(rating);


        return convertView;
    }

    static class ViewHolder {
        TextView overview;
        TextView textViewSearch;
        RatingBar ratingBar;
        ImageView imageViewMovie;
    }
}
