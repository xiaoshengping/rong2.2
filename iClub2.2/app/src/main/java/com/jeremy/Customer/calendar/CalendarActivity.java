package com.jeremy.Customer.calendar;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.MyDialog;
import com.jeremy.Customer.uilt.SQLhelper;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.MyTitleBar;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/2.
 */
public class CalendarActivity extends Activity implements View.OnClickListener, CalendarCard.OnCellClickListener {
    private ViewPager mViewPager;
    private int mCurrentIndex = 0;
    private CalendarCard[] mShowViews;
    private CalendarViewAdapter<CalendarCard> adapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    private LinearLayout preImgBtn, nextImgBtn;
    private TextView monthText;
    private ImageButton closeImgBtn;
    private TextView calendar_tips_tv, calendar_tips_tips_tv;
    private LinearLayout calendar_tips_ll;
    private Button calendar_confirm_b;
    private MyTitleBar mytitle;

    private DbUtils dbu;
    private Day user;
    //    private TextView tv;
    private List<Day> list;
    public static int userType = 2;//1、商家 ，2、个人
    private String id = "";
    //    public static List<DayBean> dayBeanslist = new ArrayList<DayBean>();
    public static List<DayBean> todayBeanslist = new ArrayList<DayBean>();
    public static List<DayBean> nextdayBeanslist = new ArrayList<DayBean>();

    public int PASTDUE = 1;//已过期
    public int OFFER = 2;//邀约
    public int BEDEFEATED = 3;//加载失败
    public int ING = 4;//加载中
    public int SUCCEED = 5;//邀约成功、
    public int FAILURE = 6;//邀约失败
    public int NULL = 7;//无公司名

    private int tipsType;
    private int resumeid;
    private String uid = "13632255697";
    private String companyName;
    private int month = 0;
    private boolean toMonth = true;
    private boolean touch = true;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar);

        Bundle bundle = getIntent().getExtras();
        userType = bundle.getInt("userType");
        id = bundle.getString("Personid");
        resumeid = bundle.getInt("Resumeid");

        mytitle = (MyTitleBar)findViewById(R.id.mytitle);

        //初始化界面状态
        init();
        loadingDialog = new LoadingDialog(this, "");
        loadingDialog.show();
        if (userType == 1) {
            mytitle.setTextViewText("邀约日程");
            initRoute(DateUtil.getYear() + "-" + (DateUtil.getMonth() > 9 ? DateUtil.getMonth() : ("0" + DateUtil.getMonth())), true);

        } else {
            mytitle.setTextViewText("修改日程");
            initTalentsRoute(DateUtil.getYear() + "-" + (DateUtil.getMonth() > 9 ? DateUtil.getMonth() : ("0" + DateUtil.getMonth())), true);

        }

    }

    private void binding() {
        mViewPager = (ViewPager) this.findViewById(R.id.vp_calendar);
        preImgBtn = (LinearLayout) this.findViewById(R.id.btnPreMonth);
        nextImgBtn = (LinearLayout) this.findViewById(R.id.btnNextMonth);
        monthText = (TextView) this.findViewById(R.id.tvCurrentMonth);
        calendar_tips_ll = (LinearLayout) this.findViewById(R.id.calendar_tips_ll);
        calendar_tips_tips_tv = (TextView) this.findViewById(R.id.calendar_tips_tips_tv);
        calendar_confirm_b = (Button) this.findViewById(R.id.calendar_confirm_b);
//        closeImgBtn = (ImageButton) this.findViewById(R.id.btnClose);
        preImgBtn.setOnClickListener((View.OnClickListener) this);
        nextImgBtn.setOnClickListener((View.OnClickListener) this);
//        closeImgBtn.setOnClickListener((View.OnClickListener) this);
    }

    private void init() {
        binding();
        i = "";
        if (userType == 2) {
            calendar_tips_ll.setVisibility(View.INVISIBLE);
            calendar_tips_tips_tv.setVisibility(View.INVISIBLE);
//            calendar_confirm_b.setVisibility(View.INVISIBLE);
        }

        //获取商家账号
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        uid = null;
        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
            companyName = cursor.getString(6);
        }

        if (companyName.equals("") && userType == 1) {
            calendar_confirm_b.setBackgroundResource(R.drawable.calendar_button_n_shape);
            touch = false;
            dialog(NULL);
        } else {
            calendar_confirm_b.setBackgroundResource(R.drawable.calendar_button_shape);
            touch = true;
        }

