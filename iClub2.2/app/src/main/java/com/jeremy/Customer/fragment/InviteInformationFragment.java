package com.jeremy.Customer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.CommentBean;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.InviteGradeValue;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.uilt.MercharInviteParticularsActivity;
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
public class InviteInformationFragment extends Fragment implements View.OnClickListener {

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
    private ResumeValueBean resumeValueBean;

    public InviteInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_invite_information, container, false);
        ViewUtils.inject(this, view);

        init();
        return view;
    }

    private void init() {
        //intiResumeListData();
        initView();


    }

    private void initView() {
        experienceMoreLayout.setOnClickListener(this);
        oneselfMoreLayout.setOnClickListener(this);
        resumeInfoTv.setOnClickListener(this);
        //commentCountTv.setOnClickListener(this);
       resumeValueBean = ((MercharInviteParticularsActivity) getActivity()).getResumeValueBean();
           if (resumeValueBean!=null){
        resumeInfoTv.setText(resumeValueBean.getResumeInfo());
        resumeExperienceTv.setText(resumeValueBean.getResumeWorkExperience());
        resumeQqTv.setText(resumeValueBean.getResumeQq());
        resumeEmailTv.setText(resumeValueBean.getResumeEmail());
        resumeMobileTv.setText(resumeValueBean.getResumeMobile());



           }
        resumeInfoTv.post(new Runnable() {
            @Override
            public void run() {
                if (resumeInfoTv.getLineCount() > 4) {
                    resumeInfoTv.setLines(4);
                }

            }
        });
        resumeExperienceTv.post(new Runnable() {
            @Override
            public void run() {
                if (resumeExperienceTv.getLineCount() > 4) {
                    resumeExperienceTv.setLines(4);
                }

            }
        });
        initInviteData();
        //inviCommenData();




    }

    private void inviCommenData() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getInviteComment(resumeValueBean.getResumeid()), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
               if (result != null) {
                    ArtistParme<CommentBean> commentBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<CommentBean>>() {
                    });
                    if (commentBean.getState().equals("success")) {
                        if (commentBean.getTotal() > 0) {
                            commentCountTv.setText(commentBean.getTotal() + "位商家评论过");
                        } else {
                            commentCountTv.setText("还没有商家进行评论哦~");
                            //commentCountTv.setTextColor(0xff777778);
                        }
                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
//                reputation_tipe_tv.setTextColor(0xffDEDDE2);
//                reputation_tipe_tv.setText("网路异常，请稍后再试！");
            }
        });




    }

    private void initInviteData() {
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getInviteGrade(resumeValueBean.getPersonid() + ""), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    ParmeBean<InviteGradeValue> parmeBean = JSONObject.parseObject(responseInfo.result, new TypeReference<ParmeBean<InviteGradeValue>>() {
                    });
                    InviteGradeValue inviteGradeValue = parmeBean.getValue();
                    transactionRecordTv.setText(inviteGradeValue.getTransactionRecord() + "");
                    integrityTv.setText(inviteGradeValue.getIntegrity() + "");
                    authenticityTv.setText(inviteGradeValue.getAuthenticity() + "");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.oneself_more_layout:
                resumeInfoTv.post(new Runnable() {
                    @Override
                    public void run() {
                        resumeInfoTv.setLines(resumeInfoTv.getLineCount());
                        oneselfMoreLayout.setVisibility(View.GONE);

                    }
                });
                break;
            case R.id.experience_more_layout:
                resumeExperienceTv.post(new Runnable() {
                    @Override
                    public void run() {
                        resumeExperienceTv.setLines(resumeExperienceTv.getLineCount());
                        experienceMoreLayout.setVisibility(View.GONE);

                    }
                });

                break;




        }
    }


}
