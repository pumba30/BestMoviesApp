package com.pundroid.bestmoviesapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.slidingmenu.NavDrawerItem;

import java.util.ArrayList;

/**
 * Created by pumba30 on 21.08.2015.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NavDrawerItem> mNavDrawerItems;
    private boolean mLogin;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, boolean login) {
        this.mContext = context;
        this.mNavDrawerItems = navDrawerItems;
        this.mLogin = login;
    }

    @Override
    public int getCount() {
        return mNavDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (!mLogin) {
            if (position == 4) {
                viewHolder.mImageView.setAlpha(0.3f);
                viewHolder.mTextView.setAlpha(0.3f);
            }
        } else {
            viewHolder.mTextView.setAlpha(1f);
            viewHolder.mImageView.setAlpha(1f);
        }

        viewHolder.mImageView.setImageResource(mNavDrawerItems.get(position).getIcon());
        viewHolder.mTextView.setText(mNavDrawerItems.get(position).getTitle());
        return convertView;
    }

    private static class ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
    }
}
