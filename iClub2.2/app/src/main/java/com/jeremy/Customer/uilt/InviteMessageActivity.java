package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.InvitePagerAdapter;
import com.jeremy.Customer.fragment.AcceptInviteFragment;
import com.jeremy.Customer.fragment.InviteMessageFragment;
import com.jeremy.Customer.fragment.TalentSucceedFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class InviteMessageActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.invite_radio_rg)
    private RadioGroup inviteRadioGrop;
    @ViewInject(R.id.nvite_radio_bt)
    private RadioButton adoptRadioButton;
    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;

    @ViewInject(R.id.resume_pager)
    private ViewPager invitePager;
    @ViewInject(R.id.main_tabpager)
    TabPageIndicator tabPageIndicator;

    private InvitePagerAdapter adapter;
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_message);
        ViewUtils.inject(this);
        inti();
    }
    private void inti() {
        addFragment();
        //FragmentInviteTabAdapter fragmentTabAdapter=new FragmentInviteTabAdapter(InviteMessageActivity.this,fragments,R.id.invite_fragment_layout,inviteRadioGrop);
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("邀约消息");
        //initiViewPager();
        //initiRadioGroup();
        adapter = new InvitePagerAdapter(fragments, getSupportFragmentManager());
        invitePager.setAdapter(adapter);
        tabPageIndicator.setViewPager(invitePager);


    }

    private void addFragment() {
        fragments.add(new InviteMessageFragment());
        fragments.add(new AcceptInviteFragment());
        fragments.add(new TalentSucceedFragment());
    }

  /* *//*
   * 初始化Viewpager
   * *//*

    public void initiViewPager(){

        ResumePagerAdapter adapter=new ResumePagerAdapter(getSupportFragmentManager(),fragments);
        resumePager.setAdapter(adapter);

        resumePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) inviteRadioGrop.getChildAt(position)).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
   *//*
   * 初始化RadioGrup
   * *//*

    public void initiRadioGroup(){

        inviteRadioGrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (i == checkedId) {
                        resumePager.setCurrentItem(i);
                        Fragment fragment = fragments.get(i);
                        FragmentTransaction ft = obtainFragmentTransaction(i);

                        getCurrentFragment().onPause(); // 暂停当前tab
//                getCurrentFragment().onStop(); // 暂停当前tab

                        if (fragment.isAdded()) {
//                    fragment.onStart(); // 启动目标tab的onStart()
                            fragment.onResume(); // 启动目标tab的onResume()
                        } else {
                            ft.add(R.id.invite_fragment_layout, fragment);
                        }
                        showTab(i); // 显示目标tab
                        ft.commit();
                    }
                }

            }
        });

    }
    *//**
     * 切换tab
     *
     * @param idx
     *//*
    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);

            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }
    *//**
     * 获取一个带动画的FragmentTransaction
     *
     * @param
     * @return
     *//*
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // 设置切换动画
//        if(index > currentTab){
//            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
//        }else{
//            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
//        }
        return ft;
    }
    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }
    public class ResumePagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;
        public ResumePagerAdapter(FragmentManager fm,List<Fragment> data) {
            super(fm);
            this.data=data;
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }*/



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {
       finish();
    }
}
