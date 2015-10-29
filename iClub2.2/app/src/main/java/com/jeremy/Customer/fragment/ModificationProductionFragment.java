package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
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
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.uilt.AddMusicActivity;
import com.jeremy.Customer.uilt.AddPictureActivity;
import com.jeremy.Customer.uilt.AddVideoActivity;
import com.jeremy.Customer.uilt.ModificationResumeActivity;
import com.jeremy.Customer.uilt.SQLhelper;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModificationProductionFragment extends Fragment implements View.OnClickListener {

    @ViewInject(R.id.show_video_resume_iv)
    private ImageView showVideoResumeIv;
    @ViewInject(R.id.show_music_resume_tv)
    private TextView showMusicResumeTv;
    @ViewInject(R.id.show_music_resume_two)
    private TextView showMusicResumeTwo;

    @ViewInject(R.id.show_picture_resume_one)
    private ImageView showPictureResumeOne;
    @ViewInject(R.id.show_picture_resume_two)
    private ImageView showPictureResumeTwo;
    @ViewInject(R.id.show_picture_resume_three)
    private ImageView showPictureResumeThree;
    @ViewInject(R.id.show_picture_resume_four)
    private ImageView showPictureResumeFour;

    @ViewInject(R.id.modification_picture_tv)
    private TextView modificationPictureTv;
    @ViewInject(R.id.modification_music_tv)
    private TextView modificationMusicTv;
    @ViewInject(R.id.modification_video_tv)
    private TextView modificationVideoTv;

    private  ResumeValueBean resumeValueBean;

    public ModificationProductionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_modification_production, container, false);
        ViewUtils.inject(this,view);
        init();
        return view;
    }

    private void init() {
        initView();

    }

    private void initView() {
        modificationVideoTv.setOnClickListener(this);
        modificationMusicTv.setOnClickListener(this);
        modificationPictureTv.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
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
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<ResumeValueBean> artistParme = JSONObject.parseObject(result, new TypeReference<ArtistParme<ResumeValueBean>>() {
                    });
                    if (artistParme.getState().equals("success")) {
                        List<ResumeValueBean> resumeValueBeans = artistParme.getValue();
                        resumeValueBean = resumeValueBeans.get(Integer.valueOf(((ModificationResumeActivity) getActivity()).getPosition()));
                        if (resumeValueBean != null) {
                            if (resumeValueBean.getResumeMovie().size() != 0) {
                                showVideoResumeIv.setImageBitmap(createVideoThumbnail(AppUtilsUrl.ImageBaseUrl+resumeValueBean.getResumeMovie().get(0).getPath(),10,10));
                            }
                            if (resumeValueBean.getResumeMusic().size() != 0) {
                                showMusicResumeTv.setText(resumeValueBean.getResumeMusic().get(0).getTitle());
                                if (resumeValueBean.getResumeMusic().size() >1) {
                                    showMusicResumeTwo.setText(resumeValueBean.getResumeMusic().get(1).getTitle());
                                }
                            }
                            if (resumeValueBean.getResumePicture().size()!=0){
                                MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl+resumeValueBean.getResumePicture().get(0).getPath(),showPictureResumeOne,MyAppliction.options);
                               if (resumeValueBean.getResumePicture().size()>1){
                                   MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl+resumeValueBean.getResumePicture().get(1).getPath(),showPictureResumeTwo,MyAppliction.options);
                                   if (resumeValueBean.getResumePicture().size()>2){
                                       MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl+resumeValueBean.getResumePicture().get(2).getPath(),showPictureResumeThree,MyAppliction.options);
                                      if (resumeValueBean.getResumePicture().size()>3){
                                          MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl+resumeValueBean.getResumePicture().get(3).getPath(),showPictureResumeFour,MyAppliction.options);

                                      }
                                   }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modification_video_tv:
                Intent videoIntent=new Intent(getActivity(),AddVideoActivity.class);
                videoIntent.putExtra("resumeValueBean", resumeValueBean);
                startActivity(videoIntent);
                break;
            case R.id.modification_music_tv:
                Intent musicIntent=new Intent(getActivity(),AddMusicActivity.class);
                musicIntent.putExtra("resumeValueBean", resumeValueBean);
                startActivity(musicIntent);
                break;
            case R.id.modification_picture_tv:
                Intent picturnIntent=new Intent(getActivity(),AddPictureActivity.class);
                picturnIntent.putExtra("resumeValueBean",resumeValueBean );
                startActivity(picturnIntent);
                break;




        }
    }
}
