package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MineMoreActivity extends ActionBarActivity implements View.OnClickListener {

             @ViewInject(R.id.tailt_text)
             private TextView tailtText;
             @ViewInject(R.id.tailt_return_tv)
             private TextView tailtReturnTv;
             @ViewInject(R.id.modification_psw_tv)
             private TextView modificationPswTv;
             @ViewInject(R.id.feedback_tv)
             private TextView feedBackTv;
             @ViewInject(R.id.about_tv)
             private  TextView aboutTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_more);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();
    }

    private void intiView() {
        tailtText.setText("更多");
        tailtReturnTv.setOnClickListener(this);
        modificationPswTv.setOnClickListener(this);
        feedBackTv.setOnClickListener(this);
        aboutTextView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper=new SQLhelper(MineMoreActivity.this);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query("user", null, null, null, null, null, null);
        String uid=null;
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        switch (v.getId()){

            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.modification_psw_tv:
                if (!TextUtils.isEmpty(uid)){
                    Intent modificationPswIntent=new Intent(MineMoreActivity.this,ModificationPswActivity.class);
                    startActivity(modificationPswIntent);
                }else {
                    MyAppliction.showToast("你还没有登录哦!");
                }
                break;
            case R.id.feedback_tv:
              Intent feedBackIntent=new Intent(MineMoreActivity.this,FeedBackActivity.class);
                startActivity(feedBackIntent);
                break;
            case R.id.about_tv:
                Intent aboutIntent=new Intent(MineMoreActivity.this,AbouyMessageActivity.class);
                startActivity(aboutIntent);
                //showrCaiGameAlert();
                break;
        }
    }

    //关于
    private void showrCaiGameAlert() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.about_layout);
        ImageView showRoleIv = (ImageView) window.findViewById(R.id.show_role_iv);
        showRoleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }

}
