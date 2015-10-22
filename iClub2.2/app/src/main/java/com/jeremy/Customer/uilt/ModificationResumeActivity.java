package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.fragment.ModificationInformationFragment;
import com.jeremy.Customer.fragment.ModificationProductionFragment;
import com.jeremy.Customer.fragment.ModificationResumeTabAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ModificationResumeActivity extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener {

    private ModificationProductionFragment modificationProductionFragment;
    private ModificationInformationFragment modificationInformationFragment;

    @ViewInject(R.id.resume_radioGroup)
    private RadioGroup resumeRadioGroup;
    @ViewInject(R.id.oneself_informaction_rb)
    private RadioButton oneselfInformactionRb;
    @ViewInject(R.id.message_layout)
    private LinearLayout messageLayout;

    private String position;
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
        resumeRadioGroup.setOnCheckedChangeListener(this);
        oneselfInformactionRb.setChecked(true);
        List<Fragment> listFragment=new ArrayList<>();
        modificationInformationFragment=new ModificationInformationFragment();
        modificationProductionFragment=new ModificationProductionFragment();

        listFragment.add(modificationInformationFragment);
        listFragment.add(modificationProductionFragment);
        ResumeValueBean resumeValueBeans= (ResumeValueBean) getIntent().getSerializableExtra("resumeValueBeans");
        String positions=getIntent().getStringExtra("position");
        ModificationResumeActivity.this.setPosition(positions);
        ModificationResumeTabAdapter fragmentInviteTabAdapter=new ModificationResumeTabAdapter(ModificationResumeActivity.this,listFragment,R.id.resume_fragment_layout,resumeRadioGroup);


    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.oneself_production_rb:
                messageLayout.setVisibility(View.GONE);
                Toast.makeText(ModificationResumeActivity.this,"jdjjd",Toast.LENGTH_LONG);
                break;
            case R.id.oneself_informaction_rb:
                Toast.makeText(ModificationResumeActivity.this,"j2222",Toast.LENGTH_LONG);
                messageLayout.setVisibility(View.VISIBLE);
                break;


        }
    }
}
