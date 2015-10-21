package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MerchantParticularsActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_particulars);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        initView();




    }

    private void initView() {
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("招聘详情");
        saveText.setVisibility(View.VISIBLE);
        saveText.setText("保存");
        saveText.setOnClickListener(this);




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:

                break;



        }




    }
}
