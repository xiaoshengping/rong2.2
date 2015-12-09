package com.jeremy.Customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.MyDialog;
import com.jeremy.Customer.fragment.FragmentTabAdapter;
import com.jeremy.Customer.fragment.HomeFragment;
import com.jeremy.Customer.fragment.MineFragment;
import com.jeremy.Customer.fragment.RecruitmentFragment;
import com.jeremy.Customer.fragment.TalentFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeActivity extends ActionBarActivity {
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    private RadioGroup homeRG;
    public Boolean isFirstIn = false;
    private long mExitTime;
    private LoadingDialog loadingDialog;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences pref = this.getSharedPreferences("myActivityName", 0);
        //取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = pref.getBoolean("isFirstIn", true);
        if(isFirstIn) {
            Intent intent = new Intent().setClass(HomeActivity.this,PagesActivity.class);
            startActivityForResult(intent,0);
        }
        HomeFragment.setStart(0);
        startPage();
        init();

    }

    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    //初始化启动页
    private void startPage(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;

        loadingDialog = new LoadingDialog(this,1);
        loadingDialog.show();
        loadingDialog.getWindow().setLayout(screenWidth, screenHeigh - getStatusBarHeight(HomeActivity.this));

        timer.schedule(task, 2000, 1000);       // timeTask

    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    try {
                        if (HomeFragment.getStart() == 2) {
//                            HomeFragment.setSV();
                            timer.cancel();
                            loadingDialog.dismiss();
                        } else if (HomeFragment.getStart() == 1) {
                            timer.cancel();
                            loadingDialog.dismiss();
//                            dialog();
                        }
                    }catch (Exception e) {

                    }

                }
            });
        }
    };
    private MyDialog dialog2;

    //提示框
    private void dialog() {
        dialog2 = new MyDialog(this, Identification.TOOLTIP, Identification.NETWORKANOMALY);
        dialog2.setDetermine(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recommend_list.setVisibility(View.GONE);
                dialog2.dismiss();
            }
        });

        dialog2.show();
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
