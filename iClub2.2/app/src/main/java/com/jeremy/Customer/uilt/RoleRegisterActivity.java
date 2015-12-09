package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
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
    @ViewInject(R.id.rcai_particulars_tv)
    private TextView rcaiTextView;
    @ViewInject(R.id.sjia_particulars_tv)
    private TextView sjiaTextView;
    /*@ViewInject(R.id.show_particulars_iv)
    private ImageView showParticulars;*/



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
            case R.id.rcai_particulars_tv:
                showrCaiGameAlert();
                break;
            case R.id.sjia_particulars_tv:
                showsJiaGameAlert();
                break;
        }
    }

    //人才详情
    private void showrCaiGameAlert() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.show_role_layout);
        ImageView showRoleIv = (ImageView) window.findViewById(R.id.show_role_iv);
        showRoleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
    //商家详情
    private void showsJiaGameAlert() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.show_role_sjia_layou);
        ImageView showRoleIv = (ImageView) window.findViewById(R.id.show_role_iv);
        showRoleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
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
