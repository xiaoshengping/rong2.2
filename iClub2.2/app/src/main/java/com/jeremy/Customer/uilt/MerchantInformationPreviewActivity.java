package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.BMerchantValueBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MerchantInformationPreviewActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;

    @ViewInject(R.id.company_name_tv)
    private TextView companyNmeTv;
    @ViewInject(R.id.company_phone_tv)
    private TextView companyPhoneTv;
    @ViewInject(R.id.company_email_tv)
    private TextView companyEmailTv;
    @ViewInject(R.id.company_http_tv)
    private TextView companyHttpTv;
    @ViewInject(R.id.company_address_tv)
    private TextView companyAdressTv;
    @ViewInject(R.id.resumeInfo_tv)
    private TextView merchantInfoTv;
    @ViewInject(R.id.oneself_more_layout)
    private LinearLayout MerchantMoreLayout;
    @ViewInject(R.id.oneself_more_tv)
    private TextView MerchantMoreTv;

    private String uid;
    private BMerchantValueBean bMerchantValueBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_information_preview);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        initView();
    }

    private void initView() {
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("商家信息");
        saveText.setVisibility(View.VISIBLE);
        saveText.setText("编辑");
        saveText.setOnClickListener(this);
        MerchantMoreTv.setOnClickListener(this);
        selectDatabase();
        if (!TextUtils.isEmpty(uid)){
            initData(uid);
        }
        //Log.e("lines.......",merchantInfoTv.getLineCount()+"");
        if (merchantInfoTv.getLineCount()>0&&merchantInfoTv.getLineCount()<=4){
            MerchantMoreLayout.setVisibility(View.GONE);
            merchantInfoTv.setLines(merchantInfoTv.getLineCount());
        }else if (merchantInfoTv.getLineCount()==0){
            MerchantMoreLayout.setVisibility(View.GONE);
            merchantInfoTv.setText("暂无公司介绍");
        }else {
            MerchantMoreLayout.setVisibility(View.VISIBLE);
            merchantInfoTv.setLines(4);
        }


    }

    //查询数据库
    private void selectDatabase() {
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }

        cursor.close();
        db.close();



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData(uid);
    }

    private void initData(String uid) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("uid",uid);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAcquireMerchant(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<BMerchantValueBean> parmeBean= JSONObject.parseObject(responseInfo.result,new TypeReference<ParmeBean<BMerchantValueBean>>(){});
                if ( parmeBean.getState().equals("success")){
                  bMerchantValueBean=parmeBean.getValue();
                    if (bMerchantValueBean!=null){
                        companyNmeTv.setText(bMerchantValueBean.getBEcompanyName());
                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEphone())){
                            companyPhoneTv.setText(bMerchantValueBean.getBEphone());
                        }else {
                            companyPhoneTv.setText("********");
                        }
                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEemail())){
                            companyEmailTv.setText(bMerchantValueBean.getBEemail());
                        }else {
                            companyEmailTv.setText("********");
                        }
                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEweb())){
                            companyHttpTv.setText(bMerchantValueBean.getBEweb());
                        }else {
                            companyHttpTv.setText("********");
                        }
                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEaddress())){
                            companyAdressTv.setText(bMerchantValueBean.getBEaddress());
                        }else {
                            companyAdressTv.setText("********");
                        }

                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEcompanyInfo())){
                            merchantInfoTv.setText(bMerchantValueBean.getBEcompanyInfo());
                        }else {
                            companyAdressTv.setText("********");
                        }


                    }



                }



            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:
                Intent merchantIntent=new Intent(this, MerchantInformationActivity.class);
                merchantIntent.putExtra("merchantFalg","previewMerchant");
                merchantIntent.putExtra("bMerchantValueBean",bMerchantValueBean);
                startActivity(merchantIntent);
                break;
            case R.id.oneself_more_tv:
                merchantInfoTv.setLines(merchantInfoTv.getLineCount());
                break;



        }



    }
}
