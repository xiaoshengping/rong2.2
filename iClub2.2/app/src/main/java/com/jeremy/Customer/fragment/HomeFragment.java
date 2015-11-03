package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ActivityBean;
import com.jeremy.Customer.bean.ArtistHeadBean;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.RecruitmentListBean;
import com.jeremy.Customer.bean.TalentValueBean;
import com.jeremy.Customer.uilt.ActivityDetailActivity;
import com.jeremy.Customer.uilt.JobDetailsActivity;
import com.jeremy.Customer.uilt.RecommenListActivity;
import com.jeremy.Customer.uilt.TalentsDetailsActivity;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.RoundAngleImageView;
import com.jeremy.Customer.view.SlideShowView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    @ViewInject(R.id.advertisement_ll)
    private LinearLayout advertisement_ll;

    private TextView home_more1, home_more2, home_more3;
    private RoundAngleImageView activity_picture_riv1, activity_picture_riv2;
    private TextView activity_name_tv1, activity_name_tv2;

    private RoundAngleImageView recommend_the_virtuous_and_able_riv1, recommend_the_virtuous_and_able_riv2, recommend_the_virtuous_and_able_riv3, recommend_the_virtuous_and_able_riv4, recommend_the_virtuous_and_able_riv5, recommend_the_virtuous_and_able_riv6;
    private TextView recommend_the_virtuous_and_able_tv1, recommend_the_virtuous_and_able_tv2, recommend_the_virtuous_and_able_tv3, recommend_the_virtuous_and_able_tv4, recommend_the_virtuous_and_able_tv5, recommend_the_virtuous_and_able_tv6;

    private TextView item_position_name_tv1, item_position_name_tv2, item_position_name_tv3;
    private TextView item_position_salary_tv1, item_position_salary_tv2, item_position_salary_tv3;
    private TextView item_position_time_tv1, item_position_time_tv2, item_position_time_tv3;
    private TextView item_position_site_tv1, item_position_site_tv2, item_position_site_tv3;

    private LinearLayout item_position_ll1,item_position_ll2,item_position_ll3;

    private static ScrollView scrollView;

    private BitmapUtils bitmapUtils;

    private ArtistParme<RecruitmentListBean> recruitmentListBean;
    private ArtistParme<TalentValueBean> talentValueBean;
    private ArtistParme<ActivityBean> activityBean;

    private static int start = 0;

    public static void setStart(int start) {
        HomeFragment.start = start;
    }

    public static int getStart() {
        return start;
    }

    public static void setSV(){
        scrollView.setVisibility(View.VISIBLE);
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ViewUtils.inject(this, view);
        bitmapUtils = new BitmapUtils(getActivity());
        binding(view);
        initAdvertisement();
        initActivity();
        initRecommendTheVirtuousAndAble();
        initRecommendedWork();
        return view;
    }

    private void binding(View view) {
        home_more1 = (TextView) view.findViewById(R.id.home_more1);
        home_more2 = (TextView) view.findViewById(R.id.home_more2);
        home_more3 = (TextView) view.findViewById(R.id.home_more3);
        activity_picture_riv1 = (RoundAngleImageView) view.findViewById(R.id.activity_picture_riv1);
        activity_picture_riv2 = (RoundAngleImageView) view.findViewById(R.id.activity_picture_riv2);
        activity_name_tv1 = (TextView) view.findViewById(R.id.activity_name_tv1);
        activity_name_tv2 = (TextView) view.findViewById(R.id.activity_name_tv2);
        recommend_the_virtuous_and_able_riv1 = (RoundAngleImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv1);
        recommend_the_virtuous_and_able_riv2 = (RoundAngleImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv2);
        recommend_the_virtuous_and_able_riv3 = (RoundAngleImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv3);
        recommend_the_virtuous_and_able_riv4 = (RoundAngleImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv4);
        recommend_the_virtuous_and_able_riv5 = (RoundAngleImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv5);
        recommend_the_virtuous_and_able_riv6 = (RoundAngleImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv6);
        recommend_the_virtuous_and_able_tv1 = (TextView) view.findViewById(R.id.recommend_the_virtuous_and_able_tv1);
        recommend_the_virtuous_and_able_tv2 = (TextView) view.findViewById(R.id.recommend_the_virtuous_and_able_tv2);
        recommend_the_virtuous_and_able_tv3 = (TextView) view.findViewById(R.id.recommend_the_virtuous_and_able_tv3);
        recommend_the_virtuous_and_able_tv4 = (TextView) view.findViewById(R.id.recommend_the_virtuous_and_able_tv4);
        recommend_the_virtuous_and_able_tv5 = (TextView) view.findViewById(R.id.recommend_the_virtuous_and_able_tv5);
        recommend_the_virtuous_and_able_tv6 = (TextView) view.findViewById(R.id.recommend_the_virtuous_and_able_tv6);
        item_position_name_tv1 = (TextView) view.findViewById(R.id.item_position_name_tv1);
        item_position_salary_tv1 = (TextView) view.findViewById(R.id.item_position_salary_tv1);
        item_position_time_tv1 = (TextView) view.findViewById(R.id.item_position_time_tv1);
        item_position_site_tv1 = (TextView) view.findViewById(R.id.item_position_site_tv1);

        item_position_name_tv2 = (TextView) view.findViewById(R.id.item_position_name_tv2);
        item_position_salary_tv2 = (TextView) view.findViewById(R.id.item_position_salary_tv2);
        item_position_time_tv2 = (TextView) view.findViewById(R.id.item_position_time_tv2);
        item_position_site_tv2 = (TextView) view.findViewById(R.id.item_position_site_tv2);

        item_position_name_tv3 = (TextView) view.findViewById(R.id.item_position_name_tv3);
        item_position_salary_tv3 = (TextView) view.findViewById(R.id.item_position_salary_tv3);
        item_position_time_tv3 = (TextView) view.findViewById(R.id.item_position_time_tv3);
        item_position_site_tv3 = (TextView) view.findViewById(R.id.item_position_site_tv3);

        item_position_ll1 = (LinearLayout)view.findViewById(R.id.item_position_ll1);
        item_position_ll2 = (LinearLayout)view.findViewById(R.id.item_position_ll2);
        item_position_ll3 = (LinearLayout)view.findViewById(R.id.item_position_ll3);

        scrollView = (ScrollView)view.findViewById(R.id.scrollView);

        home_more1.setOnClickListener(this);
        home_more2.setOnClickListener(this);
        home_more3.setOnClickListener(this);
        recommend_the_virtuous_and_able_riv1.setOnClickListener(this);
        recommend_the_virtuous_and_able_riv2.setOnClickListener(this);
        recommend_the_virtuous_and_able_riv3.setOnClickListener(this);
        recommend_the_virtuous_and_able_riv4.setOnClickListener(this);
        recommend_the_virtuous_and_able_riv5.setOnClickListener(this);
        recommend_the_virtuous_and_able_riv6.setOnClickListener(this);
        item_position_ll1.setOnClickListener(this);
        item_position_ll2.setOnClickListener(this);
        item_position_ll3.setOnClickListener(this);
        activity_picture_riv1.setOnClickListener(this);
        activity_picture_riv2.setOnClickListener(this);

        scrollView.setVisibility(View.GONE);

    }

    //初始化广告栏
    private void initAdvertisement() {
        HttpUtils headHttpUtils = new HttpUtils();
        headHttpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getArtistHead(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<ArtistHeadBean> headBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<ArtistHeadBean>>() {
                    });
                    if ("success".equals(headBean.getState())) {

                        SlideShowView ssv = new SlideShowView(getActivity(), headBean.getValue());
                        advertisement_ll.addView(ssv);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (getResources().getDimension(R.dimen.advertisement_height)));
                        layoutParams.setMargins(0, 0, 0, 0);
                        ssv.setLayoutParams(layoutParams);

                        start++;

                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
//                progressbar.setVisibility(View.GONE);
//                londing_tip.setVisibility(View.VISIBLE);
                start = -10;
            }
        });

    }

    //初始化活动
    private void initActivity() {
        HttpUtils headHttpUtils = new HttpUtils();
        headHttpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getActivity(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    activityBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<ActivityBean>>() {
                    });
                    if ("success".equals(activityBean.getState())) {
                        if (activityBean.getValue().size() != 0) {
                            bitmapUtils.display(activity_picture_riv1, AppUtilsUrl.ImageBaseUrl + activityBean.getValue().get(0).getImage());
                            bitmapUtils.display(activity_picture_riv2, AppUtilsUrl.ImageBaseUrl + activityBean.getValue().get(1).getImage());
                            activity_name_tv1.setText(activityBean.getValue().get(0).getTitle());
                            activity_name_tv2.setText(activityBean.getValue().get(1).getTitle());

//                            AssetManager mgr=getActivity().getAssets();//得到AssetManager
//                            Typeface tf=Typeface.createFromAsset(mgr, "fonts/asdfa.otf");//根据路径得到Typeface
//                            activity_name_tv1.setTypeface(tf);//设置字体

                        }

                    }

                }

                start++;

            }

            @Override
            public void onFailure(HttpException e, String s) {
//                progressbar.setVisibility(View.GONE);
//                londing_tip.setVisibility(View.VISIBLE);
                start = -10;
            }
        });
    }

    //初始化推荐人才
    private void initRecommendTheVirtuousAndAble() {
        HttpUtils headHttpUtils = new HttpUtils();
        headHttpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getRecommendTheVirtuousAndAble(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    talentValueBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<TalentValueBean>>() {
                    });
                    if ("success".equals(talentValueBean.getState())) {
                        if (talentValueBean.getValue().size() != 0) {

                            bitmapUtils.display(recommend_the_virtuous_and_able_riv1, AppUtilsUrl.ImageBaseUrl + talentValueBean.getValue().get(0).getUsericon());
                            bitmapUtils.display(recommend_the_virtuous_and_able_riv2, AppUtilsUrl.ImageBaseUrl + talentValueBean.getValue().get(1).getUsericon());
                            bitmapUtils.display(recommend_the_virtuous_and_able_riv3, AppUtilsUrl.ImageBaseUrl + talentValueBean.getValue().get(2).getUsericon());
                            bitmapUtils.display(recommend_the_virtuous_and_able_riv4, AppUtilsUrl.ImageBaseUrl + talentValueBean.getValue().get(3).getUsericon());
                            bitmapUtils.display(recommend_the_virtuous_and_able_riv5, AppUtilsUrl.ImageBaseUrl + talentValueBean.getValue().get(4).getUsericon());
                            bitmapUtils.display(recommend_the_virtuous_and_able_riv6, AppUtilsUrl.ImageBaseUrl + talentValueBean.getValue().get(5).getUsericon());

                            recommend_the_virtuous_and_able_tv1.setText(talentValueBean.getValue().get(0).getResumeZhName());
                            recommend_the_virtuous_and_able_tv2.setText(talentValueBean.getValue().get(1).getResumeZhName());
                            recommend_the_virtuous_and_able_tv3.setText(talentValueBean.getValue().get(2).getResumeZhName());
                            recommend_the_virtuous_and_able_tv4.setText(talentValueBean.getValue().get(3).getResumeZhName());
                            recommend_the_virtuous_and_able_tv5.setText(talentValueBean.getValue().get(4).getResumeZhName());
                            recommend_the_virtuous_and_able_tv6.setText(talentValueBean.getValue().get(5).getResumeZhName());

                        }

                    }

                }

                start++;

            }

            @Override
            public void onFailure(HttpException e, String s) {
//                progressbar.setVisibility(View.GONE);
//                londing_tip.setVisibility(View.VISIBLE);
                start = -10;
            }
        });
    }

    //初始化推荐工作
    private void initRecommendedWork() {
        HttpUtils headHttpUtils = new HttpUtils();
        headHttpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getRecommendedWork(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    recruitmentListBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<RecruitmentListBean>>() {
                    });
                    if ("success".equals(recruitmentListBean.getState())) {

                        if (recruitmentListBean.getValue().get(0).getPosition() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(0).getPosition().equals("")) {
                            } else {
                                item_position_name_tv1.setText(recruitmentListBean.getValue().get(0).getPosition());
                            }
                        }
                        if (recruitmentListBean.getValue().get(0).getWorkPay() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(0).getWorkPay().equals("")) {
                            } else {
                                item_position_salary_tv1.setText(recruitmentListBean.getValue().get(0).getWorkPay());
                            }
                        }
                        if (recruitmentListBean.getValue().get(0).getWorkingTime() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(0).getWorkingTime().equals("")) {
                            } else {
                                item_position_time_tv1.setText("工作时间  " + recruitmentListBean.getValue().get(0).getWorkingTime());
                            }
                        }
                        if (recruitmentListBean.getValue().get(0).getWorkPlace() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(0).getWorkPlace().equals("")) {
                            } else {
                                item_position_site_tv1.setText("地点   " + recruitmentListBean.getValue().get(0).getWorkPlace());
                            }
                        }

                        if (recruitmentListBean.getValue().get(1).getPosition() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(1).getPosition().equals("")) {
                            } else {
                                item_position_name_tv2.setText(recruitmentListBean.getValue().get(1).getPosition());
                            }
                        }
                        if (recruitmentListBean.getValue().get(1).getWorkPay() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(1).getWorkPay().equals("")) {
                            } else {
                                item_position_salary_tv2.setText(recruitmentListBean.getValue().get(1).getWorkPay());
                            }
                        }
                        if (recruitmentListBean.getValue().get(1).getWorkingTime() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(1).getWorkingTime().equals("")) {
                            } else {
                                item_position_time_tv2.setText("工作时间  " + recruitmentListBean.getValue().get(1).getWorkingTime());
                            }
                        }
                        if (recruitmentListBean.getValue().get(1).getWorkPlace() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(1).getWorkPlace().equals("")) {
                            } else {
                                item_position_site_tv2.setText("地点   " + recruitmentListBean.getValue().get(1).getWorkPlace());
                            }
                        }


                        if (recruitmentListBean.getValue().get(2).getPosition() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(2).getPosition().equals("")) {
                            } else {
                                item_position_name_tv3.setText(recruitmentListBean.getValue().get(2).getPosition());
                            }
                        }
                        if (recruitmentListBean.getValue().get(2).getWorkPay() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(2).getWorkPay().equals("")) {
                            } else {
                                item_position_salary_tv3.setText(recruitmentListBean.getValue().get(2).getWorkPay());
                            }
                        }
                        if (recruitmentListBean.getValue().get(2).getWorkingTime() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(2).getWorkingTime().equals("")) {
                            } else {
                                item_position_time_tv3.setText("工作时间  " + recruitmentListBean.getValue().get(2).getWorkingTime());
                            }
                        }
                        if (recruitmentListBean.getValue().get(2).getWorkPlace() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(2).getWorkPlace().equals("")) {
                            } else {
                                item_position_site_tv3.setText("地点   " + recruitmentListBean.getValue().get(2).getWorkPlace());
                            }
                        }


                    }

                }

                start++;

            }

            @Override
            public void onFailure(HttpException e, String s) {
//                progressbar.setVisibility(View.GONE);
//                londing_tip.setVisibility(View.VISIBLE);
                start = -10;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_more1:
                TouchMore(Identification.ACTIVITY);
                break;
            case R.id.home_more2:
                TouchMore(Identification.TALENTS);
                break;
            case R.id.home_more3:
                TouchMore(Identification.PROSITION);
                break;
            case R.id.recommend_the_virtuous_and_able_riv1:
                Talents(0);
                break;
            case R.id.recommend_the_virtuous_and_able_riv2:
                Talents(1);
                break;
            case R.id.recommend_the_virtuous_and_able_riv3:
                Talents(2);
                break;
            case R.id.recommend_the_virtuous_and_able_riv4:
                Talents(3);
                break;
            case R.id.recommend_the_virtuous_and_able_riv5:
                Talents(4);
                break;
            case R.id.recommend_the_virtuous_and_able_riv6:
                Talents(5);
                break;
            case R.id.item_position_ll1:
                Position(0);
                break;
            case R.id.item_position_ll2:
                Position(1);
                break;
            case R.id.item_position_ll3:
                Position(2);
                break;
            case R.id.activity_picture_riv1:
                Activity(0);
                break;
            case R.id.activity_picture_riv2:
                Activity(1);
                break;
        }
    }

    private void Activity(int i){
        Intent intent = new Intent();
        intent.setClass(getActivity(), ActivityDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Detail", activityBean.getValue().get(i));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void Position(int i){
        Intent intent = new Intent(getActivity(), JobDetailsActivity.class);  //方法1
        Bundle bundle = new Bundle();
        bundle.putSerializable("Detail", recruitmentListBean.getValue().get(i));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void Talents(int i) {
        Intent intent = new Intent(getActivity(), TalentsDetailsActivity.class);  //方法1
        Bundle bundle = new Bundle();
        bundle.putSerializable("Detail", talentValueBean.getValue().get(i));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void TouchMore(int ident) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), RecommenListActivity.class);
        intent.putExtra("Ident", ident);
        startActivity(intent);
    }


}
