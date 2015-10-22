package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.fragment.FragmentResumeTabAdapter;
import com.jeremy.Customer.fragment.OneselfInformationFragment;
import com.jeremy.Customer.fragment.OneselfProductionFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ModificationResumeActivity extends ActionBarActivity {

    private OneselfInformationFragment oneselfInformationFragment;
    private OneselfProductionFragment oneselfProductionFragment;

    @ViewInject(R.id.resume_radioGroup)
    private RadioGroup resumeRadioGroup;
    @ViewInject(R.id.oneself_informaction_rb)
    private RadioButton oneselfInformactionRb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_resume);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        initView();


    }

    private void initView() {
        oneselfInformactionRb.setChecked(true);
        List<Fragment> listFragment=new ArrayList<>();
        oneselfInformationFragment=new OneselfInformationFragment();
        oneselfProductionFragment=new OneselfProductionFragment();
        listFragment.add(oneselfInformationFragment);
        listFragment.add(oneselfProductionFragment);
        ResumeValueBean resumeValueBeans= (ResumeValueBean) getIntent().getSerializableExtra("resumeValueBeans");
        FragmentResumeTabAdapter fragmentInviteTabAdapter=new FragmentResumeTabAdapter(ModificationResumeActivity.this,listFragment,R.id.resume_fragment_layout,resumeRadioGroup);


    }


}
