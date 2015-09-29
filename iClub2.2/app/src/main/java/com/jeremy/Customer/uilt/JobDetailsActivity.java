package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.PictureAdapter;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.MyDialog;
import com.jeremy.Customer.view.MyGridView;


public class JobDetailsActivity extends Activity{

    private MyGridView company_picture_production_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_details);
        initPictureProduction();
    }

    //初始化图片作品
    private void initPictureProduction(){
        company_picture_production_list = (MyGridView)findViewById(R.id.company_picture_production_list);
        PictureAdapter pictureAdapter = new PictureAdapter(this);
        company_picture_production_list.setAdapter(pictureAdapter);
    }

    private MyDialog dialog2;
    //提示框
    private void dialog() {
        dialog2 = new MyDialog(this, Identification.TOOLTIP,Identification.NETWORKANOMALY);
        dialog2.setDetermine(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

        dialog2.show();
    }

    public void back(View v) {
        finish();
    }

}

