package com.jeremy.Customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jeremy.Customer.R;

/**
 * Created by Administrator on 2015/9/25.
 */
public class VideoAdapter extends BaseAdapter {

    private ViewVideo viewVideo;
    private LayoutInflater mInflater;

    public VideoAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_video, null);
            viewVideo = new ViewVideo();

            convertView.setTag(viewVideo);
        } else {
            viewVideo = (ViewVideo) convertView.getTag();
        }
        return convertView;
    }

    public class ViewVideo{

    }

}
