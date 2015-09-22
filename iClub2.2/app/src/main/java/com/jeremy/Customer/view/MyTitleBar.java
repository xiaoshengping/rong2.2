package com.jeremy.Customer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;

/**
 * Created by Administrator on 2015/9/22.
 */
public class MyTitleBar extends RelativeLayout{

    private TextView  textView;

    public MyTitleBar(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_title_bar, this);
        textView=(TextView)findViewById(R.id.mytitle_name);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyTitleBar);
        String title_name_text = array.getString(R.styleable.MyTitleBar_title_name_text);
        textView.setText(title_name_text);
    }

    public void setTextViewText(String text) {
        textView.setText(text);
    }

}