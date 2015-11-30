package com.jeremy.Customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;


/**
 * Created by Administrator on 2015/11/30.
 */
public class Advertisement extends LinearLayout{

    private ImageView activity_picture_iv;
    private TextView activity_name_tv;
//    private BitmapUtils bitmapUtils;

    public Advertisement(Context context) {
        super(context);
//        bitmapUtils = new BitmapUtils(context);

    }
    public Advertisement(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.advertisement, this);
        activity_picture_iv = (ImageView)findViewById(R.id.activity_picture_iv);
        activity_name_tv = (TextView)findViewById(R.id.activity_name_tv);

    }

    public void setActivityNameTv(String str) {
        this.activity_name_tv.setText(str);
//        bitmapUtils.display(activity_picture_iv, AppUtilsUrl.ImageBaseUrl + str2);
    }
    public ImageView getActivity_picture_iv() {
        return activity_picture_iv;
    }
}
