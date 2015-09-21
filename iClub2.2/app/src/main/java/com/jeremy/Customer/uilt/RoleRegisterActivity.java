package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RoleRegisterActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.talents_user_tv)
    private TextView talentsUserTv;
    @ViewInject(R.id.merchant_user_tv)
    private TextView merchantUserTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_register);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {

        intiView();

    }

    private void intiView() {
        talentsUserTv.setOnClickListener(this);
        merchantUserTv.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.talents_user_tv:
                Intent talentsRegisterIntent=new Intent(RoleRegisterActivity.this,RegisterActivity.class);
                talentsRegisterIntent.putExtra("falge","talentsUser");
                startActivity(talentsRegisterIntent);
                break;

            case R.id.merchant_user_tv:
                Intent merchantRegisterIntent=new Intent(RoleRegisterActivity.this,RegisterActivity.class);
                merchantRegisterIntent.putExtra("falge","merchantUser");
                startActivity(merchantRegisterIntent);
                break;
        }
    }
}
