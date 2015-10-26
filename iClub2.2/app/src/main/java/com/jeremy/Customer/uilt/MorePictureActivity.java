package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.GridView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumePictureAdapter;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MorePictureActivity extends ActionBarActivity {
    @ViewInject(R.id.show_picture_gridview)
    private GridView showPictureGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_picture);
        ViewUtils.inject(this);
        ResumeValueBean resumeValueBean = (ResumeValueBean) getIntent().getSerializableExtra("MorePictureActivity");
        ResumePictureAdapter resumePictureAdapter=new ResumePictureAdapter(resumeValueBean.getResumePicture(),this);
        showPictureGridView.setAdapter(resumePictureAdapter);
        resumePictureAdapter.notifyDataSetChanged();
    }


}
