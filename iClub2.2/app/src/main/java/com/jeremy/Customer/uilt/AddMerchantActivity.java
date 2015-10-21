package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.RecruitmentHistoryValueBean;
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

public class AddMerchantActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;

    //添加招聘
    @ViewInject(R.id.position_edit)
    private EditText positionEdit;
    @ViewInject(R.id.workPay_edit)
    private EditText workPayEdit;
    @ViewInject(R.id.recruitingNumbers_edit)
    private EditText recruitingNumbersEdit;
    @ViewInject(R.id.profession_classification_tv)
    private TextView professionClassfitionTv;
    @ViewInject(R.id.work_address_tv)
    private TextView workAddressTv;
    @ViewInject(R.id.experience_require_tv)
    private TextView experienceRequireTv;
    @ViewInject(R.id.work_describe_tv)
    private TextView  workDescribeTv;

    @ViewInject(R.id.jobRequirements_layout)
    private LinearLayout jobRequirementsLayout;
    @ViewInject(R.id.jobInfo_layout)
    private LinearLayout jobInfoLayout;

    private  String merchantWork;
    private String merchantInfo;

    private RecruitmentHistoryValueBean recruitmentHistoryValueBean;
    private  String uid=null;
    private Intent intent;
    private HttpUtils httpUtils;
    private RequestParams requestParams;
    private static final int INFOLT_HINT_DATA=7;//工作要求
    private static final int EXPERIENCE_HINT_DATA=8;//职位描述
    /*private AreaBean areaBean = new AreaBean();
    private int job_classfite_num = -1;//职业类别
    private int job_city_num = -1;//工作地点*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_merchant);
        ViewUtils.inject(this);
        init();
    }

    private void init() {

        intiView();
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        cursor.close();
        db.close();


    }
    private void intiView() {
        httpUtils=new HttpUtils();
        intent=getIntent();
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加招聘");
        saveText.setText("保存");
        saveText.setOnClickListener(this);
        saveText.setVisibility(View.VISIBLE);
        jobRequirementsLayout.setOnClickListener(this);
        jobInfoLayout.setOnClickListener(this);
        professionClassfitionTv.setOnClickListener(this);
        workAddressTv.setOnClickListener(this);
        recruitmentHistoryValueBean= (RecruitmentHistoryValueBean) intent.getSerializableExtra("recruitmentHistoryValueBean");
        if (recruitmentHistoryValueBean!=null){
            workAddressTv.setText(recruitmentHistoryValueBean.getWorkPlace());
            workAddressTv.setTextColor(getResources().getColor(R.color.white));
            professionClassfitionTv.setTextColor(getResources().getColor(R.color.white));
            positionEdit.setText(recruitmentHistoryValueBean.getPosition());
            if (!TextUtils.isEmpty(recruitmentHistoryValueBean.getJobRequirements())){
                experienceRequireTv.setText(recruitmentHistoryValueBean.getJobRequirements());
                experienceRequireTv.setTextColor(getResources().getColor(R.color.white));
            }else {
                experienceRequireTv.setText("写一下经验要求哦(必填)");
                experienceRequireTv.setTextColor(getResources().getColor(R.color.hunTextColor));
            }
            if (!TextUtils.isEmpty(recruitmentHistoryValueBean.getJobInfo())){
                workDescribeTv.setText(recruitmentHistoryValueBean.getJobInfo());
                workDescribeTv.setTextColor(getResources().getColor(R.color.white));
            }else {
                workDescribeTv.setText("写一下职位描述哦(必填)");
                workDescribeTv.setTextColor(getResources().getColor(R.color.hunTextColor));
            }
            merchantWork=recruitmentHistoryValueBean.getJobRequirements();
            merchantInfo=recruitmentHistoryValueBean.getJobInfo();
            workPayEdit.setText(recruitmentHistoryValueBean.getWorkPay());
            recruitingNumbersEdit.setText(recruitmentHistoryValueBean.getRecruitingNumbers());
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jobRequirements_layout:
                Intent infoIntent = new Intent(AddMerchantActivity.this, OneselfExperienceActivity.class);  //方法1
                infoIntent.putExtra("hintData","infoIntent");
                if (!TextUtils.isEmpty(experienceRequireTv.getText().toString())){
                    infoIntent.putExtra("content",experienceRequireTv.getText().toString());
                }
                startActivityForResult(infoIntent, INFOLT_HINT_DATA);
                break;
            case R.id.jobInfo_layout:
                Intent workIntent = new Intent(AddMerchantActivity.this, OneselfExperienceActivity.class);  //方法1
                workIntent.putExtra("hintData","workIntent");
                if (!TextUtils.isEmpty(workDescribeTv.getText().toString())){
                    workIntent.putExtra("content",workDescribeTv.getText().toString());
                }
                startActivityForResult(workIntent, EXPERIENCE_HINT_DATA);
                break;
            case R.id.save_text:
                intiData();
                break;
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.profession_classification_tv:


                break;
            case R.id.work_address_tv:


                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case INFOLT_HINT_DATA:
                if (data.getStringExtra("infoIntent").toString().equals("notData")){
                    if (experienceRequireTv.getText().toString().equals("写一下经验要求哦(必填)")){
                        experienceRequireTv.setText("写一下经验要求哦(必填)");
                        experienceRequireTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        experienceRequireTv.setText(experienceRequireTv.getText().toString());
                        experienceRequireTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else if (data.getStringExtra("infoIntent").toString().equals("data")){
                    if (experienceRequireTv.getText().toString().equals("写一下经验要求哦(必填)")){
                        experienceRequireTv.setText("写一下经验要求哦(必填)");
                        experienceRequireTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        experienceRequireTv.setText(experienceRequireTv.getText().toString());
                        experienceRequireTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else {
                    experienceRequireTv.setText(data.getStringExtra("infoIntent").toString());
                    experienceRequireTv.setTextColor(getResources().getColor(R.color.textColor242424));
                }

                break;
            case EXPERIENCE_HINT_DATA:
                if (data.getStringExtra("workIntent").toString().equals("notData")){
                    if (workDescribeTv.getText().toString().equals("写一下职位描述哦(必填)")){
                        workDescribeTv.setText("写一下职位描述哦(必填)");
                        workDescribeTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        workDescribeTv.setText(workDescribeTv.getText().toString());
                        workDescribeTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }
                }else if (data.getStringExtra("workIntent").toString().equals("data")){
                    if (workDescribeTv.getText().toString().equals("写一下职位描述哦(必填)")){
                        workDescribeTv.setText("写一下职位描述哦(必填)");
                        workDescribeTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        workDescribeTv.setText(workDescribeTv.getText().toString());
                        workDescribeTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else {
                    workDescribeTv.setText(data.getStringExtra("workIntent").toString());
                    workDescribeTv.setTextColor(getResources().getColor(R.color.textColor242424));
                }

                break;
        }
    }

    private void intiEditData() {
        RequestParams requestParams=new RequestParams();
        String position=positionEdit.getText().toString();
        String workPay=workPayEdit.getText().toString();
        String recruitingNumbers=recruitingNumbersEdit.getText().toString();
        if (!TextUtils.isEmpty(position)&&!TextUtils.isEmpty(workPay)&&!TextUtils.isEmpty(recruitingNumbers)
                &&!TextUtils.isEmpty(merchantWork)
                &&!TextUtils.isEmpty(merchantInfo)){
            requestParams.addBodyParameter("jobid",recruitmentHistoryValueBean.getJobId()+"");
            //requestParams.addBodyParameter("jobCategory",job_classfite_num+"");
            //requestParams.addBodyParameter("cityid",job_city_num+"");
            requestParams.addBodyParameter("position",position);
            requestParams.addBodyParameter("workPay",workPay);
            requestParams.addBodyParameter("recruitingNumbers",recruitingNumbers);
            requestParams.addBodyParameter("jobRequirements",merchantWork);
            requestParams.addBodyParameter("jobInfo", merchantInfo);
            MyAppliction.showToast("正在保存数据......");

            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getEditJod(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    // Log.e("result",responseInfo.result);
                    if (responseInfo.result!=null){
                        MyAppliction.showToast("保存数据成功");
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {

                    MyAppliction.showToast("网络请求超时");
                }
            });


        }else{
            MyAppliction.showExitGameAlert("您输入的内容不全", AddMerchantActivity.this);

        }






    }



    private void intiData() {
        RequestParams requestParams = new RequestParams();
        String position = positionEdit.getText().toString();
        String workPay = workPayEdit.getText().toString();
        String recruitingNumbers = recruitingNumbersEdit.getText().toString();
        String merchantInfo=experienceRequireTv.getText().toString();
        String merchantWork=workDescribeTv.getText().toString();
        if (!TextUtils.isEmpty(position) && !TextUtils.isEmpty(workPay) && !TextUtils.isEmpty(merchantWork)
                && !TextUtils.isEmpty(merchantInfo) && !TextUtils.isEmpty(recruitingNumbers)) {
            requestParams.addBodyParameter("uid", uid);
            //requestParams.addBodyParameter("jobCategory", job_classfite_num + "");
            //requestParams.addBodyParameter("cityid", job_city_num + "");
            requestParams.addBodyParameter("position", position);
            requestParams.addBodyParameter("workPay", workPay);
            requestParams.addBodyParameter("recruitingNumbers", recruitingNumbers);
            requestParams.addBodyParameter("jobRequirements", merchantWork);
            requestParams.addBodyParameter("jobInfo", merchantInfo);
            MyAppliction.showToast("正在保存数据......");

            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddJod(), requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (responseInfo.result != null) {
                        MyAppliction.showToast("保存数据成功");
                        finish();
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {


                }
            });


        } else {

            MyAppliction.showExitGameAlert("您输入的内容不全", AddMerchantActivity.this);

        }
    }

    }
