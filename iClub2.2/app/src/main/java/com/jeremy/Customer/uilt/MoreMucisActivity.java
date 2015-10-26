package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumeMusicAdapter;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MoreMucisActivity extends ActionBarActivity {
    @ViewInject(R.id.show_music_lv)
    private ListView showMusicLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_mucis);
        ViewUtils.inject(this);
        ResumeValueBean resumeValueBean= (ResumeValueBean) getIntent().getSerializableExtra("MoreMucisActivity");
        ResumeMusicAdapter resumeMusicAdapter=new ResumeMusicAdapter(resumeValueBean.getResumeMusic(),MoreMucisActivity.this);
        showMusicLv.setAdapter(resumeMusicAdapter);
        resumeMusicAdapter.notifyDataSetChanged();

    }




}
