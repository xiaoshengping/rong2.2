package com.jeremy.Customer.uilt;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.fragment.FragmentResumeTabAdapter;
import com.jeremy.Customer.fragment.OneselfInformationFragment;
import com.jeremy.Customer.fragment.OneselfProductionFragment;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.CustomImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ResumeParticularsActivity extends ActionBarActivity  implements View.OnClickListener {


    @ViewInject(R.id.usericon_background_iv)
    private CustomImageView customImageView;
    @ViewInject(R.id.resume_radioGroup)
    private RadioGroup resumeRadioGroup;
    @ViewInject(R.id.oneself_informaction_rb)
    private RadioButton oneselfInformactionRb;
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
    private ResumeValueBean resumeValueBeans;

    public String  position;//个数
    private String  positions;


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
        oneselfInformactionRb.setChecked(true);
        talenBackIv.setOnClickListener(this);
        cpmpileResumeTv.setOnClickListener(this);
        returnTv.setOnClickListener(this);
         List<Fragment> listFragment=new ArrayList<>();
        oneselfInformationFragment=new OneselfInformationFragment();
        oneselfProductionFragment=new OneselfProductionFragment();
        listFragment.add(oneselfInformationFragment);
        listFragment.add(oneselfProductionFragment);
        resumeValueBeans= (ResumeValueBean) getIntent().getSerializableExtra("resumeValueBeans");

         positions= getIntent().getStringExtra("position");
        Log.e("positions",positions);
        ResumeParticularsActivity.this.setPosition(positions);
        FragmentResumeTabAdapter fragmentInviteTabAdapter=new FragmentResumeTabAdapter(ResumeParticularsActivity.this,listFragment,R.id.resume_fragment_layout,resumeRadioGroup);
        if (resumeValueBeans!=null){
        MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + resumeValueBeans.getUsericon(), customImageView, MyAppliction.RoundedOptions);
        resumeZhNameTv.setText(resumeValueBeans.getResumeZhName());
         if (resumeValueBeans.getResumeSex()==0){
             resumeSexIv.setBackgroundResource(R.mipmap.man_icon);
         }else if (resumeValueBeans.getResumeSex()==1){
             resumeSexIv.setBackgroundResource(R.mipmap.woman_icon);
         }
        resumeAgeTv.setText(resumeValueBeans.getResumeAge()+"");
        resumeWorkPlaceTv.setText(resumeValueBeans.getResumeWorkPlace());
        resumeJobNameIsdTv.setText(resumeValueBeans.getResumeJobCategoryName());
        browseNumberTv.setText(resumeValueBeans.getCommentCount()+"");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        intiResumeListData();
    }

    private void intiResumeListData() {
        HttpUtils httpUtils=new HttpUtils();
        SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        String resumeListUrl= AppUtilsUrl.getResumeLista(uid);
        httpUtils.send(HttpRequest.HttpMethod.GET, resumeListUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<ResumeValueBean> artistParme = JSONObject.parseObject(result, new TypeReference<ArtistParme<ResumeValueBean>>() {
                    });
                    if (artistParme.getState().equals("success")) {

                        ResumeValueBean  resumeValueBeans = artistParme.getValue().get(Integer.valueOf(positions));
                        if (resumeValueBeans!=null){
                            MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + resumeValueBeans.getUsericon(), customImageView, MyAppliction.RoundedOptions);
                            resumeZhNameTv.setText(resumeValueBeans.getResumeZhName());
                            if (resumeValueBeans.getResumeSex()==0){
                                resumeSexIv.setBackgroundResource(R.mipmap.man_icon);
                            }else if (resumeValueBeans.getResumeSex()==1){
                                resumeSexIv.setBackgroundResource(R.mipmap.woman_icon);
                            }
                            resumeAgeTv.setText(resumeValueBeans.getResumeAge()+"");
                            resumeWorkPlaceTv.setText(resumeValueBeans.getResumeWorkPlace());
                            resumeJobNameIsdTv.setText(resumeValueBeans.getResumeJobCategoryName());
                            browseNumberTv.setText(resumeValueBeans.getCommentCount()+"");
                        }
                    }


                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailure.......", s);
            }
        });





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.return_tv:
               finish();
                break;
            case R.id.cpmpile_resume_tv:
                Intent intent=new Intent(ResumeParticularsActivity.this,ModificationResumeActivity.class);
                intent.putExtra("resumeValueBeans",resumeValueBeans);
                intent.putExtra("position",positions);
                startActivity(intent);
                break;



        }
    }

    public  String getPosition() {
        return position;
    }

    public  void setPosition(String position) {
        this.position = position;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
