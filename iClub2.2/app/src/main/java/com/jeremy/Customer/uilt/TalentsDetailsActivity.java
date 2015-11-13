package com.jeremy.Customer.uilt;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.MusicAdapter;
import com.jeremy.Customer.adapter.PictureAdapter;
import com.jeremy.Customer.adapter.VideoAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.CommentBean;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.MyDialog;
import com.jeremy.Customer.bean.TalentValueBean;
import com.jeremy.Customer.bean.Utility;
import com.jeremy.Customer.bean.mine.ResumeMovie;
import com.jeremy.Customer.bean.mine.ResumeMusic;
import com.jeremy.Customer.bean.mine.ResumePicture;
import com.jeremy.Customer.calendar.CalendarActivity;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.MusicActivity;
import com.jeremy.Customer.view.MyGridView;
import com.jeremy.Customer.view.MyScrollView;
import com.jeremy.Customer.view.MyTitleBar;
import com.jeremy.Customer.view.SpaceImageDetailActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
    private TextView unfold1_tv, unfold2_tv, unfold3_tv, unfold4_tv;

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
    private MyTitleBar fanhui_b;
    private Button yaoyue_b;
    private LinearLayout yaoyue_ll;
    Timer timer = new Timer();
    private int statusBarHeight;
    private int floatingCollarH1, floatingCollarH2;
    private LinearLayout floating_collar_ll;
    private TextView floating_collar_name_tv;

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

        individual_works_ll = (LinearLayout) b.findViewById(R.id.individual_works_ll);
        personal_data_ll = (LinearLayout) a.findViewById(R.id.personal_data_ll);
        talents_hear_iv = (ImageView) findViewById(R.id.talents_hear_iv);
        talents_back_iv = (ImageView) findViewById(R.id.talents_back_iv);
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
        fanhui_b = (MyTitleBar) findViewById(R.id.fanhui_b);
        yaoyue_b = (Button) findViewById(R.id.yaoyue_b);
        yaoyue_ll = (LinearLayout) findViewById(R.id.yaoyue_ll);
        yaoyue_ll.setVisibility(View.GONE);
        unfold1_tv = (TextView) b.findViewById(R.id.unfold1_tv);
        unfold2_tv = (TextView) b.findViewById(R.id.unfold2_tv);
        unfold3_tv = (TextView) b.findViewById(R.id.unfold3_tv);
        unfold4_tv = (TextView) b.findViewById(R.id.unfold4_tv);
        floating_collar_ll = (LinearLayout) b.findViewById(R.id.floating_collar_ll);
        floating_collar_name_tv = (TextView) b.findViewById(R.id.floating_collar_name_tv);
        floating_collar_ll.setVisibility(View.GONE);

        myScrollView1.setOnScrollListener(this);
        myScrollView2.setOnScrollListener(this);
//        personal_data_button_tv1.setOnClickListener(this);
//        individual_works_button_tv1.setOnClickListener(this);
        self_introduction_button_tv.setOnClickListener(this);
        work_experience_button_tv.setOnClickListener(this);
        unfold1_tv.setOnClickListener(this);
        unfold2_tv.setOnClickListener(this);
        unfold3_tv.setOnClickListener(this);
        unfold4_tv.setOnClickListener(this);

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
                button_height = head_height - personal_data_button_tv.getMeasuredHeight() - Identification.dip2px(TalentsDetailsActivity.this, (float) 45);
                return true;
            }
        });

        ViewTreeObserver vto1 = personal_data_ll.getViewTreeObserver();
        vto1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                statusBarHeight = frame.top;
                int h = personal_data_ll.getMeasuredHeight();
                if (h < height + button_height - head_height - statusBarHeight) {
                    personal_data_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height + button_height - head_height - statusBarHeight));
                }
                return true;
            }
        });
        ViewTreeObserver vto2 = individual_works_ll.getViewTreeObserver();
        vto2.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
