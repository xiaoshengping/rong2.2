package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.jeremy.Customer.R;
import com.jeremy.Customer.view.CustomImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ResumeParticularsActivity extends ActionBarActivity {

    @ViewInject(R.id.usericon_background_iv)
    private CustomImageView customImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_particulars);
        ViewUtils.inject(this);
        customImageView.setBackgroundResource(R.mipmap.ic_launcher);
    }


}
