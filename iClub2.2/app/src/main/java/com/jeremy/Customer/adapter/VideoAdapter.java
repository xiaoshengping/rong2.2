package com.jeremy.Customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeMovie;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.BitmapUtils;

import java.util.HashMap;
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
    private int maxNum = 0;

    public VideoAdapter(Context context, List<ResumeMovie> resumeMovie) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.resumeMovie = resumeMovie;
        bitmapUtils = new BitmapUtils(context);
        if (resumeMovie.size() > 1) {
            maxNum = 1;
        } else {
            maxNum = resumeMovie.size();
        }
    }

    public void setMaxNum() {
        if (maxNum == 1) {
            maxNum = resumeMovie.size();
        } else if (resumeMovie.size() > 1) {
            maxNum = 1;
        } else {
            maxNum = resumeMovie.size();
        }
    }


    @Override
    public int getCount() {
        return maxNum;
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
            viewVideo.talents_video_button_iv = (ImageView) convertView.findViewById(R.id.talents_video_button_iv);
            convertView.setTag(viewVideo);
        } else {
            viewVideo = (ViewVideo) convertView.getTag();
        }
//        viewVideo.talents_video_button_iv.setImageBitmap(createVideoThumbnail(AppUtilsUrl.ImageBaseUrl + resumeMovie.get(position).getPath(),10,10));
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

    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }


public class ViewVideo {

    private ImageView talents_video_back_iv;
    private ImageView talents_video_button_iv;

}

}
