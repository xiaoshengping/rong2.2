package com.jeremy.Customer.bean;

import android.content.Context;

/**
 * Created by Administrator on 2015/9/22.
 */
public class Identification {
    public static int ACTIVITY = 1;//活动
    public static int TALENTS = 2;//人才
    public static int PROSITION = 3;//职位


    //提示框种类
    public static int TOOLTIP = 1;//提示框（单个按钮）
    //提示框信息
    public static int NETWORKANOMALY = 1;//网络异常

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
