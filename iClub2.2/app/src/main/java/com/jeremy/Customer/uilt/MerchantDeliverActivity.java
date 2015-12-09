package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import com.jeremy.Customer.adapter.MerchantMessageListAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.MerchantMessageValueBean;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
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

public class MerchantDeliverActivity extends ActionBarActivity implements View.OnClickListener,  PullToRefreshBase.OnRefreshListener2<ListView>{

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;


    @ViewInject(R.id.merchant_message_lv)
    //private ListView informationListv;
    private PullToRefreshListView informationListv;
    @ViewInject(R.id.add_application_layout)
    private LinearLayout mercharntDeliverLayout;
    private List<MerchantMessageValueBean> informationValueBeans;
    private MerchantMessageListAdapter merchantMessageListAdapter;
    private int offset=0;
    @ViewInject(R.id.yichan_text)
    private TextView yichanText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_deliver);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
               initView();


    }

    private void initView() {
        tailtText.setText("投递消息");
        tailtReturnTv.setOnClickListener(this);
        informationValueBeans=new ArrayList<>();
        merchantMessageListAdapter = new MerchantMessageListAdapter(informationValueBeans, this);
        informationListv.setAdapter(merchantMessageListAdapter);
        informationListv.setMode(PullToRefreshBase.Mode.BOTH);
        informationListv.setOnRefreshListener(this);
        ILoadingLayout endLabels  = informationListv
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = informationListv
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        informationListv.setRefreshing();

        informationListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intiResumeData(informationValueBeans.get(position-1).getApplyerResumeid());


            }
        });


    }
    private void intiData(int offset) {
        SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);


        }
        HttpUtils httpUtils=new HttpUtils();
        String informationUrl= AppUtilsUrl.getMessageMerchantList(uid, offset);
        httpUtils.send(HttpRequest.HttpMethod.POST, informationUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<MerchantMessageValueBean>>() {
                }, informationValueBeans, merchantMessageListAdapter);
                //MyAppliction.showToast(informationValueBeans.size()+"");
                yichanText.setVisibility(View.GONE);
                if (informationValueBeans.size() == 0) {
                    mercharntDeliverLayout.setVisibility(View.VISIBLE);
                    informationListv.setVisibility(View.GONE);
                } else {
                    mercharntDeliverLayout.setVisibility(View.GONE);
                    informationListv.setVisibility(View.VISIBLE);
                }
                informationListv.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                MyAppliction.showToast("网络异常，请稍后重试...");
                showAnim();
                yichanText.setVisibility(View.VISIBLE);
                informationListv.onRefreshComplete();
                Log.e("onFailure", s);
            }
        });




    }
    private void showAnim() {
        Animation appAnim = AnimationUtils.loadAnimation(this, R.anim.alpthe);
        yichanText.startAnimation(appAnim);

    }
    private void intiResumeData(String resumeid) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid", resumeid);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPreviewResume(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result=responseInfo.result;
                if (result!=null){
                    ParmeBean<ResumeValueBean> artistParme= JSONObject.parseObject(result, new TypeReference<ParmeBean<ResumeValueBean>>() {
                    });
                    ResumeValueBean resumeValueBeans=    artistParme.getValue();
                    Intent intent=new Intent(MerchantDeliverActivity.this,MercharInviteParticularsActivity.class);
                    intent.putExtra("resumeValueBeans", resumeValueBeans);
                    startActivity(intent);
                }



            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });




    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        informationValueBeans.clear();
        offset=0;
        intiData(offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset=offset+10;
        intiData(offset);
    }

}
