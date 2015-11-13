package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ActivityBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by Administrator on 2015/9/23.
 */
public class ActivityDetailActivity extends Activity {

    private ImageView activity_poster_iv;
    private TextView activity_name_tv,activity_time_tv,activity_content_tv,activity_address_tv;
    private Button immediately_attend_b;

    private BitmapUtils bitmapUtils;
    private ActivityBean activityBean;

    private boolean pastDue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitydetails);
        bitmapUtils = new BitmapUtils(this);
        activityBean = (ActivityBean) getIntent().getSerializableExtra("Detail");
        init();
    }

    private void init(){

        activity_poster_iv = (ImageView)findViewById(R.id.activity_poster_iv);
        activity_name_tv = (TextView)findViewById(R.id.activity_name_tv);
        activity_time_tv = (TextView)findViewById(R.id.activity_time_tv);
        activity_content_tv = (TextView)findViewById(R.id.activity_content_tv);
        activity_address_tv = (TextView)findViewById(R.id.activity_address_tv);
        immediately_attend_b = (Button)findViewById(R.id.immediately_attend_b);

        bitmapUtils.display(activity_poster_iv, AppUtilsUrl.ImageBaseUrl + activityBean.getImage());
        activity_name_tv.setText(activityBean.getTitle());
        activity_time_tv.setText("  截止日期：" + activityBean.getDate());
        activity_content_tv.setText(activityBean.getContent());
        activity_address_tv.setText(" "+activityBean.getAddress());

        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month+1;
        int day = time.monthDay;
        String[] s = activityBean.getDate().split("-");
//        activity_name_tv.setText(year+"-"+month+"-"+day);
        if(Integer.parseInt(s[0])==year){
            if(Integer.parseInt(s[1])==month){
                if(Integer.parseInt(s[2])>=day){
                    pastDue = false;
                }else {
                    pastDue = true;
                }
            }else if(Integer.parseInt(s[1])<month){
                pastDue = true;
            }else if(Integer.parseInt(s[1])>month){
                pastDue = false;
            }
        }else if(Integer.parseInt(s[0])<year){
            pastDue = true;
        }else if(Integer.parseInt(s[0])>year){
            pastDue = false;
        }

        if(pastDue){
            immediately_attend_b.setBackgroundColor(0xff878787);
            immediately_attend_b.setText("活动已截止");
        }

    }

    public void immediately_attend(View v) {
        if(!pastDue){
            Intent intent = new Intent();
            intent.setClass(ActivityDetailActivity.this, ActivityApplyActivity.class);
            intent.putExtra("ActivityId", activityBean.getActivitieid());
            startActivity(intent);
        }
    }

    public void back(View v) {
        finish();
    }

}
