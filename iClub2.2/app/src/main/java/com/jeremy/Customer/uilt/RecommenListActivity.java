package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.RecommendListAdater;
import com.jeremy.Customer.bean.ActivityBean;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.CommentBean;
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

public class RecommenListActivity extends Activity implements PullToRefreshBase.OnRefreshListener2<ListView> {

    private MyTitleBar mytitle;
    //    private ListView recommend_list;
    private int identi = 1;

    private PullToRefreshListView recommend_list;
    private TextView network_hint;
    private List<ActivityBean> activityData = new ArrayList<>();
    private List<TalentValueBean> talentValueBean = new ArrayList<>();
    private List<RecruitmentListBean> recruitmentListData = new ArrayList<>();
    private List<CommentBean> commentDate = new ArrayList<>();
    private RecommendListAdater adater;
    private String url;
    private int id;

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
        network_hint = (TextView) findViewById(R.id.network_hint);
        network_hint.setVisibility(View.GONE);

        Bundle bundle = this.getIntent().getExtras();
        identi = bundle.getInt("Ident");
        url = bundle.getString("URL");
        id = bundle.getInt("ID");
        if (identi == Identification.ACTIVITY) {
            intiActivity();
        } else if (identi == Identification.TALENTS) {
            intiTalents();
        } else if (identi == Identification.HOTJOBS) {
            intiProsition();
        } else if (identi == Identification.COMMENT) {
            intiComment();
        }


        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (identi == Identification.ACTIVITY) {
                    Intent intent = new Intent();
                    intent.setClass(RecommenListActivity.this, ActivityDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Detail", activityData.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (identi == Identification.TALENTS) {
                    Intent intent = new Intent(RecommenListActivity.this, TalentsDetailsActivity.class);  //方法1
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Detail", talentValueBean.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (identi == Identification.HOTJOBS) {
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
        adater = new RecommendListAdater(identi, this, activityData);
        recommend_list.setAdapter(adater);

        recommend_list.setMode(PullToRefreshBase.Mode.BOTH);
        recommend_list.setOnRefreshListener(this);
        ILoadingLayout endLabels = recommend_list
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels = recommend_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在加载...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        recommend_list.setRefreshing();

    }

    //获取活动列表
    private void initActivityListData(int offsett) {

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        HttpUtils headHttpUtils = new HttpUtils();
        headHttpUtils.configCurrentHttpCacheExpiry(1000);
        headHttpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getActivity(offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<ActivityBean> activityBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<ActivityBean>>() {
                    });
                    if ("success".equals(activityBean.getState())) {

                        if (offset == 0) {
                            activityData.clear();
                        }

//                        if (activityBean.getValue().size() != 0) {
                        if (activityBean.getTotal() > activityData.size()) {
                            if (activityData.size() == 0) {
                                activityData.addAll(activityBean.getValue());
                                adater = new RecommendListAdater(identi, RecommenListActivity.this, activityData);
                                recommend_list.setAdapter(adater);
                            } else {
                                activityData.addAll(activityBean.getValue());
                                adater.setActivityBean(activityData);
                                adater.notifyDataSetChanged();
                            }
                        }  else {
                            Toast.makeText(RecommenListActivity.this, "已加载全部内容", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        recommend_list.onRefreshComplete();
                        loadingDialog.dismiss();
                        dialog();
                    }
                    recommend_list.onRefreshComplete();

                    loadingDialog.dismiss();

                }


            }
//            }

            @Override
            public void onFailure(HttpException e, String s) {
//                    adater = new RecommendListAdater();
//                    recommend_list.setAdapter(adater);
                recommend_list.onRefreshComplete();
                loadingDialog.dismiss();
//                recommend_list.setRefreshing(false);
//                recommend_list.on;
                dialog();
            }
        });

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
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels = recommend_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在加载...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        recommend_list.setRefreshing();

