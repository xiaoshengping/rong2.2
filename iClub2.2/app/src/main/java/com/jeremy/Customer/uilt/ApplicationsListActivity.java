package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.jeremy.Customer.http.MyAppliction;
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

    @ViewInject(R.id.add_application_layout)
    private LinearLayout addApplicationLayout;
    @ViewInject(R.id.add_application_tv)
    private TextView addApplicationTv;
    @ViewInject(R.id.anew_refresh_tv)
    private TextView anewRefrashTv;
    @ViewInject(R.id.tixing_text)
    private TextView tixingText;
    @ViewInject(R.id.yichan_text)
    private TextView yichanText;
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
        addApplicationTv.setOnClickListener(this);
        View addView= LayoutInflater.from(this).inflate(R.layout.add_resume_layout, null);
        addResumeTv= (TextView) addView.findViewById(R.id.add_resume_tv);
        ListView listView=recruitmentHistoryLv.getRefreshableView();
        listView.addFooterView(addView);
        addResumeTv.setOnClickListener(this);
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("我的招聘");
        addResumeTv.setText("继续添加招聘");
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
                Intent intent = new Intent(ApplicationsListActivity.this, MerchantParticularsActivity.class);
                intent.putExtra("recruitmentHistoryValueBean", recruitmentHistoryValueBean.get(position - 1));
                intent.putExtra("position", (position-1)+"");
                startActivity(intent);
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

    @Override
    protected void onRestart() {
        super.onRestart();

        recruitmentHistoryLv.setRefreshing();
    }

    private void initView() {
        /*SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
            bdName=cursor.getString(6);
        }*/

    }

    private void initRecruitmentHistoryData(int offset) {
        SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
            bdName=cursor.getString(6);
        }
        if (!TextUtils.isEmpty(uid)){
            HttpUtils httpUtils=new HttpUtils();
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRecruitmentHistoryList(uid, offset), new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String result=responseInfo.result;

                    if (result!=null){
                        HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<RecruitmentHistoryValueBean>>() {
                        }, recruitmentHistoryValueBean, recruitmentHistoryAdapter);
                        yichanText.setVisibility(View.GONE);
                        if (recruitmentHistoryValueBean.size()!=0){
                            addResumeTv.setVisibility(View.VISIBLE);
                            addResumeTv.setText("继续添加招聘");
                            recruitmentHistoryLv.setVisibility(View.VISIBLE);
                        }else {
                            recruitmentHistoryLv.setVisibility(View.GONE);
                            addApplicationLayout.setVisibility(View.VISIBLE);
                        }
                        recruitmentHistoryLv.onRefreshComplete();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    addResumeTv.setVisibility(View.GONE);
                    yichanText.setVisibility(View.VISIBLE);
                    showAnim();
                    recruitmentHistoryLv.onRefreshComplete();


                }
            });


        }





    }

    private void showAnim() {
        Animation appAnim = AnimationUtils.loadAnimation(this, R.anim.alpthe);
        yichanText.startAnimation(appAnim);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.add_resume_tv:
                if (!TextUtils.isEmpty(bMerchantValueBean.getBEcompanyName())){
                    Intent intent=new Intent(ApplicationsListActivity.this,AddMerchantActivity.class);
                    intent.putExtra("fagle","addMerchant");
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(ApplicationsListActivity.this,MerchantInformationActivity.class);
                    intent.putExtra("merchantFalg","addMerchant");
                    startActivity(intent);
                    MyAppliction.showToast("先完善商家信息再添加招聘");

                }
                break;
            case R.id.add_application_tv:
                if (!TextUtils.isEmpty(bMerchantValueBean.getBEcompanyName())){
                    Intent intent=new Intent(ApplicationsListActivity.this,AddMerchantActivity.class);
                    intent.putExtra("fagle","addMerchant");
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(ApplicationsListActivity.this,MerchantInformationActivity.class);
                    intent.putExtra("merchantFalg","addMerchant");
                    startActivity(intent);
                    MyAppliction.showToast("先完善商家信息再添加招聘");

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
