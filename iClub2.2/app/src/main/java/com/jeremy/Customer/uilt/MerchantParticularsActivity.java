package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.RecruitmentHistoryValueBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MerchantParticularsActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;
    @ViewInject(R.id.job_name_tv)
    private TextView jobNameTv;
    @ViewInject(R.id.pay_tv)
    private TextView payTv;
    @ViewInject(R.id.time_neirong_tv)
    private TextView timeNeirongTv;
    @ViewInject(R.id.deliver_tv)
    private TextView deliverTv;
    @ViewInject(R.id.bn_name_tv)
    private TextView bnNameTv;
    @ViewInject(R.id.address_tv)
    private TextView addressTv;
    @ViewInject(R.id.wonging_time_tv)
    private TextView wongingTimeTv;
    @ViewInject(R.id.long_time_tv)
    private TextView longTimeTv;
    @ViewInject(R.id.application_number_tv)
    private TextView applicationNumberTv;
    @ViewInject(R.id.resumeInfo_tv)
    private TextView resumeInfoTv;
    @ViewInject(R.id.resume_WorkExperience_tv)
    private TextView workExperienceTv;
    @ViewInject(R.id.oneself_more_layout)
    private LinearLayout oneselfMoreLayout;
    @ViewInject(R.id.experience_more_layout)
    private LinearLayout experienceMoreLayout;

    private String  position;


    private  RecruitmentHistoryValueBean recruitmentHistoryValueBean;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_particulars);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        initView();




    }

    private void initView() {
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("招聘详情");
        saveText.setVisibility(View.VISIBLE);
        saveText.setText("修改");
        saveText.setOnClickListener(this);
        oneselfMoreLayout.setOnClickListener(this);
        experienceMoreLayout.setOnClickListener(this);
        position=getIntent().getStringExtra("position");
         recruitmentHistoryValueBean= (RecruitmentHistoryValueBean) getIntent().getSerializableExtra("recruitmentHistoryValueBean");
          if (recruitmentHistoryValueBean!=null){
              String a[] = MerchantParticularsActivity.this.getString(R.string.position).split(recruitmentHistoryValueBean.getJobcategory()+":")[1].split(",");
              jobNameTv.setText(a[0]);
              payTv.setText(recruitmentHistoryValueBean.getWorkPay());
              timeNeirongTv.setText(recruitmentHistoryValueBean.getPuttime());
              deliverTv.setText(recruitmentHistoryValueBean.getViewCount()+"");
              bnNameTv.setText(recruitmentHistoryValueBean.getCompanyName());
              addressTv.setText(recruitmentHistoryValueBean.getWorkPlace());
              wongingTimeTv.setText(recruitmentHistoryValueBean.getWorkingTime());
              longTimeTv.setText(recruitmentHistoryValueBean.getWorkingHours());
              applicationNumberTv.setText(recruitmentHistoryValueBean.getRecruitingNumbers());
              resumeInfoTv.setText(recruitmentHistoryValueBean.getJobRequirements());
              workExperienceTv.setText(recruitmentHistoryValueBean.getJobInfo());

          }
        workExperienceTv.post(new Runnable() {
            @Override
            public void run() {
                if (workExperienceTv.getLineCount()>4){
                    workExperienceTv.setLines(4);
                }

            }
        });

        resumeInfoTv.post(new Runnable() {
            @Override
            public void run() {
                if (resumeInfoTv.getLineCount() > 4) {
                    resumeInfoTv.setLines(4);
                }

            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:
                Intent intent=new Intent(MerchantParticularsActivity.this,AddMerchantActivity.class);
                intent.putExtra("recruitmentHistoryValueBean", recruitmentHistoryValueBean);
                intent.putExtra("fagle","editMerchant");
                startActivity(intent);
                break;
            case R.id.oneself_more_layout:
                resumeInfoTv.post(new Runnable() {
                    @Override
                    public void run() {

                        resumeInfoTv.setLines(resumeInfoTv.getLineCount());
                    }
                });

                oneselfMoreLayout.setVisibility(View.GONE);
                break;
            case R.id.experience_more_layout:

                workExperienceTv.post(new Runnable() {
                    @Override
                    public void run() {
                        workExperienceTv.setLines(workExperienceTv.getLineCount());

                    }
                });

                experienceMoreLayout.setVisibility(View.GONE);
                break;


        }




    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initRecruitmentHistoryData();
    }

    private void initRecruitmentHistoryData() {
        SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
         String uid = null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
        }
        if (!TextUtils.isEmpty(uid)){
            HttpUtils httpUtils=new HttpUtils();
            RequestParams requestParams=new RequestParams();
            requestParams.addBodyParameter("uid", uid);
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRecruitmentHistoryListOne(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String result=responseInfo.result;

                    if (result!=null){
                        ArtistParme<RecruitmentHistoryValueBean> artistParme= JSONObject.parseObject(result,new TypeReference<ArtistParme<RecruitmentHistoryValueBean>>(){});
                        if (artistParme.getState().equals("success")){
                            RecruitmentHistoryValueBean recruitmentHistoryValueBean=artistParme.getValue().get(Integer.valueOf(position));
                            if (recruitmentHistoryValueBean!=null){
                                //jobNameTv.setText(recruitmentHistoryValueBean.get);
                                payTv.setText(recruitmentHistoryValueBean.getWorkPay());
                                timeNeirongTv.setText(recruitmentHistoryValueBean.getPuttime());
                                deliverTv.setText(recruitmentHistoryValueBean.getViewCount()+"");
                                bnNameTv.setText(recruitmentHistoryValueBean.getCompanyName());
                                addressTv.setText(recruitmentHistoryValueBean.getWorkPlace());
                                wongingTimeTv.setText(recruitmentHistoryValueBean.getWorkingTime());
                                longTimeTv.setText(recruitmentHistoryValueBean.getWorkingHours());
                                applicationNumberTv.setText(recruitmentHistoryValueBean.getRecruitingNumbers());
                                resumeInfoTv.setText(recruitmentHistoryValueBean.getJobRequirements());
                                workExperienceTv.setText(recruitmentHistoryValueBean.getJobInfo());

                            }




                        }
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });


        }





    }


}
