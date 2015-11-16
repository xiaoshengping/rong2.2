package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jeremy.Customer.R;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.MyInputBox;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class ActivityApplyActivity extends Activity {

    private String activityId;

    private MyInputBox activity_name, activity_age, activity_sex, activity_phone, activity_mailbox;

    private HttpUtils httpUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_activity_apply);
        activityId = getIntent().getStringExtra("ActivityId");
        activity_name = (MyInputBox) findViewById(R.id.activity_name);
        activity_age = (MyInputBox) findViewById(R.id.activity_age);
        activity_sex = (MyInputBox) findViewById(R.id.activity_sex);
        activity_phone = (MyInputBox) findViewById(R.id.activity_phone);
        activity_mailbox = (MyInputBox) findViewById(R.id.activity_mailbox);

        httpUtils = new HttpUtils();
    }

    private void intiEditData() {
        RequestParams requestParams = new RequestParams();
        String name = activity_name.getEditText();
        String age = activity_age.getEditText();
        String sex = activity_sex.getEditText();
        String phone = activity_phone.getEditText();
        String email = activity_mailbox.getEditText();

        requestParams.addBodyParameter("name", name);
        requestParams.addBodyParameter("age", age);
        requestParams.addBodyParameter("sex", sex);
        requestParams.addBodyParameter("phone", phone);
        requestParams.addBodyParameter("email", email);
        requestParams.addBodyParameter("activityId", activityId);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getEditJod(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // Log.e("result",responseInfo.result);
                if (responseInfo.result != null) {
                    Toast.makeText(ActivityApplyActivity.this, "报名成功", Toast.LENGTH_LONG).show();
//                        MyAppliction.showToast("保存数据成功");
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


//        }else{

//        }

    }

    public void apply_button(View v) {
        intiEditData();
    }

    public void back(View v) {
        finish();
    }


//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
////            Toast.makeText(ActivityApplyActivity.this, "你点了软键盘回车按钮",
////                    Toast.LENGTH_SHORT).show();
////            switch (v.getId()){
////                case R.id.activity_name:
////                    Toast.makeText(ActivityApplyActivity.this, activity_name.getEditText().toString(),
////                            Toast.LENGTH_SHORT).show();
////                    break;
////            }
//        }
//        return false;
//    }
}
