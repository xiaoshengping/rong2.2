package com.jeremy.Customer.uilt;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.MusicAdapter;
import com.jeremy.Customer.adapter.PictureAdapter;
import com.jeremy.Customer.adapter.VideoAdapter;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.TalentValueBean;
import com.jeremy.Customer.bean.Utility;
import com.jeremy.Customer.bean.mine.ResumeMovie;
import com.jeremy.Customer.bean.mine.ResumeMusic;
import com.jeremy.Customer.bean.mine.ResumePicture;
import com.jeremy.Customer.calendar.CalendarActivity;
import com.jeremy.Customer.myActivity;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.MusicActivity;
import com.jeremy.Customer.view.MyGridView;
import com.jeremy.Customer.view.MyScrollView;
import com.jeremy.Customer.view.SpaceImageDetailActivity;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;


public class TalentsDetailsActivity extends Activity implements View.OnClickListener, MyScrollView.OnScrollListener {

    private ListView video_production_list, music_production_list;
    private MyGridView picture_production_list;
    private TextView invite_a_few_tv;
    //    private RelativeLayout talents_basics_rl;
    private ImageView talents_back_iv;
    //    private TextView talents_back_baffle_tv;
    private LinearLayout floating_collar;
    //    private MyScrollView myScrollView;
    private TextView personal_data_button_tv, individual_works_button_tv;
    private TextView personal_data_button_tv1, individual_works_button_tv1;
    private LinearLayout individual_works_ll, personal_data_ll;
    private ImageView talents_hear_iv;
    private TextView talents_name_tv;
    private ImageView talents_sex_iv;
    private TextView talents_age_tv, talents_site_tv, talents_profession_tv;
    private TextView talents_self_introduction_tv, talents_work_experience_tv, talents_reputation_tv;
    private TextView comment_button_tv;

    private int mScrollY = 0;
    private int mScrollY1 = 0;
    private int mScrollY2 = 0;
    private int oldScrollY = 0;
    private int oldScrollY1 = 0;

    private Bitmap bitmap;

    private BitmapUtils bitmapUtils;

    private TalentValueBean talentValueBean;

    private Thread thread;
    private static final int MAX = 6;//初始maxLine大小
    private int maxLines;
    private static final int TIME = 40;//间隔时间
    private boolean hasMesure1 = false, hasMesure2 = false;
    private TextView self_introduction_button_tv, work_experience_button_tv;

    private String states = null;//用户类型

    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private int currIndex = 0;// 当前页卡编号

    private TextView tooltips_tv;

    private MyScrollView myScrollView1, myScrollView2;
    private TextView interspace1_tv, interspace2_tv;
    private LinearLayout head_ll;

    private int head_height;
    private int button_height;

    private Button fanhui_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talents_details);

        bitmapUtils = new BitmapUtils(this);
        talentValueBean = (TalentValueBean) getIntent().getSerializableExtra("Detail");

//        bitmap = getHttpBitmap(AppUtilsUrl.ImageBaseUrl + talentValueBean.getUsericon());

        InitTextView();
        InitViewPager();

    }

