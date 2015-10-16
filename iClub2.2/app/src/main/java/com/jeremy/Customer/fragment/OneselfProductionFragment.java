package com.jeremy.Customer.fragment;


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

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
        Bundle bundle=getArguments();
        ResumeValueBean resumeValueBeans= (ResumeValueBean) bundle.getSerializable("resumeValueBeans");
        Log.e("hdhfhffjfjf",resumeValueBeans.getResumeEmail());
        if (resumeValueBeans!=null){
            showVideoResumeIv.setImageBitmap(getVideoThumbnail(AppUtilsUrl.ImageBaseUrl + resumeValueBeans.getResumeMovie().get(0).getPath(), 1700, 1000,
                    MediaStore.Images.Thumbnails.MINI_KIND));
            if (resumeValueBeans.getResumeMusic().size()!=0){
                showMusicResumeTv.setText(resumeValueBeans.getResumeMusic().get(0).getTitle());
                if (resumeValueBeans.getResumeMusic().size()>=2){
                    showMusicResumeTwo.setText(resumeValueBeans.getResumeMusic().get(1).getTitle());
                }
            }

        }

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
