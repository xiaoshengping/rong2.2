package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.uilt.CommentCountActivity;
import com.jeremy.Customer.uilt.ResumeParticularsActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneselfInformationFragment extends Fragment implements View.OnClickListener {

      @ViewInject(R.id.resumeInfo_tv)
      private TextView resumeInfoTv;
      @ViewInject(R.id.oneself_more_layout)
      private LinearLayout oneselfMoreLayout;
      @ViewInject(R.id.experience_more_layout)
      private LinearLayout experienceMoreLayout;
      @ViewInject(R.id.resume_WorkExperience_tv)
      private TextView resumeExperienceTv;
      @ViewInject(R.id.experience_more_tv)
      private TextView experienceMoreTv;
      @ViewInject(R.id.oneself_more_tv)
      private TextView oneselfMoreTv;
      @ViewInject(R.id.resumeQq_tv)
      private TextView resumeQqTv;
     @ViewInject(R.id.resumeEmail_tv)
     private TextView resumeEmailTv;
     @ViewInject(R.id.resumeMobile_tv)
     private TextView resumeMobileTv;
     @ViewInject(R.id.authenticity_tv)
     private TextView authenticityTv;
     @ViewInject(R.id.integrity_tv)
     private TextView integrityTv;
    @ViewInject(R.id.transactionRecord_tv)
    private TextView transactionRecordTv;
    @ViewInject(R.id.commentCount_tv)
    private TextView commentCountTv;

    private List<ResumeValueBean> resumeValueBeans;


    public OneselfInformationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_oneself_information, container, false);
        ViewUtils.inject(this,view);

          init();
        return view;
    }

    private void init() {

        initView();


    }



    private void initView() {
        experienceMoreLayout.setOnClickListener(this);
        oneselfMoreLayout.setOnClickListener(this);
        resumeInfoTv.setOnClickListener(this);
        commentCountTv.setOnClickListener(this);
       ResumeValueBean resumeValueBean=((ResumeParticularsActivity) getActivity()).getResumeValueBean();
        if (resumeValueBean!=null){
            resumeInfoTv.setText(resumeValueBean.getResumeInfo());
            resumeExperienceTv.setText(resumeValueBean.getResumeWorkExperience());
            resumeQqTv.setText(resumeValueBean.getResumeQq());
            resumeEmailTv.setText(resumeValueBean.getResumeEmail());
            resumeMobileTv.setText(resumeValueBean.getResumeMobile());
            authenticityTv.setText(resumeValueBean.getAuthenticity()+"");
            integrityTv.setText(resumeValueBean.getIntegrity()+"");
            transactionRecordTv.setText(resumeValueBean.getTransactionRecord()+"");
            commentCountTv.setText(resumeValueBean.getCommentCount()+"位商家评论过");

            resumeInfoTv.post(new Runnable() {
                @Override
                public void run() {
                    if (resumeInfoTv.getLineCount() > 4) {
                        resumeInfoTv.setLines(4);
                    }

                }
            });
            resumeExperienceTv.post(new Runnable() {
                @Override
                public void run() {
                    if (resumeExperienceTv.getLineCount() > 4) {
                        resumeExperienceTv.setLines(4);
                    }

                }
            });


        }




       }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

                resumeExperienceTv.post(new Runnable() {
                    @Override
                    public void run() {
                        resumeExperienceTv.setLines(resumeExperienceTv.getLineCount());
                    }
                });
                experienceMoreLayout.setVisibility(View.GONE);
                break;
            case R.id.commentCount_tv:
                Intent intent =new Intent(getActivity(), CommentCountActivity.class);
                intent.putExtra("falge","resume");
                startActivity(intent);

                break;


        }
    }



}
