package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.RecruitmentHistoryValueBean;
import com.lidroid.xutils.ViewUtils;
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
         recruitmentHistoryValueBean= (RecruitmentHistoryValueBean) getIntent().getSerializableExtra("recruitmentHistoryValueBean");
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
                if (resumeInfoTv.getLineCount()>4){
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
}
