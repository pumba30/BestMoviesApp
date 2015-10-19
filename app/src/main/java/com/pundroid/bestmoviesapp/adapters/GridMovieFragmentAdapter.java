package com.pundroid.bestmoviesapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
    private List<Bitmap> mPosterImages;
    private List<String> mPathList;

    public GridMovieFragmentAdapter(Context context, List<Bitmap> posterImages) {
        mContext = context;
        mPosterImages = posterImages;
        Log.d(TAG, "constructor");
    }

    public GridMovieFragmentAdapter(List<String> pathToPoster, Context context) {
        mContext = context;
        mPathList = pathToPoster;
        Log.d(TAG, "constructor");
    }

    @Override
    public int getCount() {
        if (mPosterImages != null) {
            return mPosterImages.size();
        }
        return mPathList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mPosterImages != null) {
            return mPosterImages.indexOf(position);
        }
        return mPathList.indexOf(position);
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

        if (mPosterImages != null) {
            Bitmap image = mPosterImages.get(position);
            viewHolder.mImageView.setImageBitmap(image);
        } else {
            String path = RestClient.BASE_PATH_TO_IMAGE_W780 + mPathList.get(position);
            Log.d(TAG, path);
            Picasso.with(mContext).load(path).into(viewHolder.mImageView);
        }
        return convertView;
    }


    private static class ViewHolder {
        public ImageView mImageView;
        public GridView mGridViewLarge;
    }

}
