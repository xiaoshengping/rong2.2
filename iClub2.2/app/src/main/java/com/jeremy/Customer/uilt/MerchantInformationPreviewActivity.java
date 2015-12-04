package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.BMerchantValueBean;
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
    @ViewInject(R.id.show_merchant_picture_one)
    private ImageView showPictureOne;
    @ViewInject(R.id.show_merchant_picture_two)
    private ImageView showPictureTwo;
    @ViewInject(R.id.show_merchant_picture_three)
    private ImageView showPictureThree;
    @ViewInject(R.id.show_merchant_picture_four)
    private ImageView showPictureFour;

    private String uid;
    private BMerchantValueBean bMerchantValueBean;
    private LoadingDialog loadingDialog;

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
        showPictureOne.setOnClickListener(this);
        showPictureTwo.setOnClickListener(this);
        showPictureThree.setOnClickListener(this);
        showPictureFour.setOnClickListener(this);
        MerchantMoreLayout.setOnClickListener(this);
        loadingDialog=new LoadingDialog(this,"正在加载.....");
        selectDatabase();
        if (!TextUtils.isEmpty(uid)){
            initData(uid);
        }

        merchantInfoTv.post(new Runnable() {
            @Override
            public void run() {
                if (merchantInfoTv.getLineCount()>4){
                    merchantInfoTv.setLines(4);
                }

            }
        });



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
        requestParams.addBodyParameter("uid", uid);
        loadingDialog.show();
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAcquireMerchant(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<BMerchantValueBean> parmeBean = JSONObject.parseObject(responseInfo.result, new TypeReference<ParmeBean<BMerchantValueBean>>() {
                });
                if (parmeBean.getState().equals("success")) {
                    bMerchantValueBean = parmeBean.getValue();
                    if (bMerchantValueBean != null) {
                        companyNmeTv.setText(bMerchantValueBean.getBEcompanyName());
                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEphone())) {
                            companyPhoneTv.setText(bMerchantValueBean.getBEphone());
                        } else {
                            companyPhoneTv.setText("********");
                        }
                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEemail())) {
                            companyEmailTv.setText(bMerchantValueBean.getBEemail());
                        } else {
                            companyEmailTv.setText("********");
                        }
                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEweb())) {
                            companyHttpTv.setText(bMerchantValueBean.getBEweb());
                        } else {
                            companyHttpTv.setText("********");
                        }
                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEaddress())) {
                            companyAdressTv.setText(bMerchantValueBean.getBEaddress());
                        } else {
                            companyAdressTv.setText("********");
                        }

                        if (!TextUtils.isEmpty(bMerchantValueBean.getBEcompanyInfo())) {
                            merchantInfoTv.setText(bMerchantValueBean.getBEcompanyInfo());
                        } else {
                            companyAdressTv.setText("********");
                        }
                        if (bMerchantValueBean.getBEpicture().size() != 0) {
                            MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + bMerchantValueBean.getBEpicture().get(0).getPath(), showPictureOne, MyAppliction.options);
                            if (bMerchantValueBean.getBEpicture().size() > 1) {
                                MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + bMerchantValueBean.getBEpicture().get(1).getPath(), showPictureTwo, MyAppliction.options);
                                if (bMerchantValueBean.getBEpicture().size() > 2) {
                                    MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + bMerchantValueBean.getBEpicture().get(2).getPath(), showPictureThree, MyAppliction.options);
                                    if (bMerchantValueBean.getBEpicture().size() > 3) {
                                        MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + bMerchantValueBean.getBEpicture().get(3).getPath(), showPictureFour, MyAppliction.options);


                                    } else {
                                        showPictureFour.setVisibility(View.GONE);
                                    }

                                } else {
                                    showPictureThree.setVisibility(View.GONE);
                                    showPictureFour.setVisibility(View.GONE);
                                }

                            } else {
                                showPictureTwo.setVisibility(View.GONE);
                                showPictureThree.setVisibility(View.GONE);
                                showPictureFour.setVisibility(View.GONE);
                            }


                        } else {
                            showPictureOne.setVisibility(View.GONE);
                            showPictureTwo.setVisibility(View.GONE);
                            showPictureThree.setVisibility(View.GONE);
                            showPictureFour.setVisibility(View.GONE);
                        }
                        loadingDialog.dismiss();


                    }


                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                MyAppliction.showToast("网络异常...");
                saveText.setVisibility(View.GONE);
                MerchantMoreTv.setVisibility(View.GONE);
                loadingDialog.dismiss();
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
                merchantInfoTv.post(new Runnable() {
                    @Override
                    public void run() {
                        merchantInfoTv.setLines(merchantInfoTv.getLineCount());

                    }
                });
                MerchantMoreLayout.setVisibility(View.GONE);
                break;
            case R.id.show_merchant_picture_one:
                imageBrower(0,bMerchantValueBean);
                break;
            case R.id.show_merchant_picture_two:
                imageBrower(1,bMerchantValueBean);
                break;
            case R.id.show_merchant_picture_three:
                imageBrower(2,bMerchantValueBean);
                break;
            case R.id.show_merchant_picture_four:
                imageBrower(3,bMerchantValueBean);
                break;


        }



    }

    private void imageBrower(int position,BMerchantValueBean urls) {
        Intent intent = new Intent(MerchantInformationPreviewActivity.this, ImagePagerBeActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerBeActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerBeActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

}
