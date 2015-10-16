package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
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
import com.jeremy.Customer.bean.TalentValueBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.MyTitleBar;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import static com.jeremy.Customer.bean.Identification.PROSITION;

public class RecommenListActivity extends Activity implements PullToRefreshBase.OnRefreshListener2<ListView> {

    private MyTitleBar mytitle;
    //    private ListView recommend_list;
    private int identi = 1;

    private PullToRefreshListView recommend_list;
    private List<TalentValueBean> talentValueBean = new ArrayList<>();
    private List<RecruitmentListBean> recruitmentListData = new ArrayList<>();
    private RecommendListAdater adater;

    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);
        inti();

    }

    //初始化
    private void inti() {

        mytitle = (MyTitleBar) findViewById(R.id.mytitle);
        recommend_list = (PullToRefreshListView) findViewById(R.id.recommend_list);

        Bundle bundle = this.getIntent().getExtras();
        identi = bundle.getInt("Ident");
        if (identi == Identification.ACTIVITY) {
            intiActivity();
        } else if (identi == Identification.TALENTS) {
            intiTalents();
        } else if (identi == Identification.PROSITION) {
            intiProsition();
        }

        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (identi == Identification.ACTIVITY) {
                    Intent intent = new Intent();
                    intent.setClass(RecommenListActivity.this, ActivityDetailActivity.class);
                    startActivity(intent);
                } else if (identi == Identification.TALENTS) {
                    Intent intent = new Intent(RecommenListActivity.this, TalentsDetailsActivity.class);  //方法1
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Detail", talentValueBean.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (identi == Identification.PROSITION) {
                    Intent intent = new Intent(RecommenListActivity.this, JobDetailsActivity.class);  //方法1
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Detail", recruitmentListData.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }

    //初始化活动列表
    private void intiActivity() {
        mytitle.setTextViewText("活动");
        RecommendListAdater adater = new RecommendListAdater(this, identi);
        recommend_list.setAdapter(adater);
    }

    //初始化人才列表
    private void intiTalents() {
        adater = new RecommendListAdater(this, talentValueBean, Identification.TALENTS);
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

        mytitle.setTextViewText("热门人才");
//        RecommendListAdater adater = new RecommendListAdater(this,identi);
//        recommend_list.setAdapter(adater);
    }

    private LoadingDialog loadingDialog;

    //获取人才列表（非搜索）
    private void initTalentsListData(int city, int job, int offset) {

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getRecommendTheVirtuousAndAble(offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<TalentValueBean> talentValue = JSONObject.parseObject(result, new TypeReference<ArtistParme<TalentValueBean>>() {
                    });
                    if (talentValue.getState().equals("success")) {
                        if(talentValue.getValue()!=null) {
                            talentValueBean.addAll(talentValue.getValue());
                            adater.setTalentValueBean(talentValueBean);
                            adater.notifyDataSetChanged();
                        }else {
                            Toast.makeText(RecommenListActivity.this, "以上已为全部内容", Toast.LENGTH_LONG).show();
                        }

                    }
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

    private MyDialog dialog2;

    //提示框
    private void dialog() {
        dialog2 = new MyDialog(this, Identification.TOOLTIP, Identification.NETWORKANOMALY);
        dialog2.setDetermine(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recommend_list.setVisibility(View.GONE);
                dialog2.dismiss();
            }
        });

        dialog2.show();
    }


    //初始化职位列表
    private void intiProsition() {

        adater = new RecommendListAdater(RecommenListActivity.this, PROSITION, recruitmentListData);
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

                Intent intent = new Intent(RecommenListActivity.this, JobDetailsActivity.class);  //方法1
                Bundle bundle = new Bundle();
                bundle.putSerializable("Detail", recruitmentListData.get(position-1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        mytitle.setTextViewText("热门职位");
//        RecommendListAdater adater = new RecommendListAdater(this, identi);
//        recommend_list.setAdapter(adater);
    }

    //获取招聘列表（非搜索）
    private void initPrositionListData(int city, int job, int offset) {

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getRecommendedWork(offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<RecruitmentListBean> recruitmentListBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<RecruitmentListBean>>() {
                    });
                    if (recruitmentListBean.getState().equals("success")) {
                        if(recruitmentListBean.getValue()!=null) {
                            recruitmentListData.addAll(recruitmentListBean.getValue());
                            adater.setRecruitmentListData(recruitmentListData);
                            adater.notifyDataSetChanged();
                        }else {
                            Toast.makeText(RecommenListActivity.this, "以上已为全部内容", Toast.LENGTH_LONG).show();
                        }

                    }
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


    public void back(View v) {
        finish();
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (identi == Identification.ACTIVITY) {

        } else if (identi == Identification.TALENTS) {
            talentValueBean.clear();
            offset = 0;
            initTalentsListData(0, 0, offset);
        } else if (identi == Identification.PROSITION) {
            recruitmentListData.clear();
            offset = 0;
            initPrositionListData(0, 0, offset);
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

        if (identi == Identification.ACTIVITY) {

        } else if (identi == Identification.TALENTS) {
            offset = offset + 10;
            initTalentsListData(0, 0, offset);
        } else if (identi == Identification.PROSITION) {
            offset = offset + 10;
            initPrositionListData(0, 0, offset);
        }

    }
}
