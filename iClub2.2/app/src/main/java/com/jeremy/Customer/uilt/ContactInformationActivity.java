package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
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

public class ContactInformationActivity extends ActionBarActivity implements View.OnClickListener {

       @ViewInject(R.id.tailt_return_tv)
       private TextView tailtReturnTv;
       @ViewInject(R.id.tailt_text)
       private TextView tailtText;
       @ViewInject(R.id.save_text)
       private TextView saveText;

    @ViewInject(R.id.resumeQq_ev)
    private EditText resumeQqEv;
    @ViewInject(R.id.resumeEmail_et)
    private EditText resumeEmailEt;
    @ViewInject(R.id.resumeMobile_et)
    private EditText resumeMobileEt;

    private ResumeValueBean resumeValueBean;
    private Intent intent;
    private static  int CONCATIONINFO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        initView();





    }

    private void initView() {
          intent=getIntent();
          String QQ=intent.getStringExtra("QQ");
          String Emial=intent.getStringExtra("Email");
          String mobile=intent.getStringExtra("mobile");

            resumeQqEv.setText(QQ);
            resumeEmailEt.setText(Emial);
            resumeMobileEt.setText(mobile);

        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("修改联系方式");
        saveText.setText("保存");
        saveText.setVisibility(View.VISIBLE);
        saveText.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                setResult(CONCATIONINFO, intent);
                finish();
                break;
            case R.id.save_text:
                saveData();

                break;



        }


    }

    private void saveData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        String resumeQq=resumeQqEv.getText().toString();
        String resumeEmail=resumeEmailEt.getText().toString();
        String resumeMobile=resumeMobileEt.getText().toString();

          if (!TextUtils.isEmpty(intent.getStringExtra("resumeid"))){
              requestParams.addBodyParameter("resumeid",intent.getStringExtra("resumeid"));
        if (!TextUtils.isEmpty(resumeQq)){
            requestParams.addBodyParameter("resumeQq",resumeQq);
          if (!TextUtils.isEmpty(resumeEmail)){
              requestParams.addBodyParameter("resumeEmail",resumeEmail);
              if (!TextUtils.isEmpty(resumeMobile)){
                  requestParams.addBodyParameter("resumeMobile",resumeMobile);
                  httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCompileResume(),requestParams, new RequestCallBack<String>() {
                      @Override
                      public void onSuccess(ResponseInfo<String> responseInfo) {
                          //Log.e("kdkkfkkfk",responseInfo.result);
                          if (responseInfo.result!=null){
                              intent.putExtra("QQ", resumeQqEv.getText().toString());
                              intent.putExtra("Email", resumeEmailEt.getText().toString());
                              intent.putExtra("mobile", resumeMobileEt.getText().toString());
                              setResult(CONCATIONINFO, intent);
                              finish();

                          }


                      }

                      @Override
                      public void onFailure(HttpException e, String s) {

                      }
                  });


              }else {
                  MyAppliction.showToast("请输入电话号码");
              }

          }else {
              MyAppliction.showToast("请输入电子邮箱");
          }

        }else {
            MyAppliction.showToast("请输入QQ号码");

        }
          }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            setResult(CONCATIONINFO, intent);
        }

        return super.onKeyDown(keyCode, event);
    }
}
