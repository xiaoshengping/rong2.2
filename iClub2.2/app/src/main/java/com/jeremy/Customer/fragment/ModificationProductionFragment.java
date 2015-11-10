package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModificationProductionFragment extends Fragment implements View.OnClickListener {



    @ViewInject(R.id.add_picture_tv)
    private TextView addPictureTv;
    @ViewInject(R.id.add_video_tv)
    private TextView addVideoTv;
    @ViewInject(R.id.add_music_tv)
    private TextView addMusicTv;

    private ResumeValueBean resumeValueBean;

    public ModificationProductionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_modification_production, container, false);
        ViewUtils.inject(this, view);
        init();
        return view;
    }

    private void init() {
        initView();

    }

    private void initView() {
        addPictureTv.setOnClickListener(this);
        addVideoTv.setOnClickListener(this);
        addMusicTv.setOnClickListener(this);

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
            uid = cursor.getString(1);

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

                    }


                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailure.......", s);
            }
        });





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.add_picture_tv:
                Intent picturnIntent=new Intent(getActivity(),AddPictureActivity.class);
                picturnIntent.putExtra("resumeid", resumeValueBean.getResumeid()+"");
                picturnIntent.putExtra("fagle","productionResume");
                startActivity(picturnIntent);
                break;
            case R.id.add_video_tv:
                Intent videoIntent=new Intent(getActivity(),AddVideoActivity.class);
                videoIntent.putExtra("resumeid", resumeValueBean.getResumeid()+"");
                videoIntent.putExtra("fagle","productionResume");
                startActivity(videoIntent);
                break;
            case R.id.add_music_tv:
                Intent musicIntent=new Intent(getActivity(),AddMusicActivity.class);
                musicIntent.putExtra("resumeid", resumeValueBean.getResumeid()+"");
                musicIntent.putExtra("fagle","productionResume");
                startActivity(musicIntent);
                break;


        }

    }
}
