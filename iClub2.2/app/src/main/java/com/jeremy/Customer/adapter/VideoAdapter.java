package com.jeremy.Customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeMovie;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/9/25.
 */
public class VideoAdapter extends BaseAdapter {

    private ViewVideo viewVideo;
    private LayoutInflater mInflater;
    private List<ResumeMovie> resumeMovie;
    private BitmapUtils bitmapUtils;
    private Context mContext;

    public VideoAdapter(Context context,List<ResumeMovie> resumeMovie){
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.resumeMovie = resumeMovie;
        bitmapUtils=new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return resumeMovie.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_video, null);
            viewVideo = new ViewVideo();
//            viewVideo.talents_video_back_iv = (ImageView)convertView.findViewById(R.id.talents_video_back_iv);
            viewVideo.talents_video_button_iv = (ImageView)convertView.findViewById(R.id.talents_video_button_iv);
            convertView.setTag(viewVideo);
        } else {
            viewVideo = (ViewVideo) convertView.getTag();
        }

//        bitmapUtils.display(viewVideo.talents_video_back_iv, AppUtilsUrl.ImageBaseUrl + resumeMovie.get);
        viewVideo.talents_video_button_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(AppUtilsUrl.ImageBaseUrl + resumeMovie.get(position).getPath()), "video/mp4");
                mContext.startActivity(intent);

            }
        });

        return convertView;
    }

    public class ViewVideo{

        private ImageView talents_video_back_iv;
        private ImageView talents_video_button_iv;

    }

}
