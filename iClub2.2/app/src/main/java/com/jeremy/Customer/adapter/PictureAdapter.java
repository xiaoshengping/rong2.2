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
public class PictureAdapter extends BaseAdapter {

    private ViewMusic viewMusic;
    private LayoutInflater mInflater;

    public PictureAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 5;
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
            convertView = mInflater.inflate(R.layout.item_mucic, null);
            viewMusic = new ViewMusic();

            convertView.setTag(viewMusic);
        } else {
            viewMusic = (ViewMusic) convertView.getTag();
        }
        return convertView;
    }

    public class ViewMusic{

    }

}
