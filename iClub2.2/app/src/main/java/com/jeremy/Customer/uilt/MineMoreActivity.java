package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
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
        }
    }
}
