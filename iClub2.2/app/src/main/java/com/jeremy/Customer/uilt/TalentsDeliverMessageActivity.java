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
import com.jeremy.Customer.adapter.ResumeMessageListAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.ResumeMessageValueBean;
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

public class TalentsDeliverMessageActivity extends ActionBarActivity implements View.OnClickListener,  PullToRefreshBase.OnRefreshListener2<ListView> {
    @ViewInject(R.id.message_listView)
    //private ListView MessageListView;
    private PullToRefreshListView MessageListView;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;

    private List<ResumeMessageValueBean> informationValueBeans;
    private ResumeMessageListAdapter resumeMessageListAdapter;
    private int offset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talents_deliver_message);
        ViewUtils.inject(this);
        inti();


    }

    private void inti() {
        tailtText.setText("投递消息");
        tailtReturnTv.setOnClickListener(this);
        intiListView();


    }

    private void intiListView() {
        informationValueBeans=new ArrayList<>();
        resumeMessageListAdapter = new ResumeMessageListAdapter(informationValueBeans, TalentsDeliverMessageActivity.this);
        MessageListView.setAdapter(resumeMessageListAdapter);
        MessageListView.setMode(PullToRefreshBase.Mode.BOTH);
        MessageListView.setOnRefreshListener(this);
        ILoadingLayout endLabels  = MessageListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = MessageListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        MessageListView.setRefreshing();

        MessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(TalentsDeliverMessageActivity.this, MerchantJobParticularActivity.class);
                intent.putExtra("informationValueBeans", informationValueBeans.get(position - 1).getJobid());
                startActivity(intent);


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


    private void intiData(int offset) {
        SQLhelper sqLhelper = new SQLhelper(TalentsDeliverMessageActivity.this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        String uid = null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
        }
        HttpUtils httpUtils = new HttpUtils();
        String informationUrl = AppUtilsUrl.getMessageList(uid, offset);
        httpUtils.send(HttpRequest.HttpMethod.POST, informationUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<ResumeMessageValueBean>>() {
                }, informationValueBeans, resumeMessageListAdapter);
                MessageListView.onRefreshComplete();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                MessageListView.onRefreshComplete();
                //Log.e("hdhfhhf", s);
            }
        });
    }
}
