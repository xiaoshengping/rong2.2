package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RoleRegisterActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.talents_user_tv)
    private TextView talentsUserTv;
    @ViewInject(R.id.merchant_user_tv)
    private TextView merchantUserTv;
    @ViewInject(R.id.tailt_return_iv)
    private ImageView tailtReturnIv;
    @ViewInject(R.id.rcai_textView)
    private TextView rcaiTextView;
    @ViewInject(R.id.sjia_textView)
    private TextView sjiaTextView;
    @ViewInject(R.id.show_particulars_iv)
    private ImageView showParticulars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_register);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();

    }

    private void intiView() {
        talentsUserTv.setOnClickListener(this);
        merchantUserTv.setOnClickListener(this);
        tailtReturnIv.setOnClickListener(this);
        rcaiTextView.setOnClickListener(this);
        sjiaTextView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.talents_user_tv:
                Intent talentsRegisterIntent=new Intent(RoleRegisterActivity.this,RegisterActivity.class);
                talentsRegisterIntent.putExtra("falge","talentsUser");
                startActivityForResult(talentsRegisterIntent, 14);
                break;

            case R.id.merchant_user_tv:
                Intent merchantRegisterIntent=new Intent(RoleRegisterActivity.this,RegisterActivity.class);
                merchantRegisterIntent.putExtra("falge","merchantUser");
                startActivityForResult(merchantRegisterIntent,14);
                break;
            case R.id.tailt_return_iv:
                finish();
                break;
            case R.id.rcai_textView:
                showParticulars.setVisibility(View.VISIBLE);
                showParticulars.setBackgroundResource(R.mipmap.rcai_particulars_icon);
                break;
            case R.id.sjia_textView:
                showParticulars.setVisibility(View.VISIBLE);
                showParticulars.setBackgroundResource(R.mipmap.sjia_parculars_icon);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==14){
            if (data.getStringExtra("colesActivity").equals("colesActivity")){
                finish();
            }
        }
    }
}
