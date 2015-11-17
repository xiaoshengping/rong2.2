package com.jeremy.Customer.uilt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.MessageBean;
import com.jeremy.Customer.bean.ParmeBean;
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

//昵称
public class NickNameActivity extends ActionBarActivity  implements View.OnClickListener {

    @ViewInject(R.id.nickNamr_edti_text)
    private EditText nickNamrEdti;
    @ViewInject(R.id.tailt_return_tv)
    private Button tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;
    /*@ViewInject(R.id.edit_text_size)
    private TextView editTextSize;*/

    private  String uid;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        ViewUtils.inject(this);

           inti();


    }

    private void inti() {
        intiView();



    }

    private void intiView() {
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("修改昵称");
        saveText.setVisibility(View.VISIBLE);
        saveText.setOnClickListener(this);
        nickNamrEdti.setText(getIntent().getStringExtra("userName"));
        //editTextSize.setText(nickNamrEdti.getText().length()+"/8");
        nickNamrEdti.setSelection(nickNamrEdti.getText().toString().length());
        SQLhelper sqLhelper=new SQLhelper(this);
        db= sqLhelper.getWritableDatabase();
        cursor=db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //editTextSize.setText(nickNamrEdti.getText().length()+"/8");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:
                saveData(uid);
                break;
            
        }

    }

    private void saveData(final String uid) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("uid",uid);
            if (!TextUtils.isEmpty(nickNamrEdti.getText().toString())){
                requestParams.addBodyParameter("username",nickNamrEdti.getText().toString());
                httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getEditUserName(), requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ParmeBean<MessageBean> parmeBean = JSONObject.parseObject(responseInfo.result, new TypeReference<ParmeBean<MessageBean>>() {
                        });
                        if (!TextUtils.isEmpty(responseInfo.result)) {
                            if (parmeBean.getState().equals("success")) {
                                if (parmeBean.getValue().getMessage().equals("success")) {
                                    MyAppliction.showToast("修改昵称成功");
                                    update(uid, nickNamrEdti.getText().toString());
                                    finish();
                                } else {
                                    MyAppliction.showToast("修改昵称失败");
                                }

                            }


                        }


                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
            }else {
                MyAppliction.showToast("你还没有输入昵称");

            }

        }else {
            MyAppliction.showToast("uid不能为空");
        }

    }
    /**
     * 更新昵称
     */
    public void update(String uid,String userName){
        SQLhelper sqLhelper= new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLhelper.USERNAME, userName);
        db.update(SQLhelper.tableName, contentValues,
                "uid=?",
                new String[]{uid});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();

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
}