//        List<DayBean> list = new ArrayList<DayBean>();
//        DayBean dayBean = new DayBean();
//        dayBean.setDay("2015-08-");
//        dayBean.setStatus("0");
//        list.add(dayBean);
//        dayBeanslist = list;
//        monthText

    }


    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                measureDirection(position);
                updateCalendarView(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        LayoutSizes ls = new LayoutSizes();
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ls.layoutHeigh(screenWidth, 313, 269));
        layoutParams.setMargins(0, 0, 0, 0);
        mViewPager.setLayoutParams(layoutParams);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPreMonth:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                break;
            case R.id.btnNextMonth:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
//                modificationRoute(DateUtil.getYear() + "-" + (DateUtil.getMonth() > 9 ? DateUtil.getMonth() : ("0" + DateUtil.getMonth())));
                break;
//            case R.id.btnClose:
//                finish();
//                break;
            default:
                break;
        }
    }

    public static String i = "";
    private boolean leisure = true;


    @Override
    public void clickDate(CustomDate date) {

//        if(date){
//        calendar_confirm_b.setText(date.getMonth()+"");

        if (date.getMonth() == mou) {
            String str = date.year + "-" + (date.month > 9 ? date.month : ("0" + date.month)) + "-" + (date.day > 9 ? date.day : ("0" + date.day));

            if (DateUtil.isCurrentMonth(date) && date.getDay() <= DateUtil.getCurrentMonthDay()) {
                dialog(PASTDUE);
            } else {

                if (userType == 1) {
//            tv.setText(dayBeanslist.get(date.day-1).getDay());
                    if (tipsType == -1) {
                        if (toMonth) {
                            if (todayBeanslist.get(date.day - 1).getStatus().equals("1") && todayBeanslist.get(date.day - 1).getDay().equals(str)) {

                            } else {
                                i = str;
                            }
                        } else {
                            if (nextdayBeanslist.get(date.day - 1).getStatus().equals("1") && nextdayBeanslist.get(date.day - 1).getDay().equals(str)) {

                            } else {
                                i = str;
                            }
                        }
                    } else {
                        if (tipsType == BEDEFEATED) {
                            dialog(BEDEFEATED);
                        } else {
                            dialog(ING);
                        }
                        //显示获取失败
                    }


                } else if (userType == 2) {


                    if (tipsType == -1) {
                        if (toMonth) {
                            if (todayBeanslist.get(date.day - 1).getStatus().equals("1")) {
                                todayBeanslist.get(date.day - 1).setStatus("0");
//                            modificationRoute(DateUtil.getYear() + "-" + (DateUtil.getMonth() > 9 ? DateUtil.getMonth() : ("0" + DateUtil.getMonth())));
                            } else {
                                todayBeanslist.get(date.day - 1).setStatus("1");
                            }
                        } else {
                            if (nextdayBeanslist.get(date.day - 1).getStatus().equals("1")) {
                                nextdayBeanslist.get(date.day - 1).setStatus("0");
//                            modificationRoute(DateUtil.getYear() + "-" + (DateUtil.getMonth() > 9 ? DateUtil.getMonth() : ("0" + DateUtil.getMonth())));
                            } else {
                                nextdayBeanslist.get(date.day - 1).setStatus("1");
                            }
                        }

                    } else {
                        if (tipsType == BEDEFEATED) {
                            dialog(BEDEFEATED);
                        } else {
                            dialog(ING);
                        }
                        //显示获取失败
                    }


//                try {
//                    list = dbu.findAll(Selector.from(Day.class).where(WhereBuilder.b("id", "!=", -1).and("day", "=", date.year + "-" + (date.month > 9 ? date.month : ("0" + date.month)) + "-" + (date.day > 9 ? date.day : ("0" + date.day)))).orderBy("id").limit(10));
//
//
//                } catch (DbException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                if (list == null || list.size() == 0) {
//                    user.setYear(date.year);
//                    user.setMonth(date.month);
//                    user.setDay(date.year + "-" + (date.month > 9 ? date.month : ("0" + date.month)) + "-" + (date.day > 9 ? date.day : ("0" + date.day)));
//                    //			db.save(user);
//                    try {
//                        dbu.save(user);
//                    } catch (DbException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                } else {
//
//                    try {
//                        dbu.deleteAll(list);//(Selector.from(Day.class).where(WhereBuilder.b("id", "<", 54).and("day","=","2015-06-25")).orderBy("id").limit(10));
//                    } catch (DbException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }

                }
            }

            updateCalendarView(7);
        }

    }


    //    private String mou;
    private int mou;

    @Override
    public void changeDate(CustomDate date) {
        monthText.setText(date.year + "年" + date.month + "月");
        mou = date.month;
        if (DateUtil.getMonth() == date.getMonth()) {
            preImgBtn.setVisibility(View.INVISIBLE);
            nextImgBtn.setVisibility(View.VISIBLE);
            toMonth = true;
//            modificationRoute(date.year + "-" + (DateUtil.getMonth() > 9 ? DateUtil.getMonth() : ("0" + DateUtil.getMonth())));
        } else {
            preImgBtn.setVisibility(View.VISIBLE);
            nextImgBtn.setVisibility(View.INVISIBLE);
            toMonth = false;
        }
//        if(userType==1) {
//        if (month != date.month) {
//            initRoute(date.year + "-" + (date.month > 9 ? date.month : ("0" + date.month)));
//            month=date.month;
//        }

//        }else if(userType==2){

//        }
    }

    /**
     * 计算方向
     *
     * @param arg0
     */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    // 更新日历视图
    private void updateCalendarView(int arg0) {
        mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
    }


    //商家进入初始化日历
    private void initRoute(String yearAndMonth, final boolean tomonth) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getRoute(id, yearAndMonth), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<DayBean> dayBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<DayBean>>() {
                    });
                    if (dayBean.getState().equals("success")) {

                        if (tomonth) {
                            todayBeanslist = dayBean.getValue();
                            initRoute(DateUtil.getYear() + "-" + ((DateUtil.getMonth() + 1) > 9 ? (DateUtil.getMonth() + 1) : ("0" + (DateUtil.getMonth() + 1))), false);
                        } else {
                            loadingDialog.dismiss();
                            nextdayBeanslist = dayBean.getValue();

                            new Handler().postDelayed(new Runnable() {

                                public void run() {

                                    CalendarCard[] views = new CalendarCard[3];
                                    for (int i = 0; i < 3; i++) {
                                        views[i] = new CalendarCard(CalendarActivity.this, CalendarActivity.this);
                                    }
                                    adapter = new CalendarViewAdapter<CalendarCard>(views);
                                    setViewPager();
                                }

                            }, 300);
                        }
//                            updateCalendarView(7);
                        tipsType = -1;

                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                loadingDialog.dismiss();
                tipsType = BEDEFEATED;
                dialog(BEDEFEATED);
            }
        });

    }

    //人才进入初始化日历
    private void initTalentsRoute(String yearAndMonth, final boolean tomon) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getTalentsRoute(uid, yearAndMonth), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = null;
                result = responseInfo.result;

                if (result != null) {
                    ArtistParme<DayBean> dayBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<DayBean>>() {
                    });
                    if (dayBean.getState().equals("success")) {
                        if (tomon) {
                            todayBeanslist = dayBean.getValue();
                            initTalentsRoute(DateUtil.getYear() + "-" + ((DateUtil.getMonth() + 1) > 9 ? (DateUtil.getMonth() + 1) : ("0" + (DateUtil.getMonth() + 1))), false);
//                            oldtodayBeanslist = dayBean.getValue();
                        } else {
                            nextdayBeanslist = dayBean.getValue();
//                            oldnextdayBeanslist = dayBean.getValue();
                            new Handler().postDelayed(new Runnable() {

                                public void run() {

                                    CalendarCard[] views = new CalendarCard[3];
                                    for (int i = 0; i < 3; i++) {
                                        views[i] = new CalendarCard(CalendarActivity.this, CalendarActivity.this);
                                    }
                                    adapter = new CalendarViewAdapter<CalendarCard>(views);
                                    setViewPager();
                                    tipsType = -1;

                                }

                            }, 300);

                            loadingDialog.dismiss();

//                            CalendarCard[] views = new CalendarCard[3];
//                            for (int i = 0; i < 3; i++) {
//                                views[i] = new CalendarCard(CalendarActivity.this, CalendarActivity.this);
//                            }
//                            adapter = new CalendarViewAdapter<CalendarCard>(views);
//                            setViewPager();
//                            mShowViews[1].update();
                        }

//                            updateCalendarView(7);
                        tipsType = -1;

                    }

                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog(BEDEFEATED);
            }
        });

    }

    //人才修改行程
    private void modificationRoute(String yearAndMonth, final boolean tomonmod) {
        String s = "";
        if (tomonmod) {
            for (int i = 0; i < todayBeanslist.size(); i++) {
                if (todayBeanslist.get(i).getStatus().equals("1")) {
                    if ((i + 1) != todayBeanslist.size()) {
                        s = s + todayBeanslist.get(i).getDay() + ",";
                    } else {
                        s = s + todayBeanslist.get(i).getDay();
                    }

                }
            }

        } else {
            for (int i = 0; i < nextdayBeanslist.size(); i++) {
                if (nextdayBeanslist.get(i).getStatus().equals("1")) {
                    if ((i + 1) != nextdayBeanslist.size()) {
                        s = s + nextdayBeanslist.get(i).getDay() + ",";
                    } else {
                        s = s + nextdayBeanslist.get(i).getDay();
                    }

                }
            }
        }

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getModificationRoute(uid, s, yearAndMonth), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    SendParme<ViewCountBean> viewCountBean = JSONObject.parseObject(result, new TypeReference<SendParme<ViewCountBean>>() {
                    });
                    if (viewCountBean.getState().equals("success")) {
                        ViewCountBean viewCountData = JSONObject.parseObject(viewCountBean.getValue(), ViewCountBean.class);

                        if (viewCountData.getMessage().equals("success")) {
                            if(tomonmod) {
                                modificationRoute(DateUtil.getYear() + "-" + ((DateUtil.getMonth() + 1) > 9 ? (DateUtil.getMonth() + 1) : ("0" + (DateUtil.getMonth() + 1))), false);
                            }else {
                                dialog("日程修改成功");
                            }
                        } else if (viewCountData.getMessage().equals("failure")) {
                            dialog("日程修改失败，请稍后重试");
                        } else {
                            dialog("日程修改失败，请稍后重试");
                        }
                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog(BEDEFEATED);
            }
        });
    }
    private void dialog(String str) {
        dialog2 = new MyDialog(this, Identification.TOOLTIP, Identification.FREEDOM,str);
        dialog2.setDetermine(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                recommend_list.setVisibility(View.GONE);
                dialog2.dismiss();

            }
        });

        dialog2.show();
    }

    private boolean ing = false;

    //邀约
    public void Invite() {
        ing = true;
        loadingDialog = new LoadingDialog(this, "");
        loadingDialog.show();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getInvite(i, uid, resumeid), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    SendParme<ViewCountBean> viewCountBean = JSONObject.parseObject(result, new TypeReference<SendParme<ViewCountBean>>() {
                    });
//                    Toast.makeText(CalendarActivity.this, i, Toast.LENGTH_LONG).show();
                    if (viewCountBean.getState().equals("success")) {
                        ViewCountBean viewCountData = JSONObject.parseObject(viewCountBean.getValue(), ViewCountBean.class);
                        loadingDialog.dismiss();
                        if (viewCountData.getMessage().equals("success")) {
                            dialog(SUCCEED);
//                            Toast.makeText(CalendarActivity.this, "邀约成功", Toast.LENGTH_LONG).show();

                        } else if (viewCountData.getMessage().equals("failure")) {
                            loadingDialog.dismiss();
                            dialog(FAILURE);
//                            Toast.makeText(CalendarActivity.this, "邀约失败", Toast.LENGTH_LONG).show();
                        } else {

//                                Toast.makeText(CalendarActivity.this, "邀约失败", Toast.LENGTH_LONG).show();
                        }
                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                loadingDialog.dismiss();
                dialog(FAILURE);
            }
        });
    }


    private MyDialog dialog2;

    //提示框
    private void dialog(int tipsTyp) {

        switch (tipsTyp) {
            case 1://过期
                Toast.makeText(CalendarActivity.this, "此日期已过，请重新选择", Toast.LENGTH_LONG).show();
                break;
            case 2://邀约
                dialog2 = new MyDialog(this, Identification.MAINTAINORREMOVE, Identification.FREEDOM, "确定邀约吗？");
                dialog2.setDetermine(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Invite();
                        dialog2.dismiss();
                    }
                });
                dialog2.setCancel(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });

                dialog2.show();

                break;
            case 3://加载失败
                dialog2 = new MyDialog(this, Identification.TOOLTIP, Identification.NETWORKANOMALY);
                dialog2.setDetermine(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        dialog2.dismiss();
                    }
                });

                dialog2.show();
                break;
            case 5://邀约成功
                dialog2 = new MyDialog(this, Identification.TOOLTIP, Identification.OFFER);
                dialog2.setDetermine(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        dialog2.dismiss();
                    }
                });

                dialog2.show();
                break;
            case 6://邀约失败
                dialog2 = new MyDialog(this, Identification.TOOLTIP, Identification.NETWORKANOMALY);
                dialog2.setDetermine(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                recommend_list.setVisibility(View.GONE);
                        dialog2.dismiss();
                    }
                });

                dialog2.show();
                break;
            case 7://无公司名
