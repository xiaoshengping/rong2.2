package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumeVideoAdapter;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MoreVideoActivity extends ActionBarActivity {
    @ViewInject(R.id.show_video_lv)
    private ListView showVideoLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_video);
        ViewUtils.inject(this);
        ResumeValueBean resumeValueBean= (ResumeValueBean) getIntent().getSerializableExtra("MoreVideoActivity");
        ResumeVideoAdapter resumeVideoAdapter=new ResumeVideoAdapter(resumeValueBean.getResumeMovie(),MoreVideoActivity.this);
        showVideoLv.setAdapter(resumeVideoAdapter);
        resumeVideoAdapter.notifyDataSetChanged();

    }


}
