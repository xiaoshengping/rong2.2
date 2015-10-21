package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ActivityBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by Administrator on 2015/9/23.
 */
public class ActivityDetailActivity extends Activity {

    private ImageView activity_poster_iv;
    private TextView activity_name_tv,activity_time_tv,activity_content_tv;

    private BitmapUtils bitmapUtils;
    private ActivityBean activityBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitydetails);
        bitmapUtils = new BitmapUtils(this);
        activityBean = (ActivityBean) getIntent().getSerializableExtra("Detail");
        init();
    }

    private void init(){

        activity_poster_iv = (ImageView)findViewById(R.id.activity_poster_iv);
        activity_name_tv = (TextView)findViewById(R.id.activity_name_tv);
        activity_time_tv = (TextView)findViewById(R.id.activity_time_tv);
        activity_content_tv = (TextView)findViewById(R.id.activity_content_tv);

        bitmapUtils.display(activity_poster_iv, AppUtilsUrl.ImageBaseUrl + activityBean.getImage());
        activity_name_tv.setText(activityBean.getTitle());
        activity_time_tv.setText(activityBean.getDate());
        activity_content_tv.setText(activityBean.getContent());
    }

    public void immediately_attend(View v) {
        Intent intent = new Intent();
        intent.setClass(ActivityDetailActivity.this, ActivityApplyActivity.class);
        startActivity(intent);
    }

    public void back(View v) {
        finish();
    }

}
