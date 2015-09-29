package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.jeremy.Customer.adapter.RecommendListAdater;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.MyDialog;
import com.jeremy.Customer.bean.RecruitmentListBean;
import com.jeremy.Customer.uilt.JobDetailsActivity;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.url.HttpHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import static com.jeremy.Customer.bean.Identification.PROSITION;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecruitmentFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView>{

    private PullToRefreshListView recommend_list;
    private List<RecruitmentListBean> recruitmentListData = new ArrayList<>();;
    private RecommendListAdater adater;

    private int offset=0;

    public RecruitmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruitment, container, false);
        ViewUtils.inject(this, view);
//        initRecruitmentListData(0, 0, offset);
        intiProsition(view);
        return view;

    }

    //初始化职位列表
    private void intiProsition(View view) {
        recommend_list = (PullToRefreshListView)view.findViewById(R.id.recommend_list);
        adater = new RecommendListAdater(getActivity(), PROSITION ,recruitmentListData);
        recommend_list.setAdapter(adater);
//        Toast.makeText(getActivity(), recruitmentListData.size() + "", Toast.LENGTH_LONG).show();
        recommend_list.setMode(PullToRefreshBase.Mode.BOTH);
        recommend_list.setOnRefreshListener(this);
        ILoadingLayout endLabels  = recommend_list
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = recommend_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        recommend_list.setRefreshing();

        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), JobDetailsActivity.class);  //方法1
//                intent.putCharSequenceArrayListExtra("Detail",recruitmentListData);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Detail", recruitmentListData.get(position));
                intent.putExtras(bundle);
//                intent.putExtra("Status", areaBean.PROVINCE);
                startActivity(intent);
            }
        });
    }

    //获取招聘列表（非搜索）
    private void initRecruitmentListData(int city, int job,int offset) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getRecruitmentList(city, job, offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<RecruitmentListBean>>() {
                    }, recruitmentListData, adater);
                    adater = new RecommendListAdater(getActivity(), PROSITION ,recruitmentListData);
                    recommend_list.setAdapter(adater);
                    recommend_list.onRefreshComplete();

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog();
            }
        });


    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            recruitmentListData.clear();
            offset = 0;
            initRecruitmentListData(0, 0, offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset=offset+10;
            initRecruitmentListData(0, 0, offset);
    }

    private MyDialog dialog2;
    //提示框
    private void dialog() {
        dialog2 = new MyDialog(getActivity(), Identification.TOOLTIP,Identification.NETWORKANOMALY);
        dialog2.setDetermine(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recommend_list.setVisibility(View.GONE);
                dialog2.dismiss();
            }
        });

        dialog2.show();
    }

}
