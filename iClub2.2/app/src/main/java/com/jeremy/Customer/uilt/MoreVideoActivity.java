package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumeVideoAdapter;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MoreVideoActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.show_video_lv)
    private ListView showVideoLv;

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;

    private ResumeValueBean resumeValueBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_video);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        initView();

    }

    private void initView() {
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("更多视频");
        resumeValueBean= (ResumeValueBean) getIntent().getSerializableExtra("MoreVideoActivity");
        ResumeVideoAdapter resumeVideoAdapter=new ResumeVideoAdapter(resumeValueBean.getResumeMovie(),MoreVideoActivity.this);
        showVideoLv.setAdapter(resumeVideoAdapter);
        resumeVideoAdapter.notifyDataSetChanged();

        showVideoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                Uri videoUri = Uri.parse(AppUtilsUrl.ImageBaseUrl + resumeValueBean.getResumeMovie().get(position).getPath());
                videoIntent.setDataAndType(videoUri, "video/mp4");
                startActivity(videoIntent);
            }
        });


    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
