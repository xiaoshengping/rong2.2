package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.InviteMessageListAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.InviteMessgaeListValueBean;
import com.jeremy.Customer.uilt.CompanyInviteMessageActivity;
import com.jeremy.Customer.uilt.SQLhelper;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class TalentSucceedFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView>{

    @ViewInject(R.id.invite_successful_list_lv)
    private PullToRefreshListView inviteSuccessfulListLv;
    private HttpUtils httpUtils;
    private RequestParams requestParams;
    private InviteMessageListAdapter inviteMessagelistAdapter;
    private int offset=0;
    private List<InviteMessgaeListValueBean> inviteMessgaeListValueBeans;



    public TalentSucceedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_talent_succeed, container, false);
        ViewUtils.inject(this, view);
        inti();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        inviteSuccessfulListLv.setRefreshing();
    }

    private void inti() {
        intiView();
        //intiData();
        intiListView();


    }
    private void intiView() {
        httpUtils=new HttpUtils();
        requestParams=new RequestParams();


    }

    private void intiData(int offset) {
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        /*if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("uid",uid);
        }
        requestParams.addBodyParameter("value", "complete");*/
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getInviteMessage(uid, "complete", offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                // Log.e("inviteintiData",result);
                if (!TextUtils.isEmpty(result)) {
                    HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<InviteMessgaeListValueBean>>() {
                    }, inviteMessgaeListValueBeans, inviteMessagelistAdapter);
                    inviteSuccessfulListLv.onRefreshComplete();
                    /*if (inviteMessgaeListValueBeans.size()==0){
                        inviteSuccessfulListLv.setVisibility(View.GONE);
                        messageTv.setVisibility(View.VISIBLE);
                    }*/
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                inviteSuccessfulListLv.onRefreshComplete();
            }
        });



    }

    private void intiListView( ) {
        inviteMessgaeListValueBeans=new ArrayList<InviteMessgaeListValueBean>();
        inviteMessagelistAdapter=new InviteMessageListAdapter(inviteMessgaeListValueBeans,getActivity(),inviteSuccessfulListLv);
        inviteSuccessfulListLv.setAdapter(inviteMessagelistAdapter);
        inviteSuccessfulListLv.setMode(PullToRefreshBase.Mode.BOTH);
        inviteSuccessfulListLv.setOnRefreshListener(this);
        ILoadingLayout endLabels  = inviteSuccessfulListLv
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = inviteSuccessfulListLv
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        inviteSuccessfulListLv.setRefreshing();
        inviteSuccessfulListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Log.e("kjjjsjjj", inviteMessgaeListValueBeans.get(position - 1).getStatus());
               /* if (inviteMessgaeListValueBeans.get(position - 1).getStatus().equals("3") ||
                        inviteMessgaeListValueBeans.get(position - 1).getStatus().equals("4")) {
                    MyAppliction.showToast("您已经完成评论!");
                } else {
                    showExitGameAlert(position);
                }*/
                Intent intent = new Intent(getActivity(), CompanyInviteMessageActivity.class);
                intent.putExtra("InviteMessgaeListValueBean", inviteMessgaeListValueBeans.get(position-1));
                intent.putExtra("flage", "AcceptInviteFragment");
                startActivity(intent);

            }
        });

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        inviteMessgaeListValueBeans.clear();
        offset=0;
        intiData(offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

        offset=offset+10;
        intiData(offset);

    }

}
