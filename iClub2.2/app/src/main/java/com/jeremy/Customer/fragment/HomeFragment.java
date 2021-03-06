package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ActivityBean;
import com.jeremy.Customer.bean.ArtistHeadBean;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.MyDialog;
import com.jeremy.Customer.bean.RecruitmentListBean;
import com.jeremy.Customer.bean.TalentValueBean;
import com.jeremy.Customer.uilt.ActivityDetailActivity;
import com.jeremy.Customer.uilt.JobDetailsActivity;
import com.jeremy.Customer.uilt.RecommenListActivity;
import com.jeremy.Customer.uilt.TalentsDetailsActivity;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.Advertisement;
import com.jeremy.Customer.view.MyScrollView;
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
public class HomeFragment extends Fragment implements View.OnClickListener, View.OnTouchListener,MyScrollView.OnScrollListener {

    @ViewInject(R.id.advertisement_ll)
    private LinearLayout advertisement_ll;

    private TextView home_more1, home_more2, home_more3;
    //    private RoundAngleImageView activity_picture_riv1, activity_picture_riv2;
//    private TextView activity_name_tv1, activity_name_tv2;
    private Advertisement advertisement1, advertisement2;

    private ImageView recommend_the_virtuous_and_able_riv1;
    private ImageView recommend_the_virtuous_and_able_riv2, recommend_the_virtuous_and_able_riv3, recommend_the_virtuous_and_able_riv4, recommend_the_virtuous_and_able_riv5, recommend_the_virtuous_and_able_riv6;
    private TextView recommend_the_virtuous_and_able_tv1, recommend_the_virtuous_and_able_tv2, recommend_the_virtuous_and_able_tv3, recommend_the_virtuous_and_able_tv4, recommend_the_virtuous_and_able_tv5, recommend_the_virtuous_and_able_tv6;

    private TextView item_position_name_tv1, item_position_name_tv2, item_position_name_tv3, item_position_name_tv4, item_position_name_tv5;
    private TextView item_position_salary_tv1, item_position_salary_tv2, item_position_salary_tv3, item_position_salary_tv4, item_position_salary_tv5;
    private TextView item_position_time_tv1, item_position_time_tv2, item_position_time_tv3, item_position_time_tv4, item_position_time_tv5;
    private TextView item_position_site_tv1, item_position_site_tv2, item_position_site_tv3, item_position_site_tv4, item_position_site_tv5;

    private LinearLayout item_position_ll1, item_position_ll2, item_position_ll3, item_position_ll4, item_position_ll5;

    private static MyScrollView scrollView;
    private static ImageView home_icon_iv;

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

    public static void setSV() {
        scrollView.setVisibility(View.VISIBLE);
        home_icon_iv.setVisibility(View.GONE);
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
        return view;
    }

    private void binding(View view) {
        home_more1 = (TextView) view.findViewById(R.id.home_more1);
        home_more2 = (TextView) view.findViewById(R.id.home_more2);
        home_more3 = (TextView) view.findViewById(R.id.home_more3);
//        activity_picture_riv1 = (RoundAngleImageView) view.findViewById(R.id.activity_picture_riv1);
//        activity_picture_riv2 = (RoundAngleImageView) view.findViewById(R.id.activity_picture_riv2);
//        activity_name_tv1 = (TextView) view.findViewById(R.id.activity_name_tv1);
//        activity_name_tv2 = (TextView) view.findViewById(R.id.activity_name_tv2);
        advertisement1 = (Advertisement) view.findViewById(R.id.advertisement1);
        advertisement2 = (Advertisement) view.findViewById(R.id.advertisement2);
        recommend_the_virtuous_and_able_riv1 = (ImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv1);
        recommend_the_virtuous_and_able_riv2 = (ImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv2);
        recommend_the_virtuous_and_able_riv3 = (ImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv3);
        recommend_the_virtuous_and_able_riv4 = (ImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv4);
        recommend_the_virtuous_and_able_riv5 = (ImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv5);
        recommend_the_virtuous_and_able_riv6 = (ImageView) view.findViewById(R.id.recommend_the_virtuous_and_able_riv6);
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

        item_position_name_tv4 = (TextView) view.findViewById(R.id.item_position_name_tv4);
        item_position_salary_tv4 = (TextView) view.findViewById(R.id.item_position_salary_tv4);
        item_position_time_tv4 = (TextView) view.findViewById(R.id.item_position_time_tv4);
        item_position_site_tv4 = (TextView) view.findViewById(R.id.item_position_site_tv4);

        item_position_name_tv5 = (TextView) view.findViewById(R.id.item_position_name_tv5);
        item_position_salary_tv5 = (TextView) view.findViewById(R.id.item_position_salary_tv5);
        item_position_time_tv5 = (TextView) view.findViewById(R.id.item_position_time_tv5);
        item_position_site_tv5 = (TextView) view.findViewById(R.id.item_position_site_tv5);

        item_position_ll1 = (LinearLayout) view.findViewById(R.id.item_position_ll1);
        item_position_ll2 = (LinearLayout) view.findViewById(R.id.item_position_ll2);
        item_position_ll3 = (LinearLayout) view.findViewById(R.id.item_position_ll3);
        item_position_ll4 = (LinearLayout) view.findViewById(R.id.item_position_ll4);
        item_position_ll5 = (LinearLayout) view.findViewById(R.id.item_position_ll5);

        scrollView = (MyScrollView) view.findViewById(R.id.scrollView);
        home_icon_iv = (ImageView) view.findViewById(R.id.home_icon_iv);

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
        item_position_ll4.setOnClickListener(this);
        item_position_ll5.setOnClickListener(this);
//        activity_picture_riv1.setOnClickListener(this);
//        activity_picture_riv2.setOnClickListener(this);
        advertisement1.setOnClickListener(this);
        advertisement2.setOnClickListener(this);
        home_icon_iv.setOnClickListener(this);

        recommend_the_virtuous_and_able_riv1.setOnTouchListener(this);
        recommend_the_virtuous_and_able_riv2.setOnTouchListener(this);
        recommend_the_virtuous_and_able_riv3.setOnTouchListener(this);
        recommend_the_virtuous_and_able_riv4.setOnTouchListener(this);
        recommend_the_virtuous_and_able_riv5.setOnTouchListener(this);
        recommend_the_virtuous_and_able_riv6.setOnTouchListener(this);

        scrollView .setOnScrollListener(this);
        scrollView.setVisibility(View.GONE);

    }

