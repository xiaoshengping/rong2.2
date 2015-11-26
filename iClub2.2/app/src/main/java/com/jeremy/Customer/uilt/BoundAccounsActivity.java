package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

public class BoundAccounsActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.accounts_edit)
    private EditText accontsEdit;
    @ViewInject(R.id.psw_edit)
    private EditText pawEdit;
    @ViewInject(R.id.no_register_tv)
    private TextView noRegisterTv;
    @ViewInject(R.id.bound_button)
    private Button boundButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_accouns);
        inti();
        MyAppliction.showToast(getIntent().getStringExtra("QQdata"));
    }

    private void inti() {
        intiView();
    }

    private void intiView() {
        boundButton.setOnClickListener(this);
        noRegisterTv.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.no_register_tv:
                Intent intent=new Intent(BoundAccounsActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.bound_button:
                saveDate();
                break;
        }
    }

    private void saveDate() {
        HttpUtils httpUtils =new HttpUtils();
        RequestParams requestParams=new RequestParams();

        if (!TextUtils.isEmpty(accontsEdit.getText().toString())){
            requestParams.addBodyParameter("uid", accontsEdit.getText().toString());
            if (!TextUtils.isEmpty(pawEdit.getText().toString())){
                requestParams.addBodyParameter("psw", pawEdit.getText().toString());
                requestParams.addBodyParameter("openid", getIntent().getStringExtra("QQdata"));
                httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.BindingWeiboQq(), new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        MyAppliction.showToast("绑定成功");
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
            }else {
                MyAppliction.showToast("请输入已注册密码");
            }

        }else {
            MyAppliction.showToast("请输入已注册账号");
        }



    }
}
