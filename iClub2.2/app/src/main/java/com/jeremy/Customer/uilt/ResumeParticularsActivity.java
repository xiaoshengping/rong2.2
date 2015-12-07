package com.jeremy.Customer.uilt;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumePagerAdapter;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.fragment.OneselfInformationFragment;
import com.jeremy.Customer.fragment.OneselfProductionFragment;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.CustomImageView;
import com.jeremy.Customer.view.CustomViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class ResumeParticularsActivity extends ActionBarActivity  implements View.OnClickListener {


    @ViewInject(R.id.usericon_background_iv)
    private CustomImageView customImageView;
    @ViewInject(R.id.resumeZhName_tv)
    private TextView resumeZhNameTv;
    @ViewInject(R.id.resumeSex_iv)
    private ImageView  resumeSexIv;
    @ViewInject(R.id.resumeAge_tv)
    private TextView resumeAgeTv;
    @ViewInject(R.id.resume_WorkPlace_tv)
    private TextView resumeWorkPlaceTv;
    @ViewInject(R.id.resumeJobName_isd_tv)
    private TextView resumeJobNameIsdTv;
    @ViewInject(R.id.talen_back_iv)
    private ImageView talenBackIv;
    @ViewInject(R.id.cpmpile_resume_tv)
    private TextView cpmpileResumeTv;
    @ViewInject(R.id.browse_number_tv)
    private TextView browseNumberTv;
    @ViewInject(R.id.return_tv)
    private TextView returnTv;



    private OneselfInformationFragment oneselfInformationFragment;
    private OneselfProductionFragment oneselfProductionFragment;
    private ResumeValueBean resumeValueBean;
    private LoadingDialog loadingDialog;

    @ViewInject(R.id.resume_pager)
    private CustomViewPager invitePager;
    @ViewInject(R.id.main_tabpager)
    TabPageIndicator tabPageIndicator;
    private ResumePagerAdapter adapter;
    List<Fragment> fragments=new ArrayList<>();
    private String resumeid;
    private String resumeids;
    private ResumeValueBean resumeValueBeanss;
    private Bitmap  bitmaps;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_particulars);
         ViewUtils.inject(this);
            init();



    }

    private void init() {
        initView();


    }


    private void initView() {
        handler=new Handler();
        loadingDialog = new LoadingDialog(this,"正在加载数据……");
        loadingDialog.show();
        cpmpileResumeTv.setOnClickListener(this);
        returnTv.setOnClickListener(this);
        oneselfInformationFragment=new OneselfInformationFragment();
        oneselfProductionFragment=new OneselfProductionFragment();
        addFragment();
        adapter = new ResumePagerAdapter(fragments, getSupportFragmentManager());
        invitePager.setAdapter(adapter);
        tabPageIndicator.setViewPager(invitePager);

        resumeids= getIntent().getStringExtra("resumeid");
        intiResumeData();
        ResumeParticularsActivity.this.setResumeValueBean(resumeValueBeanss);
        ResumeParticularsActivity.this.setResumeid(resumeids);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        intiResumeData();
    }

    private void intiResumeData() {

        HttpUtils httpUtils=new HttpUtils();
        String resumeListUrl= AppUtilsUrl.getResumeLista(resumeids);
        httpUtils.send(HttpRequest.HttpMethod.GET, resumeListUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ParmeBean<ResumeValueBean> artistParme = JSONObject.parseObject(result, new TypeReference<ParmeBean<ResumeValueBean>>() {
                    });
                    resumeValueBeanss = artistParme.getValue();
                    if (resumeValueBeanss != null) {
                        MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + resumeValueBeanss.getUsericon(), customImageView, MyAppliction.RoundedOptions);
                        resumeZhNameTv.setText(resumeValueBeanss.getResumeZhName());
                        if (resumeValueBeanss.getResumeSex() == 0) {
                            resumeSexIv.setBackgroundResource(R.mipmap.man_icon);
                            resumeAgeTv.setTextColor(getResources().getColor(R.color.textColor0299fe));
                        } else if (resumeValueBeanss.getResumeSex() == 1) {
                            resumeSexIv.setBackgroundResource(R.mipmap.woman_icon);
                            resumeAgeTv.setTextColor(getResources().getColor(R.color.textColorf56f94));
                        }
                        resumeAgeTv.setText(resumeValueBeanss.getResumeAge() + "");
                        resumeWorkPlaceTv.setText(resumeValueBeanss.getResumeWorkPlace());
                        resumeJobNameIsdTv.setText(resumeValueBeanss.getResumeJobCategoryName());
                        browseNumberTv.setText(resumeValueBeanss.getCommentCount() + "");
                       /* MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + resumeValueBeanss.getUsericon(), talenBackIv, MyAppliction.RoundedOptions);

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) talenBackIv.getDrawable();
                        bitmap = bitmapDrawable.getBitmap();
                        talenBackIv.setImageBitmap(fastblur(ResumeParticularsActivity.this, bitmap, 100));*/
                        BitmapUtils bitmapUtils = new BitmapUtils(ResumeParticularsActivity.this);

                        bitmapUtils.display(talenBackIv, AppUtilsUrl.ImageBaseUrl + resumeValueBeanss.getUsericon(), new DefaultBitmapLoadCallBack<ImageView>() {
                            @Override
                            public void onLoadCompleted(ImageView container, String uri, final Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
                                super.onLoadCompleted(container, uri, bitmap, config, from);

                                new Thread(){
                                    public void run(){
                                        bitmaps=bitmap;
                                        handler.post(runnableUi);
                                    }
                                }.start();



                            }
                        });


                        loadingDialog.dismiss();


                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                loadingDialog.dismiss();
                MyAppliction.showToast("网络异常...");
                Log.e("onFailure.......", s);
            }
        });





    }
    // 构建Runnable对象，在runnable中更新界面
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面

            Bitmap bitmap=fastblur(ResumeParticularsActivity.this,bitmaps,30);
            if (bitmap!=null){
                talenBackIv.setImageBitmap(bitmap);
            }
        }

    };
    public Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
    private void addFragment() {
        fragments.add(oneselfInformationFragment);
        fragments.add(oneselfProductionFragment);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.return_tv:
               finish();
                break;
            case R.id.cpmpile_resume_tv:
                    Intent intent=new Intent(ResumeParticularsActivity.this,ModificationResumeActivity.class);
                    intent.putExtra("resumeValueBean",resumeValueBeanss);
                    startActivity(intent);

                break;



        }
    }

    public String getResumeid() {
        return resumeid;
    }

    public void setResumeid(String resumeid) {
        this.resumeid = resumeid;
    }

    public ResumeValueBean getResumeValueBean() {
        return resumeValueBean;
    }

    public void setResumeValueBean(ResumeValueBean resumeValueBean) {
        this.resumeValueBean = resumeValueBean;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
