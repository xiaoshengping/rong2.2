package com.jeremy.Customer.uilt;


import android.content.Intent;
import android.os.Bundle;
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
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
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
   /* @ViewInject(R.id.resume_radioGroup)
    private RadioGroup resumeRadioGroup;
    @ViewInject(R.id.oneself_informaction_rb)
    private RadioButton oneselfInformactionRb;*/
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
                       // new OneselfProductionAsynctack(talenBackIv,AppUtilsUrl.ImageBaseUrl + resumeValueBeanss.getUsericon()).execute();
                        loadingDialog.dismiss();

                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                loadingDialog.dismiss();
                Log.e("onFailure.......", s);
            }
        });





    }
   /* private void blur(Bitmap bkg, View view, float radius) {
        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        //canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
        RenderScript rs = RenderScript.create(this);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
        rs.destroy();
    }

    class OneselfProductionAsynctack extends AsyncTask<String, Void, Bitmap> {
        private ImageView imgView;
        private String path;

        public OneselfProductionAsynctack(ImageView imageView,String path) {
            this.imgView = imageView;
            this.path = path;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //这里的创建缩略图方法是调用VideoUtil类的方法，也是通过 android中提供的 ThumbnailUtils.createVideoThumbnail(vidioPath, kind);
            Bitmap bitmap = getImageThumbnail(path, 100, 100);
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {

            //imgView.setImageBitmap(bitmap);
            blur(bitmap,imgView,0.8f);
        }
    }


    private Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }*/


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
