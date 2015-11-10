package com.example.xiaoshengping.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Package com.qianfeng.zw.customview
 * @作 用:
 * @创 建 人: zhangwei
 * @日 期: 15/4/3 14:03
 * @修 改 人:
 * @日 期:
 */
public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
