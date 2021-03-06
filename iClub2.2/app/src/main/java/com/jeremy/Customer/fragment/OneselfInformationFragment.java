package com.jeremy.Customer.fragment;


import android.content.Intent;
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
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.uilt.CommentCountActivity;
import com.jeremy.Customer.uilt.ResumeParticularsActivity;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

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

    private ResumeValueBean resumeValueBean;


    public OneselfInformationFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        intiResumeData();
    }
    private void intiResumeData() {

        HttpUtils httpUtils=new HttpUtils().configDefaultHttpCacheExpiry(0);
        String resumeListUrl= AppUtilsUrl.getResumeLista(((ResumeParticularsActivity) getActivity()).getResumeid());
        httpUtils.send(HttpRequest.HttpMethod.GET, resumeListUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ParmeBean<ResumeValueBean> artistParme = JSONObject.parseObject(result, new TypeReference<ParmeBean<ResumeValueBean>>() {
                    });
                     resumeValueBean = artistParme.getValue();
                    if (resumeValueBean != null) {
                        resumeInfoTv.setText(resumeValueBean.getResumeInfo());
                        resumeExperienceTv.setText(resumeValueBean.getResumeWorkExperience());
                        resumeQqTv.setText(resumeValueBean.getResumeQq());
                        resumeEmailTv.setText(resumeValueBean.getResumeEmail());
                        resumeMobileTv.setText(resumeValueBean.getResumeMobile());
                        authenticityTv.setText(resumeValueBean.getAuthenticity() + "");
                        integrityTv.setText(resumeValueBean.getIntegrity() + "");
                        transactionRecordTv.setText(resumeValueBean.getTransactionRecord() + "");
                        if (resumeValueBean.getCommentCount() == 0) {
                            commentCountTv.setText("暂没有商家评论过");
                        } else {
                            commentCountTv.setText(resumeValueBean.getCommentCount() + "位商家评论过");
                        }
                        resumeInfoTv.setVisibility(View.VISIBLE);
                        resumeInfoTv.post(new Runnable() {
                            @Override
                            public void run() {
                                if (resumeInfoTv.getLineCount() > 4) {
                                    resumeInfoTv.setLines(4);
                                }

                            }
                        });
                        resumeExperienceTv.setVisibility(View.VISIBLE);
                        resumeExperienceTv.post(new Runnable() {
                            @Override
                            public void run() {
                                if (resumeExperienceTv.getLineCount() > 4) {
                                    resumeExperienceTv.setLines(4);
                                }

                            }
                        });


                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

                resumeInfoTv.setText("网络异常，无法显示...");
                resumeExperienceTv.setText("网络异常，无法显示...");
                resumeQqTv.setText("网络异常，无法显示...");
                resumeEmailTv.setText("网络异常，无法显示...");
                resumeMobileTv.setText("网络异常，无法显示...");
                oneselfMoreTv.setVisibility(View.GONE);
                experienceMoreTv.setVisibility(View.GONE);
                Log.e("onFailure.......", s);
            }
        });





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

        initView();


    }



    private void initView() {
        experienceMoreLayout.setOnClickListener(this);
        oneselfMoreLayout.setOnClickListener(this);
        resumeInfoTv.setOnClickListener(this);
        commentCountTv.setOnClickListener(this);
        intiResumeData();



       }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.oneself_more_layout:
                resumeInfoTv.post(new Runnable() {
                    @Override
                    public void run() {
                        resumeInfoTv.setLines(resumeInfoTv.getLineCount());


                    }
                });
                oneselfMoreLayout.setVisibility(View.GONE);
                break;
            case R.id.experience_more_layout:

                resumeExperienceTv.post(new Runnable() {
                    @Override
                    public void run() {
                        resumeExperienceTv.setLines(resumeExperienceTv.getLineCount());
                    }
                });
                experienceMoreLayout.setVisibility(View.GONE);
                break;
            case R.id.commentCount_tv:
                if (resumeValueBean!=null) {
                    if (resumeValueBean.getCommentCount() != 0) {
                        Intent intent = new Intent(getActivity(), CommentCountActivity.class);
                        intent.putExtra("falge", "resume");
                        startActivity(intent);
                    }
                }
                break;


        }
    }



}
