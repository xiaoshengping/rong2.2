package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MineMoreActivity extends ActionBarActivity implements View.OnClickListener {

             @ViewInject(R.id.tailt_text)
             private TextView tailtText;
             @ViewInject(R.id.tailt_return_tv)
             private TextView tailtReturnTv;
             @ViewInject(R.id.modification_psw_tv)
             private TextView modificationPswTv;
             @ViewInject(R.id.feedback_tv)
             private TextView feedBackTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_more);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();
    }

    private void intiView() {
        tailtText.setText("更多");
        tailtReturnTv.setOnClickListener(this);
        modificationPswTv.setOnClickListener(this);
        feedBackTv.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.modification_psw_tv:
                Intent modificationPswIntent=new Intent(MineMoreActivity.this,ModificationPswActivity.class);
                startActivity(modificationPswIntent);
                break;
            case R.id.feedback_tv:
              Intent feedBackIntent=new Intent(MineMoreActivity.this,FeedBackActivity.class);
                startActivity(feedBackIntent);
                break;
        }
    }
}
