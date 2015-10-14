package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
    @ViewInject(R.id.not_resume_tv)
    private TextView notResumeTv;
    private TextView addResumeTv;
    private PullToRefreshListView resumeListLv;
    private HttpUtils httpUtils;
    private List<ResumeValueBean> resumeValueBeans;
    private ResumeListAdapter resumeListAdapter;
    private int offset=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiListView();

        intiView();
    }

    private void intiView() {
        tailtText.setText("我的简历");




    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (resumeValueBeans.size()==0){
            notResumeTv.setVisibility(View.VISIBLE);
            addResumeTv.setText("马上添加");
        }else {
            notResumeTv.setVisibility(View.GONE);
            addResumeTv.setText("继续添加");
        }*/
        resumeListLv.setRefreshing();
    }

    private void intiListView() {
        View addView= LayoutInflater.from(this).inflate(R.layout.add_resume_layout,null);
        addResumeTv= (TextView) addView.findViewById(R.id.add_resume_tv);
        //retrunTv= (TextView) view.findViewById(R.id.role_retrun_tv);
        resumeListLv= (PullToRefreshListView) findViewById(R.id.resume_list_lv);
        ListView listView=resumeListLv.getRefreshableView();
        listView.addFooterView(addView);
        addResumeTv.setOnClickListener(this);
        tailtReturnTv.setOnClickListener(this);
        resumeValueBeans=new ArrayList<ResumeValueBean>();
        resumeListAdapter=new ResumeListAdapter(resumeValueBeans,ResumeActivity.this,resumeListLv);
        listView.setAdapter(resumeListAdapter);
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
                if (resumeValueBeans.size()!=0){
                    Intent intent = new Intent(ResumeActivity.this, ResumeParticularsActivity.class);
                    //intent.putExtra("resumeValueBeans", resumeValueBeans.get(position-1));
                    //intent.putExtra("flage", "ResumeFragment");
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
        String uid=null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        String resumeListUrl= AppUtilsUrl.getResumeList(uid, offset);
        httpUtils.send(HttpRequest.HttpMethod.POST, resumeListUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {


                String result=responseInfo.result;
                if (result!=null){
                    HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<ResumeValueBean>>() {
                    }, resumeValueBeans, resumeListAdapter);
                    resumeListLv.onRefreshComplete();
                    if (resumeValueBeans.size()!=0){
                        notResumeTv.setVisibility(View.GONE);
                        addResumeTv.setText("继续添加");
                    }else {
                        notResumeTv.setVisibility(View.VISIBLE);
                        addResumeTv.setText("马上添加");

                    }


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
            case  R.id.add_resume_tv:
                Intent intent =new Intent(ResumeActivity.this,AddResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.tailt_return_tv:
                finish();
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
        resumeValueBeans.clear();
        offset=offset+10;
        intiResumeListData(offset);
    }
}
