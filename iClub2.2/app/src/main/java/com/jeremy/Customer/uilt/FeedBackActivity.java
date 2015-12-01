package com.jeremy.Customer.uilt;

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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

public class FeedBackActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.about_idea_edit)
    private EditText aboutIdeaEdit;
    @ViewInject(R.id.feedback_finist_button)
    private Button feedbackFinistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();
    }

    private void intiView() {
        tailtText.setText("更多");
        tailtReturnTv.setOnClickListener(this);
        feedbackFinistButton.setOnClickListener(this);
        aboutIdeaEdit.setSelection(aboutIdeaEdit.getHint().toString().length());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.feedback_finist_button:
                String aboutIdeaString =aboutIdeaEdit.getText().toString();
                if (!TextUtils.isEmpty(aboutIdeaString)){
                    aboutIntiData();
                }else {
                    // Toast.makeText(AmendAboutActivity.this, "您还没有输入内容", Toast.LENGTH_LONG).show();
                    MyAppliction.showExitGameAlert("您还没有输入内容", FeedBackActivity.this);
                }
                break;
        }
    }
    private void aboutIntiData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("content",aboutIdeaEdit.getText().toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAboutIdea(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result= responseInfo.result;
                //  Log.e("result",result);
                MyAppliction.showToast("谢谢您宝贵的意见!");
                finish();

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });



    }

}
