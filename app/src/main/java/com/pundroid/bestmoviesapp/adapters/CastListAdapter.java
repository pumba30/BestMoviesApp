package com.pundroid.bestmoviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.objects.Actor;
import com.pundroid.bestmoviesapp.utils.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pumba30 on 27.08.2015.
 */
public class CastListAdapter extends BaseAdapter {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_tab_cast, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.nameActor = (TextView) convertView.findViewById(R.id.name_actor_describe);
            viewHolder.characterActor = (TextView) convertView.findViewById(R.id.character_actor_describe);
            viewHolder.imageActor = (ImageView) convertView.findViewById(R.id.imageView_actor);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Actor actor = actors.get(position);
        if (actor.getProfilePath() != null
                || actor.getName() != null
                || actor.getCharacter() != null) {
            viewHolder.nameActor.setText(actor.getName());
            viewHolder.characterActor.setText(actor.getCharacter());
            String path = actor.getProfilePath();
            if (path != null) {
                resizeImage(viewHolder.imageActor);
                Picasso.with(context).load(RestClient.BASE_PATH_TO_IMAGE_W92 + path)
                        .into(viewHolder.imageActor);
            } else {
                resizeImage(viewHolder.imageActor);
                viewHolder.imageActor.setImageResource(R.drawable.ic_question_mark);
            }

        }
        return convertView;
    }



    private void resizeImage(ImageView imageView) {
        imageView.getLayoutParams().height = 138;
        imageView.getLayoutParams().width = 92;

    }


    static class ViewHolder {
        TextView nameActor;
        TextView characterActor;
        ImageView imageActor;
    }
}
