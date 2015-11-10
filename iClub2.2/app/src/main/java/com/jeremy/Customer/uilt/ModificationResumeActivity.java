package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumePagerAdapter;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.fragment.ModificationInformationFragment;
import com.jeremy.Customer.fragment.ModificationProductionFragment;
import com.jeremy.Customer.view.CustomViewPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class ModificationResumeActivity extends ActionBarActivity implements View.OnClickListener {






    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;


    @ViewInject(R.id.resume_pager)
    private CustomViewPager invitePager;
    @ViewInject(R.id.main_tabpager)
    TabPageIndicator tabPageIndicator;
    private ResumePagerAdapter adapter;
    List<Fragment> fragments=new ArrayList<>();

    private ResumeValueBean resumeValueBeans;

    private  String position;
    private String positions;
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
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("编辑简历");

        positions=  getIntent().getStringExtra("positions");
        ModificationResumeActivity.this.setPosition(positions);

        addFragment();
        adapter = new ResumePagerAdapter(fragments, getSupportFragmentManager());
        invitePager.setAdapter(adapter);
        tabPageIndicator.setViewPager(invitePager);
    }

    private void addFragment() {
        fragments.add(new ModificationInformationFragment());
        fragments.add(new ModificationProductionFragment());


    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
           case R.id.job_classfite_layout:


               break;
           case R.id.job_city_layout:

               break;





        }
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
