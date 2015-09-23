package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jeremy.Customer.R;

/**
 * Created by Administrator on 2015/9/23.
 */
public class ActivityDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitydetails);

    }

    public void immediately_attend(View v){
        Intent intent = new Intent();
        intent.setClass(ActivityDetailActivity.this, ActivityApplyActivity.class);
        startActivity(intent);
    }

    public void back(View v) {
        finish();
    }

}