//                Rect frame = new Rect();
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//                statusBarHeight = frame.top;
                int h = individual_works_ll.getMeasuredHeight();
                if (h < height + button_height - head_height - statusBarHeight) {
                    individual_works_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (height + button_height - head_height - statusBarHeight)));
                }
                floatingCollarH1 = head_height + statusBarHeight - button_height;
                return true;
            }
        });


        ViewTreeObserver vto5 = floating_collar_ll.getViewTreeObserver();
        vto5.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                floatingCollarH2 = floatingCollarH1 + personal_data_button_tv.getMeasuredHeight();

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
        if (talentValueBean.getUsericon().equals("")) {
        } else {
            bitmapUtils.display(talents_back_iv, AppUtilsUrl.ImageBaseUrl + talentValueBean.getUsericon());
            bitmapUtils.display(talents_hear_iv, AppUtilsUrl.ImageBaseUrl + talentValueBean.getUsericon());
            maoboli();
        }
        invite_a_few_tv.setText("邀约数\n" + talentValueBean.getInviteCount());
        talents_name_tv.setText(talentValueBean.getResumeZhName());
        if (talentValueBean.getResumeSex() == 0) {
            talents_sex_iv.setImageResource(R.mipmap.man);
            talents_age_tv.setTextColor(0xff0299FE);
        } else {
            talents_sex_iv.setImageResource(R.mipmap.woman);
            talents_age_tv.setTextColor(0xffF56F94);
        }
        talents_age_tv.setText(talentValueBean.getResumeAge() + "");
        talents_site_tv.setText(talentValueBean.getResumeWorkPlace());
        talents_profession_tv.setText(talentValueBean.getResumeJobCategoryName());
        talents_self_introduction_tv.setText(talentValueBean.getResumeInfo());
        talents_work_experience_tv.setText(talentValueBean.getResumeWorkExperience());
        talents_reputation_tv.setText(talentValueBean.getIntegrity() + "\n" + talentValueBean.getAuthenticity() + "\n" + talentValueBean.getTransactionRecord());

        yaoyue_b.setVisibility(View.GONE);

        //获取评论
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUtilsUrl.getComment(talentValueBean.getResumeid(), "getCommentByPerson.action?resumeid=", 0), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    ArtistParme<CommentBean> commentBean = JSONObject.parseObject(result, new TypeReference<ArtistParme<CommentBean>>() {
                    });
                    if (commentBean.getState().equals("success")) {
                        if (commentBean.getTotal() > 0) {
                            comment_button_tv.setText(commentBean.getTotal() + "位商家评论过");
                            comment_button_tv.setOnClickListener(TalentsDetailsActivity.this);
                        } else {
                            comment_button_tv.setText("还没有商家进行评论哦~");
                            comment_button_tv.setTextColor(0xff777778);
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

    }

    private void maoboli() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) talents_back_iv.getDrawable();
                    bitmap = bitmapDrawable.getBitmap();
                    talents_back_iv.setImageBitmap(Identification.fastblur(TalentsDetailsActivity.this, bitmap, 30));
//                        timer.cancel();
                } catch (Exception e) {
                    maoboli();
                }
//                BitmapDrawable bitmapDrawable = (BitmapDrawable) talents_back_iv.getDrawable();
//                bitmap = bitmapDrawable.getBitmap();
//                talents_back_iv.setImageBitmap(Identification.fastblur(TalentsDetailsActivity.this, bitmap, 30));
            }
        }, 100);
    }

    private VideoAdapter videoAdapter;
    private Bitmap[] bitmaps = new Bitmap[3];//talentValueBean.getResumeMovie().size()];
