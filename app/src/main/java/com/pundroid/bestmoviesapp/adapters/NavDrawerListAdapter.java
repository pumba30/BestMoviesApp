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

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private boolean login;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, boolean login) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.login = login;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        if (!login) {
            if (position == 4) {
                txtTitle.setAlpha(0.3f);
                imgIcon.setAlpha(0.3f);
            }
        } else {
            txtTitle.setAlpha(1f);
            imgIcon.setAlpha(1f);
        }

        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());


        return convertView;
    }
}
