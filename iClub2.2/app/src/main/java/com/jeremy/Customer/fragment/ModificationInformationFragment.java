package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.jeremy.Customer.uilt.CommentCountActivity;
import com.jeremy.Customer.uilt.ContactInformationActivity;
import com.jeremy.Customer.uilt.ModificationResumeActivity;
import com.jeremy.Customer.uilt.OneselfExperienceActivity;
import com.jeremy.Customer.uilt.SQLhelper;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModificationInformationFragment extends Fragment implements View.OnClickListener {

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
    @ViewInject(R.id.modification_contact)
    private TextView modificationContact;
    @ViewInject(R.id.modification_work_tv)
    private TextView modificationWorkTv;
    @ViewInject(R.id.modification_oneself_tv)
    private TextView modificationOneselfTv;




    private List<ResumeValueBean> resumeValueBeans;
    private static final int INFOLT_HINT_DATA=7;//自我介绍
    private static final int EXPERIENCE_HINT_DATA=8;//工作经验
    private static final int CONCATIONINFO=9;//联系方式
    private ResumeValueBean resumeValueBean;

    public ModificationInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_modification_information, container, false);
        ViewUtils.inject(this, view);
         init();
        return view;
    }

    private void init() {
        initView();
        intiResumeListData();

    }



    private void initView() {
        experienceMoreLayout.setOnClickListener(this);
        oneselfMoreLayout.setOnClickListener(this);
        resumeInfoTv.setOnClickListener(this);
        modificationContact.setOnClickListener(this);
        modificationWorkTv.setOnClickListener(this);
        modificationOneselfTv.setOnClickListener(this);
        commentCountTv.setOnClickListener(this);
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
            case R.id.modification_oneself_tv:
                Intent infoIntent = new Intent(getActivity(), OneselfExperienceActivity.class);  //方法1
                infoIntent.putExtra("hintData","modificationInfoIntent");
                if (!TextUtils.isEmpty(resumeInfoTv.getText().toString())){
                    infoIntent.putExtra("content",resumeInfoTv.getText().toString());
                }
                startActivityForResult(infoIntent, INFOLT_HINT_DATA);

                break;
            case R.id.modification_work_tv:
                Intent workIntent = new Intent(getActivity(), OneselfExperienceActivity.class);  //方法1
                workIntent.putExtra("hintData","modificationWorkIntent");
                if (!TextUtils.isEmpty(resumeExperienceTv.getText().toString())){
                    workIntent.putExtra("content",resumeExperienceTv.getText().toString());
                }
                startActivityForResult(workIntent, EXPERIENCE_HINT_DATA);

                break;
            case R.id.modification_contact:
                Intent intent=new Intent(getActivity(), ContactInformationActivity.class);

                    intent.putExtra("QQ",resumeQqTv.getText().toString());


                    intent.putExtra("Email",resumeEmailTv.getText().toString());


                    intent.putExtra("mobile",resumeMobileTv.getText().toString());
                  intent.putExtra("resumeid",resumeValueBean.getResumeid()+"");
                startActivityForResult(intent, CONCATIONINFO);


                break;
            case R.id.commentCount_tv:
                Intent commentIntent =new Intent(getActivity(), CommentCountActivity.class);
                commentIntent.putExtra("falge","resume");
                startActivity(commentIntent);

                break;



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INFOLT_HINT_DATA:
                if (data.getStringExtra("modificationInfoIntent").toString().equals("notData")){
                    if (resumeInfoTv.getText().toString().equals("介绍一下自己")){
                        resumeInfoTv.setText("介绍一下自己");
                        resumeInfoTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        resumeInfoTv.setText(resumeInfoTv.getText().toString());
                        resumeInfoTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else if (data.getStringExtra("modificationInfoIntent").toString().equals("data")){
                    if (resumeInfoTv.getText().toString().equals("介绍一下自己")){
                        resumeInfoTv.setText("介绍一下自己");
                        resumeInfoTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        resumeInfoTv.setText(resumeInfoTv.getText().toString());
                        resumeInfoTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }
                }else {
                    resumeInfoTv.setText(data.getStringExtra("modificationInfoIntent").toString());
                    resumeInfoTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    initInfoData(resumeValueBean.getResumeid() + "", "modificationInfoIntent", data.getStringExtra("modificationInfoIntent").toString());
                }

                break;
            case EXPERIENCE_HINT_DATA:
                if (data.getStringExtra("modificationWorkIntent").toString().equals("notData")){
                    if (resumeExperienceTv.getText().toString().equals("分享一下自己工作经验")){
                        resumeExperienceTv.setText("分享一下自己工作经验");
                        resumeExperienceTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        resumeExperienceTv.setText(resumeExperienceTv.getText().toString());
                        resumeExperienceTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }
                }else if (data.getStringExtra("modificationWorkIntent").toString().equals("data")){
                    if (resumeExperienceTv.getText().toString().equals("分享一下自己工作经验")){
                        resumeExperienceTv.setText("分享一下自己工作经验");
                        resumeExperienceTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        resumeExperienceTv.setText(resumeExperienceTv.getText().toString());
                        resumeExperienceTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else {
                    resumeExperienceTv.setText(data.getStringExtra("modificationWorkIntent").toString());
                    resumeExperienceTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    initInfoData(resumeValueBean.getResumeid() + "", "modificationWorkIntent", data.getStringExtra("modificationWorkIntent").toString());
                }

                break;
            case CONCATIONINFO:
                resumeQqTv.setText(data.getStringExtra("QQ").toString());
                resumeEmailTv.setText(data.getStringExtra("Email").toString());
                resumeMobileTv.setText(data.getStringExtra("mobile").toString());

                break;
        }
    }

    private void initInfoData(String resumeId,String text,String info) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid", resumeId);
        if (text.equals("modificationInfoIntent")){
            requestParams.addBodyParameter("resumeInfo",info);
        }else if (text.equals("modificationWorkIntent")){
            requestParams.addBodyParameter("resumeWorkExperience",info);
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCompileResume(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });



    }

    private void intiResumeListData() {
        HttpUtils httpUtils=new HttpUtils();
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
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<ResumeValueBean> artistParme = JSONObject.parseObject(result, new TypeReference<ArtistParme<ResumeValueBean>>() {
                    });
                    if (artistParme.getState().equals("success")) {
                        resumeValueBeans = artistParme.getValue();
                        resumeValueBean = resumeValueBeans.get(Integer.valueOf(((ModificationResumeActivity) getActivity()).getPosition()));
                        resumeInfoTv.setText(resumeValueBean.getResumeInfo());
                        resumeExperienceTv.setText(resumeValueBean.getResumeWorkExperience());
                        resumeQqTv.setText(resumeValueBean.getResumeQq());
                        resumeEmailTv.setText(resumeValueBean.getResumeEmail());
                        resumeMobileTv.setText(resumeValueBean.getResumeMobile());
                        authenticityTv.setText(resumeValueBean.getAuthenticity() + "");
                        integrityTv.setText(resumeValueBean.getIntegrity() + "");
                        transactionRecordTv.setText(resumeValueBean.getTransactionRecord() + "");
                        commentCountTv.setText(resumeValueBean.getCommentCount() + "位商家评论过");
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
