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
    private Context mContext;
    private ArrayList<Actor> mActors;


    public CastListAdapter(Context context, ArrayList<Actor> actors) {
        this.mContext = context;
        this.mActors = actors;

    }


    @Override
    public int getCount() {
        return mActors.size();
    }

    @Override
    public Object getItem(int position) {
        return mActors.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_list_tab_cast, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mNameActor = (TextView) convertView.findViewById(R.id.name_actor_describe);
            viewHolder.mCharacterActor = (TextView) convertView.findViewById(R.id.character_actor_describe);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageView_actor);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Actor actor = mActors.get(position);
        if (actor.getProfilePathWeb() != null
                || actor.getName() != null
                || actor.getCharacter() != null) {
            viewHolder.mNameActor.setText(actor.getName());
            viewHolder.mCharacterActor.setText(actor.getCharacter());
            String path = actor.getProfilePathWeb();
            if (path != null) {
                resizeImage(viewHolder.mImageView);
                Picasso.with(mContext).load(RestClient.BASE_PATH_TO_IMAGE_W92 + path)
                        .into(viewHolder.mImageView);
            } else {
                resizeImage(viewHolder.mImageView);
                viewHolder.mImageView.setImageResource(R.drawable.ic_question_mark);
            }

        }
        return convertView;
    }



    private void resizeImage(ImageView imageView) {
        imageView.getLayoutParams().height = 138;
        imageView.getLayoutParams().width = 92;

    }


    static class ViewHolder {
        TextView mNameActor;
        TextView mCharacterActor;
        ImageView mImageView;
    }
}
