package com.pundroid.bestmoviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.object.Actor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pumba30 on 27.08.2015.
 */
public class CastListAdapter extends BaseAdapter {
    public static final String BASE_PATH_TO_IMAGE = "http://image.tmdb.org/t/p/w92";
    private Context context;
    private ArrayList<Actor> actors;


    public CastListAdapter(Context context, ArrayList<Actor> actors) {
        this.context = context;
        this.actors = actors;

    }


    @Override
    public int getCount() {
        return actors.size();
    }

    @Override
    public Object getItem(int position) {
        return actors.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_tab_cast, parent, false);

        }
        TextView textView1 = (TextView) convertView.findViewById(R.id.name_actor_describe);
        TextView textView2 = (TextView) convertView.findViewById(R.id.character_actor_describe);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView_actor);

        textView1.setText(actors.get(position).getNameActor());
        textView2.setText(actors.get(position).getCharacterActor());
        String path = BASE_PATH_TO_IMAGE + actors.get(position).getPathToImageActor();
        Picasso.with(context).load(path).into(imageView);


        return convertView;
    }
}
