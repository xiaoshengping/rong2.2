package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.InvitePagerAdapter;
import com.jeremy.Customer.fragment.MerchantAcceptInviteFragment;
import com.jeremy.Customer.fragment.MerchantInviteMessageFragment;
import com.jeremy.Customer.fragment.MerchantSuccessfulFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class MerchantInviteActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;

    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();

    @ViewInject(R.id.resume_pager)
    private ViewPager invitePager;
    @ViewInject(R.id.main_tabpager)
    TabPageIndicator tabPageIndicator;
    private InvitePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_invite);
        ViewUtils.inject(this);
        inti();
    }
    private void inti() {
        addFragment();
       // FragmentInviteTabAdapter fragmentTabAdapter=new FragmentInviteTabAdapter(MerchantInviteActivity.this,fragments,R.id.invite_fragment_layout,inviteRadioGrop);
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("商家邀约消息");

        adapter = new InvitePagerAdapter(fragments, getSupportFragmentManager());
        invitePager.setAdapter(adapter);
        tabPageIndicator.setViewPager(invitePager);
    }

    private void addFragment() {
        fragments.add(new MerchantInviteMessageFragment());
        fragments.add(new MerchantAcceptInviteFragment());
        fragments.add(new MerchantSuccessfulFragment());
    }
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
