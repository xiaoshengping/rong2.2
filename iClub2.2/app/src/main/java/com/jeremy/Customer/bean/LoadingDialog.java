package com.jeremy.Customer.bean;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;

/**
 * Created by Administrator on 2015/10/9.
 */
public class LoadingDialog extends Dialog {

    private Context context;
    private String msg;
    private Animation hyperspaceJumpAnimation;
    private ImageView spaceshipImage;
    private View v;

    public LoadingDialog(Context context, String msg) {
        super(context, R.style.loading_dialog);
        this.setCancelable(false);
        this.context = context;
        this.msg = msg;
        createLoadingDialog();
    }
    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        this.setCancelable(false);
        this.context = context;
//        this.msg = msg;
//        createLoadingDialog();
    }

    private void createLoadingDialog() {

        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        spaceshipImage = (ImageView) v.findViewById(R.id.img);

        tipTextView.setText(msg);

        spaceshipImage.setBackgroundResource(R.anim.loading);
        AnimationDrawable anim = (AnimationDrawable) spaceshipImage.getBackground();
        anim.start();

//        // 加载动画
//        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                context, R.anim.loading_animation);
//        hyperspaceJumpAnimation.setFillAfter(true);
//        // 使用ImageView显示动画
//        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
//
//        hyperspaceJumpAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                hyperspaceJumpAnimation.setFillAfter(true);
//                // 使用ImageView显示动画
//                spaceshipImage.startAnimation(hyperspaceJumpAnimation);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

        super.setContentView(v);

//        Dialog loadingDialog = new Dialog(context, R.style.);// 创建自定义样式dialog
//
//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
//        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT,
//                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
//        return loadingDialog;

    }

}
