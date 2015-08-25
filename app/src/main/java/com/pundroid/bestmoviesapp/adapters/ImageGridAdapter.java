package com.pundroid.bestmoviesapp.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pundroid.bestmoviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pumba30 on 19.08.2015.
 */
public class ImageGridAdapter extends BaseAdapter {

    private static final String TAG = ImageGridAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<String> pathToPoster;

    public ImageGridAdapter(Context context, ArrayList<String> pathToPoster) {
        this.context = context;
        this.pathToPoster = pathToPoster;
        Log.d(TAG, "constr ImageGridADApter");
    }

    @Override
    public int getCount() {
        return pathToPoster.size();
    }

    @Override
    public Object getItem(int position) {
        return pathToPoster.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.image_view_layout, parent, false);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_item_movie);
            viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.imageView.setAdjustViewBounds(true);
            if (context.getResources()
                    .getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                viewHolder.imageView.setRotation(90f);
            }
            convertView.setTag(viewHolder);
        }


        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        String poster = pathToPoster.get(position);
        Picasso.with(context).load(poster).into(viewHolder.imageView);

        Log.d(TAG, poster);

        return convertView;
    }


    private static class ViewHolder {
        public ImageView imageView;
    }


}
