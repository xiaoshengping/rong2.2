package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.InformationValueBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class DetailedInformationActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.tailt_return_tv)
    private Button tailtReturnButton;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    //公告详情
    @ViewInject(R.id.detail_information_layout)
    private LinearLayout detailLayout;
    @ViewInject(R.id.particular_title_tv)
    private TextView tailteDetailTv;
    @ViewInject(R.id.particular_time_tv)
    private TextView timeDetailTv;
    @ViewInject(R.id.particular_content_tv)
    private TextView contentDetailTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_information);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();
    }

    private void intiView() {
        tailtText.setText("公告消息");
        tailtReturnButton.setOnClickListener(this);
        InformationValueBean informationValueBean= (InformationValueBean) getIntent().getSerializableExtra("informationValueBeans");
        tailteDetailTv.setText(informationValueBean.getTitle());
        contentDetailTv.setText(informationValueBean.getContent());
        timeDetailTv.setText(informationValueBean.getPutdate());
    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