        mytitle.setTextViewText("热门人才");
//        RecommendListAdater adater = new RecommendListAdater(this,identi);
//        recommend_list.setAdapter(adater);
    }

    private LoadingDialog loadingDialog;

    //获取人才列表（非搜索）
    private void initTalentsListData(int city, int job, int offsett) {

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getRecommendTheVirtuousAndAble(offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<TalentValueBean> talentValue = JSONObject.parseObject(result, new TypeReference<ArtistParme<TalentValueBean>>() {
                    });
                    if (talentValue.getState().equals("success")) {

                        if (offset == 0) {
                            talentValueBean.clear();
                        }

                        if (talentValue.getTotal() > talentValueBean.size()) {
                            if (talentValueBean.size() == 0) {
                                talentValueBean.addAll(talentValue.getValue());
                                adater = new RecommendListAdater(RecommenListActivity.this, talentValueBean, Identification.TALENTS);
                                recommend_list.setAdapter(adater);
                            } else {
                                talentValueBean.addAll(talentValue.getValue());
                                adater.setTalentValueBean(talentValueBean);
                                adater.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(RecommenListActivity.this, "已加载全部内容", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        recommend_list.onRefreshComplete();
                        loadingDialog.dismiss();
                        dialog();
                    }
                    recommend_list.onRefreshComplete();

                    loadingDialog.dismiss();

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
//                adater = new RecommendListAdater();
//                recommend_list.setAdapter(adater);
                recommend_list.onRefreshComplete();
                loadingDialog.dismiss();
//                recommend_list.setRefreshing(false);
//                recommend_list.on;
                dialog();
            }
        });

    }

    //初始化评论列表
    private void intiComment() {
        adater = new RecommendListAdater(commentDate, Identification.COMMENT, this);
        recommend_list.setAdapter(adater);
//        Toast.makeText(getActivity(), recruitmentListData.size() + "", Toast.LENGTH_LONG).show();
        recommend_list.setMode(PullToRefreshBase.Mode.BOTH);
        recommend_list.setOnRefreshListener(this);
        ILoadingLayout endLabels = recommend_list
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels = recommend_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在加载...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        recommend_list.setRefreshing();

        mytitle.setTextViewText("评论");
//        RecommendListAdater adater = new RecommendListAdater(this,identi);
//        recommend_list.setAdapter(adater);
    }

    //初始化合作评论
    private void initcCollaborateComment(int offsett) {

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getComment(id, url, offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<CommentBean> commentBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<CommentBean>>() {
                    });
                    if (commentBean.getState().equals("success")) {

                        if (offset == 0) {
                            commentDate.clear();
                        }

                        if (commentBean.getTotal() > commentDate.size()) {
                            if (commentDate.size() == 0) {
                                commentDate.addAll(commentBean.getValue());
                                adater = new RecommendListAdater(commentDate, Identification.COMMENT, RecommenListActivity.this);
                                recommend_list.setAdapter(adater);
                            } else {
                                commentDate.addAll(commentBean.getValue());
                                adater.setCommentBean(commentDate);
                                adater.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(RecommenListActivity.this, "已加载全部内容", Toast.LENGTH_LONG).show();
                        }


//                        if(commentBean.getValue()!=null) {
                        /*List<CommentBean> commentDate = commentBean.getValue();
                        adater = new ReputationAdapter(ReputationActivity.this,commentDate);
                        reputation_list.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if(commentDate.size()!=0) {
                            reputation_tipe_tv.setTextColor(0x00000000);
                        }else {
                            reputation_tipe_tv.setTextColor(0xffDEDDE2);
                        }*/
//                        }

                    }else {
                        recommend_list.onRefreshComplete();
                        loadingDialog.dismiss();
                        dialog();
                    }

                    recommend_list.onRefreshComplete();

                    loadingDialog.dismiss();

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

//                adater = new RecommendListAdater();
//                recommend_list.setAdapter(adater);
                recommend_list.onRefreshComplete();
                loadingDialog.dismiss();

                dialog();

//                reputation_tipe_tv.setTextColor(0xffDEDDE2);
//                reputation_tipe_tv.setText("网路异常，请稍后再试！");
            }
        });
    }

    private MyDialog dialog2;

    //提示框
    private void dialog() {
        network_hint.setVisibility(View.VISIBLE);
    }


    //初始化职位列表
    private void intiProsition() {

        adater = new RecommendListAdater(RecommenListActivity.this, Identification.HOTJOBS, recruitmentListData);
        recommend_list.setAdapter(adater);
//        Toast.makeText(getActivity(), recruitmentListData.size() + "", Toast.LENGTH_LONG).show();
        recommend_list.setMode(PullToRefreshBase.Mode.BOTH);
        recommend_list.setOnRefreshListener(this);
        ILoadingLayout endLabels = recommend_list
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels = recommend_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在加载...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        recommend_list.setRefreshing();

        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(getActivity(), myActivity.class);  //方法1
//                startActivity(intent);

                Intent intent = new Intent(RecommenListActivity.this, JobDetailsActivity.class);  //方法1
                Bundle bundle = new Bundle();
                bundle.putSerializable("Detail", recruitmentListData.get(position - 1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        mytitle.setTextViewText("热门工作");
//        RecommendListAdater adater = new RecommendListAdater(this, identi);
//        recommend_list.setAdapter(adater);
    }

    //获取招聘列表（非搜索）
    private void initPrositionListData(int city, int job, int offsett) {

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getRecommendedWork(offset), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<RecruitmentListBean> recruitmentListBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<RecruitmentListBean>>() {
                    });
                    if (recruitmentListBean.getState().equals("success")) {

                        if (offset == 0) {
                            recruitmentListData.clear();
                        }

                        if (recruitmentListBean.getTotal() > recruitmentListData.size()) {
                            if (recruitmentListData.size() == 0) {
                                recruitmentListData.addAll(recruitmentListBean.getValue());
                                adater = new RecommendListAdater(RecommenListActivity.this, Identification.HOTJOBS, recruitmentListData);
                                recommend_list.setAdapter(adater);
                            } else {
                                recruitmentListData.addAll(recruitmentListBean.getValue());
                                adater.setRecruitmentListData(recruitmentListData);
                                adater.notifyDataSetChanged();
                            }
                        }  else {
                            Toast.makeText(RecommenListActivity.this, "已加载全部内容", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        recommend_list.onRefreshComplete();
                        loadingDialog.dismiss();
                        dialog();
                    }
                    recommend_list.onRefreshComplete();

                    loadingDialog.dismiss();

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
//                adater = new RecommendListAdater();
//                recommend_list.setAdapter(adater);
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
        network_hint.setVisibility(View.GONE);
        if (identi == Identification.ACTIVITY) {
            offset = 0;
            initActivityListData(offset);
        } else if (identi == Identification.TALENTS) {
            offset = 0;
            initTalentsListData(0, 0, offset);
        } else if (identi == Identification.HOTJOBS) {
            offset = 0;
            initPrositionListData(0, 0, offset);
        } else if (identi == Identification.COMMENT) {
            offset = 0;
            initcCollaborateComment(offset);
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        network_hint.setVisibility(View.GONE);
        if (identi == Identification.ACTIVITY) {
            offset = activityData.size();
            initActivityListData(offset);
        } else if (identi == Identification.TALENTS) {
            offset = talentValueBean.size();
            initTalentsListData(0, 0, offset);
        } else if (identi == Identification.HOTJOBS) {
            offset = recruitmentListData.size();
            initPrositionListData(0, 0, offset);
        } else if (identi == Identification.COMMENT) {
            offset = commentDate.size();
            initcCollaborateComment(offset);
        }

    }
}
