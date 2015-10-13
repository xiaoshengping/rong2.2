package com.jeremy.Customer.uilt;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

public class AddMusicProductionActivity extends ActionBarActivity implements View.OnClickListener {
    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.add_music_production_bt)
    private Button addMusicProductionBt;
    @ViewInject(R.id.confirm_bt)
    private Button confirmBt;
    @ViewInject(R.id.show_music_tv)
    private TextView showMusicTv;
    @ViewInject(R.id.music_layout)
    private LinearLayout musicLayout;
    @ViewInject(R.id.music_name_ed)
    private EditText musicNameEd;


    private  String musicPath;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music_production);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initView();

    }

    private void initView() {
        intent=getIntent();
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加音乐");
        addMusicProductionBt.setOnClickListener(this);
        confirmBt.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tailt_return_tv:
                intent.putExtra("musicPath","");
                intent.putExtra("musicName","");
                setResult(10, intent);
                finish();
                break;
            case R.id.add_music_production_bt:
                showDialog();
                break;
            case R.id.confirm_bt:
                if (!TextUtils.isEmpty(musicNameEd.getText().toString())){
                    if (!TextUtils.isEmpty(musicPath)){

                        intent.putExtra("musicPath",musicPath);
                        intent.putExtra("musicName",musicNameEd.getText().toString());
                        setResult(10, intent);
                        finish();
                    }else {
                        MyAppliction.showToast("你还没有选择音频文件");
                    }
                }else {
                    MyAppliction.showToast("请输入音乐名字");
                }




                break;
        }
    }
    private void showDialog() {
        View view = this.getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = this.getWindowManager().getDefaultDisplay().getHeight();
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
        pictureDialogButton.setText("音乐库");
        photographDialogButton.setText("录音");
        pictureDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromRecord = new Intent();
                intentFromRecord.setType("audio/*");
                intentFromRecord.setAction(Intent.ACTION_GET_CONTENT);
                intentFromRecord.putExtra("return-data", true);
                startActivityForResult(intentFromRecord, 5);
                dialog.dismiss();
            }
        });
        photographDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, 5);
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
            case 5:
                if (null != data) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    } else {
                        // 音频捕获并保存到指定的fileUri意图
                        // Toast.makeText(this, "Music saved to:\n" + data.getData(),
                        //  Toast.LENGTH_LONG).show();
                        Cursor c = getContentResolver().query(uri,
                                new String[] { MediaStore.MediaColumns.DATA },
                                null, null, null);
                        if (c != null && c.moveToFirst()) {
                            // tv.setText("上传中请等待......");
                            // pb.setVisibility(View.VISIBLE);
                            musicPath = c.getString(0);
                            if (!TextUtils.isEmpty(musicPath)){
                                musicLayout.setVisibility(View.VISIBLE);
                                File file=new File(musicPath);
                                showMusicTv.setText(file.getName());
                            }
                        }

                    }
                }

                break;
        }

    }
}
