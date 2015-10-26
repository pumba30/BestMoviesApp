package com.pundroid.bestmoviesapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.utils.PicassoBitmapTransformation;
import com.pundroid.bestmoviesapp.utils.RestClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by pumba30 on 19.08.2015.
 */
public class GridMovieFragmentAdapter extends BaseAdapter {

    private static final String TAG = GridMovieFragmentAdapter.class.getSimpleName();
    public static final int DESIRED_WIDTH = 2;//size half screen (screen/2)
    private Context mContext;
    private List<String> mPathList;


    public GridMovieFragmentAdapter(List<String> pathToPoster, Context context) {
        mContext = context;
        mPathList = pathToPoster;
        Log.d(TAG, "constructor");
    }

    @Override
    public int getCount() {
        return mPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPathList.get(position);
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
        String path;
        Picasso.with(mContext).setIndicatorsEnabled(true);
        Picasso.with(mContext).setLoggingEnabled(true);
        if (!mPathList.get(position).contains("data")) {
            path = RestClient.BASE_PATH_TO_IMAGE_W342 + mPathList.get(position);
            Transformation transformation = new PicassoBitmapTransformation(mContext, DESIRED_WIDTH);
            Picasso.with(mContext).load(path).transform(transformation).into(viewHolder.mImageView);

        } else {
            path = mPathList.get(position);
            Uri uri = Uri.parse("file://" + path);
            Log.d(TAG, "Path " + uri);
            Transformation transformation = new PicassoBitmapTransformation(mContext, DESIRED_WIDTH);
            Picasso.with(mContext).load(uri).transform(transformation).into(viewHolder.mImageView);
        }


        return convertView;
    }


    private static class ViewHolder {
        public ImageView mImageView;
        public GridView mGridViewLarge;
    }

}
