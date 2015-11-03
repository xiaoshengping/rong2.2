package com.jeremy.Customer.uilt;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumeVideoAdapter;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

public class AddVideoActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;
    @ViewInject(R.id.show_video_iv)
    private ImageView showVideoIv;
    @ViewInject(R.id.add_video_bt)
    private Button  addVideoBt;
    @ViewInject(R.id.show_video_lv)
    private ListView showVideoLv;


    private ResumeValueBean resumeValueBean;
    private  String videoPath;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();


    }

    private void initView() {
        loadingDialog = new LoadingDialog(this,"正在更新数据……");
        resumeValueBean= (ResumeValueBean) getIntent().getSerializableExtra("resumeValueBean");
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加图片");
        saveText.setVisibility(View.VISIBLE);
        saveText.setOnClickListener(this);
        saveText.setText("上传");
        addVideoBt.setOnClickListener(this);
        if (resumeValueBean!=null){
            ResumeVideoAdapter resumeVideoAdapter=new ResumeVideoAdapter(resumeValueBean.getResumeMovie(),AddVideoActivity.this);
            showVideoLv.setAdapter(resumeVideoAdapter);
            resumeVideoAdapter.notifyDataSetChanged();
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:
                if (getIntent().getSerializableExtra("fagle").equals("modifactionResume")){
                if (!TextUtils.isEmpty(resumeValueBean.getResumeid()+"")||!TextUtils.isEmpty(videoPath)){
                    addVideoData(resumeValueBean.getResumeid()+"",videoPath);
                }else {
                    MyAppliction.showExitGameAlert("你还没有选择照片", AddVideoActivity.this);
                }
                }else if (getIntent().getSerializableExtra("fagle").equals("productionResume")){
                    if (!TextUtils.isEmpty(getIntent().getStringExtra("resumeid").toString())||!TextUtils.isEmpty(videoPath)){
                        addVideoData(getIntent().getStringExtra("resumeid").toString(),videoPath);
                    }else {
                        MyAppliction.showExitGameAlert("你还没有选择照片", AddVideoActivity.this);
                    }

                }
                    break;
            case R.id.add_video_bt:
                showDialog();
                break;
        }
    }

    private void addVideoData(String resumeid,String moviePath) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid",resumeid);
        requestParams.addBodyParameter("movie", new File(moviePath));
        loadingDialog.show();
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddMovie(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                loadingDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                loadingDialog.dismiss();
            }
        });



    }


    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(AddVideoActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button videoDialogButton= (Button) view.findViewById(R.id.picture_dialog_button);
        Button shootDialogButton= (Button) view.findViewById(R.id.photograph_dialog_button);
        Button cancelDialogButton= (Button) view.findViewById(R.id.cancel_dialog_button);
        videoDialogButton.setText("视频");
        shootDialogButton.setText("拍摄");
        videoDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 4);
                dialog.dismiss();
            }
        });
        shootDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 4);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 4:
                if (null != data) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    } else {
                        // 视频捕获并保存到指定的fileUri意图
                        //Toast.makeText(this, "Video saved to:\n" + data.getData(),
                        //   Toast.LENGTH_LONG).show();
                        Cursor c = getContentResolver().query(uri,
                                new String[]{MediaStore.MediaColumns.DATA},
                                null, null, null);
                        if (c != null && c.moveToFirst()) {
                            // tv.setText("上传中请等待......");
                            // pb.setVisibility(View.VISIBLE);
                            videoPath = c.getString(0);
                                if (!TextUtils.isEmpty(videoPath)) {
                                    showVideoIv.setVisibility(View.VISIBLE);
                                    showVideoIv.setImageBitmap(getVideoThumbnail(videoPath, 1700, 1000,
                                            MediaStore.Images.Thumbnails.MINI_KIND));
                                }
                        }

                    }
                }
                break;

        }
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        //System.out.println("w"+bitmap.getWidth());
        //System.out.println("h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
