package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jeremy.Customer.R;
import com.jeremy.Customer.fragment.AcceptInviteFragment;
import com.jeremy.Customer.fragment.FragmentInviteTabAdapter;
import com.jeremy.Customer.fragment.InviteMessageFragment;
import com.jeremy.Customer.fragment.TalentSucceedFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

public class InviteMessageActivity extends ActionBarActivity {
    @ViewInject(R.id.invite_radio_rg)
    private RadioGroup inviteRadioGrop;
    @ViewInject(R.id.nvite_radio_bt)
    private RadioButton adoptRadioButton;
    /*@ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.tailt_return_tv)
    private  TextView tailtReturnTv;*/


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
        FragmentInviteTabAdapter fragmentTabAdapter=new FragmentInviteTabAdapter(InviteMessageActivity.this,fragments,R.id.invite_fragment_layout,inviteRadioGrop);




    }

    private void addFragment() {
        fragments.add(new InviteMessageFragment());
        fragments.add(new AcceptInviteFragment());
        fragments.add(new TalentSucceedFragment());
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
