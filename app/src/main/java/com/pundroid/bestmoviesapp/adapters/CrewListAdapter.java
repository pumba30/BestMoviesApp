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

    private Context mContext;
    private ArrayList<CrewMember> mCrewMembers;

    public CrewListAdapter(Context context, ArrayList<CrewMember> crewMembers) {
        this.mContext = context;
        this.mCrewMembers = crewMembers;
    }

    @Override
    public int getCount() {
        return mCrewMembers.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return mCrewMembers.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_list_tab_crew, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageView_memberCrew);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.name_memberCrew_describe);
            viewHolder.mNameJob = (TextView) convertView.findViewById(R.id.job_memberCrew_describe);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CrewMember crewMember = mCrewMembers.get(position);
        if (crewMember.getProfilePath() != null
                || crewMember.getName() != null
                || crewMember.getJob() != null) {
            viewHolder.mTextView.setText(crewMember.getName());
            viewHolder.mNameJob.setText(crewMember.getJob());
            String path = crewMember.getProfilePath();
            if (path != null) {
                resizeImage(viewHolder.mImageView);
                Picasso.with(mContext).load(RestClient.BASE_PATH_TO_IMAGE_W92
                        + path).into(viewHolder.mImageView);
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
        ImageView mImageView;
        TextView mTextView;
        TextView mNameJob;
    }
}
