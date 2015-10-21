package com.jeremy.Customer.uilt;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.fragment.FragmentResumeTabAdapter;
import com.jeremy.Customer.fragment.OneselfInformationFragment;
import com.jeremy.Customer.fragment.OneselfProductionFragment;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.CustomImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ResumeParticularsActivity extends ActionBarActivity  implements View.OnClickListener {

    @ViewInject(R.id.usericon_background_iv)
    private CustomImageView customImageView;
    @ViewInject(R.id.resume_radioGroup)
    private RadioGroup resumeRadioGroup;
    @ViewInject(R.id.oneself_informaction_rb)
    private RadioButton oneselfInformactionRb;
    @ViewInject(R.id.resumeZhName_tv)
    private TextView resumeZhNameTv;
    @ViewInject(R.id.resumeSex_iv)
    private ImageView  resumeSexIv;
    @ViewInject(R.id.resumeAge_tv)
    private TextView resumeAgeTv;
    @ViewInject(R.id.resume_WorkPlace_tv)
    private TextView resumeWorkPlaceTv;
    @ViewInject(R.id.resumeJobName_isd_tv)
    private TextView resumeJobNameIsdTv;
    @ViewInject(R.id.talen_back_iv)
    private ImageView talenBackIv;
    @ViewInject(R.id.cpmpile_resume_tv)
    private TextView cpmpileResumeTv;
    @ViewInject(R.id.browse_number_tv)
    private TextView browseNumberTv;



    private OneselfInformationFragment oneselfInformationFragment;
    private OneselfProductionFragment oneselfProductionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_particulars);
         ViewUtils.inject(this);
            init();



    }

    private void init() {
        initView();


    }

    private void initView() {
        oneselfInformactionRb.setChecked(true);
        talenBackIv.setOnClickListener(this);
        cpmpileResumeTv.setOnClickListener(this);
         List<Fragment> listFragment=new ArrayList<>();
        oneselfInformationFragment=new OneselfInformationFragment();
        oneselfProductionFragment=new OneselfProductionFragment();
        listFragment.add(oneselfInformationFragment);
        listFragment.add(oneselfProductionFragment);
        ResumeValueBean resumeValueBeans= (ResumeValueBean) getIntent().getSerializableExtra("resumeValueBeans");
        FragmentResumeTabAdapter fragmentInviteTabAdapter=new FragmentResumeTabAdapter(ResumeParticularsActivity.this,listFragment,R.id.resume_fragment_layout,resumeRadioGroup,resumeValueBeans);
        MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + resumeValueBeans.getUsericon(), customImageView, MyAppliction.RoundedOptions);
        resumeZhNameTv.setText(resumeValueBeans.getResumeZhName());
         if (resumeValueBeans.getResumeSex()==0){
             resumeSexIv.setBackgroundResource(R.mipmap.man_icon);
         }else if (resumeValueBeans.getResumeSex()==1){
             resumeSexIv.setBackgroundResource(R.mipmap.woman_icon);
         }
        resumeAgeTv.setText(resumeValueBeans.getResumeAge()+"");
        resumeWorkPlaceTv.setText(resumeValueBeans.getResumeWorkPlace());
        resumeJobNameIsdTv.setText(resumeValueBeans.getResumeJobName());
        browseNumberTv.setText(resumeValueBeans.getCommentCount()+"");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.talen_back_iv:
               finish();
                break;
            case R.id.cpmpile_resume_tv:

                break;



        }
    }
}
