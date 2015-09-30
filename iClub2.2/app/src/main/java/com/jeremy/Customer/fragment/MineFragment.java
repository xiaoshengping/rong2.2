package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.uilt.AnnouncementMessageActivity;
import com.jeremy.Customer.uilt.InviteMessageActivity;
import com.jeremy.Customer.uilt.LoginActivity;
import com.jeremy.Customer.uilt.MineMoreActivity;
import com.jeremy.Customer.uilt.SQLhelper;
import com.jeremy.Customer.uilt.TalentsDeliverMessageActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {




    @ViewInject(R.id.touxiang_iv)
    private ImageView touXiangIv;
    @ViewInject(R.id.announcement_message_tv)
    private TextView announcementMessageTv;
    @ViewInject(R.id.share_tv)
    private TextView shareTv;
    @ViewInject(R.id.exit_login)
    private TextView exitLoginTv;
    @ViewInject(R.id.petname_tv)
    private TextView petNameTv;
    @ViewInject(R.id.resume_zhaoping_tv)
    private TextView resumeZhaoPingTv;
    @ViewInject(R.id.invite_message_tv)
    private TextView inviteMessageTv;
    @ViewInject(R.id.journey_merchant_tv)
    private TextView journeyMerchantTv;
    @ViewInject(R.id.deliver_message_tv)
    private TextView deliverMessageTv;
    @ViewInject(R.id.mine_layout)
    private LinearLayout mineLayout;
    @ViewInject(R.id.more_mine_tv)
    private TextView moreMineTv;


    private  UMSocialService mController;


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_mine, container, false);
        //TextView textView= (TextView) getActivity().findViewById(R.id.text);
        ViewUtils.inject(this, view);

           inti();

        return view;
    }

    private void inti() {
        intiView();
        umengShare();//分享
    }

    private void umengShare() {
        // 首先在您的Activity中添加如下成员变量
         mController = UMServiceFactory.getUMSocialService("com.umeng.share");
          // 设置分享内容
        mController.setShareContent("我开始用【iclub】App了,全新的演艺招聘模式，专业的娱乐行业服务平台!");
            // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(getActivity(), R.mipmap.icon));
           //΢微信
        String appID = "wx4083a90ea6888404";
        String appSecret = "45d30589a4f8a3efe7ae617c746f6b8c";

       // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(getActivity(),appID,appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),appID,appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        weixinContent.setShareContent("我开始用【iclub】App了,全新的演艺招聘模式，专业的娱乐行业服务平台!");
        //设置title
        weixinContent.setTitle("iClub软件分享");
        //设置分享内容跳转URL
        weixinContent.setTargetUrl("http://www.iclubapps.com/");
        //设置分享图片
        weixinContent.setShareImage(new UMImage(getActivity(), R.mipmap.icon));
        mController.setShareMedia(weixinContent);

        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("我开始用【iclub】App了,全新的演艺招聘模式，专业的娱乐行业服务平台!");
//设置朋友圈title
        circleMedia.setTitle("iClub软件分享");
        circleMedia.setShareImage(new UMImage(getActivity(), R.mipmap.icon));
        circleMedia.setTargetUrl("http://www.iclubapps.com/");
        mController.setShareMedia(circleMedia);

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), "1102291619",
                "aPJ8P5pCeUTKK0Id");
        qqSsoHandler.addToSocialSDK();
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("我开始用【iclub】App了,全新的演艺招聘模式，专业的娱乐行业服务平台!");
        qqShareContent.setTitle("iClub软件分享");
        qqShareContent.setShareImage(new UMImage(getActivity(), R.mipmap.icon));
        qqShareContent.setTargetUrl("http://www.iclubapps.com/");
        mController.setShareMedia(qqShareContent);

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), "1102291619",
                "aPJ8P5pCeUTKK0Id");
        qZoneSsoHandler.addToSocialSDK();
        qZoneSsoHandler.addToSocialSDK();
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent("我开始用【iclub】App了,全新的演艺招聘模式，专业的娱乐行业服务平台!");
        qzone.setTargetUrl("http://www.iclubapps.com/");
        qzone.setTitle("iClub软件分享");
        qzone.setShareImage(new UMImage(getActivity(), R.mipmap.icon));
        mController.setShareMedia(qzone);
        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

    }

    private void intiView() {
        touXiangIv.setOnClickListener(this);
        announcementMessageTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);
        exitLoginTv.setOnClickListener(this);
        moreMineTv.setOnClickListener(this);
        deliverMessageTv.setOnClickListener(this);
        inviteMessageTv.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.touxiang_iv:
                Intent intent =new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.announcement_message_tv:
                Intent announcementIntent =new Intent(getActivity(), AnnouncementMessageActivity.class);
                startActivity(announcementIntent);

                break;
            case R.id.share_tv:
                mController.openShare(getActivity(), false);

                break;
            case R.id.exit_login:
                showExitGameAlert();
                break;
            case R.id.more_mine_tv:
                Intent  moreIntent =new Intent(getActivity(), MineMoreActivity.class);
                startActivity(moreIntent);
                break;
            case R.id.deliver_message_tv:
                Intent TalentsDeliverMessageIntent=new Intent(getActivity(), TalentsDeliverMessageActivity.class);
                startActivity(TalentsDeliverMessageIntent);
                break;
            case R.id.invite_message_tv:
                Intent InviteMessageIntent=new Intent(getActivity(), InviteMessageActivity.class);
                startActivity(InviteMessageIntent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String state=null;
        while (cursor.moveToNext()) {
            state = cursor.getString(4);

        }

        if (!TextUtils.isEmpty(state)){
            mineLayout.setVisibility(View.VISIBLE);
            exitLoginTv.setVisibility(View.VISIBLE);
            if (state.equals("2")){
                resumeZhaoPingTv.setText("我的简历");
                inviteMessageTv.setText("邀约消息");
                journeyMerchantTv.setText("我的行程");
                deliverMessageTv.setText("投递消息");
                Drawable drawableResume= getResources().getDrawable(R.mipmap.resume_icon);
                drawableResume.setBounds(0, 0, drawableResume.getMinimumWidth(), drawableResume.getMinimumHeight());
                resumeZhaoPingTv.setCompoundDrawables(null, drawableResume, null, null);
                Drawable drawableInvite= getResources().getDrawable(R.mipmap.invite_icon);
                drawableInvite.setBounds(0, 0, drawableInvite.getMinimumWidth(), drawableInvite.getMinimumHeight());
                inviteMessageTv.setCompoundDrawables(null, drawableInvite, null, null);
                Drawable drawableInviteDater= getResources().getDrawable(R.mipmap.invite_dater_icon);
                drawableInviteDater.setBounds(0, 0, drawableInviteDater.getMinimumWidth(), drawableInviteDater.getMinimumHeight());
                journeyMerchantTv.setCompoundDrawables(null, drawableInviteDater, null, null);
                Drawable drawableDeliver= getResources().getDrawable(R.mipmap.deliver_icon);
                drawableDeliver.setBounds(0, 0, drawableDeliver.getMinimumWidth(), drawableDeliver.getMinimumHeight());
                deliverMessageTv.setCompoundDrawables(null, drawableDeliver, null, null);

            }else if (state.equals("3")){
                resumeZhaoPingTv.setText("我的招聘");
                inviteMessageTv.setText("邀约消息");
                journeyMerchantTv.setText("商家信息");
                deliverMessageTv.setText("投递消息");
                Drawable drawableResume= getResources().getDrawable(R.mipmap.resume_icon);
                drawableResume.setBounds(0, 0, drawableResume.getMinimumWidth(), drawableResume.getMinimumHeight());
                resumeZhaoPingTv.setCompoundDrawables(null, drawableResume, null, null);
                Drawable drawableInvite= getResources().getDrawable(R.mipmap.invite_icon);
                drawableInvite.setBounds(0, 0, drawableInvite.getMinimumWidth(), drawableInvite.getMinimumHeight());
                inviteMessageTv.setCompoundDrawables(null, drawableInvite, null, null);
                Drawable drawableInviteDater= getResources().getDrawable(R.mipmap.merchant_message_icon);
                drawableInviteDater.setBounds(0, 0, drawableInviteDater.getMinimumWidth(), drawableInviteDater.getMinimumHeight());
                journeyMerchantTv.setCompoundDrawables(null, drawableInviteDater, null, null);
                Drawable drawableDeliver= getResources().getDrawable(R.mipmap.deliver_icon);
                drawableDeliver.setBounds(0, 0, drawableDeliver.getMinimumWidth(), drawableDeliver.getMinimumHeight());
                deliverMessageTv.setCompoundDrawables(null, drawableDeliver, null, null);
            }

        }else {
            mineLayout.setVisibility(View.GONE);
            exitLoginTv.setVisibility(View.GONE);

        }



        cursor.close();
        db.close();




    }
    //对话框
    private void showExitGameAlert() {
        final AlertDialog dlg = new AlertDialog.Builder(getActivity()).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText("确定注销账号？");
        // 为确认按钮添加事件,执行退出应用操作
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText("确定");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exitLoginTv.setVisibility(View.GONE);
                mineLayout.setVisibility(View.GONE);
                SQLhelper sqLhelper = new SQLhelper(getActivity());
                SQLiteDatabase db = sqLhelper.getWritableDatabase();
                Cursor cursor = db.query("user", null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    String uid = cursor.getString(0);
                    if (uid != null) {
                        db.delete("user", null, null);

                    }
                }

                cursor.close();
                db.close();
                dlg.cancel();
            }
        });

        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
