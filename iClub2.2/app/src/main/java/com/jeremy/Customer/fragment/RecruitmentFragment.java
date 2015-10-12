package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.RecommendListAdater;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.MyDialog;
import com.jeremy.Customer.bean.RecruitmentListBean;
import com.jeremy.Customer.citySelection.CitySelectionActivity;
import com.jeremy.Customer.uilt.JobChoiceActivity;
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
public class RecruitmentFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView> {

    private PullToRefreshListView recommend_list;
    private List<RecruitmentListBean> recruitmentListData = new ArrayList<>();
    ;
    private RecommendListAdater adater;

    private Button selected_city, selected_position;
    private int citynum = 0;//城市id
    private int jobnum = 0;//城市id

    private int offset = 0;

    public RecruitmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruitment, container, false);
        recommend_list = (PullToRefreshListView) view.findViewById(R.id.recommend_list);
        ViewUtils.inject(this, view);
//        initRecruitmentListData(0, 0, offset);
        conditionsSelected(view);
        intiProsition();
        return view;

    }

    private void conditionsSelected(View view) {
        selected_city = (Button) view.findViewById(R.id.selected_city);
        selected_position = (Button) view.findViewById(R.id.selected_position);

        selected_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CitySelectionActivity.class);  //方法1
                startActivityForResult(intent, 0);
            }
        });
        selected_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JobChoiceActivity.class);  //方法1
                startActivityForResult(intent, 0);
            }
        });

    }

    //初始化职位列表
    private void intiProsition() {

        adater = new RecommendListAdater(getActivity(), PROSITION, recruitmentListData);
        recommend_list.setAdapter(adater);
//        Toast.makeText(getActivity(), recruitmentListData.size() + "", Toast.LENGTH_LONG).show();
        recommend_list.setMode(PullToRefreshBase.Mode.BOTH);
        recommend_list.setOnRefreshListener(this);
        ILoadingLayout endLabels = recommend_list
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels = recommend_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        recommend_list.setRefreshing();

        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(getActivity(), myActivity.class);  //方法1
//                startActivity(intent);

                Intent intent = new Intent(getActivity(), JobDetailsActivity.class);  //方法1
                Bundle bundle = new Bundle();
                bundle.putSerializable("Detail", recruitmentListData.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private LoadingDialog loadingDialog;

    //获取招聘列表（非搜索）
    private void initRecruitmentListData(int city, int job, int offset) {

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.show();

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getRecruitmentList(city, job, offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    HttpHelper.baseToUrl(result, new TypeReference<ArtistParme<RecruitmentListBean>>() {
                    }, recruitmentListData, adater);
                    adater = new RecommendListAdater(getActivity(), PROSITION, recruitmentListData);
                    recommend_list.setAdapter(adater);
                    recommend_list.onRefreshComplete();

                    loadingDialog.dismiss();

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                adater = new RecommendListAdater();
                recommend_list.setAdapter(adater);
                recommend_list.onRefreshComplete();
                loadingDialog.dismiss();
//                recommend_list.setRefreshing(false);
//                recommend_list.on;
                dialog();
            }
        });


    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        recruitmentListData.clear();
        offset = 0;
        initRecruitmentListData(citynum, jobnum, offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset = offset + 10;
        initRecruitmentListData(citynum, jobnum, offset);
    }

    private MyDialog dialog2;

    //提示框
    private void dialog() {
        dialog2 = new MyDialog(getActivity(), Identification.TOOLTIP, Identification.NETWORKANOMALY);
        dialog2.setDetermine(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recommend_list.setVisibility(View.GONE);
                dialog2.dismiss();
            }
        });

        dialog2.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();

        if (resultCode == Identification.CITYSELECTION) {
         /*获取Bundle中的数据，注意类型和key*/
            int city = bundle.getInt("City");
            String cName = bundle.getString("CityName");
            if (city >= 0) {
                if (city != 0) {
                    selected_city.setText(cName);
                } else {
                    selected_city.setText("选择城市");
                }
                citynum = city;
                recruitmentListData.clear();

//                recommend_list.setRefreshing(true);

            }
        } else if(resultCode == Identification.JOBCHOICE){
            int job = bundle.getInt("Job");
            String pName = bundle.getString("JobName");
//        if(job>=0&&job!=10){
            if (job != 0) {
                selected_position.setText(pName);
            } else {

                selected_position.setText("选择职位");
            }
            jobnum = job;
//            if(searchStatusfalse) {
//                update(getActivity(), citynum, jobnum, sousuo,offset);
//            }else {
            recruitmentListData.clear();
//        initRecruitmentListData(citynum, jobnum, offset);
//            }
        }

        recommend_list.onRefreshComplete();
        recommend_list.setRefreshing(true);

    }


}
