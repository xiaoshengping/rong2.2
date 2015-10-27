package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
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

         resumeValueBean= (ResumeValueBean) getIntent().getSerializableExtra("resumeValueBean");
        if (resumeValueBean!=null){
            resumeQqEv.setText(resumeValueBean.getResumeQq());
            resumeEmailEt.setText(resumeValueBean.getResumeEmail());
            resumeMobileEt.setText(resumeValueBean.getResumeMobile());
        }
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

          if (!TextUtils.isEmpty(resumeValueBean.getResumeid()+"")){
              requestParams.addBodyParameter("resumeid",resumeValueBean.getResumeid()+"");
        if (!TextUtils.isEmpty(resumeQq)){
            requestParams.addBodyParameter("resumeQq",resumeQq);
          if (!TextUtils.isEmpty(resumeEmail)){
              requestParams.addBodyParameter("resumeEmail",resumeEmail);
              if (!TextUtils.isEmpty(resumeMobile)){
                  requestParams.addBodyParameter("resumeMobile",resumeMobile);
                  httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCompileResume(),requestParams, new RequestCallBack<String>() {
                      @Override
                      public void onSuccess(ResponseInfo<String> responseInfo) {
                          Log.e("kdkkfkkfk",responseInfo.result);
                          if (responseInfo.result!=null){
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
}
