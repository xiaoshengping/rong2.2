package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ProductionResumeActivity extends ActionBarActivity implements View.OnClickListener {


    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;
    @ViewInject(R.id.add_picture_tv)
    private TextView addPictureTv;
    @ViewInject(R.id.add_video_tv)
    private TextView addVideoTv;
    @ViewInject(R.id.add_music_tv)
    private TextView addMusicTv;

    private String resumeid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_resume);
        ViewUtils.inject(this);
        init();

    }

    private void init() {

        initView();



    }

    private void initView() {
        resumeid= getIntent().getStringExtra("resumeid");
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加作品");
        saveText.setVisibility(View.VISIBLE);
        saveText.setText("完成");
        saveText.setOnClickListener(this);
        addPictureTv.setOnClickListener(this);
        addVideoTv.setOnClickListener(this);
        addMusicTv.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:
                 finish();
                break;
            case R.id.add_picture_tv:
                Intent picturnIntent=new Intent(ProductionResumeActivity.this,AddPictureActivity.class);
                picturnIntent.putExtra("resumeid", resumeid);
                picturnIntent.putExtra("fagle","productionResume");
                startActivity(picturnIntent);
                break;
            case R.id.add_video_tv:
                Intent videoIntent=new Intent(ProductionResumeActivity.this,AddVideoActivity.class);
                videoIntent.putExtra("resumeid", resumeid);
                videoIntent.putExtra("fagle","productionResume");
                startActivity(videoIntent);
                break;
            case R.id.add_music_tv:
                Intent musicIntent=new Intent(ProductionResumeActivity.this,AddMusicActivity.class);
                musicIntent.putExtra("resumeid", resumeid);
                musicIntent.putExtra("fagle","productionResume");
                startActivity(musicIntent);
                break;


        }

    }





}
