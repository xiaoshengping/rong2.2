package com.jeremy.Customer.uilt;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.LoginValueBean;
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
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

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
    private LoadingDialog loadingDialog;

    private HttpUtils httpUtils;
    private  UMSocialService mController ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(LoginActivity.this, "1102291619",
                "aPJ8P5pCeUTKK0Id");
        qqSsoHandler.addToSocialSDK();
        intiView();
        
        
    }

    private void intiView() {
        loadingDialog=new LoadingDialog(this,"正在登录......");
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
                  qqLogin();
                break;
            case R.id.weibo_login:
                weiboLogin();
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
        //第三方QQ登录
    private void qqLogin() {
        mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                Toast.makeText(LoginActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(LoginActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                Toast.makeText(LoginActivity.this, "授权完成", Toast.LENGTH_SHORT).show();
                //获取相关授权信息
                mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(LoginActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            for (String key : keys) {
                                MyAppliction.showToast(info.get(key).toString());
                                sb.append(key + "=" + info.get(key).toString() + "\r\n");
                            }
                            //String qqId = info.get("openid").toString();
                            //qqLoginData(qqId);
                            //MyAppliction.showToast(qqId);
                              Intent intent=new Intent(LoginActivity.this,BoundAccounsActivity.class);
                              intent.putExtra("QQdata",sb.toString().charAt(0));
                              startActivity(intent);
                            Log.d("TestData", sb.toString());
                        } else {
                            Log.d("TestData", "发生错误：" + status);
                        }
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void qqLoginData(String qqId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
       // requestParams.addBodyParameter("");
        httpUtils.send(HttpRequest.HttpMethod.POST, "", requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }

    //微博第三方登录
    private void weiboLogin() {
        //在新浪微博登录按钮中实现下面的方法，点击按钮则弹出新浪微博登录页面
        mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.SINA, new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    Toast.makeText(LoginActivity.this, "授权成功.", Toast.LENGTH_SHORT).show();
                    weiboLoginAccesstoken(); //获取用户信息
                } else {
                    Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }

            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
        });



    }
          //获取用户信息
    private void weiboLoginAccesstoken() {

        mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, new SocializeListeners.UMDataListener() {
            @Override
            public void onStart() {
                Toast.makeText(LoginActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                if (status == 200 && info != null) {
                    StringBuilder sb = new StringBuilder();
                    Set<String> keys = info.keySet();
                    for (String key : keys) {
                        sb.append(key + "=" + info.get(key).toString() + "\r\n");
                    }
                    Log.d("TestData", sb.toString());
                } else {
                    Log.d("TestData", "发生错误：" + status);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if(ssoHandler!= null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }
    //app登录
    private void intiLoginData(final String uid,String psw) throws NoSuchAlgorithmException {

        if (!TextUtils.isEmpty(uid)){
            if (uid.length()==11){
                if (!TextUtils.isEmpty(pswEdit.getText().toString())){
                    if (pswEdit.getText().toString().length()>=6){
                        loadingDialog.show();
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
                                        loadingDialog.dismiss();

                                    }else {

                                        MyAppliction.showExitGameAlert("用户名或密码错误",LoginActivity.this);
                                        loadingDialog.dismiss();
                                    }
                                }


                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                MyAppliction.showExitGameAlert("网络出错了",LoginActivity.this);
                                loadingDialog.dismiss();

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            hintKbTwo();
        }

        return super.onTouchEvent(event);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
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
