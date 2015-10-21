package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.RecruitmentHistoryAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.BMerchantValueBean;
import com.jeremy.Customer.bean.mine.RecruitmentHistoryValueBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.url.HttpHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsListActivity extends ActionBarActivity  implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;


    @ViewInject(R.id.recruiment_history_list_lv)
    private PullToRefreshListView recruitmentHistoryLv;
    private List<RecruitmentHistoryValueBean> recruitmentHistoryValueBean;
    private RecruitmentHistoryAdapter recruitmentHistoryAdapter;
    private int offset=0;

    private BMerchantValueBean bMerchantValueBean;
    private RequestParams requestParams;
    private String uid;
    private String bdName;
    private TextView addResumeTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications_list);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();
        intiListView();


    }
    private void intiListView() {
        View addView= LayoutInflater.from(this).inflate(R.layout.add_resume_layout,null);
        addResumeTv= (TextView) addView.findViewById(R.id.add_resume_tv);
        ListView listView=recruitmentHistoryLv.getRefreshableView();
        listView.addFooterView(addView);
        addResumeTv.setOnClickListener(this);
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("我的招聘");
        requestParams=new RequestParams();
        recruitmentHistoryValueBean=new ArrayList<RecruitmentHistoryValueBean>();
        recruitmentHistoryAdapter=new RecruitmentHistoryAdapter(recruitmentHistoryValueBean,this,recruitmentHistoryLv);
        recruitmentHistoryLv.setAdapter(recruitmentHistoryAdapter);
        recruitmentHistoryLv.setMode(PullToRefreshBase.Mode.BOTH);
        recruitmentHistoryLv.setOnRefreshListener(this);
        ILoadingLayout endLabels  = recruitmentHistoryLv
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = recruitmentHistoryLv
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        recruitmentHistoryLv.setRefreshing();
        recruitmentHistoryLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Intent intent = new Intent(getActivity(), AddRecruitmentActivity.class);
                intent.putExtra("recruitmentHistoryValueBean", recruitmentHistoryValueBean.get(position - 1));
                intent.putExtra("falgeData", "RecruitmentHistoryFragment");
                startActivity(intent);*/
            }
        });

        intitData();


    }

    @Override
    public void onResume() {
        super.onResume();
        intitData();
        recruitmentHistoryLv.setRefreshing();
        SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
            bdName=cursor.getString(6);
        }
    }


    private void initView() {
        SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
            bdName=cursor.getString(6);
        }

    }

    private void initRecruitmentHistoryData(int offset) {

        if (!TextUtils.isEmpty(uid)){
            HttpUtils httpUtils=new HttpUtils();
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRecruitmentHistoryList(uid, offset), new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String result=responseInfo.result;

                    if (result!=null){
                        HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<RecruitmentHistoryValueBean>>() {
                        }, recruitmentHistoryValueBean, recruitmentHistoryAdapter);
                        recruitmentHistoryLv.onRefreshComplete();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });


        }





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.add_resume_tv:
                if (!TextUtils.isEmpty(bdName)){
                    Intent intent=new Intent(ApplicationsListActivity.this,AddMerchantActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(ApplicationsListActivity.this,MerchantInformationActivity.class);
                    startActivity(intent);

                }
                break;

        }
    }

    private void intitData() {
        SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            HttpUtils httpUtils=new HttpUtils();
            requestParams.addBodyParameter("uid", uid);
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAcquireMerchant(), requestParams, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            String result = responseInfo.result;
                            if (!TextUtils.isEmpty(result)) {
                                ParmeBean<BMerchantValueBean> parmeBean = JSONObject.parseObject(result, new TypeReference<ParmeBean<BMerchantValueBean>>() {
                                });
                                bMerchantValueBean = parmeBean.getValue();



                            }


                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    }

            );


        }
        cursor.close();
        db.close();


    }







    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        recruitmentHistoryValueBean.clear();
        int offset=0;
        initRecruitmentHistoryData(offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset=offset+10;
        initRecruitmentHistoryData(offset);
    }
}