//                dialog2 = new MyDialog(this, Identification.AFFIRM, Identification.OFFER);
//                dialog2.setDetermine(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
//                        startActivity(intent);
////                recommend_list.setVisibility(View.GONE);
//                        dialog2.dismiss();
//                    }
//                });
//                dialog2.setCancel(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog2.dismiss();
//                    }
//                });
//
//                dialog2.show();
                break;
        }


//        Toast.makeText(CalendarActivity.this, "提示", Toast.LENGTH_LONG).show();

        /*dialog2 = new JobDetailsDialog(this,tipsTyp);
//        EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
        dialog2.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipsTyp==OFFER) {
//                    calendar_confirm_b.setBackgroundResource(R.drawable.calendar_button_n_shape);
//                    i = "ing";
                    Invite();

                }
                if(tipsTyp==SUCCEED){
                    finish();
                }
                dialog2.dismiss();
            }
        });
        dialog2.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

//        Window window = dialog2.getWindow();
//        window.setGravity(Gravity.TOP);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        // 设置透明度为0.3
//        lp.alpha = 0.6f;
//        window.setAttributes(lp);

        dialog2.show();*/
    }

    public void offer(View v) {
        if (touch) {
            if (userType == 1) {
                if (i.equals("")) {
                    Toast.makeText(CalendarActivity.this, "未选择邀约日期", Toast.LENGTH_LONG).show();
                } else {
//                    if (!ing) {
                    dialog(OFFER);
//                    } else {
//                        dialog(ING);
//                    }

                }
            } else {
                modificationRoute(DateUtil.getYear() + "-" + (DateUtil.getMonth() > 9 ? DateUtil.getMonth() : ("0" + DateUtil.getMonth())), true);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            modificationRoute(DateUtil.getYear() + "-" + (DateUtil.getMonth() > 9 ? DateUtil.getMonth() : ("0" + DateUtil.getMonth())), true);
//            modificationRoute(DateUtil.getYear() + "-" + ((DateUtil.getMonth() + 1) > 9 ? (DateUtil.getMonth() + 1) : ("0" + (DateUtil.getMonth() + 1))),false);
//            toresult="";
//            nextresult="";
//            ArtistParme<DayBean> toresultdayBean = JSONObject.parseObject(toresult, new TypeReference<ArtistParme<DayBean>>() {
//            });
//            ArtistParme<DayBean> nextresultdayBean = JSONObject.parseObject(nextresult, new TypeReference<ArtistParme<DayBean>>() {
//            });
//            todayBeanslist = toresultdayBean.getValue();
//            nextdayBeanslist = nextresultdayBean.getValue();
//            if(todayBeanslist==null){
//                toresult="";
//                nextresult="";
//            }else {
            if (userType == 2) {
//                toresult="";
//                nextresult="";

                Toast.makeText(CalendarActivity.this, "取消日程修改", Toast.LENGTH_LONG).show();

            } else {

            }
            finish();
            return true;
        }
        return false;

    }

    public void back(View v) {
        CalendarActivity.this.finish();
    }


}