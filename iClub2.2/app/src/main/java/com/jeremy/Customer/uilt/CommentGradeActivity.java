package com.jeremy.Customer.uilt;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.InviteMessgaeListValueBean;
import com.jeremy.Customer.bean.mine.MerchantInviteValueBean;
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

public class CommentGradeActivity extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.tailt_return_tv)
    private Button tailtReturn;


    //加减
    @ViewInject(R.id.grade_radiogroup)
    private RadioGroup gradeRadiogroup;
    @ViewInject(R.id.honesty_grade_radiogroup)
    private RadioGroup honestyRradeRg;
    @ViewInject(R.id.honesty_three_grade)
    private RadioButton honestyThreeGrade;
    @ViewInject(R.id.truth_three_grade)
    private RadioButton truthThreeGrade;
    @ViewInject(R.id.truth_state_tv)
    private TextView truthStateTv;
    @ViewInject(R.id.honesty_state_tv)
    private TextView honestyStateTv;



    //提交按钮
    @ViewInject(R.id.commit_comment_tv)
    private TextView commitCommentTv;

    //数据
    private HttpUtils httpUtils;
    private RequestParams requestParams;
    private String uid=null;
    @ViewInject(R.id.comment_context_et)
    private EditText commentContextEt;
    private InviteMessgaeListValueBean inviteMessgaeListValueBeans;
    private MerchantInviteValueBean merchantInviteValueBeans;
    //真实性
    private String authenticity;
    private boolean truthGrade;
    //诚实性
    private String integrity;
    private boolean honestyGrade;
    private  String falge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_grade);
        ViewUtils.inject(this);
        inti();
    }
    private void inti() {
        intiView();


    }

    private void intiView() {
        falge=getIntent().getStringExtra("falgeData");

        SQLhelper sqLhelper=new SQLhelper(CommentGradeActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        inviteMessgaeListValueBeans= (InviteMessgaeListValueBean) getIntent().getSerializableExtra("inviteMessgaeListValueBeans");
        merchantInviteValueBeans= (MerchantInviteValueBean) getIntent().getSerializableExtra("MerchantInviteValueBean");

        gradeRadiogroup.setOnCheckedChangeListener(this);
        honestyRradeRg.setOnCheckedChangeListener(this);
        commitCommentTv.setOnClickListener(this);
        tailtReturn.setOnClickListener(this);
        tailtText.setText("评论");
        httpUtils=new HttpUtils();
        requestParams=new RequestParams();
        commentContextEt.setSelection(commentContextEt.getText().toString().length());


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()){

            case R.id.grade_radiogroup:

                switch (checkedId){

                    case R.id.truth_one_grade:
                        authenticity="-5";
                        // Toast.makeText(CooperationCommentActivity.this, authenticity, Toast.LENGTH_LONG).show();
                        truthStateTv.setText("很差");
                        break;
                    case R.id.truth_two_grade:
                        authenticity="0";
                        //Toast.makeText(CooperationCommentActivity.this, authenticity, Toast.LENGTH_LONG).show();
                        truthStateTv.setText("一般");
                        break;
                    case R.id.truth_three_grade:
                        authenticity="1";
                        truthStateTv.setText("好");
                        break;
                    case R.id.truth_four_grade:
                        authenticity="3";
                        truthStateTv.setText("很好");
                        break;
                    case R.id.truth_five_grade:
                        authenticity="5";
                        truthStateTv.setText("非常好");
                        break;

                }



                break;
            case R.id.honesty_grade_radiogroup:
                switch (checkedId){
                    case R.id.honesty_one_grade:
                        integrity="-5";
                        honestyStateTv.setText("很差");
                        break;
                    case R.id.honesty_two_grade:
                        integrity="0";
                        honestyStateTv.setText("一般");
                        break;
                    case R.id.honesty_three_grade:
                        integrity="1";
                        honestyStateTv.setText("好");
                        break;
                    case R.id.honesty_four_grade:
                        integrity="3";
                        honestyStateTv.setText("很好");
                        break;
                    case R.id.honesty_five_grade:
                        integrity="5";
                        honestyStateTv.setText("非常好");
                        break;


                }
                break;

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commit_comment_tv:
                if (falge.equals("SuccessfulInviteFragment")){

                    if (!TextUtils.isEmpty(commentContextEt.getText().toString())&&!TextUtils.isEmpty(authenticity)&&!TextUtils.isEmpty(integrity)){
                        commentContentData("resumeid", inviteMessgaeListValueBeans.getInviteResume().getResumeid(), "beid", inviteMessgaeListValueBeans.getInvitePerson().getId(), AppUtilsUrl.getCommentCommit());
                        commentGradeData(inviteMessgaeListValueBeans.getInvitePerson().getId());
                       // adoptData(AppUtilsUrl.getModificationResume(), "3", inviteMessgaeListValueBeans.getInviteid());
                    }else {
                        MyAppliction.showExitGameAlert("您还没有输入评论内容或者分数", CommentGradeActivity.this);
                    }


                }else if (falge.equals("MerchantSuccessfulInviteFragment")){


                    if (!TextUtils.isEmpty(commentContextEt.getText().toString())&&!TextUtils.isEmpty(authenticity)&&!TextUtils.isEmpty(integrity)){
                        commentContentData("uid", uid, "resumeid", merchantInviteValueBeans.getInviteResume().getResumeid()+"", AppUtilsUrl.getCommentResume());
                        commentGradeData(merchantInviteValueBeans.getInviteResume().getPersonid() + "");
                       // adoptData(AppUtilsUrl.getModificationMerchant(), "3", merchantInviteValueBeans.getInviteid());
                    }else {
                        MyAppliction.showExitGameAlert("您还没有输入评论内容或者分数", CommentGradeActivity.this);
                    }

                }

                break;
            case R.id.tailt_return_tv:
                finish();
                break;
        }

    }

    private void adoptData(String url,String status,String inviteid) {

        requestParams.addBodyParameter("inviteid",inviteid);
        requestParams.addBodyParameter("status", status);
        httpUtils.send(HttpRequest.HttpMethod.POST, url,requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailure", s);
            }
        });
    }



    private void commentGradeData(String personid) {
        requestParams.addBodyParameter("authenticity",authenticity );
        requestParams.addBodyParameter("integrity", integrity);
        requestParams.addBodyParameter("personid", personid);
        //Log.e("truthGrade", truthGrade);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCommentGrade(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                showExitGameAlert("评论成功。");
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailureonFailure",s);
            }
        });





    }

    //评论成功对话框
    public void showExitGameAlert(String text) {
        final AlertDialog dlg = new AlertDialog.Builder(CommentGradeActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.tishi_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                dlg.cancel();
            }
        });
    }


    private void commentContentData(String id,String uid,String beuid,String beuidValue,String url) {
        requestParams.addBodyParameter(id, uid);
        requestParams.addBodyParameter(beuid, beuidValue);
        requestParams.addBodyParameter("body", commentContextEt.getText().toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("sjdjjjfjj",responseInfo.result);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailure", s);
            }
        });





    }

}
