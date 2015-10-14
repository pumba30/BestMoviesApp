package com.pundroid.bestmoviesapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.utils.RestClient;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pumba30 on 19.08.2015.
 */
public class GridMovieFragmentAdapter extends BaseAdapter {

    private static final String TAG = GridMovieFragmentAdapter.class.getSimpleName();
    private Context mContext;
    private List<String> mPathToPoster;

    public GridMovieFragmentAdapter(Context context, List<String> pathToPoster) {
        this.mContext = context;
        this.mPathToPoster = pathToPoster;
        Log.d(TAG, "constructor");
    }

    @Override
    public int getCount() {
        return mPathToPoster.size();
    }

    @Override
    public Object getItem(int position) {
        return mPathToPoster.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.image_view_layout, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image_item_movie);
            viewHolder.mGridViewLarge = (GridView) parent.findViewById(R.id.gridViewMovieItem_large);
            viewHolder.mImageView.setAdjustViewBounds(true);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        String poster = mPathToPoster.get(position);

        // if use a tablet, download large size image
        if (viewHolder.mGridViewLarge != null) {
            Picasso.with(mContext).load(RestClient.BASE_PATH_TO_IMAGE_W780 + poster).into(viewHolder.mImageView);
        } else {
            Picasso.with(mContext).load(RestClient.BASE_PATH_TO_IMAGE_W342 + poster).into(viewHolder.mImageView);
        }
        return convertView;
    }


    private static class ViewHolder {
        public ImageView mImageView;
        public GridView mGridViewLarge;
    }


}
