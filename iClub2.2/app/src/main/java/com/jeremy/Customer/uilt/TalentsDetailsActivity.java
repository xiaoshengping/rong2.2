package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.MusicAdapter;
import com.jeremy.Customer.adapter.PictureAdapter;
import com.jeremy.Customer.adapter.VideoAdapter;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.Utility;
import com.jeremy.Customer.view.MyGridView;
import com.jeremy.Customer.view.MyScrollView;


public class TalentsDetailsActivity extends Activity implements View.OnClickListener, MyScrollView.OnScrollListener {

    private ListView video_production_list, music_production_list;
    private MyGridView picture_production_list;
    private TextView invite_a_few_tv;
    private RelativeLayout talents_basics_rl;
    private ImageView talents_back_iv;
    private TextView talents_back_baffle_tv;
    private LinearLayout floating_collar;
    private MyScrollView myScrollView;
    private TextView personal_data_button_tv, individual_works_button_tv;
    private TextView personal_data_button_tv1, individual_works_button_tv1;
    private LinearLayout individual_works_ll, personal_data_ll;

    private int mScrollY = 0;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talents_details);
        init();
        initTitleAnimation();
        initVideoProduction();
        initMusicProduction();
        initPictureProduction();
    }

    private void init() {
        myScrollView = (MyScrollView) findViewById(R.id.talents_detail_msv);
        myScrollView.setOnScrollListener(this);

        personal_data_button_tv = (TextView) findViewById(R.id.personal_data_button_tv);
        individual_works_button_tv = (TextView) findViewById(R.id.individual_works_button_tv);
        personal_data_button_tv1 = (TextView) findViewById(R.id.personal_data_button_tv1);
        individual_works_button_tv1 = (TextView) findViewById(R.id.individual_works_button_tv1);
        individual_works_ll = (LinearLayout) findViewById(R.id.individual_works_ll);
        personal_data_ll = (LinearLayout) findViewById(R.id.personal_data_ll);

        personal_data_button_tv.setOnClickListener(this);
        individual_works_button_tv.setOnClickListener(this);
        personal_data_button_tv1.setOnClickListener(this);
        individual_works_button_tv1.setOnClickListener(this);
    }

    //初始化标题栏动画
    private void initTitleAnimation() {
        invite_a_few_tv = (TextView) findViewById(R.id.invite_a_few_tv);
        talents_basics_rl = (RelativeLayout) findViewById(R.id.talents_basics_rl);
        talents_back_iv = (ImageView) findViewById(R.id.talents_back_iv);
        talents_back_baffle_tv = (TextView) findViewById(R.id.talents_back_baffle_tv);
        floating_collar = (LinearLayout) findViewById(R.id.floating_collar);


        bitmap = ((BitmapDrawable) talents_back_iv.getDrawable()).getBitmap();
    }

    //初始化视频作品
    private void initVideoProduction() {
        video_production_list = (ListView) findViewById(R.id.video_production_list);
        VideoAdapter videoAdapter = new VideoAdapter(this);
        video_production_list.setAdapter(videoAdapter);
        Utility.setListViewHeightBasedOnChildren(video_production_list);
    }

    //初始化音乐作品
    private void initMusicProduction() {
        music_production_list = (ListView) findViewById(R.id.music_production_list);
        MusicAdapter musicAdapter = new MusicAdapter(this);
        music_production_list.setAdapter(musicAdapter);
        Utility.setListViewHeightBasedOnChildren(music_production_list);
    }

    //初始化图片作品
    private void initPictureProduction() {
        picture_production_list = (MyGridView) findViewById(R.id.picture_production_list);
        PictureAdapter pictureAdapter = new PictureAdapter(this);
        picture_production_list.setAdapter(pictureAdapter);
    }

    @Override
    public void onScroll(int scrollY) {//监听ScrollView滚动进度

        mScrollY = scrollY;

        TextView a = (TextView) findViewById(R.id.talents_name_tv);
        //邀约数淡出淡入动画
        int invite = Identification.dip2px(this, 100);
        float lucency = ((float) (invite - scrollY) / (float) invite);//控件透明度
        invite_a_few_tv.setAlpha(lucency);
        invite_a_few_tv.setTranslationY(-(int) (scrollY * 0.7));

        //用户基本资料跟随滑动动画
        if (scrollY <= Identification.dip2px(this, 130)) {
            talents_basics_rl.setTranslationY(-scrollY);
        } else {
            talents_basics_rl.setTranslationY(-Identification.dip2px(this, 130));
        }
        if (scrollY >= Identification.dip2px(this, 106)) {
            int zoomMax = Identification.dip2px(this, 280);//最大缩放距离
            float zoom = (float) (zoomMax - (((scrollY) - Identification.dip2px(this, 106)))) / (float) zoomMax;//基本资料缩放比
            if (zoom >= 0.8 && zoom <= 1) {
                talents_basics_rl.setScaleY(zoom);
                talents_basics_rl.setScaleX(zoom);
            } else {
//                talents_basics_rl.setScaleY(zoom);
//                talents_basics_rl.setScaleX(zoom);
            }
        } else {
            talents_basics_rl.setScaleY((float) 1);
            talents_basics_rl.setScaleX((float) 1);
        }

        //人才背景滑动动画
        if (scrollY <= Identification.dip2px(this, 106)) {
//        talents_back_iv.setScaleY((float) 0.5);
            talents_back_iv.setTranslationY(-(int) (scrollY * 0.7));
            talents_back_baffle_tv.setTranslationY(-(int) (scrollY * 1.7));
        } else {
            talents_back_iv.setTranslationY(-(int) (Identification.dip2px(this, 106) * 0.7));
            talents_back_baffle_tv.setTranslationY(-(int) (Identification.dip2px(this, 106) * 1.7));
        }

        //显示浮框
        if (scrollY >= Identification.dip2px(this, 324)) {
            floating_collar.setVisibility(View.VISIBLE);
        } else {
            floating_collar.setVisibility(View.GONE);
        }

        //毛玻璃化
        if (scrollY > 5 && scrollY <= Identification.dip2px(this, 106)) {

            int max = Identification.dip2px(this, 106);
            float maoboli = ((float) scrollY / (float) max) * 10.f;

            if (Build.VERSION.SDK_INT > 16) {
                Bitmap bitmap1 = bitmap.copy(bitmap.getConfig(), true);

                final RenderScript rs = RenderScript.create(this);
                final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                        Allocation.USAGE_SCRIPT);
                final Allocation output = Allocation.createTyped(rs, input.getType());
                final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setRadius(maoboli);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(bitmap1);
                talents_back_iv.setImageBitmap(bitmap1);
            } else {
//                talents_back_iv.setImageBitmap(Identification.fastblur(this, bitmap, 10));
            }

        } else if (scrollY <= 5) {
//            talents_back_iv.setImageResource(R.drawable.talents_details_button_l_shape);
                talents_back_iv.setImageBitmap(bitmap);

        }


        a.setText(scrollY + "");
    }


    @Override
    public void onClick(View v) {
        personal_data_ll.setVisibility(View.GONE);
        individual_works_ll.setVisibility(View.GONE);

        boolean a = false;

        switch (v.getId()) {
            case R.id.personal_data_button_tv:
                a = false;
                break;
            case R.id.individual_works_button_tv:
                a = true;
                break;
            case R.id.personal_data_button_tv1:
                a = false;
                break;
            case R.id.individual_works_button_tv1:
                a = true;
                break;
        }

        buttonView(a);

    }

    private void buttonView(boolean b) {//点击事件显示，b:true右、反之左
        if (b) {
            individual_works_button_tv.setBackgroundResource(R.drawable.talents_details_button_r_shape);
            individual_works_button_tv1.setBackgroundResource(R.drawable.talents_details_button_r_shape);
            personal_data_button_tv.setBackgroundColor(0x00ffffff);
            personal_data_button_tv1.setBackgroundColor(0x00ffffff);

            individual_works_button_tv.setTextColor(0xffffffff);
            individual_works_button_tv1.setTextColor(0xffffffff);
            personal_data_button_tv.setTextColor(0xff282a31);
            personal_data_button_tv1.setTextColor(0xff282a31);

            myScrollView.smoothScrollTo(0, mScrollY);
            individual_works_ll.setVisibility(View.VISIBLE);
        } else {
            individual_works_button_tv.setBackgroundColor(0xffffffff);
            individual_works_button_tv1.setBackgroundColor(0xffffffff);
            personal_data_button_tv.setBackgroundResource(R.drawable.talents_details_button_l_shape);
            personal_data_button_tv1.setBackgroundResource(R.drawable.talents_details_button_l_shape);

            individual_works_button_tv.setTextColor(0xff282a31);
            individual_works_button_tv1.setTextColor(0xff282a31);
            personal_data_button_tv.setTextColor(0xffffffff);
            personal_data_button_tv1.setTextColor(0xffffffff);

            personal_data_ll.setVisibility(View.VISIBLE);
        }
    }

    public void back(View v) {
        finish();
    }

}

