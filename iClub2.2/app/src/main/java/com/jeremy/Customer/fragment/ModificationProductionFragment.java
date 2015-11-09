package com.jeremy.Customer.fragment;


import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.Customer.R;
import com.lidroid.xutils.ViewUtils;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModificationProductionFragment extends Fragment implements View.OnClickListener {



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


    }

    @Override
    public void onResume() {
        super.onResume();
        //intiResumeListData();
    }

   /* private void intiResumeListData() {
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





    }*/

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
        /*switch (v.getId()){
            case R.id.modification_video_tv:
                Intent videoIntent=new Intent(getActivity(),AddVideoActivity.class);
                videoIntent.putExtra("resumeValueBean", resumeValueBean);
                videoIntent.putExtra("fagle","modifactionResume");
                startActivity(videoIntent);
                break;
            case R.id.modification_music_tv:
                Intent musicIntent=new Intent(getActivity(),AddMusicActivity.class);
                musicIntent.putExtra("resumeValueBean", resumeValueBean);
                musicIntent.putExtra("fagle","modifactionResume");
                startActivity(musicIntent);
                break;
            case R.id.modification_picture_tv:
                Intent picturnIntent=new Intent(getActivity(),AddPictureActivity.class);
                picturnIntent.putExtra("resumeValueBean",resumeValueBean );
                picturnIntent.putExtra("fagle","modifactionResume");
                startActivity(picturnIntent);
                break;




        }*/
    }
}
