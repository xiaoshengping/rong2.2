package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.MusicAdapter;
import com.jeremy.Customer.adapter.PictureAdapter;
import com.jeremy.Customer.adapter.VideoAdapter;
import com.jeremy.Customer.bean.Utility;
import com.jeremy.Customer.view.MyGridView;


public class TalentsDetailsActivity extends Activity{

    private ListView video_production_list,music_production_list;
    private MyGridView picture_production_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talents_details);
        initVideoProduction();
        initMusicProduction();
        initPictureProduction();
    }

    //初始化视频作品
    private void initVideoProduction(){
        video_production_list = (ListView)findViewById(R.id.video_production_list);
        VideoAdapter videoAdapter = new VideoAdapter(this);
        video_production_list.setAdapter(videoAdapter);
        Utility.setListViewHeightBasedOnChildren(video_production_list);
    }

    //初始化音乐作品
    private void initMusicProduction(){
        music_production_list = (ListView)findViewById(R.id.music_production_list);
        MusicAdapter musicAdapter = new MusicAdapter(this);
        music_production_list.setAdapter(musicAdapter);
        Utility.setListViewHeightBasedOnChildren(music_production_list);
    }

    //初始化图片作品
    private void initPictureProduction(){
        picture_production_list = (MyGridView)findViewById(R.id.picture_production_list);
        PictureAdapter pictureAdapter = new PictureAdapter(this);
        picture_production_list.setAdapter(pictureAdapter);
    }


}

