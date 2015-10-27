package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumePictureAdapter;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MorePictureActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.show_picture_gridview)
    private GridView showPictureGridView;

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_picture);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        initView();



    }

    private void initView() {
        ResumeValueBean resumeValueBean = (ResumeValueBean) getIntent().getSerializableExtra("MorePictureActivity");
        ResumePictureAdapter resumePictureAdapter=new ResumePictureAdapter(resumeValueBean.getResumePicture(),this);
        showPictureGridView.setAdapter(resumePictureAdapter);
        resumePictureAdapter.notifyDataSetChanged();
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("更多照片");


    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
