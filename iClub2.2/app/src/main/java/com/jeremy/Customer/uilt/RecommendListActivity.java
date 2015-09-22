package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.view.MyTitleBar;

public class RecommendListActivity extends Activity {

//    @ViewInject(R.id.mytitle)
//    private MyTitleBar mytitle;
    private MyTitleBar mytitle;
    private int identi = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);
        inti();

    }

    //初始化
    private void inti(){

        mytitle = (MyTitleBar)findViewById(R.id.mytitle);

        Bundle bundle = this.getIntent().getExtras();
        identi = bundle.getInt("Ident");
        if(identi== Identification.ACTIVITY){
            intiActivity();
        }else if(identi== Identification.TALENTS){
            intiTalents();
        }else if(identi== Identification.PROSITION){
            intiProsition();
        }
    }

    //初始化活动列表
    private void intiActivity(){
        mytitle.setTextViewText("活动");
    }

    //初始化人才列表
    private void intiTalents(){
        mytitle.setTextViewText("热门人才");
    }

    //初始化职位列表
    private void intiProsition(){
        mytitle.setTextViewText("热门职位");
    }

    public void back(View v){
        finish();
    }


}