    private LoadingDialog loadingDialog;

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

                        initActivity();

                    }else {
                        start = 1;
                        try {
                            loadingDialog.dismiss();
                        } catch (Exception a) {

                        }
                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
//                progressbar.setVisibility(View.GONE);
//                londing_tip.setVisibility(View.VISIBLE);
                start = 1;
                try {
                    loadingDialog.dismiss();
                } catch (Exception a) {

                }
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
                            bitmapUtils.display(advertisement1.getActivity_picture_iv(), AppUtilsUrl.ImageBaseUrl + activityBean.getValue().get(0).getImage());
                            bitmapUtils.display(advertisement2.getActivity_picture_iv(), AppUtilsUrl.ImageBaseUrl + activityBean.getValue().get(1).getImage());
//                            activity_name_tv1.setText(activityBean.getValue().get(0).getTitle());
//                            activity_name_tv2.setText(activityBean.getValue().get(1).getTitle());
                            advertisement1.setActivityNameTv(activityBean.getValue().get(0).getTitle());
                            advertisement2.setActivityNameTv(activityBean.getValue().get(1).getTitle());

//                            AssetManager mgr=getActivity().getAssets();//得到AssetManager
//                            Typeface tf=Typeface.createFromAsset(mgr, "fonts/asdfa.otf");//根据路径得到Typeface
//                            activity_name_tv1.setTypeface(tf);//设置字体

                        }

                    }else {
                        start = 1;
                        try {
                            loadingDialog.dismiss();
                        } catch (Exception a) {

                        }
                    }

                }

                initRecommendTheVirtuousAndAble();


            }

            @Override
            public void onFailure(HttpException e, String s) {
//                progressbar.setVisibility(View.GONE);
//                londing_tip.setVisibility(View.VISIBLE);
                start = 1;
                try {
                    loadingDialog.dismiss();
                } catch (Exception a) {

                }
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

                    }else {
                        start = 1;
                        try {
                            loadingDialog.dismiss();
                        } catch (Exception a) {

                        }
                    }

                }

                initRecommendedWork();

            }

            @Override
            public void onFailure(HttpException e, String s) {
//                progressbar.setVisibility(View.GONE);
//                londing_tip.setVisibility(View.VISIBLE);
                start = 1;
                try {
                    loadingDialog.dismiss();
                } catch (Exception a) {

                }
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
                        //
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

                        if (recruitmentListBean.getValue().get(3).getPosition() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(3).getPosition().equals("")) {
                            } else {
                                item_position_name_tv4.setText(recruitmentListBean.getValue().get(3).getPosition());
                            }
                        }
                        if (recruitmentListBean.getValue().get(3).getWorkPay() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(3).getWorkPay().equals("")) {
                            } else {
                                item_position_salary_tv4.setText(recruitmentListBean.getValue().get(3).getWorkPay());
                            }
                        }
                        if (recruitmentListBean.getValue().get(3).getWorkingTime() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(3).getWorkingTime().equals("")) {
                            } else {
                                item_position_time_tv4.setText("工作时间  " + recruitmentListBean.getValue().get(3).getWorkingTime());
                            }
                        }
                        if (recruitmentListBean.getValue().get(3).getWorkPlace() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(3).getWorkPlace().equals("")) {
                            } else {
                                item_position_site_tv4.setText("地点   " + recruitmentListBean.getValue().get(3).getWorkPlace());
                            }
                        }

                        if (recruitmentListBean.getValue().get(4).getPosition() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(4).getPosition().equals("")) {
                            } else {
                                item_position_name_tv5.setText(recruitmentListBean.getValue().get(4).getPosition());
                            }
                        }
                        if (recruitmentListBean.getValue().get(4).getWorkPay() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(4).getWorkPay().equals("")) {
                            } else {
                                item_position_salary_tv5.setText(recruitmentListBean.getValue().get(4).getWorkPay());
                            }
                        }
                        if (recruitmentListBean.getValue().get(4).getWorkingTime() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(4).getWorkingTime().equals("")) {
                            } else {
                                item_position_time_tv5.setText("工作时间  " + recruitmentListBean.getValue().get(4).getWorkingTime());
                            }
                        }
                        if (recruitmentListBean.getValue().get(4).getWorkPlace() == null) {
                        } else {
                            if (recruitmentListBean.getValue().get(4).getWorkPlace().equals("")) {
                            } else {
                                item_position_site_tv5.setText("地点   " + recruitmentListBean.getValue().get(4).getWorkPlace());
                            }
                        }

                    }else {
                        start = 1;
                        try {
                            loadingDialog.dismiss();
                        } catch (Exception a) {

                        }
                    }

                }

                start = 2;
                setSV();
                try {
                    loadingDialog.dismiss();
                } catch (Exception a) {

                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
//                progressbar.setVisibility(View.GONE);
//                londing_tip.setVisibility(View.VISIBLE);
                start = 1;
                try {
                    loadingDialog.dismiss();
                } catch (Exception a) {

                }

                dialog();
            }
        });
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


    @Override
    public void onClick(View v) {
        try {


            switch (v.getId()) {
                case R.id.home_more1:
                    TouchMore(Identification.ACTIVITY);
                    break;
                case R.id.home_more2:
                    TouchMore(Identification.TALENTS);
                    break;
                case R.id.home_more3:
                    TouchMore(Identification.HOTJOBS);
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
                case R.id.item_position_ll4:
                    Position(3);
                    break;
                case R.id.item_position_ll5:
                    Position(4);
                    break;
                case R.id.advertisement1:
                    Activity(0);
                    break;
                case R.id.advertisement2:
                    Activity(1);
                    break;
                case R.id.home_icon_iv:
                    loadingDialog = new LoadingDialog(getActivity(), "正在更新数据……");
                    loadingDialog.show();
                    initAdvertisement();
//                home_icon_iv.setVisibility(View.GONE);
                    break;
            }
        } catch (Exception e) {

        }
    }


    private void Activity(int i) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ActivityDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Detail", activityBean.getValue().get(i));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void Position(int i) {
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            click = true;
            switch (v.getId()) {
                case R.id.recommend_the_virtuous_and_able_riv1:
                    Click(recommend_the_virtuous_and_able_tv1);
                    break;
                case R.id.recommend_the_virtuous_and_able_riv2:
                    Click(recommend_the_virtuous_and_able_tv2);
                    break;
                case R.id.recommend_the_virtuous_and_able_riv3:
                    Click(recommend_the_virtuous_and_able_tv3);
                    break;
                case R.id.recommend_the_virtuous_and_able_riv4:
                    Click(recommend_the_virtuous_and_able_tv4);
                    break;
                case R.id.recommend_the_virtuous_and_able_riv5:
                    Click(recommend_the_virtuous_and_able_tv5);
                    break;
                case R.id.recommend_the_virtuous_and_able_riv6:
                    Click(recommend_the_virtuous_and_able_tv6);
                    break;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            recommend_the_virtuous_and_able_tv1.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv2.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv3.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv4.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv5.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv6.setBackgroundResource(R.drawable.click_r_nomal_shape);

        }

        return false;
    }

    private void Click(View v) {
        v.setBackgroundResource(R.drawable.click_r_focus_shape);
    }

    private boolean click = false;
    @Override
    public void onScroll(int scrollY) {
        if(click) {
            click = false;
            recommend_the_virtuous_and_able_tv1.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv2.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv3.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv4.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv5.setBackgroundResource(R.drawable.click_r_nomal_shape);
            recommend_the_virtuous_and_able_tv6.setBackgroundResource(R.drawable.click_r_nomal_shape);
        }
    }
}
