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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.MerchantInviteListAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.MerchantInviteValueBean;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.uilt.MercharInviteParticularsActivity;
import com.jeremy.Customer.uilt.SQLhelper;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MerchantInviteMessageFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView>,View.OnClickListener {

    @ViewInject(R.id.merchant_invite_message_list_lv)
    private PullToRefreshListView merchantInviteMessageLv;
    @ViewInject(R.id.accept_layout)
    private LinearLayout accpetLayout;
    @ViewInject(R.id.anew_refresh_tv)
    private TextView anewRefreshTv;
    @ViewInject(R.id.tixing_text)
    private TextView tixingText;
    @ViewInject(R.id.yichan_text)
    private TextView yichanText;
    private HttpUtils httpUtils;
    private List<MerchantInviteValueBean> merchantInviteValueBeans;
    private MerchantInviteListAdapter inviteMessagelistAdapter;
    private int offset=0;
    public MerchantInviteMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_merchant_invite_message, container, false);
        ViewUtils.inject(this, view);
        inti();
        return view;
    }

    private void inti() {
        intiView();

    }

    private void intiView() {
        anewRefreshTv.setOnClickListener(this);

        intiListView();


    }
    private void intiData(int offset) {
        httpUtils=new HttpUtils().configCurrentHttpCacheExpiry(0);
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
        }
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getMerchantInvite(uid, "note", offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //Log.e("inviteintiData", result);
                if (!TextUtils.isEmpty(result)) {
                    HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<MerchantInviteValueBean>>() {
                    }, merchantInviteValueBeans, inviteMessagelistAdapter);
                    if (merchantInviteValueBeans.size() == 0) {
                        accpetLayout.setVisibility(View.VISIBLE);
                        merchantInviteMessageLv.setVisibility(View.GONE);
                        yichanText.setVisibility(View.GONE);
                    } else {
                        yichanText.setVisibility(View.GONE);
                        accpetLayout.setVisibility(View.GONE);
                        merchantInviteMessageLv.setVisibility(View.VISIBLE);
                    }
                    merchantInviteMessageLv.onRefreshComplete();


                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                accpetLayout.setVisibility(View.GONE);
                merchantInviteMessageLv.setVisibility(View.VISIBLE);
                merchantInviteMessageLv.onRefreshComplete();
                showAnim();
                yichanText.setVisibility(View.VISIBLE);
                Log.e("onFailure", s);
            }
        });



    }
    private void showAnim() {
        Animation appAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpthe);
        yichanText.startAnimation(appAnim);

    }
    private void intiListView() {
        merchantInviteValueBeans=new ArrayList<MerchantInviteValueBean>();
        inviteMessagelistAdapter=new MerchantInviteListAdapter(merchantInviteValueBeans,getActivity(),merchantInviteMessageLv);
        if (merchantInviteValueBeans!=null){
            merchantInviteMessageLv.setAdapter(inviteMessagelistAdapter);
        }
        merchantInviteMessageLv.setMode(PullToRefreshBase.Mode.BOTH);
        merchantInviteMessageLv.setOnRefreshListener(this);
        ILoadingLayout endLabels  = merchantInviteMessageLv
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = merchantInviteMessageLv
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        merchantInviteMessageLv.setRefreshing();
        merchantInviteMessageLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent=new Intent(getActivity(),MercharInviteParticularsActivity.class);
                intent.putExtra("resumeValueBeans", merchantInviteValueBeans.get(position-1).getInviteResume());
                startActivity(intent);

            }
        });

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        merchantInviteValueBeans.clear();
        offset=0;
        intiData(offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

        offset=merchantInviteValueBeans.size();
        intiData(offset);



    }

    @Override
    public void onClick(View v) {
        MyAppliction.showToast("刷新完成");
        merchantInviteMessageLv.setRefreshing();
    }
}
