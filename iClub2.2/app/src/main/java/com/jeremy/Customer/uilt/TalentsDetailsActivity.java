package com.jeremy.Customer.uilt;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
    private TextView talents_age_tv,talents_site_tv,talents_profession_tv;
    private TextView talents_self_introduction_tv,talents_work_experience_tv,talents_reputation_tv;
    private TextView comment_button_tv;

    private int mScrollY = 0;
    private Bitmap bitmap;

    private BitmapUtils bitmapUtils;

    private TalentValueBean talentValueBean;

    private Thread thread;
    private static final int MAX = 6;//初始maxLine大小
    private int maxLines;
    private static final int TIME = 40;//间隔时间
    private boolean hasMesure1 = false,hasMesure2 = false;
    private TextView self_introduction_button_tv,work_experience_button_tv;

    private String states = null;//用户类型

    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
//    private TextView job_details_tv, company_details_tv;// 页卡头标
    private int currIndex = 0;// 当前页卡编号

    private TextView tooltips_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talents_details);

        bitmapUtils=new BitmapUtils(this);
        talentValueBean = (TalentValueBean) getIntent().getSerializableExtra("Detail");

        InitTextView();
        InitViewPager();

    }

    /**
     * 初始化头标
     */
    private float w;
    private int width;
    private int tooltips_width;
    private void InitTextView() {
        personal_data_button_tv = (TextView) findViewById(R.id.personal_data_button_tv);
        individual_works_button_tv = (TextView) findViewById(R.id.individual_works_button_tv);
        tooltips_tv = (TextView)findViewById(R.id.tooltips_tv);

        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
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

        init(item_talents_personal_data , item_talents_personage_production);
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
        int one = (int)w;
        int two = (int)w;

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
//                    if (currIndex == 1) {
//                        animation = new TranslateAnimation(one, 0, 0, 0);
//                    } else if (currIndex == 2) {
//                        animation = new TranslateAnimation(two, 0, 0, 0);
//                    }
                    animation = new TranslateAnimation((tooltips_width+(2*w)), one, 0, 0);
                    break;
                case 1:
                        animation = new TranslateAnimation( one,(tooltips_width+(2*w)), 0, 0);
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
//            if(arg1!=0) {
//                tooltips_tv.setTranslationX((float)((tooltips_width+(2*w))*arg1)+w).setFillAfter(true);

//                tooltips_tv.setText(((tooltips_width + (float) (2 * w)) * arg1 + w)+"");
//                int evaluate = (Integer) evaluator.evaluate(arg1, 0XFF8744ad, 0XFF777778);
//                job_details_tv.setTextColor(evaluate);
//                job_details_tv.setTextSize(18-(arg1*2));
//                int evaluates = (Integer) evaluator.evaluate(arg1, 0XFF777778, 0XFF8744ad);
//                company_details_tv.setTextSize(16+(arg1*2));
//                company_details_tv.setTextColor(evaluates);
//            }

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }








    //初始化界面
    private void init(View a , View b) {

        //获取登录状态
        SQLhelper sqLhelper=new SQLhelper(this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        states=null;
        while (cursor.moveToNext()) {
            states = cursor.getString(4);

        }

//        personal_data_button_tv = (TextView) findViewById(R.id.personal_data_button_tv);
//        individual_works_button_tv = (TextView) findViewById(R.id.individual_works_button_tv);
        personal_data_button_tv1 = (TextView) findViewById(R.id.personal_data_button_tv1);
        individual_works_button_tv1 = (TextView) findViewById(R.id.individual_works_button_tv1);
        individual_works_ll = (LinearLayout) b.findViewById(R.id.individual_works_ll);
        personal_data_ll = (LinearLayout) a.findViewById(R.id.personal_data_ll);
        talents_hear_iv = (ImageView)findViewById(R.id.talents_hear_iv);
        talents_name_tv = (TextView)findViewById(R.id.talents_name_tv);
        talents_sex_iv = (ImageView)findViewById(R.id.talents_sex_iv);
        talents_age_tv = (TextView)findViewById(R.id.talents_age_tv);
        talents_site_tv = (TextView)findViewById(R.id.talents_site_tv);
        talents_profession_tv = (TextView)findViewById(R.id.talents_profession_tv);
        talents_self_introduction_tv = (TextView) a.findViewById(R.id.talents_self_introduction_tv);
        talents_work_experience_tv = (TextView) a.findViewById(R.id.talents_work_experience_tv);
        talents_reputation_tv = (TextView) a.findViewById(R.id.talents_reputation_tv);
        invite_a_few_tv = (TextView)findViewById(R.id.invite_a_few_tv);
        self_introduction_button_tv = (TextView) a.findViewById(R.id.self_introduction_button_tv);
        work_experience_button_tv = (TextView) a.findViewById(R.id.work_experience_button_tv);
        comment_button_tv = (TextView) a.findViewById(R.id.comment_button_tv);

//        personal_data_button_tv.setOnClickListener(this);
//        individual_works_button_tv.setOnClickListener(this);
        personal_data_button_tv1.setOnClickListener(this);
        individual_works_button_tv1.setOnClickListener(this);
        self_introduction_button_tv.setOnClickListener(this);
        work_experience_button_tv.setOnClickListener(this);
        comment_button_tv.setOnClickListener(this);

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
        bitmapUtils.display(talents_back_iv, AppUtilsUrl.ImageBaseUrl + talentValueBean.getResumeUserbg());
        invite_a_few_tv.setText("邀约数\n" + talentValueBean.getInviteCount());
        bitmapUtils.display(talents_hear_iv, AppUtilsUrl.ImageBaseUrl + talentValueBean.getUsericon());
        talents_name_tv.setText(talentValueBean.getResumeZhName());
        if(talentValueBean.getResumeSex()==0){
            talents_sex_iv.setImageResource(R.mipmap.man_big);
        }else {
            talents_sex_iv.setImageResource(R.mipmap.woman_big);
        }
        talents_age_tv.setText(talentValueBean.getResumeAge()+"");
        talents_site_tv.setText(talentValueBean.getResumeWorkPlace());
        talents_profession_tv.setText(talentValueBean.getResumeJobCategoryName());
        talents_self_introduction_tv.setText(talentValueBean.getResumeInfo());
        talents_work_experience_tv.setText(talentValueBean.getResumeWorkExperience());
        talents_reputation_tv.setText(talentValueBean.getIntegrity()+"\n"+talentValueBean.getAuthenticity()+"\n"+talentValueBean.getTransactionRecord());

    }

    //初始化标题栏动画
    private void initTitleAnimation() {
        invite_a_few_tv = (TextView) findViewById(R.id.invite_a_few_tv);
//        talents_basics_rl = (RelativeLayout) findViewById(R.id.talents_basics_rl);
        talents_back_iv = (ImageView) findViewById(R.id.talents_back_iv);
//        talents_back_baffle_tv = (TextView) findViewById(R.id.talents_back_baffle_tv);
        floating_collar = (LinearLayout) findViewById(R.id.floating_collar);


//        bitmap = ((BitmapDrawable) talents_back_iv.getDrawable()).getBitmap();
    }

    //初始化视频作品
    private void initVideoProduction(View b) {
        video_production_list = (ListView) b.findViewById(R.id.video_production_list);
        List<ResumeMovie> resumeMovieData = talentValueBean.getResumeMovie();
        VideoAdapter videoAdapter = new VideoAdapter(this,resumeMovieData);
        video_production_list.setAdapter(videoAdapter);
        Utility.setListViewHeightBasedOnChildren(video_production_list);
    }

    //初始化音乐作品
    private void initMusicProduction(View b) {
        music_production_list = (ListView) b.findViewById(R.id.music_production_list);
        List<ResumeMusic> resumeMusic = talentValueBean.getResumeMusic();
        MusicAdapter musicAdapter = new MusicAdapter(this,resumeMusic);
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
                overridePendingTransition(R.anim.out_to_not,R.anim.music_in);
            }
        });

    }

    private List<ResumePicture> resumePicture;

    //初始化图片作品
    private void initPictureProduction(View b) {
        picture_production_list = (MyGridView) b.findViewById(R.id.picture_production_list);
        resumePicture = talentValueBean.getResumePicture();
        PictureAdapter pictureAdapter = new PictureAdapter(this,resumePicture);
        picture_production_list.setAdapter(pictureAdapter);

        picture_production_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TalentsDetailsActivity.this, SpaceImageDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList) resumePicture);
                bundle.putInt("num", 2);
                bundle.putInt("MaxNum",resumePicture.size());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.spaceimagedetail_in,R.anim.out_to_not);
            }
        });
    }

    @Override
    public void onScroll(int scrollY) {//监听ScrollView滚动进度

        mScrollY = scrollY;

//        TextView a = (TextView) findViewById(R.id.talents_name_tv);
       /* //邀约数淡出淡入动画
        int invite = Identification.dip2px(this, 100);
        float lucency = ((float) (invite - scrollY) / (float) invite);//控件透明度
        invite_a_few_tv.setAlpha(lucency);
        invite_a_few_tv.setTranslationY(-(int) (scrollY * 0.7));
*/
        /*//用户基本资料跟随滑动动画
        if (scrollY <= Identification.dip2px(this, 130)) {
            talents_basics_rl.setTranslationY(-scrollY);
        } else {
            talents_basics_rl.setTranslationY(-Identification.dip2px(this, 130));
        }
        if (scrollY >= Identification.dip2px(this, 106)) {
            int zoomMax = Identification.dip2px(this, 280);//最大缩放距离
            float zoom = (float) (zoomMax - (((scrollY) - Identification.dip2px(this, 106)))) / (float) zoomMax;//基本资料缩放比
            if (zoom >= 0.8 && zoom <= 1) {
                talents_basics_rl.setScaleY(zoom);
                talents_basics_rl.setScaleX(zoom);
            } else {
//                talents_basics_rl.setScaleY(zoom);
//                talents_basics_rl.setScaleX(zoom);
            }
        } else {
            talents_basics_rl.setScaleY((float) 1);
            talents_basics_rl.setScaleX((float) 1);
        }*/

      /*  //人才背景滑动动画
        if (scrollY <= Identification.dip2px(this, 324)) {
//        talents_back_iv.setScaleY((float) 0.5);
            talents_back_iv.setTranslationY(-(int) (scrollY * 0.28));
            talents_back_baffle_tv.setTranslationY(-(int) (scrollY * 0.555555));
        } else {
            *//*talents_back_iv.setTranslationY(-(int) (Identification.dip2px(this, 106) * 0.7));
            talents_back_baffle_tv.setTranslationY(-(int) (Identification.dip2px(this, 106) * 1.7));*//*
        }*/

        /*//显示浮框
        if (scrollY >= Identification.dip2px(this, 324)) {
            floating_collar.setVisibility(View.VISIBLE);
        } else {
            floating_collar.setVisibility(View.GONE);
        }*/

        /*//毛玻璃化
        if (scrollY!=0&&scrollY <= Identification.dip2px(this, 324)) {

            int max = Identification.dip2px(this, 324);
            float maoboli = ((float) scrollY / (float) max) * 20.f;

            if (Build.VERSION.SDK_INT > 16) {
                Bitmap bitmap1 = bitmap.copy(bitmap.getConfig(), true);

                final RenderScript rs = RenderScript.create(this);
                final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                        Allocation.USAGE_SCRIPT);
                final Allocation output = Allocation.createTyped(rs, input.getType());
                final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setRadius(maoboli);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(bitmap1);
                talents_back_iv.setImageBitmap(bitmap1);
            } else {
//                talents_back_iv.setImageBitmap(Identification.fastblur(this, bitmap, 10));
            }

        } else {
//            talents_back_iv.setImageResource(R.drawable.talents_details_button_l_shape);
//                talents_back_iv.setImageBitmap(bitmap);

        }*/


//        a.setText(scrollY + "");
    }


    @Override
    public void onClick(View v) {
//        personal_data_ll.setVisibility(View.GONE);
//        individual_works_ll.setVisibility(View.GONE);

        boolean a = false;

        switch (v.getId()) {
            case R.id.personal_data_button_tv:
                a = false;
                break;
            case R.id.individual_works_button_tv:
                a = true;
                break;
            case R.id.personal_data_button_tv1:
                a = false;
                break;
            case R.id.individual_works_button_tv1:
                a = true;
                break;
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
                intent.putExtra("URL","getCommentByPerson.action?resumeid=");
                intent.putExtra("ID",talentValueBean.getResumeid());
                startActivity(intent);
                break;
        }

//        buttonView(a);

    }

    private void buttonView(boolean b) {//点击事件显示，b:true右、反之左
        if (b) {
            individual_works_button_tv.setBackgroundResource(R.drawable.talents_details_button_r_shape);
            individual_works_button_tv1.setBackgroundResource(R.drawable.talents_details_button_r_shape);
            personal_data_button_tv.setBackgroundColor(0x00ffffff);
            personal_data_button_tv1.setBackgroundColor(0x00ffffff);

            individual_works_button_tv.setTextColor(0xffffffff);
            individual_works_button_tv1.setTextColor(0xffffffff);
            personal_data_button_tv.setTextColor(0xff282a31);
            personal_data_button_tv1.setTextColor(0xff282a31);

//            myScrollView.smoothScrollTo(0, mScrollY);
            individual_works_ll.setVisibility(View.VISIBLE);
        } else {
            individual_works_button_tv.setBackgroundColor(0xffffffff);
            individual_works_button_tv1.setBackgroundColor(0xffffffff);
            personal_data_button_tv.setBackgroundResource(R.drawable.talents_details_button_l_shape);
            personal_data_button_tv1.setBackgroundResource(R.drawable.talents_details_button_l_shape);

            individual_works_button_tv.setTextColor(0xff282a31);
            individual_works_button_tv1.setTextColor(0xff282a31);
            personal_data_button_tv.setTextColor(0xffffffff);
            personal_data_button_tv1.setTextColor(0xffffffff);

            personal_data_ll.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 打开TextView
     */
    @SuppressLint("HandlerLeak")
    private void toggle(final TextView view){

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int lines = msg.what;
                //这里接受到消息，让后更新TextView设置他的maxLine就行了
                view.setMaxLines(lines);
                view.postInvalidate();
            }
        };
        if(thread != null)
            handler.removeCallbacks(thread);

        thread = new Thread(){
            @Override
            public void run() {
                int count = MAX;
                while(count++ <= maxLines){
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
    public void call_for(View v){

        Intent intent1 = new Intent(TalentsDetailsActivity.this, myActivity.class);
        startActivity(intent1);


        if (TextUtils.isEmpty(states)||states.equals("1")){
//            Toast.makeText(TalendDetailsActivity.this, "非登录状态或非商家类型", Toast.LENGTH_LONG).show();
        }else if(states.equals("2")){
//            Toast.makeText(TalendDetailsActivity.this, "非商家类型", Toast.LENGTH_LONG).show();
        }else if(states.equals("3")){
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

