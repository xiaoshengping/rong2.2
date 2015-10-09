package com.jeremy.Customer.fragment;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.MessageBean;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.http.ImageUtil;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.uilt.AnnouncementMessageActivity;
import com.jeremy.Customer.uilt.InviteMessageActivity;
import com.jeremy.Customer.uilt.LoginActivity;
import com.jeremy.Customer.uilt.MineMoreActivity;
import com.jeremy.Customer.uilt.NickNameActivity;
import com.jeremy.Customer.uilt.SQLhelper;
import com.jeremy.Customer.uilt.TalentsDeliverMessageActivity;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {


    @ViewInject(R.id.touxiang_iv)
    private ImageView touXiangIv;
    @ViewInject(R.id.touxiang1_iv)
    private ImageView touXiang1Tv;
    @ViewInject(R.id.announcement_message_tv)
    private TextView announcementMessageTv;
    @ViewInject(R.id.share_tv)
    private TextView shareTv;
    @ViewInject(R.id.exit_login)
    private TextView exitLoginTv;
    @ViewInject(R.id.petname_tv)
    private TextView petNameTv;
    @ViewInject(R.id.petname1_tv)
    private TextView petname1Tv;
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
    private  String state;
    private  String uid;
    private  String userName;
    private  String userIcon;
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private File screenshotFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果


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
        petNameTv.setOnClickListener(this);
        touXiang1Tv.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.touxiang_iv:
                    showDialog();
                break;
            case R.id.touxiang1_iv:
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
            case R.id.petname_tv:
                Intent nickNameIntent=new Intent(getActivity(), NickNameActivity.class);
                if (!TextUtils.isEmpty(userName)){
                    nickNameIntent.putExtra("userName",userName);
                }else {
                    nickNameIntent.putExtra("userName","hello");
                }
                startActivity(nickNameIntent);
                break;
        }
    }










    @Override
    public void onResume() {
        super.onResume();
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);
            state = cursor.getString(4);
            userName = cursor.getString(2);
            userIcon=cursor.getString(3);
        }
            if (!TextUtils.isEmpty(state)){
                mineLayout.setVisibility(View.VISIBLE);
                exitLoginTv.setVisibility(View.VISIBLE);
                touXiangIv.setVisibility(View.VISIBLE);
                touXiang1Tv.setVisibility(View.GONE);
                petname1Tv.setVisibility(View.GONE);
                petNameTv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(userName)){
                    petNameTv.setText(userName);
                }else {
                    petNameTv.setText("请设置昵称");
                }
                if (!TextUtils.isEmpty(userIcon)){
                    MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl+userIcon,touXiangIv,MyAppliction.RoundedOptions);
                }
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
                //petNameTv.setText("点击头像登录");
                touXiang1Tv.setVisibility(View.VISIBLE);
                petname1Tv.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(uid)||tempFile.exists()){
                intiToXiangData();
            }

    }

    //上传头像
    private void intiToXiangData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("uid",uid);
        requestParams.addBodyParameter("usericon", new File(tempFile.getAbsolutePath()));
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getEditUserIcon(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
               // Log.e("000000",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    ParmeBean<MessageBean> parmeBean= com.alibaba.fastjson.JSONObject.parseObject(responseInfo.result,new TypeReference<ParmeBean<MessageBean>>(){});
                    if (parmeBean.getState().equals("success")){
                      String message=  parmeBean.getValue().getMessage();
                        if (message.equals("success")){
                            update(uid, parmeBean.getValue().getUsericon());
                            SQLhelper sqLhelper=new SQLhelper(getActivity());
                            SQLiteDatabase db= sqLhelper.getWritableDatabase();
                            Cursor cursor=db.query("user", null, null, null, null, null, null);
                            while (cursor.moveToNext()) {
                                userIcon=cursor.getString(3);
                            }
                            touXiangIv.setVisibility(View.VISIBLE);
                            if (!TextUtils.isEmpty(userIcon)){
                                MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl+userIcon,touXiangIv,MyAppliction.RoundedOptions);
                            }
                        }
                    }


                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailure", s);
            }
        });




    }


    /**
     * 更新头像
     */
    public void update(String uid,String userIcon){
        SQLhelper sqLhelper= new SQLhelper(getActivity());
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLhelper.USERICON, userIcon);
        db.update(SQLhelper.tableName, contentValues,
                "uid=?",
                new String[]{uid});
    }

    private void showDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
       final Dialog dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button pictureDialogButton= (Button) view.findViewById(R.id.picture_dialog_button);
        Button photographDialogButton= (Button) view.findViewById(R.id.photograph_dialog_button);
        Button cancelDialogButton= (Button) view.findViewById(R.id.cancel_dialog_button);
        pictureDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                //intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        photographDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 指定调用相机拍照后照片的储存路径
                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(tempFile));
                startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
                dialog.dismiss();
            }
        });
        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }




    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 将进行剪裁后的图片传递到下一个界面上
    private void sentPicToNext(Intent picdata) {
        Bundle bundle = picdata.getExtras();

        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            if (photo == null) {
                touXiangIv.setImageResource(R.mipmap.ic_launcher);
            } else {
                Bitmap zoomBitmap = ImageUtil.zoomBitmap(photo, 100, 100);
                //获取圆角图片
                Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(zoomBitmap, 200.0f);
                //获取倒影图片
                //Bitmap reflectBitmap = ImageUtil.createReflectionImageWithOrigin(roundBitmap);
                touXiangIv.setImageBitmap(roundBitmap);
//                设置文本内容为    图片绝对路径和名字
                //text.setText(tempFile.getAbsolutePath());
                //Log.e("tempFile",tempFile.getAbsolutePath());


                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(screenshotFile);
                    if (null != fos) {
                        photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();

                        //  Toast.makeText(ResumeActivity.this, "截屏文件已保存至SDCard/AndyDemo/ScreenImage/下", Toast.LENGTH_LONG).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] photodata = baos.toByteArray();
                //System.out.println(photodata.toString());

            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
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
                petNameTv.setVisibility(View.GONE);
                petname1Tv.setVisibility(View.VISIBLE);
                touXiangIv.setVisibility(View.GONE);
                touXiang1Tv.setVisibility(View.VISIBLE);

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

        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
                startPhotoZoom(Uri.fromFile(tempFile));
                break;
            case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null)
                    startPhotoZoom(data.getData());
                break;
            case PHOTO_REQUEST_CUT:// 返回的结果
                if (data != null)
                    // setPicToView(data);
                    sentPicToNext(data);

                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
       /* cursor.close();
        db.close();*/
    }
}
