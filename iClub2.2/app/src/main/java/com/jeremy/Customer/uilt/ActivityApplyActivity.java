package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jeremy.Customer.R;

public class ActivityApplyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_apply);
    }

    public void back(View v) {
        finish();
    }


}