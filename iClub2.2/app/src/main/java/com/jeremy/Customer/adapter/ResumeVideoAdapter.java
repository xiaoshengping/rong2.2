package com.jeremy.Customer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeMovie;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xiaoshengping on 2015/7/16.
 */
public class ResumeVideoAdapter extends AppBaseAdapter<ResumeMovie> {
    private ViewHodle viewHodle;
    private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示
    public ResumeVideoAdapter(List<ResumeMovie> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.resume_video_adapter_layout,null);
            viewHodle=new ViewHodle(convertView) ;
            convertView.setTag(viewHodle);

        }else {
            viewHodle= (ViewHodle) convertView.getTag();

        }
      inti(position);

        return convertView;
    }

    private void inti(final int position) {

        viewHodle.showVideoImage.setBackgroundResource(R.mipmap.resume_background_icon);
        viewHodle.daleteMarkView.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);//设置删除按钮是否显示
        viewHodle.daleteMarkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideoData(data.get(position).getResumeid() + "", position,data.get(position).getResumemovieid()+"");

            }
        });
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                String imagpath= AppUtilsUrl.ImageBaseUrl+data.get(position).getPath();
                Bitmap bitmap=createVideoThumbnail(imagpath, 10, 10);
                viewHodle.showVideoImage.setImageBitmap(bitmap);
            }
        }).start();*/


       /* String imagpath= AppUtilsUrl.ImageBaseUrl+data.get(position).getPath();
        Bitmap bitmap=createVideoThumbnail(imagpath, 10, 10);
          viewHodle.showVideoImage.setImageBitmap(bitmap);*/



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

    private void deleteVideoData(String resumeid, final int position,String videoId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid", resumeid);
        requestParams.addBodyParameter("id", videoId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getDeleteVideo(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    MyAppliction.showToast("删除成功");
                    data.remove(position);
                    notifyDataSetChanged();


                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });




    }
    public void setIsShowDelete(boolean isShowDelete){
        this.isShowDelete=isShowDelete;
        notifyDataSetChanged();
    }


    private class  ViewHodle{

        @ViewInject(R.id.show_video_imageView)
        private ImageView showVideoImage;
        @ViewInject(R.id.delete_markView)
        private ImageView daleteMarkView;
        public ViewHodle(View view) {
            ViewUtils.inject(this, view);
        }
    }


}
