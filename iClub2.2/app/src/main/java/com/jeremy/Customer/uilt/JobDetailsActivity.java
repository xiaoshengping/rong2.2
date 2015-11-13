package com.jeremy.Customer.uilt;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.PictureAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.CommentBean;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.MyDialog;
import com.jeremy.Customer.bean.RecruitmentListBean;
import com.jeremy.Customer.bean.mine.ResumePicture;
import com.jeremy.Customer.calendar.SendParme;
import com.jeremy.Customer.calendar.ViewCountBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.MyGridView;
import com.jeremy.Customer.view.MyScrollView;
import com.jeremy.Customer.view.SpaceImageDetailActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class JobDetailsActivity extends Activity implements View.OnClickListener, MyScrollView.OnScrollListener {

    private MyGridView company_picture_production_list;
    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private TextView job_details_tv, company_details_tv;// 页卡头标
    private int currIndex = 0;// 当前页卡编号

    private static final int MAX = 6;//初始maxLine大小
    private static final int MAX1 = 8;//初始maxLine大小
    private static final int TIME = 40;//间隔时间
    private int maxLines;
    private boolean hasMesure1 = false, hasMesure2 = false, hasMesure3 = false;
    private Thread thread;
    private TextView require_button_tv, describe_button_tv;
    private TextView work_pay_tv, describe_tv, require_tv, position_tv, workingtime_and_applyjobcount_tv, companyname_tv, job_informantion_tv, contact_way_tv, address_tv, reputation_value_tv, evaluate_tv;

    private TextView company_name_tv, company_introduce_tv;
    private TextView company_introduce_button_tv;
    private TextView company_contact_information_tv;
    private MyScrollView myScrollView1, myScrollView2;
    private Button yaoyue_b;
    private LinearLayout yaoyue_ll;

    private RecruitmentListBean recruitmentListBean;

    private List<ResumePicture> resumePicture;

    private BitmapUtils bitmapUtils;

    private String states = null;//用户类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_details);

        bitmapUtils = new BitmapUtils(this);
        recruitmentListBean = (RecruitmentListBean) getIntent().getSerializableExtra("Detail");

        yaoyue_b = (Button) findViewById(R.id.yaoyue_b);
        yaoyue_ll = (LinearLayout) findViewById(R.id.yaoyue_ll);
        yaoyue_ll.setVisibility(GONE);

        yaoyue_b.setVisibility(GONE);

        InitTextView();
        InitViewPager();
    }


    /**
     * 初始化头标
     */
    private void InitTextView() {
        job_details_tv = (TextView) findViewById(R.id.job_details_tv);
        company_details_tv = (TextView) findViewById(R.id.company_details_tv);

        job_details_tv.setOnClickListener(new MyOnClickListener(0));
        company_details_tv.setOnClickListener(new MyOnClickListener(1));

    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    /**
     * * 初始化ViewPager
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        View position_details = mInflater.inflate(R.layout.item_position_details, null);
        View company_details = mInflater.inflate(R.layout.item_company_details, null);
        listViews.add(position_details);
        listViews.add(company_details);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());

        initPictureProduction(company_details);
        initPositionDetails(position_details);

    }


    //初始化职位详情
    private void initPositionDetails(View view) {
        require_button_tv = (TextView) view.findViewById(R.id.require_button_tv);
        describe_button_tv = (TextView) view.findViewById(R.id.describe_button_tv);
        describe_tv = (TextView) view.findViewById(R.id.describe_tv);
        require_tv = (TextView) view.findViewById(R.id.require_tv);
        position_tv = (TextView) view.findViewById(R.id.position_tv);
        work_pay_tv = (TextView) view.findViewById(R.id.work_pay_tv);
        workingtime_and_applyjobcount_tv = (TextView) view.findViewById(R.id.workingtime_and_applyjobcount_tv);
        companyname_tv = (TextView) view.findViewById(R.id.companyname_tv);
        job_informantion_tv = (TextView) view.findViewById(R.id.job_informantion_tv);
        contact_way_tv = (TextView) view.findViewById(R.id.contact_way_tv);
        address_tv = (TextView) view.findViewById(R.id.address_tv);
        reputation_value_tv = (TextView) view.findViewById(R.id.reputation_value_tv);
        evaluate_tv = (TextView) view.findViewById(R.id.evaluate_tv);
        myScrollView1 = (MyScrollView) view.findViewById(R.id.myScrollView1);

        position_tv.setText(recruitmentListBean.getPosition());
        work_pay_tv.setText(recruitmentListBean.getWorkPay());
        workingtime_and_applyjobcount_tv.setText("发布时间  " + recruitmentListBean.getPuttime() + "        投递数量  " + recruitmentListBean.getApplyjobCount());
        companyname_tv.setText(recruitmentListBean.getCompanyName());
        String a, b, c, d;
        a = recruitmentListBean.getWorkPlace();
        b = recruitmentListBean.getRecruitingNumbers().equals("") ? "若干" : recruitmentListBean.getRecruitingNumbers();
        c = recruitmentListBean.getWorkingTime().equals("") ? "待定" : recruitmentListBean.getWorkingTime();
        d = recruitmentListBean.getWorkingHours().equals("") ? "待定" : recruitmentListBean.getWorkingHours();
        job_informantion_tv.setText(a + "\n" + b + "\n" + c + "\n" + d);
        describe_tv.setText(recruitmentListBean.getJobInfo());
        require_tv.setText(recruitmentListBean.getJobRequirements());
        reputation_value_tv.setText(recruitmentListBean.getIntegrity() + "\n" + recruitmentListBean.getAuthenticity() + "\n" + recruitmentListBean.getTransactionRecord());

        //初始化职位描述高度
        ViewTreeObserver viewTreeObserver = describe_tv.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                //只需要获取一次就可以了
                if (!hasMesure1) {
                    //这里获取到完全展示的maxLine
                    maxLines = describe_tv.getLineCount();
                    //设置maxLine的默认值，这样用户看到View就是限制了maxLine的TextView
                    describe_tv.setMaxLines(MAX);
                    hasMesure1 = true;
                }

                return true;
            }
        });
        //初始化职位要求高度
        ViewTreeObserver viewTreeObserver1 = require_tv.getViewTreeObserver();
        viewTreeObserver1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                //只需要获取一次就可以了
                if (!hasMesure2) {
                    //这里获取到完全展示的maxLine
                    maxLines = require_tv.getLineCount();
                    //设置maxLine的默认值，这样用户看到View就是限制了maxLine的TextView
                    require_tv.setMaxLines(MAX);
                    hasMesure2 = true;
                }

                return true;
            }
        });

        //获取评论
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getComment(recruitmentListBean.getPersonid(), "getCommentByBePerson.action?personid=", 0), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<CommentBean> commentBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<CommentBean>>() {
                    });
                    if (commentBean.getState().equals("success")) {
                        if (commentBean.getTotal() > 0) {
                            evaluate_tv.setText(commentBean.getTotal() + "位商家评论过");
                            evaluate_tv.setOnClickListener(JobDetailsActivity.this);
                        } else {
                            evaluate_tv.setText("还没有商家进行评论哦~");
                            evaluate_tv.setTextColor(0xff777778);
                        }
                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
//                reputation_tipe_tv.setTextColor(0xffDEDDE2);
//                reputation_tipe_tv.setText("网路异常，请稍后再试！");
            }
        });


        require_button_tv.setOnClickListener(this);
        describe_button_tv.setOnClickListener(this);
//        evaluate_tv.setOnClickListener(this);
        myScrollView1.setOnScrollListener(this);

    }


    //初始化公司详情
    private void initPictureProduction(View view) {
        company_name_tv = (TextView) view.findViewById(R.id.company_name_tv);
        company_introduce_tv = (TextView) view.findViewById(R.id.company_introduce_tv);
        company_introduce_button_tv = (TextView) view.findViewById(R.id.company_introduce_button_tv);
        company_contact_information_tv = (TextView) view.findViewById(R.id.company_contact_information_tv);
        myScrollView2 = (MyScrollView) view.findViewById(R.id.myScrollView2);

        company_name_tv.setText(recruitmentListBean.getCompanyName());
        company_introduce_tv.setText(recruitmentListBean.getBEcompanyInfo());
        company_contact_information_tv.setText("***********\n********\n" + recruitmentListBean.getWeb() + "\n" + recruitmentListBean.getAddress());

        //初始化公司简介高度
        ViewTreeObserver viewTreeObserver1 = company_introduce_tv.getViewTreeObserver();
        viewTreeObserver1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                //只需要获取一次就可以了
                if (!hasMesure3) {
                    //这里获取到完全展示的maxLine
                    maxLines = company_introduce_tv.getLineCount();
                    //设置maxLine的默认值，这样用户看到View就是限制了maxLine的TextView
                    company_introduce_tv.setMaxLines(MAX1);
                    hasMesure3 = true;
                }

                return true;
            }
        });

        company_introduce_button_tv.setOnClickListener(this);
        myScrollView2.setOnScrollListener(this);

//        company_picture_production_list = (MyGridView) view.findViewById(R.id.company_picture_production_list);
//        List<ResumePicture> resumePicture = recruitmentListBean.getBEicon();
//        PictureAdapter pictureAdapter = new PictureAdapter(this,resumePicture);
//        company_picture_production_list.setAdapter(pictureAdapter);

        company_picture_production_list = (MyGridView) view.findViewById(R.id.company_picture_production_list);
        resumePicture = recruitmentListBean.getBEpicture();
//        Toast.makeText(this,"asdff", Toast.LENGTH_LONG).show();
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        PictureAdapter pictureAdapter = new PictureAdapter(this, resumePicture, (int) (width / 3) - Identification.dip2px(this, 6));
        company_picture_production_list.setAdapter(pictureAdapter);
        company_picture_production_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JobDetailsActivity.this, SpaceImageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList) resumePicture);
                bundle.putInt("num", 2);
                bundle.putInt("MaxNum", resumePicture.size());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.spaceimagedetail_in, R.anim.out_to_not);
            }
        });

    }

    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
//        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        int one = 100;
        int two = 200;

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
//                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
//                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                    } else if (currIndex == 2) {
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                    } else if (currIndex == 1) {
                    }
                    break;
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            ArgbEvaluator evaluator = new ArgbEvaluator();
            if (arg1 != 0) {
                int evaluate = (Integer) evaluator.evaluate(arg1, 0XFF9373ee, 0XFF777778);
                job_details_tv.setTextColor(evaluate);
                job_details_tv.setTextSize(18 - (arg1 * 2));
                int evaluates = (Integer) evaluator.evaluate(arg1, 0XFF777778, 0XFF9373ee);
                company_details_tv.setTextSize(16 + (arg1 * 2));
                company_details_tv.setTextColor(evaluates);
            }

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private MyDialog dialog2;

    //提示框
    private void dialog() {
        dialog2 = new MyDialog(this, Identification.LOGINPROMPT, Identification.LOGONTOTHETALENT);
        dialog2.setDetermine(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobDetailsActivity.this, LoginActivity.class);
                startActivity(intent);
//                recommend_list.setVisibility(View.GONE);
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
    }

    /**
     * 打开TextView
     */
    @SuppressLint("HandlerLeak")
    private void toggle(final TextView view) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int lines = msg.what;
                //这里接受到消息，让后更新TextView设置他的maxLine就行了
                view.setMaxLines(lines);
                view.postInvalidate();
            }
        };
        if (thread != null)
            handler.removeCallbacks(thread);

        thread = new Thread() {
            @Override
            public void run() {
                int count = MAX;
                while (count++ <= maxLines) {
                    //每隔20mms发送消息
                    Message message = new Message();
                    message.what = count;
                    handler.sendMessage(message);

                    try {
                        Thread.sleep(TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                super.run();
            }
        };
        thread.start();
    }

    private int ss;
    private boolean yaoyue = false;
    private boolean ing = false;

    @Override
    public void onScroll(int scrollY) {
        //邀约动画
        if (ss - scrollY > 20) {//向下
            ss = scrollY;
            if (!yaoyue && !ing) {
                ing = true;
                yaoyue_ll.setVisibility(View.VISIBLE);
                yaoyue_b.setVisibility(View.VISIBLE);
                Animation animation = null;
                animation = new TranslateAnimation(0, 0, Identification.dip2px(this, 60), 0);
                animation.setDuration(100);
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ing = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                yaoyue_ll.startAnimation(animation);

            }
            yaoyue = true;
        } else {
            if (scrollY - ss > 20) {//向上
                ss = scrollY;
                if (yaoyue && !ing) {
                    ing = true;
                    Animation animation = null;
                    animation = new TranslateAnimation(0, 0, 0, Identification.dip2px(this, 60));
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ing = false;
                            yaoyue_b.setVisibility(GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    yaoyue_ll.startAnimation(animation);
                }
                yaoyue = false;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.describe_button_tv:
                describe_button_tv.setVisibility(GONE);
                toggle(describe_tv);
                break;
            case R.id.require_button_tv:
                require_button_tv.setVisibility(GONE);
                toggle(require_tv);
                break;
            case R.id.company_introduce_button_tv:
                company_introduce_button_tv.setVisibility(GONE);
                toggle(company_introduce_tv);
                break;
            case R.id.evaluate_tv:
                Intent intent = new Intent();
                intent.setClass(JobDetailsActivity.this, RecommenListActivity.class);
                intent.putExtra("Ident", Identification.COMMENT);
                intent.putExtra("URL", "getCommentByBePerson.action?personid=");
                intent.putExtra("ID", recruitmentListBean.getPersonid());
                startActivity(intent);
                break;
        }
    }

    private boolean register = false;//登录状态

    public void send(View v) {

        //获取登录状态
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        states = null;
        while (cursor.moveToNext()) {
            states = cursor.getString(4);

        }
        if (TextUtils.isEmpty(states) || states.equals("1")) {
            dialog();
        } else if (states.equals("2")) {
            Intent intent = new Intent(JobDetailsActivity.this, ChooseAResumeActivity.class);
            intent.putExtra("jobId", recruitmentListBean.getJobId());
            startActivityForResult(intent, 0);
        } else if (states.equals("3")) {
            Toast.makeText(JobDetailsActivity.this, "此账号为非人才用户，无法进行投递操作", Toast.LENGTH_LONG).show();
        }



    }

    private Bundle bundle;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bundle = data.getExtras();

//        Bundle bundle = new Bu/ndle();
//            bundle.putInt("UserType",2);
//            bundle.putInt("Personid", recruitmentListBean.getPersonid());
//            bundle.putSerializable("Detail", recruitmentListBean);
//        bundle.putInt("Resumeid",talentValueBean.getResumeid());
//        Toast.makeText(this, talentValueBean.getPersonid() + "", Toast.LENGTH_LONG).show();
//            intent.putExtras(bundle);
//            startActivity(intent);

        //投递
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getSend(recruitmentListBean.getJobId(), bundle.getInt("ResumeId")), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    SendParme<ViewCountBean> viewCountBean = JSONObject.parseObject(result, new TypeReference<SendParme<ViewCountBean>>() {
                    });
                    if (viewCountBean.getState().equals("success")) {
                        dialog(5);
//                        if (commentBean.getTotal() > 0) {
//                            comment_button_tv.setText(commentBean.getTotal() + "位商家评论过");
//                            comment_button_tv.setOnClickListener(TalentsDetailsActivity.this);
//                        } else {
//                            comment_button_tv.setText("还没有商家进行评论哦~");
//                            comment_button_tv.setTextColor(0xff777778);
//                        }
                    }else {
                        dialog(6);
                    }

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog(6);
//                reputation_tipe_tv.setTextColor(0xffDEDDE2);
//                reputation_tipe_tv.setText("网路异常，请稍后再试！");
            }
        });


    }

    //提示框
    private void dialog(int tipsTyp) {

        switch (tipsTyp) {

            case 5://邀约成功
                dialog2 = new MyDialog(this, Identification.MAINTAINORREMOVE, Identification.FREEDOM,"已成功向商家<"+recruitmentListBean.getCompanyName()+">投递简历<"+bundle.getString("ResumeName")+">");
                dialog2.setDetermine(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
//                        Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                recommend_list.setVisibility(View.GONE);
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
        }

    }

    public void back(View v) {
        finish();
    }

}

