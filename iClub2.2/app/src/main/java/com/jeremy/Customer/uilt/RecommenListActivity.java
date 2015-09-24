package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.RecommendListAdater;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.view.MyTitleBar;

public class RecommenListActivity extends Activity {

    private MyTitleBar mytitle;
    private ListView recommend_list;
    private int identi = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);
        inti();

    }

    //初始化
    private void inti() {

        mytitle = (MyTitleBar) findViewById(R.id.mytitle);
        recommend_list = (ListView)findViewById(R.id.recommend_list);

        Bundle bundle = this.getIntent().getExtras();
        identi = bundle.getInt("Ident");
        if (identi == Identification.ACTIVITY) {
            intiActivity();
        } else if (identi == Identification.TALENTS) {
            intiTalents();
        } else if (identi == Identification.PROSITION) {
            intiProsition();
        }

        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (identi == Identification.ACTIVITY) {
                    Intent intent = new Intent();
                    intent.setClass(RecommenListActivity.this, ActivityDetailActivity.class);
                    startActivity(intent);
                } else if (identi == Identification.TALENTS) {
                    Intent intent = new Intent();
                    intent.setClass(RecommenListActivity.this, TalentsDetailsActivity.class);
                    startActivity(intent);
                } else if (identi == Identification.PROSITION) {

                }
            }
        });

    }

    //初始化活动列表
    private void intiActivity() {
        mytitle.setTextViewText("活动");
        RecommendListAdater adater = new RecommendListAdater(this,identi);
        recommend_list.setAdapter(adater);
    }

    //初始化人才列表
    private void intiTalents() {
        mytitle.setTextViewText("热门人才");
        RecommendListAdater adater = new RecommendListAdater(this,identi);
        recommend_list.setAdapter(adater);
    }

    //初始化职位列表
    private void intiProsition() {
        mytitle.setTextViewText("热门职位");
        RecommendListAdater adater = new RecommendListAdater(this,identi);
        recommend_list.setAdapter(adater);
    }

    public void back(View v) {
        finish();
    }


}
