package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.RegisterValueBean;
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

public class RegisterActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.tailt_return_tv)
    private Button tailtReturnButton;
    private String userType; //用户类型
    @ViewInject(R.id.register_phone)
    private EditText registerPhoneEdit;
    @ViewInject(R.id.captcha_edit)
    private EditText captchaEdit;
    @ViewInject(R.id.captcha_button)
    private Button captchaBtton;
    @ViewInject(R.id.set_psw_edit)
    private EditText setPswEdit;
    @ViewInject(R.id.verify_psw_edit)
    private EditText verifyPswEdit;
    @ViewInject(R.id.register_finist_button)
    private Button registerFinistButton;

    private TimeCount time;
    private HttpUtils httpUtils;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();


    }

    private void intiView() {
        loadingDialog=new LoadingDialog(this,"正在注册.....");
        httpUtils=new HttpUtils();
       userType =getIntent().getStringExtra("falge");
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        registerFinistButton.setOnClickListener(this);
        tailtReturnButton.setOnClickListener(this);
        captchaBtton.setOnClickListener(this);
        if (userType.equals("talentsUser")){
            tailtText.setText("人才注册");
        }else if (userType.equals("merchantUser")){
            tailtText.setText("商家注册");
        }else if (userType.equals("forgetPsw")){
            tailtText.setText("忘记密码");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.register_finist_button:
                if (userType.equals("talentsUser")){
                    intiRegisterData(AppUtilsUrl.getRegisterData(),"恭喜您注册成功!","正在注册.....");
                     }else if (userType.equals("merchantUser")){
                      intiRegisterData(AppUtilsUrl.getRegisterData(),"恭喜您注册成功!","正在注册.....");
                     }else if (userType.equals("forgetPsw")){
                    intiRegisterData(AppUtilsUrl.getForgetData(),"恭喜您密码已找回!","修改密码中.....");
                }

                break;
            case R.id.captcha_button:
                intiVcodeData();
                break;

        }
    }
    private void intiRegisterData(String url, final String text,String tiShiYu) {

        String psw= MD5Uutils.MD5(setPswEdit.getText().toString());
        String  verifypsw=  MD5Uutils.MD5(verifyPswEdit.getText().toString());
        String uid=registerPhoneEdit.getText().toString();
        String capcha=captchaEdit.getText().toString();

        if (!TextUtils.isEmpty(uid)){
            if (uid.length()==11){
                if (!TextUtils.isEmpty(capcha)){
                    if (capcha.length()==6){
                        if (!TextUtils.isEmpty(setPswEdit.getText().toString())){
                            if (setPswEdit.getText().toString().length()>=6) {
                                if (!TextUtils.isEmpty(verifyPswEdit.getText().toString())){
                                    if (verifyPswEdit.getText().toString().length()>=6){
                                        if ((verifyPswEdit.getText().toString()).equals(setPswEdit.getText().toString())){

                                            MyAppliction.showToast(tiShiYu);
                                            RequestParams requestParams=new RequestParams();
                                            requestParams.addBodyParameter("uid",registerPhoneEdit.getText().toString());
                                            requestParams.addBodyParameter("pwd",MD5Uutils.MD5(setPswEdit.getText().toString()));
                                            requestParams.addBodyParameter("vcode", captchaEdit.getText().toString());
                                            if (userType.equals("talentsUser")){
                                                requestParams.addBodyParameter("state","2");
                                            }else if(userType.equals("merchantUser")){
                                                requestParams.addBodyParameter("state","3");
                                            }
                                            loadingDialog.show();
                                            httpUtils.send(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack<String>() {
                                                @Override
                                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                                    String rerult = responseInfo.result;
                                                    if (rerult != null) {
                                                        ParmeBean<RegisterValueBean> artistParme = JSONObject.parseObject(rerult, new TypeReference<ParmeBean<RegisterValueBean>>() {
                                                        });
                                                        RegisterValueBean registerValueBean = artistParme.getValue();
                                                        // Log.e("makeText",loginValueBean.getState());
                                                        if ("success".equals(registerValueBean.getMessage())) {

                                                            //showExitGameAlert(text);
                                                            finish();
                                                            loadingDialog.dismiss();

                                                        } else {

                                                            MyAppliction.showExitGameAlert("注册失败",RegisterActivity.this);
                                                            loadingDialog.dismiss();

                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(HttpException e, String s) {
                                                    Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                                                    loadingDialog.dismiss();
                                                }
                                            });

                                        }else {
                                            MyAppliction.showExitGameAlert("设置密码和重复密码不一致",RegisterActivity.this);

                                        }

                                    }else {

                                        MyAppliction.showToast("重复密码长度要大于6");
                                    }


                                } else {
                                    MyAppliction.showToast("请输入重复密码");
                                }


                            }else {
                                MyAppliction.showToast("密码长度要大于6");

                            }


                        }else {

                            MyAppliction.showToast("请输入密码");
                        }


                    }else {
                        MyAppliction.showToast("请输入6位正确的验证码");
                    }

                }else {
                    MyAppliction.showToast("请输入验证码");

                }


            }else {
                MyAppliction.showToast("请输入11位手机号码");

            }

        }else {

            MyAppliction.showToast("请输入手机号码");
        }






    }
    private void intiVcodeData() {
        String pohten=registerPhoneEdit.getText().toString();
        if (pohten!=null&&pohten.length()==11) {
            time.start();
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getVcodeData(pohten), new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("onFailure", s);
                }
            });
        }else {
            Toast.makeText(RegisterActivity.this, "电话号码错误", Toast.LENGTH_LONG).show();

        }

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            captchaBtton.setText("重新验证");
            captchaBtton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            captchaBtton.setClickable(false);
            captchaBtton.setText(millisUntilFinished / 1000 + "秒");
        }

    }
}
