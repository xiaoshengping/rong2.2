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
import com.jeremy.Customer.uilt.ContactInformationActivity;
import com.jeremy.Customer.uilt.ModificationResumeActivity;
import com.jeremy.Customer.uilt.OneselfExperienceActivity;
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
        intiResumeListData();
        initView();


    }

    @Override
    public void onResume() {
        super.onResume();
        intiResumeListData();
    }

    private void initView() {
        experienceMoreLayout.setOnClickListener(this);
        oneselfMoreLayout.setOnClickListener(this);
        resumeInfoTv.setOnClickListener(this);
        modificationContact.setOnClickListener(this);
        modificationWorkTv.setOnClickListener(this);
        modificationOneselfTv.setOnClickListener(this);

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
            case R.id.modification_oneself_tv:
                Intent infoIntent = new Intent(getActivity(), OneselfExperienceActivity.class);  //方法1
                infoIntent.putExtra("hintData","infoIntent");
                if (!TextUtils.isEmpty(resumeInfoTv.getText().toString())){
                    infoIntent.putExtra("content",resumeInfoTv.getText().toString());
                }
                startActivityForResult(infoIntent, INFOLT_HINT_DATA);

                break;
            case R.id.modification_work_tv:
                Intent workIntent = new Intent(getActivity(), OneselfExperienceActivity.class);  //方法1
                workIntent.putExtra("hintData","workIntent");
                if (!TextUtils.isEmpty(resumeExperienceTv.getText().toString())){
                    workIntent.putExtra("content",resumeExperienceTv.getText().toString());
                }
                startActivityForResult(workIntent, EXPERIENCE_HINT_DATA);

                break;
            case R.id.modification_contact:
                Intent intent=new Intent(getActivity(), ContactInformationActivity.class);
                intent.putExtra("resumeValueBean",resumeValueBean);
                startActivity(intent);


                break;



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INFOLT_HINT_DATA:
                if (data.getStringExtra("infoIntent").toString().equals("notData")){
                    if (resumeInfoTv.getText().toString().equals("介绍一下自己")){
                        resumeInfoTv.setText("介绍一下自己");
                        resumeInfoTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        resumeInfoTv.setText(resumeInfoTv.getText().toString());
                        resumeInfoTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else if (data.getStringExtra("infoIntent").toString().equals("data")){
                    if (resumeInfoTv.getText().toString().equals("介绍一下自己")){
                        resumeInfoTv.setText("介绍一下自己");
                        resumeInfoTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        resumeInfoTv.setText(resumeInfoTv.getText().toString());
                        resumeInfoTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else {
                    resumeInfoTv.setText(data.getStringExtra("infoIntent").toString());
                    resumeInfoTv.setTextColor(getResources().getColor(R.color.textColor242424));
                }

                break;
            case EXPERIENCE_HINT_DATA:
                if (data.getStringExtra("workIntent").toString().equals("notData")){
                    if (resumeExperienceTv.getText().toString().equals("分享一下自己工作经验")){
                        resumeExperienceTv.setText("分享一下自己工作经验");
                        resumeExperienceTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        resumeExperienceTv.setText(resumeExperienceTv.getText().toString());
                        resumeExperienceTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }
                }else if (data.getStringExtra("workIntent").toString().equals("data")){
                    if (resumeExperienceTv.getText().toString().equals("分享一下自己工作经验")){
                        resumeExperienceTv.setText("分享一下自己工作经验");
                        resumeExperienceTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        resumeExperienceTv.setText(resumeExperienceTv.getText().toString());
                        resumeExperienceTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else {
                    resumeExperienceTv.setText(data.getStringExtra("workIntent").toString());
                    resumeExperienceTv.setTextColor(getResources().getColor(R.color.textColor242424));
                }

                break;
        }
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
