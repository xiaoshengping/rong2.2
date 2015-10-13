package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jeremy.Customer.R;
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

import java.io.File;

public class AddMusicActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;
    @ViewInject(R.id.add_music_bt)
    private Button addMusicBt;
    @ViewInject(R.id.show_music_data_tv)
    private TextView showMusicDataTv;

    private String resumeid;
    private String musicPath;
    private String musicName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();


    }

    private void initView() {
        resumeid= getIntent().getStringExtra("resumeid");
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加音乐");
        saveText.setVisibility(View.VISIBLE);
        saveText.setOnClickListener(this);
        saveText.setText("上传");
        addMusicBt.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:
             if (!TextUtils.isEmpty(resumeid)&&!TextUtils.isEmpty(musicName)&&
                        !TextUtils.isEmpty(musicPath)){
                     saveMusicData("432",musicName,musicPath);
                }else {
                    MyAppliction.showToast("你还没有音频文件");
                }
                break;
            case R.id.add_music_bt:
               Intent musicIntent=new Intent(AddMusicActivity.this,AddMusicProductionActivity.class);
               startActivityForResult(musicIntent,10);
                break;


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
             musicPath=  data.getStringExtra("musicPath");
             musicName=  data.getStringExtra("musicName");
              if (!TextUtils.isEmpty(musicName)){
                  showMusicDataTv.setVisibility(View.VISIBLE);
                  showMusicDataTv.setText("你选择的音频文件: "+musicName);
              }else {
                  showMusicDataTv.setVisibility(View.VISIBLE);
                  showMusicDataTv.setText("你还没有音频文件");
              }
                break;

        }
    }

    private void saveMusicData(String resumeid,String musicName,String musicPath) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid",resumeid);
        requestParams.addBodyParameter("musicName",musicName);
        requestParams.addBodyParameter("music",new File(musicPath));
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddMusic(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("上传音乐onFailure",s);
            }
        });



    }


}
