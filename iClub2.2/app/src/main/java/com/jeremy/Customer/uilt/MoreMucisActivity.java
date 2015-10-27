package com.jeremy.Customer.uilt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumeMusicAdapter;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MoreMucisActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.show_music_lv)
    private ListView showMusicLv;

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_mucis);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        initView();
    }

    private void initView() {
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("更多音乐");
        ResumeValueBean resumeValueBean= (ResumeValueBean) getIntent().getSerializableExtra("MoreMucisActivity");
        ResumeMusicAdapter resumeMusicAdapter=new ResumeMusicAdapter(resumeValueBean.getResumeMusic(),MoreMucisActivity.this);
        showMusicLv.setAdapter(resumeMusicAdapter);
        resumeMusicAdapter.notifyDataSetChanged();



    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
