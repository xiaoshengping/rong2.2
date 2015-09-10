package com.jeremy.Customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.RadioGroup;

import com.jeremy.Customer.fragment.FragmentTabAdapter;
import com.jeremy.Customer.fragment.HomeFragment;
import com.jeremy.Customer.fragment.MineFragment;
import com.jeremy.Customer.fragment.RecruitmentFragment;
import com.jeremy.Customer.fragment.TalentFragment;

import java.util.ArrayList;


public class HomeActivity extends ActionBarActivity {
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    private RadioGroup homeRG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }
    private void init() {
        intiRadioGroup();
        addFragment();
        FragmentTabAdapter fragmentTabAdapter=new FragmentTabAdapter(this,fragments,R.id.home_layout,homeRG);

    }

    private void intiRadioGroup() {
        homeRG= (RadioGroup) findViewById(R.id.iclub_home_rg);

    }

    private void addFragment() {
        fragments.add(new HomeFragment());
        fragments.add(new TalentFragment());
        fragments.add(new RecruitmentFragment());
        fragments.add(new MineFragment());
    }

}