/*


    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }
*/



    /**
     * 初始化头标
     */
    private float w;
    private int height = 0;
    private int width;
    private int tooltips_width;

    private void InitTextView() {
        personal_data_button_tv = (TextView) findViewById(R.id.personal_data_button_tv);
        individual_works_button_tv = (TextView) findViewById(R.id.individual_works_button_tv);
        tooltips_tv = (TextView) findViewById(R.id.tooltips_tv);

        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        ViewTreeObserver vto = tooltips_tv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                tooltips_width = tooltips_tv.getMeasuredWidth();
//                tooltips_tv.setText(tooltips_width + "");
                w = (width - (tooltips_tv.getMeasuredWidth() * 2)) / 4;
                tooltips_tv.setTranslationX(w);
                return true;
            }
        });


        personal_data_button_tv.setOnClickListener(new MyOnClickListener(0));
        individual_works_button_tv.setOnClickListener(new MyOnClickListener(1));

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
        mPager = (ViewPager) findViewById(R.id.vtPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        View item_talents_personal_data = mInflater.inflate(R.layout.item_talents_personal_data, null);
        View item_talents_personage_production = mInflater.inflate(R.layout.item_talents_personage_production, null);
        listViews.add(item_talents_personal_data);
        listViews.add(item_talents_personage_production);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());

        init(item_talents_personal_data, item_talents_personage_production);
        initVideoProduction(item_talents_personage_production);
        initMusicProduction(item_talents_personage_production);
        initPictureProduction(item_talents_personage_production);

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
        int one = (int) w;
        int two = (int) w;

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (mScrollY1 > button_height && mScrollY < button_height) {
//                        head_top = true;
                    } else {
//                        head_top = false;
                    }
                    ss = mScrollY1;
                    oldScrollY = mScrollY1;
                    oldScrollY1 = mScrollY;
                    animation = new TranslateAnimation((tooltips_width + (2 * w)), one, 0, 0);
                    personal_data_button_tv.setTextColor(0xff8e44ad);
                    individual_works_button_tv.setTextColor(0xff282a31);
                    mScrollY2 = mScrollY;
                    break;
                case 1:
                    if (mScrollY2 > button_height && mScrollY < button_height) {
//                        head_top = true;
                    } else {
//                        head_top = false;
                    }
                    ss = mScrollY2;
                    oldScrollY = mScrollY2;
                    oldScrollY1 = mScrollY;
                    animation = new TranslateAnimation(one, (tooltips_width + (2 * w)), 0, 0);
                    personal_data_button_tv.setTextColor(0xff282a31);
                    individual_works_button_tv.setTextColor(0xff8e44ad);
                    mScrollY1 = mScrollY;
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            tooltips_tv.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            ArgbEvaluator evaluator = new ArgbEvaluator();

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }


    //初始化界面
    private void init(View a, View b) {

        //获取登录状态
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        states = null;
        while (cursor.moveToNext()) {
            states = cursor.getString(4);

        }

//        personal_data_button_tv1 = (TextView) findViewById(R.id.personal_data_button_tv1);
//        individual_works_button_tv1 = (TextView) findViewById(R.id.individual_works_button_tv1);
        individual_works_ll = (LinearLayout) b.findViewById(R.id.individual_works_ll);
        personal_data_ll = (LinearLayout) a.findViewById(R.id.personal_data_ll);
        talents_hear_iv = (ImageView) findViewById(R.id.talents_hear_iv);
        talents_back_iv = (ImageView)findViewById(R.id.talents_back_iv);
        talents_name_tv = (TextView) findViewById(R.id.talents_name_tv);
        talents_sex_iv = (ImageView) findViewById(R.id.talents_sex_iv);
        talents_age_tv = (TextView) findViewById(R.id.talents_age_tv);
        talents_site_tv = (TextView) findViewById(R.id.talents_site_tv);
        talents_profession_tv = (TextView) findViewById(R.id.talents_profession_tv);
        talents_self_introduction_tv = (TextView) a.findViewById(R.id.talents_self_introduction_tv);
        talents_work_experience_tv = (TextView) a.findViewById(R.id.talents_work_experience_tv);
        talents_reputation_tv = (TextView) a.findViewById(R.id.talents_reputation_tv);
        invite_a_few_tv = (TextView) findViewById(R.id.invite_a_few_tv);
        self_introduction_button_tv = (TextView) a.findViewById(R.id.self_introduction_button_tv);
        work_experience_button_tv = (TextView) a.findViewById(R.id.work_experience_button_tv);
        comment_button_tv = (TextView) a.findViewById(R.id.comment_button_tv);
        myScrollView1 = (MyScrollView) a.findViewById(R.id.myScrollView1);
        myScrollView2 = (MyScrollView) b.findViewById(R.id.myScrollView2);
        interspace1_tv = (TextView) a.findViewById(R.id.interspace1_tv);
        interspace2_tv = (TextView) b.findViewById(R.id.interspace2_tv);
        head_ll = (LinearLayout) findViewById(R.id.head_ll);
        fanhui_b = (Button) findViewById(R.id.fanhui_b);

        myScrollView1.setOnScrollListener(this);
        myScrollView2.setOnScrollListener(this);
//        personal_data_button_tv1.setOnClickListener(this);
//        individual_works_button_tv1.setOnClickListener(this);
        self_introduction_button_tv.setOnClickListener(this);
        work_experience_button_tv.setOnClickListener(this);
        comment_button_tv.setOnClickListener(this);

        fanhui_b.setAlpha(0);

        ViewTreeObserver vto = head_ll.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                head_height = head_ll.getMeasuredHeight();
                interspace1_tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, head_height));
                interspace2_tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, head_height));
                return true;
            }
        });

        ViewTreeObserver vto3 = personal_data_button_tv.getViewTreeObserver();
        vto3.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                button_height = head_height - personal_data_button_tv.getMeasuredHeight() - Identification.dip2px(TalentsDetailsActivity.this, (float) 50);
                return true;
            }
        });

        ViewTreeObserver vto1 = personal_data_ll.getViewTreeObserver();
        vto1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int h = personal_data_ll.getMeasuredHeight();
                if (h < height) {
                    personal_data_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                }
                return true;
            }
        });
        ViewTreeObserver vto2 = individual_works_ll.getViewTreeObserver();
        vto2.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int h = individual_works_ll.getMeasuredHeight();
                if (h < height) {
                    individual_works_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (height)));
                }
                return true;
            }
        });

        //初始化自我介绍高度
        ViewTreeObserver viewTreeObserver = talents_self_introduction_tv.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                //只需要获取一次就可以了
                if (!hasMesure1) {
                    //这里获取到完全展示的maxLine
                    maxLines = talents_self_introduction_tv.getLineCount();
                    //设置maxLine的默认值，这样用户看到View就是限制了maxLine的TextView
                    talents_self_introduction_tv.setMaxLines(MAX);
                    hasMesure1 = true;
                }

                return true;
            }
        });

        //初始化工作经历高度
        ViewTreeObserver viewTreeObserver1 = talents_work_experience_tv.getViewTreeObserver();
        viewTreeObserver1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                //只需要获取一次就可以了
                if (!hasMesure2) {
                    //这里获取到完全展示的maxLine
                    maxLines = talents_work_experience_tv.getLineCount();
                    //设置maxLine的默认值，这样用户看到View就是限制了maxLine的TextView
                    talents_work_experience_tv.setMaxLines(MAX);
                    hasMesure2 = true;
                }

                return true;
            }
        });

        //加载数据
        bitmapUtils.display(talents_back_iv, AppUtilsUrl.ImageBaseUrl + talentValueBean.getUsericon());
        invite_a_few_tv.setText("邀约数\n" + talentValueBean.getInviteCount());
        bitmapUtils.display(talents_hear_iv, AppUtilsUrl.ImageBaseUrl + talentValueBean.getUsericon());
        talents_name_tv.setText(talentValueBean.getResumeZhName());
        if (talentValueBean.getResumeSex() == 0) {
            talents_sex_iv.setImageResource(R.mipmap.man_big);
        } else {
            talents_sex_iv.setImageResource(R.mipmap.woman_big);
        }
        talents_age_tv.setText(talentValueBean.getResumeAge() + "");
        talents_site_tv.setText(talentValueBean.getResumeWorkPlace());
        talents_profession_tv.setText(talentValueBean.getResumeJobCategoryName());
        talents_self_introduction_tv.setText(talentValueBean.getResumeInfo());
        talents_work_experience_tv.setText(talentValueBean.getResumeWorkExperience());
        talents_reputation_tv.setText(talentValueBean.getIntegrity() + "\n" + talentValueBean.getAuthenticity() + "\n" + talentValueBean.getTransactionRecord());


        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                BitmapDrawable bitmapDrawable = (BitmapDrawable) talents_back_iv.getDrawable();
                bitmap = bitmapDrawable.getBitmap();
                talents_back_iv.setImageBitmap(Identification.fastblur(TalentsDetailsActivity.this, bitmap, 30));
            }
        }, 300);

    }


    //初始化视频作品
    private void initVideoProduction(View b) {
        video_production_list = (ListView) b.findViewById(R.id.video_production_list);
        List<ResumeMovie> resumeMovieData = talentValueBean.getResumeMovie();
        VideoAdapter videoAdapter = new VideoAdapter(this, resumeMovieData);
        video_production_list.setAdapter(videoAdapter);
        Utility.setListViewHeightBasedOnChildren(video_production_list);
    }

    //初始化音乐作品
    private void initMusicProduction(View b) {
        music_production_list = (ListView) b.findViewById(R.id.music_production_list);
        List<ResumeMusic> resumeMusic = talentValueBean.getResumeMusic();
        MusicAdapter musicAdapter = new MusicAdapter(this, resumeMusic);
        music_production_list.setAdapter(musicAdapter);
        Utility.setListViewHeightBasedOnChildren(music_production_list);

        music_production_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TalentsDetailsActivity.this, MusicActivity.class);  //方法1
                intent.putExtra("url", AppUtilsUrl.ImageBaseUrl + talentValueBean.getResumeMusic().get(position).getPath());
                intent.putExtra("musicName", talentValueBean.getResumeMusic().get(position).getTitle());
                intent.putExtras(intent);
                startActivity(intent);
                overridePendingTransition(R.anim.out_to_not, R.anim.music_in);
            }
        });

    }

    private List<ResumePicture> resumePicture;

    //初始化图片作品
    private void initPictureProduction(View b) {
        picture_production_list = (MyGridView) b.findViewById(R.id.picture_production_list);
        resumePicture = talentValueBean.getResumePicture();
        PictureAdapter pictureAdapter = new PictureAdapter(this, resumePicture);
        picture_production_list.setAdapter(pictureAdapter);

        picture_production_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TalentsDetailsActivity.this, SpaceImageDetailActivity.class);
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


    private boolean head_move = false;
    private boolean head_top = false;
    private int ss;
    private int s;
    private boolean ing = false;

    @Override
    public void onScroll(int scrollY) {//监听ScrollView滚动进度


//        talents_profession_tv.setText(head_ll.getTranslationY()+"//"+button_height);
        mScrollY = scrollY;

        /*if(head_ll.getTranslationY()==-button_height){
            Animation animation = null;
            animation = new AlphaAnimation(0, 1);
//                    animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(100);
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
            fanhui_b.startAnimation(animation);
        }*/

        if (scrollY <= button_height && !head_move && !head_top) {
            head_move = true;
            head_ll.setTranslationY(-scrollY);
            fanhui_b.setAlpha((float) scrollY / (float) button_height);

//            if (mScrollY1 < button_height) {
                myScrollView1.scrollTo(0, scrollY);
//            }
//            if (mScrollY2 < button_height) {
                myScrollView2.scrollTo(0, scrollY);
//            }
            head_move = false;
            head_top = false;
        }
        if (scrollY > button_height && !head_top && head_ll.getTranslationY() > (-button_height)) {
            head_ll.setTranslationY(-button_height);
            fanhui_b.setAlpha(1);
//            if (mScrollY1 < button_height) {
                myScrollView1.scrollTo(0, button_height);
//            }
//            if (mScrollY2 < button_height) {
                myScrollView2.scrollTo(0, button_height);
//            }
        }
        /*if (head_top && oldScrollY - scrollY - oldScrollY1 <= 0) {
            s = oldScrollY - scrollY - oldScrollY1;
            head_ll.setTranslationY(s);
            fanhui_b.setAlpha((float) (-s) / (float) button_height);
            if (mScrollY1 < button_height) {
                myScrollView1.scrollTo(0, -s);
            }
            if (mScrollY2 < button_height) {
                myScrollView2.scrollTo(0, -s);
            }

            if(ss-scrollY >10){//向下
                ss=scrollY;
            }else {
                if (scrollY - ss > 10&&!ing) {//向上
                    ing = true;
                    Animation animation = null;
                    int t = button_height - scrollY;
                    if (t < 0) t = 0;
                    animation = new TranslateAnimation(0, 0, 0, -button_height - s + t);
                    animation.setDuration(100);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (mScrollY <= button_height) {
                                head_ll.setTranslationY(-mScrollY);
//                                fanhui_b.setAlpha((float) mScrollY / (float) button_height);
                            }else {
                                head_ll.setTranslationY(-button_height);
//                                fanhui_b.setAlpha(1);
                            }
                            if (mScrollY1 < button_height) {
                                myScrollView1.scrollTo(0, button_height);
                            }
                            if (mScrollY2 < button_height) {
                                myScrollView2.scrollTo(0, button_height);
                            }
                            head_top = false;
                            ing = false;

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    head_ll.startAnimation(animation);

                }
            }

        } else if (head_ll.getTranslationY() <= 0 && head_top && oldScrollY - scrollY - oldScrollY1 > 0&&!ing) {
            head_ll.setTranslationY(0);
            ing = true;
            Animation animation = null;
            int t = button_height-scrollY;
            if(t<0) t=0;
            animation = new TranslateAnimation(0, 0, 0, -button_height+t);
//            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(100);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mScrollY <= button_height) {
                        head_ll.setTranslationY(-mScrollY);
                    }else {
                        head_ll.setTranslationY(-button_height);
                    }
                    if (mScrollY1 < button_height) {
                        myScrollView1.scrollTo(0, button_height);
                    }
                    if (mScrollY2 < button_height) {
                        myScrollView2.scrollTo(0, button_height);
                    }
                    head_top = false;
                    ing = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            head_ll.startAnimation(animation);
//            head_top = false;


        }*/

    }


    @Override
    public void onClick(View v) {

        boolean a = false;

        switch (v.getId()) {
            case R.id.self_introduction_button_tv:
                self_introduction_button_tv.setVisibility(View.GONE);
                toggle(talents_self_introduction_tv);
                break;
            case R.id.work_experience_button_tv:
                work_experience_button_tv.setVisibility(View.GONE);
                toggle(talents_work_experience_tv);
                break;
            case R.id.comment_button_tv:
                Intent intent = new Intent();
                intent.setClass(TalentsDetailsActivity.this, RecommenListActivity.class);
                intent.putExtra("Ident", Identification.COMMENT);
                intent.putExtra("URL", "getCommentByPerson.action?resumeid=");
                intent.putExtra("ID", talentValueBean.getResumeid());
                startActivity(intent);
                break;
        }

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

    //邀约
    public void call_for(View v) {

        Intent intent1 = new Intent(TalentsDetailsActivity.this, myActivity.class);
        startActivity(intent1);


        if (TextUtils.isEmpty(states) || states.equals("1")) {
//            Toast.makeText(TalendDetailsActivity.this, "非登录状态或非商家类型", Toast.LENGTH_LONG).show();
        } else if (states.equals("2")) {
//            Toast.makeText(TalendDetailsActivity.this, "非商家类型", Toast.LENGTH_LONG).show();
        } else if (states.equals("3")) {
            Intent intent = new Intent(TalentsDetailsActivity.this, CalendarActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("userType", 1);
            bundle.putString("Personid", talentValueBean.getPersonid() + "");
            bundle.putInt("Resumeid", talentValueBean.getResumeid());
//        Toast.makeText(this, talentValueBean.getPersonid()+"", Toast.LENGTH_LONG).show();
            intent.putExtras(bundle);
            startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_not);
        }

    }

    public void back(View v) {
        finish();
    }

}

