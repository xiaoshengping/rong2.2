package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ChooseAResumeAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.MyDialog;
import com.jeremy.Customer.bean.TalentValueBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.url.HttpHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ChooseAResumeActivity extends ActionBarActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {


    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.not_resume_tv)
    private TextView notResumeTv;
    @ViewInject(R.id.resume_list_lv)
    private PullToRefreshListView resumeListLv;
    private HttpUtils httpUtils;
    private List<TalentValueBean> resumeValueBeans;
    private ChooseAResumeAdapter ChooseAResumeAdapter;
    private int offset = 0;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_list);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();
        intiListView();


    }

    private void intiView() {
//        tailtReturnTv.setOnClickListener(this);
        SQLhelper sqLhelper = new SQLhelper(ChooseAResumeActivity.this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            pid = cursor.getString(1);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SQLhelper sqLhelper = new SQLhelper(ChooseAResumeActivity.this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            pid = cursor.getString(1);

        }

        resumeListLv.setRefreshing();
    }

    private void intiListView() {
        ListView listView = resumeListLv.getRefreshableView();

        resumeValueBeans = new ArrayList<TalentValueBean>();
        ChooseAResumeAdapter = new ChooseAResumeAdapter(resumeValueBeans, this, resumeListLv);
        resumeListLv.setAdapter(ChooseAResumeAdapter);
        resumeListLv.setMode(PullToRefreshBase.Mode.BOTH);
        resumeListLv.setOnRefreshListener(this);
        ILoadingLayout endLabels = resumeListLv
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels = resumeListLv
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        resumeListLv.setRefreshing();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (resumeValueBeans.size() != 0) {
//                    Intent intent = new Intent(ChooseAResumeActivity.this, ResumeParticularsActivity.class);
//                    intent.putExtra("resumeValueBeans", resumeValueBeans.get(position - 1));
//                    intent.putExtra("position", (position - 1) + "");
//                    startActivity(intent);

                    dialog2 = new MyDialog(ChooseAResumeActivity.this, Identification.MAINTAINORREMOVE, Identification.FREEDOM, "是否确认投递简历：" + resumeValueBeans.get(position - 1).getResumeJobName());
                    dialog2.setDetermine(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("ResumeId", resumeValueBeans.get(position - 1).getResumeid());
                            intent.putExtra("ResumeName", resumeValueBeans.get(position - 1).getResumeJobName());
                        /*给上一个Activity返回结果*/
                            ChooseAResumeActivity.this.setResult(Identification.JOBCHOICE, intent);
                        /*结束本Activity*/
                            ChooseAResumeActivity.this.finish();
                            dialog2.dismiss();
                        }
                    });
                    dialog2.setCancel(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });

                    dialog2.show();

                }


            }
        });
    }

    private void intiResumeListData(int offset) {
        httpUtils = new HttpUtils();
        String resumeListUrl = AppUtilsUrl.getResumeList(pid, offset);
        httpUtils.send(HttpRequest.HttpMethod.GET, resumeListUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<TalentValueBean>>() {
                    }, resumeValueBeans, ChooseAResumeAdapter);
                    resumeListLv.onRefreshComplete();
                    if (resumeValueBeans.size() != 0) {
                        notResumeTv.setVisibility(View.GONE);
                    } else {
                        notResumeTv.setVisibility(View.VISIBLE);

                    }


                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                ChooseAResumeAdapter = new ChooseAResumeAdapter(resumeValueBeans, ChooseAResumeActivity.this, resumeListLv);
                resumeListLv.setAdapter(ChooseAResumeAdapter);
                resumeListLv.onRefreshComplete();
                dialog();
            }
        });


    }

    private MyDialog dialog2;

    //提示框
    private void dialog() {
        dialog2 = new MyDialog(ChooseAResumeActivity.this, Identification.TOOLTIP, Identification.NETWORKANOMALY);
        dialog2.setDetermine(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recommend_list.setVisibility(View.GONE);
                Intent intent = new Intent();
                intent.putExtra("ResumeId", 0);
                intent.putExtra("ResumeName", "");
                        /*给上一个Activity返回结果*/
                ChooseAResumeActivity.this.setResult(Identification.JOBCHOICE, intent);
                        /*结束本Activity*/
                ChooseAResumeActivity.this.finish();
                dialog2.dismiss();
            }
        });

        dialog2.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tailt_return_tv:
                ChooseAResumeActivity.this.finish();
                break;


        }

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        resumeValueBeans.clear();
        int offset = 0;
        intiResumeListData(offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset = offset + 10;
        intiResumeListData(offset);
    }

    public void back(View v) {
        Intent intent = new Intent();
        intent.putExtra("ResumeId", 0);
        intent.putExtra("ResumeName", "");
                        /*给上一个Activity返回结果*/
        ChooseAResumeActivity.this.setResult(Identification.JOBCHOICE, intent);
                        /*结束本Activity*/
        ChooseAResumeActivity.this.finish();
    }
}
