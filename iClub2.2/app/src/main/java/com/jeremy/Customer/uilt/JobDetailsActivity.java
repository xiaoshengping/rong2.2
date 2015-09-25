package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.os.Bundle;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.PictureAdapter;
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

}

