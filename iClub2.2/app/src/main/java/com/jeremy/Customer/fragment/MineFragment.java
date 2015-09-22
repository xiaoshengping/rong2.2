package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.uilt.AnnouncementMessageActivity;
import com.jeremy.Customer.uilt.LoginActivity;
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
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**ʹ��SSO��Ȩ����������´��� */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