//    private int bitNum;

    //初始化视频作品
    private void initVideoProduction(View b) {
        video_production_list = (ListView) b.findViewById(R.id.video_production_list);
        List<ResumeMovie> resumeMovieData = talentValueBean.getResumeMovie();
        videoAdapter = new VideoAdapter(this, resumeMovieData);
        video_production_list.setAdapter(videoAdapter);
        Utility.setListViewHeightBasedOnChildren(video_production_list);
        if (resumeMovieData.size() == 0) {
            unfold1_tv.setText("");
        }

        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < resumeMovieData.size(); i++) {
            Thread run = new MyThread(i);
            service.execute(run);
        }

    }

    private class MyThread extends Thread {
        int bitNum;

        public MyThread(int i) {
            bitNum = i;
        }

        public void run() {
            bitmaps[bitNum] = createVideoThumbnail(AppUtilsUrl.ImageBaseUrl + talentValueBean.getResumeMovie().get(bitNum).getPath());
            if(bitmaps[bitNum]==null){
                BitmapDrawable b = (BitmapDrawable)getResources().getDrawable(R.mipmap.talents_back);
                bitmaps[bitNum] = b.getBitmap();
            }
            videoAdapter.setBitmap(bitmaps);
            videoAdapter.notifyDataSetChanged();
        }
    }

    private Bitmap createVideoThumbnail(String url) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 10, 10,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    private MusicAdapter musicAdapter;

    //初始化音乐作品
    private void initMusicProduction(View b) {
        music_production_list = (ListView) b.findViewById(R.id.music_production_list);
        List<ResumeMusic> resumeMusic = talentValueBean.getResumeMusic();
        musicAdapter = new MusicAdapter(this, resumeMusic);
        music_production_list.setAdapter(musicAdapter);
        Utility.setListViewHeightBasedOnChildren(music_production_list);
        if (resumeMusic.size() == 0) {
            unfold2_tv.setText("");
        }

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
    private PictureAdapter pictureAdapter;

    //初始化图片作品
    private void initPictureProduction(View b) {
        picture_production_list = (MyGridView) b.findViewById(R.id.picture_production_list);
        resumePicture = talentValueBean.getResumePicture();
        pictureAdapter = new PictureAdapter(this, resumePicture, (int) (width / 3) - Identification.dip2px(this, 6));
        picture_production_list.setAdapter(pictureAdapter);
        if (resumePicture.size() == 0) {
            unfold3_tv.setText("");
        }

        picture_production_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TalentsDetailsActivity.this, SpaceImageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList) resumePicture);
                bundle.putInt("num", position);
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
    private boolean yaoyue = false;

    @Override
    public void onScroll(int scrollY) {//监听ScrollView滚动进度
        mScrollY = scrollY;
        if (scrollY <= button_height && !head_move && !head_top) {
            head_move = true;
            head_ll.setTranslationY(-scrollY);
            fanhui_b.setAlpha((float) scrollY / (float) button_height);
            myScrollView1.scrollTo(0, scrollY);
            myScrollView2.scrollTo(0, scrollY);
            head_move = false;
            head_top = false;
        }
        if (scrollY > button_height && !head_top && head_ll.getTranslationY() > (-button_height)) {
            head_ll.setTranslationY(-button_height);
            fanhui_b.setAlpha(1);
            myScrollView1.scrollTo(0, button_height);
            myScrollView2.scrollTo(0, button_height);
        }

        //邀约动画
        if (ss - scrollY > 20) {//向下
            ss = scrollY;
            if (!yaoyue && !ing) {
                ing = true;
                yaoyue_b.setVisibility(View.VISIBLE);
                yaoyue_ll.setVisibility(View.VISIBLE);
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
                            yaoyue_b.setVisibility(View.GONE);
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
        unfold1_tv.getLocationOnScreen(location1);
        unfold2_tv.getLocationOnScreen(location2);
        unfold3_tv.getLocationOnScreen(location3);
        if (location21 == 0 && location11 == 0 && location31 == 0) {
            location11 = location1[1];
            location21 = location2[1];
            location31 = location3[1];
        }
        if (floating_collar_name_tv.getText().toString().equals(" 视频作品")) {
            if (location1[1] < floatingCollarH1) {
                floating_collar_ll.setVisibility(View.VISIBLE);
                unfold4_tv.setText(unfold1_tv.getText().toString());
                floating_collar_ll.setTranslationY((float) (floatingCollarH1 - statusBarHeight));
                if (location2[1] < floatingCollarH2) {
                    floating_collar_ll.setTranslationY((float) (floatingCollarH1 - statusBarHeight) - (scrollY - location21 + floatingCollarH2));
                }
            } else {
                floating_collar_ll.setVisibility(View.GONE);
            }
            if (location2[1] < floatingCollarH1) {
                floating_collar_name_tv.setText(" 音乐作品");
                unfold4_tv.setText(unfold2_tv.getText().toString());
                floating_collar_ll.setTranslationY((float) (floatingCollarH1 - statusBarHeight));

            }
        } else if (floating_collar_name_tv.getText().toString().equals(" 音乐作品")) {
            if (location3[1] < floatingCollarH2) {
                floating_collar_ll.setTranslationY((float) (floatingCollarH1 - statusBarHeight) - (scrollY - location31 + floatingCollarH2));
            }
            if (location3[1] < floatingCollarH1) {
                floating_collar_name_tv.setText(" 图片作品");
                unfold4_tv.setText(unfold3_tv.getText().toString());
                floating_collar_ll.setTranslationY((float) (floatingCollarH1 - statusBarHeight));
            }
            if (location2[1] > floatingCollarH1) {
                floating_collar_name_tv.setText(" 视频作品");
                unfold4_tv.setText(unfold1_tv.getText().toString());
            }
        } else if (floating_collar_name_tv.getText().toString().equals(" 图片作品")) {
            if (location3[1] > floatingCollarH1) {
                floating_collar_name_tv.setText(" 音乐作品");
                unfold4_tv.setText(unfold2_tv.getText().toString());
            }
        }

    }

    private int[] location1 = {0, 0};
    private int[] location2 = {0, 0};
    private int[] location3 = {0, 0};
    private int location11 = 0;
    private int location21 = 0;
    private int location31 = 0;


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
            case R.id.unfold1_tv:
                if (unfold1_tv.getText().toString().equals("展开")) {
                    unfold1_tv.setText("收起");
                } else if (unfold1_tv.getText().toString().equals("收起")) {
                    unfold1_tv.setText("展开");
                }
                if (unfold1_tv.getText().toString().equals("")) {
                } else {
                    videoAdapter.setMaxNum();
                    videoAdapter.notifyDataSetChanged();
                    Utility.setListViewHeightBasedOnChildren(video_production_list);
                    initWorksHeight();
                }
                break;
            case R.id.unfold2_tv:
                if (unfold2_tv.getText().toString().equals("展开")) {
                    unfold2_tv.setText("收起");
                } else if (unfold2_tv.getText().toString().equals("收起")) {
                    unfold2_tv.setText("展开");
                }
                if (unfold2_tv.getText().toString().equals("")) {
                } else {
                    musicAdapter.setMaxNum();
                    musicAdapter.notifyDataSetChanged();
                    Utility.setListViewHeightBasedOnChildren(music_production_list);
                    initWorksHeight();
                }
                break;
            case R.id.unfold3_tv:
                if (unfold3_tv.getText().toString().equals("展开")) {
                    unfold3_tv.setText("收起");
                } else if (unfold3_tv.getText().toString().equals("收起")) {
                    unfold3_tv.setText("展开");
                }
                //jlk,mmmjpo
                if (unfold3_tv.getText().toString().equals("")) {
                } else {
                    pictureAdapter.setMaxNum();
                    pictureAdapter.notifyDataSetChanged();
                    initWorksHeight();
                }
                break;
            case R.id.unfold4_tv:
                if (unfold4_tv.getText().toString().equals("展开")) {
                    unfold4_tv.setText("收起");
                } else if (unfold4_tv.getText().toString().equals("收起")) {
                    unfold4_tv.setText("展开");
                }
                if (floating_collar_name_tv.getText().toString().equals(" 视频作品")) {
                    unfold1_tv.setText(unfold4_tv.getText().toString());
                    videoAdapter.setMaxNum();
                    videoAdapter.notifyDataSetChanged();
                    Utility.setListViewHeightBasedOnChildren(video_production_list);
                    initWorksHeight();
                } else if (floating_collar_name_tv.getText().toString().equals(" 音乐作品")) {
                    unfold2_tv.setText(unfold4_tv.getText().toString());
                    musicAdapter.setMaxNum();
                    musicAdapter.notifyDataSetChanged();
                    Utility.setListViewHeightBasedOnChildren(music_production_list);
                    initWorksHeight();
                } else if (floating_collar_name_tv.getText().toString().equals(" 图片作品")) {
                    unfold3_tv.setText(unfold4_tv.getText().toString());
                    pictureAdapter.setMaxNum();
                    pictureAdapter.notifyDataSetChanged();
                    initWorksHeight();
                }
                break;
        }

    }

    private void initWorksHeight() {
        individual_works_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ViewTreeObserver vto2 = individual_works_ll.getViewTreeObserver();
        vto2.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
                int h = individual_works_ll.getMeasuredHeight();
                if (h < height + button_height - head_height - statusBarHeight) {
                    individual_works_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (height + button_height - head_height - statusBarHeight)));
                    head_ll.setTranslationY(0);
                    fanhui_b.setAlpha(0);
                }
                return true;
            }
        });
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

        //获取登录状态
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        states = null;
        while (cursor.moveToNext()) {
            states = cursor.getString(4);

        }

//        Intent intent1 = new Intent(TalentsDetailsActivity.this, myActivity.class);
//        startActivity(intent1);afdfasd


        if (TextUtils.isEmpty(states) || states.equals("1")) {
            dialog();
//            Toast.makeText(TalendDetailsActivity.this, "非登录状态或非商家类型", Toast.LENGTH_LONG).show();
        } else if (states.equals("2")) {
            Toast.makeText(TalentsDetailsActivity.this, "此账号为非商家用户，无法进行邀约操作", Toast.LENGTH_LONG).show();
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

    private MyDialog dialog2;

    //提示框
    private void dialog() {
        dialog2 = new MyDialog(this, Identification.LOGINPROMPT, Identification.LOGINPROMPTMERCHANT);
        dialog2.setDetermine(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TalentsDetailsActivity.this, LoginActivity.class);
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

    public void back(View v) {
        finish();
    }

}

