package com.jeremy.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeremy.Customer.fragment.FragmentTabAdapter;
import com.jeremy.Customer.fragment.HomeFragment;
import com.jeremy.Customer.fragment.MineFragment;
import com.jeremy.Customer.fragment.RecruitmentFragment;
import com.jeremy.Customer.fragment.TalentFragment;

import java.util.ArrayList;


public class HomeActivity extends ActionBarActivity {
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    private RadioGroup homeRG;
    private Boolean isFirstIn = false;
    private long mExitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences pref = this.getSharedPreferences("myActivityName", 0);
        //取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = pref.getBoolean("isFirstIn", true);
        if(isFirstIn) {
            Intent intent = new Intent().setClass(HomeActivity.this,PagesActivity.class);
            startActivity(intent);
        }

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
        fragments.add(new RecruitmentFragment());
        fragments.add(new TalentFragment());
        fragments.add(new MineFragment());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                HomeActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
