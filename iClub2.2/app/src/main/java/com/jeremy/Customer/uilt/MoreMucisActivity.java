package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumeMusicAdapter;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.MusicActivity;
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
        final ResumeValueBean resumeValueBean= (ResumeValueBean) getIntent().getSerializableExtra("MoreMucisActivity");
        ResumeMusicAdapter resumeMusicAdapter=new ResumeMusicAdapter(resumeValueBean.getResumeMusic(),MoreMucisActivity.this);
        showMusicLv.setAdapter(resumeMusicAdapter);
        resumeMusicAdapter.notifyDataSetChanged();
        showMusicLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (resumeValueBean!=null) {
                    Intent intentMusic = new Intent(MoreMucisActivity.this, MusicActivity.class);  //方法1
                    intentMusic.putExtra("url", AppUtilsUrl.ImageBaseUrl + resumeValueBean.getResumeMusic().get(position).getPath());
                    intentMusic.putExtra("musicName", resumeValueBean.getResumeMusic().get(position).getTitle());
                    intentMusic.putExtras(intentMusic);
                    startActivity(intentMusic);
                    overridePendingTransition(R.anim.out_to_not, R.anim.music_in);
                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
