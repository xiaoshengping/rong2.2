package com.jeremy.Customer.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.uilt.ResumeParticularsActivity;
import com.jeremy.Customer.uilt.SQLhelper;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneselfInformationFragment extends Fragment implements View.OnClickListener {

      @ViewInject(R.id.resumeInfo_tv)
      private TextView resumeInfoTv;
      @ViewInject(R.id.oneself_more_layout)
      private LinearLayout oneselfMoreLayout;
      @ViewInject(R.id.experience_more_layout)
      private LinearLayout experienceMoreLayout;
      @ViewInject(R.id.resume_WorkExperience_tv)
      private TextView resumeExperienceTv;
      @ViewInject(R.id.experience_more_tv)
      private TextView experienceMoreTv;
      @ViewInject(R.id.oneself_more_tv)
      private TextView oneselfMoreTv;
      @ViewInject(R.id.resumeQq_tv)
      private TextView resumeQqTv;
     @ViewInject(R.id.resumeEmail_tv)
     private TextView resumeEmailTv;
     @ViewInject(R.id.resumeMobile_tv)
     private TextView resumeMobileTv;
     @ViewInject(R.id.authenticity_tv)
     private TextView authenticityTv;
     @ViewInject(R.id.integrity_tv)
     private TextView integrityTv;
    @ViewInject(R.id.transactionRecord_tv)
    private TextView transactionRecordTv;
    @ViewInject(R.id.commentCount_tv)
    private TextView commentCountTv;

    private List<ResumeValueBean> resumeValueBeans;


    public OneselfInformationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_oneself_information, container, false);
        ViewUtils.inject(this,view);

          init();
        return view;
    }

    private void init() {
        intiResumeListData();
        initView();


    }

    private void initView() {
        experienceMoreLayout.setOnClickListener(this);
        oneselfMoreLayout.setOnClickListener(this);
        resumeInfoTv.setOnClickListener(this);


        if (resumeInfoTv.getLineCount()>0&&resumeInfoTv.getLineCount()<=4){
            oneselfMoreLayout.setVisibility(View.GONE);
            resumeInfoTv.setLines(resumeInfoTv.getLineCount());
        }
        if (resumeExperienceTv.getLineCount()>0&&resumeExperienceTv.getLineCount()<=4){
            experienceMoreLayout.setVisibility(View.GONE);
            resumeExperienceTv.setLines(resumeExperienceTv.getLineCount());
        }



       }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.oneself_more_layout:
                if (resumeInfoTv.getLineCount()>=4){
                    resumeInfoTv.setLines(resumeInfoTv.getLineCount());
                    oneselfMoreLayout.setVisibility(View.GONE);
                }

                break;
            case R.id.experience_more_layout:
                if (resumeExperienceTv.getLineCount()>=4){
                    resumeExperienceTv.setLines(resumeExperienceTv.getLineCount());
                    experienceMoreLayout.setVisibility(View.GONE);
                }
                break;


        }
    }

    private void intiResumeListData() {
        HttpUtils  httpUtils=new HttpUtils();
        SQLhelper sqLhelper=new SQLhelper(getActivity());
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
                String result=responseInfo.result;
                if (result!=null){
                    ArtistParme<ResumeValueBean>   artistParme= JSONObject.parseObject(result,new TypeReference<ArtistParme<ResumeValueBean>>(){});
                    if (artistParme.getState().equals("success")){
                        resumeValueBeans= artistParme.getValue();
                        ResumeValueBean resumeValueBean=  resumeValueBeans.get(Integer.valueOf(((ResumeParticularsActivity) getActivity()).getPosition()));
                        resumeInfoTv.setText(resumeValueBean.getResumeInfo());
                        resumeExperienceTv.setText(resumeValueBean.getResumeWorkExperience());
                        resumeQqTv.setText(resumeValueBean.getResumeQq());
                        resumeEmailTv.setText(resumeValueBean.getResumeEmail());
                        resumeMobileTv.setText(resumeValueBean.getResumeMobile());
                        authenticityTv.setText(resumeValueBean.getAuthenticity()+"");
                        integrityTv.setText(resumeValueBean.getIntegrity()+"");
                        transactionRecordTv.setText(resumeValueBean.getTransactionRecord()+"");
                        commentCountTv.setText(resumeValueBean.getCommentCount()+"位商家评论过");
                    }





                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailure.......", s);
            }
        });





    }

}
