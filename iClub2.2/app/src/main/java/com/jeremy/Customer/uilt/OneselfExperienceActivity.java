package com.jeremy.Customer.uilt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.view.EditTextWithDel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class OneselfExperienceActivity extends ActionBarActivity implements View.OnClickListener {

       @ViewInject(R.id.tailt_return_tv)
       private TextView tailtReturnTv;
       @ViewInject(R.id.tailt_text)
       private TextView tailtText;
       @ViewInject(R.id.save_text)
       private TextView saveText;
       @ViewInject(R.id.oneself_experrience_edit)
       private EditTextWithDel oneselfExperrienceEdit;

       private String hintData;
       private String content;
       private int INFOLT_HINT_DATA=7;//返回自我介绍
       private int EXPERIENCE_HINT_DATA=8;//返回工作经验
       private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneself_experience);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();

    }

    private void intiView() {
       intent=getIntent();
        hintData=getIntent().getStringExtra("hintData");
        content= getIntent().getStringExtra("content").toString();
        if (!TextUtils.isEmpty(content)){
            if (content.equals("介绍一下自己")){
                oneselfExperrienceEdit.setHint("介绍一下自己");
            }else if (content.equals("分享一下自己工作经验")){
                oneselfExperrienceEdit.setHint("分享一下自己工作经验");
            }else if (content.equals("写一下经验要求哦(必填)")){
                oneselfExperrienceEdit.setHint("写一下经验要求哦(必填)");
            }else if (content.equals("写一下职位描述哦(必填)")){
                oneselfExperrienceEdit.setHint("写一下职位描述哦(必填)");
            }else {
                oneselfExperrienceEdit.setText(content);
            }

        }
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("编辑简介");
        saveText.setOnClickListener(this);
        saveText.setVisibility(View.VISIBLE);
        saveText.setText("完成");
        oneselfExperrienceEdit.setCursorVisible(true);
        oneselfExperrienceEdit.setSelection(oneselfExperrienceEdit.getText().toString().length());
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tailt_return_tv:
                if (hintData.equals("infoIntent")){
                    if (TextUtils.isEmpty(oneselfExperrienceEdit.getText().toString())||oneselfExperrienceEdit.getText().toString().equals("介绍一下自己")){
                        intent.putExtra("infoIntent", "notData");
                        setResult(INFOLT_HINT_DATA, intent);
                    }else {
                        intent.putExtra("infoIntent", "data");
                        setResult(INFOLT_HINT_DATA, intent);
                    }
                }else if(hintData.equals("workIntent")){
                    if (TextUtils.isEmpty(oneselfExperrienceEdit.getText().toString())||oneselfExperrienceEdit.getText().toString().equals("分享一下自己的工作经验")){
                        intent.putExtra("workIntent", "notData");
                        setResult(EXPERIENCE_HINT_DATA, intent);
                    }else {
                        intent.putExtra("workIntent", "data");
                        setResult(EXPERIENCE_HINT_DATA, intent);
                    }
                }else if (hintData.equals("modificationInfoIntent")){
                    if (TextUtils.isEmpty(oneselfExperrienceEdit.getText().toString())||oneselfExperrienceEdit.getText().toString().equals("分享一下自己的工作经验")){
                        intent.putExtra("modificationInfoIntent", "notData");
                        setResult(EXPERIENCE_HINT_DATA, intent);
                    }else {
                        intent.putExtra("modificationInfoIntent", "data");
                        setResult(EXPERIENCE_HINT_DATA, intent);
                    }

                }else if (hintData.equals("modificationWorkIntent")){
                    if (TextUtils.isEmpty(oneselfExperrienceEdit.getText().toString())||oneselfExperrienceEdit.getText().toString().equals("分享一下自己的工作经验")){
                        intent.putExtra("modificationWorkIntent", "notData");
                        setResult(EXPERIENCE_HINT_DATA, intent);
                    }else {
                        intent.putExtra("modificationWorkIntent", "data");
                        setResult(EXPERIENCE_HINT_DATA, intent);
                    }

                }
                finish();
                break;
            case R.id.save_text:
                if (!TextUtils.isEmpty(oneselfExperrienceEdit.getText().toString())){
                if (hintData.equals("infoIntent")){
                        intent.putExtra("infoIntent", oneselfExperrienceEdit.getText().toString());
                        setResult(INFOLT_HINT_DATA, intent);
                    finish();
                }else if(hintData.equals("workIntent")){

                        intent.putExtra("workIntent", oneselfExperrienceEdit.getText().toString());
                        setResult(EXPERIENCE_HINT_DATA, intent);
                    finish();

                }else if (hintData.equals("modificationInfoIntent")){
                    intent.putExtra("modificationInfoIntent", oneselfExperrienceEdit.getText().toString());
                    setResult(INFOLT_HINT_DATA, intent);


                }else if (hintData.equals("modificationWorkIntent")){
                    intent.putExtra("modificationWorkIntent", oneselfExperrienceEdit.getText().toString());
                    setResult(EXPERIENCE_HINT_DATA, intent);


                }
                  finish();
                }else {
                    MyAppliction.showExitGameAlert("你还没有填写内容", OneselfExperienceActivity.this);
                }

                break;


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (hintData.equals("infoIntent")){
                if (TextUtils.isEmpty(oneselfExperrienceEdit.getText().toString())||oneselfExperrienceEdit.getText().toString().equals("介绍一下自己")){
                    intent.putExtra("infoIntent", "notData");
                    setResult(INFOLT_HINT_DATA, intent);
                }else {
                    intent.putExtra("infoIntent", "data");
                    setResult(INFOLT_HINT_DATA, intent);
                }
            }else if(hintData.equals("workIntent")){
                if (TextUtils.isEmpty(oneselfExperrienceEdit.getText().toString())||oneselfExperrienceEdit.getText().toString().equals("分享一下自己的工作经验")){
                    intent.putExtra("workIntent", "notData");
                    setResult(EXPERIENCE_HINT_DATA, intent);
                }else {
                    intent.putExtra("workIntent", "data");
                    setResult(EXPERIENCE_HINT_DATA, intent);
                }
            }else if (hintData.equals("modificationInfoIntent")){
                if (TextUtils.isEmpty(oneselfExperrienceEdit.getText().toString())||oneselfExperrienceEdit.getText().toString().equals("分享一下自己的工作经验")){
                    intent.putExtra("modificationInfoIntent", "notData");
                    setResult(EXPERIENCE_HINT_DATA, intent);
                }else {
                    intent.putExtra("modificationInfoIntent", "data");
                    setResult(EXPERIENCE_HINT_DATA, intent);
                }

            }else if (hintData.equals("modificationWorkIntent")){
                if (TextUtils.isEmpty(oneselfExperrienceEdit.getText().toString())||oneselfExperrienceEdit.getText().toString().equals("分享一下自己的工作经验")){
                    intent.putExtra("modificationWorkIntent", "notData");
                    setResult(EXPERIENCE_HINT_DATA, intent);
                }else {
                    intent.putExtra("modificationWorkIntent", "data");
                    setResult(EXPERIENCE_HINT_DATA, intent);
                }

            }
        }

        return super.onKeyDown(keyCode, event);
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
