package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.uilt.AddMusicActivity;
import com.jeremy.Customer.uilt.AddPictureActivity;
import com.jeremy.Customer.uilt.AddVideoActivity;
import com.jeremy.Customer.uilt.ModificationResumeActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
        resumeValueBean = ((ModificationResumeActivity) getActivity()).getResumeValueBeans();


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_picture_tv:
                Intent picturnIntent=new Intent(getActivity(),AddPictureActivity.class);
                picturnIntent.putExtra("resumeValueBean", resumeValueBean);
                picturnIntent.putExtra("fagle","modifactionResume");
                startActivity(picturnIntent);
                break;
            case R.id.add_video_tv:
                Intent videoIntent=new Intent(getActivity(),AddVideoActivity.class);
                videoIntent.putExtra("resumeValueBean", resumeValueBean);
                videoIntent.putExtra("fagle","modifactionResume");
                startActivity(videoIntent);
                break;
            case R.id.add_music_tv:
                Intent musicIntent=new Intent(getActivity(),AddMusicActivity.class);
                musicIntent.putExtra("resumeValueBean", resumeValueBean);
                musicIntent.putExtra("fagle","modifactionResume");
                startActivity(musicIntent);
                break;


        }

    }
}
