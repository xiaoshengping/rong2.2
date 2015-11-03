package com.jeremy.Customer.uilt;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.CommentCountAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.CommentcountValueBean;
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

public class CommentCountActivity extends ActionBarActivity implements View.OnClickListener
        ,PullToRefreshBase.OnRefreshListener2<ListView>{

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;

    @ViewInject(R.id.comment_list_lv)
    private PullToRefreshListView commentListLv;

    private int offset=0;
    private List<CommentcountValueBean> commentcountValueBeans;
    private CommentCountAdapter commentCountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_count);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        commentcountValueBeans=new ArrayList<>();
        commentCountAdapter=new CommentCountAdapter(commentcountValueBeans,this);
        commentListLv.setAdapter(commentCountAdapter);
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("评论");
        commentListLv.setMode(PullToRefreshBase.Mode.BOTH);
        commentListLv.setOnRefreshListener(this);
        ILoadingLayout endLabels  = commentListLv
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = commentListLv
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        commentListLv.setRefreshing();

    }

    private void initData(int offset) {
        HttpUtils httpUtils=new HttpUtils();
        SQLhelper sqLhelper=new SQLhelper(CommentCountActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String uid=null;
        String persoinId=null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
            persoinId=cursor.getString(1);
        }
        String url=null;
        if (getIntent().getStringExtra("falge").equals("resume")){
            url= AppUtilsUrl.getCommentByPersonUid(uid,offset);
        }else if(getIntent().getStringExtra("falge").equals("merchar")){
            url= AppUtilsUrl.getResumeCommentData(persoinId,offset);
        }


        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String >() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("jjdjfjfj",responseInfo.result);
                if (responseInfo.result!=null){
                    HttpHelper.baseToUrl(responseInfo.result, new TypeReference<ArtistParme<CommentcountValueBean>>() {
                    }, commentcountValueBeans, commentCountAdapter);
                   // Log.e("kdfjjjgjg", commentcountValueBeans.get(0).getBody());
                    commentListLv.onRefreshComplete();

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                commentListLv.onRefreshComplete();
            }
        });




    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        commentcountValueBeans.clear();
        int offset=0;
        initData(offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        commentcountValueBeans.clear();
        offset=offset+10;
        initData(offset);
    }
}
