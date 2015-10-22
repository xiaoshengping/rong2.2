package com.jeremy.Customer.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.uilt.ResumeParticularsActivity;
import com.jeremy.Customer.uilt.SQLhelper;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneselfProductionFragment extends Fragment {


    @ViewInject(R.id.show_video_resume_iv)
    private ImageView showVideoResumeIv;
    @ViewInject(R.id.show_music_resume_tv)
    private TextView showMusicResumeTv;
    @ViewInject(R.id.show_music_resume_two)
    private TextView showMusicResumeTwo;

    public OneselfProductionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_oneself_production, container, false);
        ViewUtils.inject(this,view);
        init();

        return view;
    }

    private void init() {
        initView();

    }

    private void initView() {
        intiResumeListData();


    }

    private void intiResumeListData() {
        HttpUtils httpUtils=new HttpUtils();
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        String resumeListUrl= AppUtilsUrl.getResumeLista(uid);
        httpUtils.send(HttpRequest.HttpMethod.GET, resumeListUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result=responseInfo.result;
                if (result!=null){
                    ArtistParme<ResumeValueBean> artistParme= JSONObject.parseObject(result, new TypeReference<ArtistParme<ResumeValueBean>>() {
                    });
                    if (artistParme.getState().equals("success")){
                      List<ResumeValueBean> resumeValueBeans= artistParme.getValue();
                        ResumeValueBean resumeValueBean=  resumeValueBeans.get(Integer.valueOf(((ResumeParticularsActivity) getActivity()).getPosition()));
                        if (resumeValueBean!=null){
                            if (resumeValueBean.getResumeMovie().size()!=0){
                                showVideoResumeIv.setImageBitmap(getVideoThumbnail(AppUtilsUrl.ImageBaseUrl + resumeValueBean.getResumeMovie().get(0).getPath(), 1700, 1000,
                                        MediaStore.Images.Thumbnails.MINI_KIND));
                            }
                            if (resumeValueBean.getResumeMusic().size()!=0){
                                showMusicResumeTv.setText(resumeValueBean.getResumeMusic().get(0).getTitle());
                                if (resumeValueBean.getResumeMusic().size()>=2){
                                    showMusicResumeTwo.setText(resumeValueBean.getResumeMusic().get(1).getTitle());
                                }
                            }

                        }
                    }





                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailure.......", s);
            }
        });





    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        //System.out.println("w"+bitmap.getWidth());
        //System.out.println("h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
