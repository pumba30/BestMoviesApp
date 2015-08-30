package com.pundroid.bestmoviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pundroid.bestmoviesapp.R;
import com.pundroid.bestmoviesapp.objects.CrewMember;
import com.pundroid.bestmoviesapp.utils.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pumba30 on 28.08.2015.
 */
public class CrewListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CrewMember> crewMembers;

    public CrewListAdapter(Context context, ArrayList<CrewMember> crewMembers) {
        this.context = context;
        this.crewMembers = crewMembers;
    }

    @Override
    public int getCount() {
        return crewMembers.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return crewMembers.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_tab_crew, parent, false);


            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView_memberCrew);
            viewHolder.nameMember = (TextView) convertView.findViewById(R.id.name_memberCrew_describe);
            viewHolder.nameJob = (TextView) convertView.findViewById(R.id.job_memberCrew_describe);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CrewMember crewMember = crewMembers.get(position);
        if (crewMember.getProfilePath() != null
                || crewMember.getName() != null
                || crewMember.getJob() != null) {
            viewHolder.nameMember.setText(crewMember.getName());
            viewHolder.nameJob.setText(crewMember.getJob());
            String path = crewMember.getProfilePath();
            if (path != null) {
                resizeImage(viewHolder.imageView);
                Picasso.with(context).load(RestClient.BASE_PATH_TO_IMAGE_W92
                        + path).into(viewHolder.imageView);
            } else {
                resizeImage(viewHolder.imageView);
                viewHolder.imageView.setImageResource(R.drawable.ic_question_mark);
            }

        }
        return convertView;
    }

    private void resizeImage(ImageView imageView) {
        imageView.getLayoutParams().height = 138;
        imageView.getLayoutParams().width = 92;
    }


    static class ViewHolder {
        ImageView imageView;
        TextView nameMember;
        TextView nameJob;
    }
}
