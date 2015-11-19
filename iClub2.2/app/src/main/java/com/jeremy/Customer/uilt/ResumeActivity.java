package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumeListAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.http.MyAppliction;
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

public class ResumeActivity extends ActionBarActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView>{

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.add_resume_layout)
    private LinearLayout notResumeLayout;
    private TextView addResumeTv;
    @ViewInject(R.id.add_resumes_tv)
    private TextView addResumeOneTv;
    @ViewInject(R.id.resume_list_lv)
    private PullToRefreshListView resumeListLv;
    private HttpUtils httpUtils;
    private List<ResumeValueBean> resumeValueBeans;
    private ResumeListAdapter resumeListAdapter;
    private int offset=0;
    private String pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();
        intiListView();


    }

    private void intiView() {
        tailtText.setText("我的简历");
        tailtReturnTv.setOnClickListener(this);
        /*SQLhelper sqLhelper=new SQLhelper(ResumeActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            pid = cursor.getString(1);

        }*/
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        resumeListLv.setRefreshing();
    }

    private void intiListView() {
        View addView= LayoutInflater.from(this).inflate(R.layout.add_resume_layout, null);
        addResumeTv= (TextView) addView.findViewById(R.id.add_resume_tv);
        ListView listView=resumeListLv.getRefreshableView();
        listView.addFooterView(addView);
        addResumeTv.setOnClickListener(this);
        addResumeTv.setText("继续添加简历");
        resumeValueBeans=new ArrayList<ResumeValueBean>();
        resumeListAdapter=new ResumeListAdapter(resumeValueBeans,this,resumeListLv);
        resumeListLv.setAdapter(resumeListAdapter);
        resumeListLv.setMode(PullToRefreshBase.Mode.BOTH);
        resumeListLv.setOnRefreshListener(this);
        ILoadingLayout endLabels  = resumeListLv
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = resumeListLv
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        resumeListLv.setRefreshing();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (resumeValueBeans.size() != 0) {
                    Intent intent = new Intent(ResumeActivity.this, ResumeParticularsActivity.class);
                    intent.putExtra("resumeid", resumeValueBeans.get(position - 1).getResumeid() + "");
                    startActivity(intent);
                }


            }
        });
    }

    private void intiResumeListData(int offset) {
        httpUtils=new HttpUtils();
        SQLhelper sqLhelper=new SQLhelper(ResumeActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            pid = cursor.getString(1);

        }
        String resumeListUrl= AppUtilsUrl.getResumeList(pid, offset);
        httpUtils.send(HttpRequest.HttpMethod.GET, resumeListUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result=responseInfo.result;
                if (result!=null){
                    MyAppliction.showToast("list刷新成功");
                    Log.e("list", result);
                    HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<ResumeValueBean>>() {
                    }, resumeValueBeans, resumeListAdapter);

                   if (resumeValueBeans.size()!=0){
                       notResumeLayout.setVisibility(View.GONE);
                        addResumeTv.setText("继续添加简历");
                       resumeListLv.setVisibility(View.VISIBLE);
                    }else {
                       addResumeOneTv.setOnClickListener(ResumeActivity.this);
                       resumeListLv.setVisibility(View.GONE);
                       notResumeLayout.setVisibility(View.VISIBLE);

                    }
                    resumeListLv.onRefreshComplete();



                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                resumeListLv.onRefreshComplete(); 
                Log.e("onFailure.......", s);
            }
        });





    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.add_resume_tv:
                Intent intent =new Intent(ResumeActivity.this,AddResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.add_resumes_tv:
                Intent intent1 =new Intent(ResumeActivity.this,AddResumeActivity.class);
                startActivity(intent1);
                break;


        }

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        resumeValueBeans.clear();
        int offset=0;
        intiResumeListData(offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset=offset+10;
        intiResumeListData(offset);
    }
}
