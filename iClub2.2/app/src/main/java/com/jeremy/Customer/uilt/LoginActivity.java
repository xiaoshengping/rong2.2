package com.jeremy.Customer.uilt;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.LoginValueBean;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.edit_phone)
    private EditText phoneEdit;
    @ViewInject(R.id.edit_psw)
    private EditText pswEdit;
    @ViewInject(R.id.login_button)
    private Button loginButton;
    @ViewInject(R.id.tailt_return_tv)
    private TextView returnTV;
    @ViewInject(R.id.register_tv)
    private TextView RegisterTv;
    @ViewInject(R.id.forget_psw_tv)
    private TextView forgetTv;
    @ViewInject(R.id.qq_login)
    private ImageView qq_login;
    @ViewInject(R.id.weibo_login)
    private ImageView weibo_login;

    private HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        intiView();
        
        
    }

    private void intiView() {
        returnTV.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        RegisterTv.setOnClickListener(this);
        forgetTv.setOnClickListener(this);
        qq_login.setOnClickListener(this);
        weibo_login.setOnClickListener(this);
        httpUtils=new HttpUtils();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.login_button:
                String uid=  phoneEdit.getText().toString();
                String  psw=MD5Uutils.MD5(pswEdit.getText().toString());

                try {
                    intiLoginData(uid,psw);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.qq_login:

                break;
            case R.id.weibo_login:

                break;
            case R.id.register_tv:
                Intent registerIntent=new Intent(LoginActivity.this,RoleRegisterActivity.class);
                startActivity(registerIntent);

                break;
            case R.id.forget_psw_tv:
                Intent forgetIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                forgetIntent.putExtra("falge","forgetPsw");
                startActivity(forgetIntent);

                break;
        }
    }



    //登录数据
    private void intiLoginData(final String uid,String psw) throws NoSuchAlgorithmException {

        if (!TextUtils.isEmpty(uid)){
            if (uid.length()==11){
                if (!TextUtils.isEmpty(pswEdit.getText().toString())){
                    if (pswEdit.getText().toString().length()>=6){
                        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getLoginData(uid, psw) , new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                String rerult=responseInfo.result;
                                // Log.e("intiData",rerult);
                                if (rerult!=null){

                                    ParmeBean<LoginValueBean> artistParme= JSONObject.parseObject(rerult, new TypeReference<ParmeBean<LoginValueBean>>() {
                                    });
                                    LoginValueBean loginValueBean=  artistParme.getValue();
                                    if ("success".equals(artistParme.getState())&&uid.equals(loginValueBean.getUid())){
                                        //Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                                        SQLhelper sqLhelper=new SQLhelper(LoginActivity.this);
                                        insertData(sqLhelper, loginValueBean.getUid(), loginValueBean.getUserName(), loginValueBean.getUserIcon(), loginValueBean.getState(),
                                                loginValueBean.getMobile(), loginValueBean.getPersonId(), loginValueBean.getCompanyName());
                                        finish();


                                    }else {

                                        MyAppliction.showExitGameAlert("用户名或密码错误",LoginActivity.this);

                                    }
                                }


                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                MyAppliction.showExitGameAlert("网络出错了",LoginActivity.this);


                            }
                        });


                    }else {
                        MyAppliction.showToast("密码长度大于或等于6");

                    }
                }else {
                    MyAppliction.showToast("请输入密码");

                }

            }else {
                MyAppliction.showToast("请输入11位电话号码");
            }

        }else {
            MyAppliction.showToast("请输入您的手机号码");


        }

    }

    public void insertData(SQLhelper sqLhelper,String uid,String userName,String userIcon,String state,String mobile,String personid ,String companyName){
        SQLiteDatabase db=sqLhelper.getWritableDatabase();
        // db.execSQL("insert into user(uid,userName,userIcon,state) values('战士',3,5,7)");
        ContentValues values=new ContentValues();
        values.put(SQLhelper.UID,uid);
        values.put(SQLhelper.PERSONID,personid);
        values.put(SQLhelper.USERNAME, userName);
        values.put(SQLhelper.USERICON, userIcon);
        values.put(SQLhelper.STSTE, state);
        values.put(SQLhelper.MOBILE, mobile);
        values.put(SQLhelper.COMPANYNAME,companyName);
        db.insert(SQLhelper.tableName, SQLhelper.UID, values);
        db.close();
    }
}
