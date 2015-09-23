package com.jeremy.Customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.view.RoundAngleImageView;

/**
 * Created by Administrator on 2015/9/22.
 */
public class RecommendListAdater extends BaseAdapter {

    private LayoutInflater mInflater;
    private ViewActivity viewActivity;
    private ViewTalents viewTalents;
    private ViewPosition viewPosition;
    private int identi;

    public RecommendListAdater(Context context,int identi) {
        this.mInflater = LayoutInflater.from(context);
        this.identi = identi;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (identi == Identification.ACTIVITY) {
            return activity(convertView);
        } else if (identi == Identification.TALENTS) {
            return talents(convertView);
        } else if (identi == Identification.PROSITION) {
            return position(convertView);
        }

        return null;
    }

    //活动
    private View activity(View view){
        if (view == null) {
            view = mInflater.inflate(R.layout.item_activity, null);
            viewActivity = new ViewActivity();

            viewActivity.activity_poster = (RoundAngleImageView) view.findViewById(R.id.activity_poster);
            viewActivity.activity_name = (TextView) view.findViewById(R.id.activity_name);

            view.setTag(viewActivity);
        } else {
            viewActivity = (ViewActivity) view.getTag();
        }

        return view;
    }
    //人才
    private View talents(View view){
        if (view == null) {
            view = mInflater.inflate(R.layout.item_talents, null);
            viewTalents = new ViewTalents();

            viewTalents.item_talents_head = (RoundAngleImageView) view.findViewById(R.id.item_talents_head);
            viewTalents.item_talents_name_tv = (TextView) view.findViewById(R.id.item_talents_name_tv);
            viewTalents.item_talents_age_tv = (TextView) view.findViewById(R.id.item_talents_age_tv);
            viewTalents.item_talents_rest_tv = (TextView) view.findViewById(R.id.item_talents_rest_tv);
            viewTalents.item_talents_sex_iv = (ImageView) view.findViewById(R.id.item_talents_sex_iv);

            view.setTag(viewTalents);
        } else {
            viewTalents = (ViewTalents) view.getTag();
        }

        return view;
    }
    //职位
    private View position(View view){
        if (view == null) {
            view = mInflater.inflate(R.layout.item_position, null);
            viewPosition = new ViewPosition();

            viewPosition.item_position_name_tv = (TextView) view.findViewById(R.id.item_position_name_tv);
            viewPosition.item_position_salary_tv = (TextView) view.findViewById(R.id.item_position_salary_tv);
            viewPosition.item_position_time_tv = (TextView) view.findViewById(R.id.item_position_time_tv);
            viewPosition.item_position_site_tv = (TextView) view.findViewById(R.id.item_position_site_tv);

            view.setTag(viewPosition);
        } else {
            viewPosition = (ViewPosition) view.getTag();
        }

        return view;
    }

    public class ViewActivity{
        private RoundAngleImageView activity_poster;
        private TextView activity_name;
    }
    public class ViewTalents{
        private RoundAngleImageView item_talents_head;
        private TextView item_talents_name_tv,item_talents_age_tv,item_talents_rest_tv;
        private ImageView item_talents_sex_iv;
    }
    public class ViewPosition{
        private TextView item_position_name_tv,item_position_salary_tv,item_position_time_tv,item_position_site_tv;
    }

}
