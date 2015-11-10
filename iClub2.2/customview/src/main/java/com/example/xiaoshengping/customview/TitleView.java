package com.example.xiaoshengping.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Package com.qianfeng.zw.customview
 * @作 用:
 * @创 建 人: zhangwei
 * @日 期: 15/4/3 14:04
 * @修 改 人:
 * @日 期:
 */
public class TitleView extends LinearLayout {
    private Button leftBtn;
    private TextView titleTv;
    private Button rightBtn;

    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        leftBtn = new Button(context);
        rightBtn = new Button(context);
        titleTv = new TextView(context);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TitleViewStyle);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.TitleViewStyle_titleTextSize, 14);
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        titleTv.setText("这个是标题");
        int titleHeight = typedArray.getInt(R.styleable.TitleViewStyle_titleheight, 0);


        LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (titleHeight != 0) {
            titleParams.height = titleHeight;
        }
        titleParams.gravity = Gravity.CENTER;
        titleTv.setLayoutParams(titleParams);
        int leftBgId = typedArray.getResourceId(R.styleable.TitleViewStyle_leftBackground, 0);
        if (leftBgId != 0) {
            leftBtn.setBackgroundResource(leftBgId);
        }
        //重绘view
        typedArray.recycle();
        addView(leftBtn);
        addView(titleTv);
    }
}
