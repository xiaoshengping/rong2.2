package com.jeremy.Customer.uilt;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.PswValueBean;
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

public class ModificationPswActivity extends ActionBarActivity implements View.OnClickListener{
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.former_psw_edit)
    private EditText formerPswEdit;
    @ViewInject(R.id.new_psw_edit)
    private EditText newPswEdit;
    @ViewInject(R.id.reset_psw_edit)
    private EditText resetPswEdit;
    @ViewInject(R.id.modification_finist_button)
    private Button modificationFinistButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_psw);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();


    }

    private void intiView() {
        tailtText.setText("修改密码");
        tailtReturnTv.setOnClickListener(this);
        modificationFinistButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.modification_finist_button:
                if (!TextUtils.isEmpty(formerPswEdit.getText().toString())){
                    if (!TextUtils.isEmpty(newPswEdit.getText().toString())){
                        if (newPswEdit.getText().toString().length()>=6){

                            if (!TextUtils.isEmpty(resetPswEdit.getText().toString())){

                                if (resetPswEdit.getText().toString().length()>=6){


                                    if ((resetPswEdit.getText().toString()).equals(newPswEdit.getText().toString()) ){


                                        intiAmndPswData();


                                    }else {
                                        MyAppliction.showToast("新密码和重复密码不一致");

                                    }


                                }else {
                                    MyAppliction.showToast("重复密码长度要大于等于6");
                                }
                            }else {
                                MyAppliction.showToast("请输入重复密码");
                            }
                        }else {
                            MyAppliction.showToast("新密码长度大于或等于6");

                        }

                    }else {
                        MyAppliction.showToast("请输入新密码");
                    }

                }else {
                    MyAppliction.showToast("请输入旧密码");
                }
                break;
        }
    }


    private void intiAmndPswData() {
        SQLhelper sqLhelper=new SQLhelper(ModificationPswActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String uid=null;

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
        }
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("uid",uid);
        requestParams.addBodyParameter("oldpwd", MD5Uutils.MD5(formerPswEdit.getText().toString()));
        requestParams.addBodyParameter("newpwd", MD5Uutils.MD5(newPswEdit.getText().toString()));
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAmendPsw(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                ParmeBean<PswValueBean> parmeBean = JSONObject.parseObject(result, new TypeReference<ParmeBean<PswValueBean>>() {
                });
                if (parmeBean.getState().equals("success")) {
                    if (parmeBean.getValue().getMessage().equals("success")) {
                        MyAppliction.showToast("修改密码成功");
                          finish();

                    } else {
                        MyAppliction.showExitGameAlert("旧密码错误!", ModificationPswActivity.this);

                    }


                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }
}
