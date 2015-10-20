package com.jeremy.Customer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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

        Bundle bundle=getArguments();
        ResumeValueBean resumeValueBeans= (ResumeValueBean) bundle.getSerializable("resumeValueBeans");
       if (resumeValueBeans!=null){
           resumeInfoTv.setText(resumeValueBeans.getResumeInfo());
           Log.e("hdhfhfkfk",resumeInfoTv.getLineCount()+"");
           if (resumeInfoTv.getLineCount()>0&&resumeInfoTv.getLineCount()<=4){
               oneselfMoreLayout.setVisibility(View.GONE);
               resumeInfoTv.setLines(resumeInfoTv.getLineCount());
           }
           resumeExperienceTv.setText(resumeValueBeans.getResumeWorkExperience());
           if (resumeExperienceTv.getLineCount()>0&&resumeExperienceTv.getLineCount()<=4){
               experienceMoreLayout.setVisibility(View.GONE);
               resumeExperienceTv.setLines(resumeExperienceTv.getLineCount());
           }
           resumeQqTv.setText(resumeValueBeans.getResumeQq());
           resumeEmailTv.setText(resumeValueBeans.getResumeEmail());
           resumeMobileTv.setText(resumeValueBeans.getResumeMobile());
           authenticityTv.setText(resumeValueBeans.getAuthenticity()+"");
           integrityTv.setText(resumeValueBeans.getIntegrity()+"");
           transactionRecordTv.setText(resumeValueBeans.getTransactionRecord()+"");
           commentCountTv.setText(resumeValueBeans.getCommentCount()+"位商家评论过");
       }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.oneself_more_layout:
                if (resumeInfoTv.getLineCount()>=4){
                    resumeInfoTv.setLines(resumeInfoTv.getLineCount());
                    oneselfMoreLayout.setVisibility(View.GONE);
                }

                break;
            case R.id.experience_more_layout:
                if (resumeExperienceTv.getLineCount()>=4){
                    resumeExperienceTv.setLines(resumeExperienceTv.getLineCount());
                    experienceMoreLayout.setVisibility(View.GONE);
                }
                break;


        }
    }
}
