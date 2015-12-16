package com.jeremy.Customer.uilt;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

public class MerchantInformationActivity extends ActionBarActivity implements View.OnClickListener {




    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;

    @ViewInject(R.id.company_name_et)
    private EditText companyNameEt;
    @ViewInject(R.id.company_phone_tv)
    private EditText companyPhoneEv;
    @ViewInject(R.id.company_email_tv)
    private EditText companyEmailEv;
    @ViewInject(R.id.company_http_tv)
    private EditText companyHttpEv;
    @ViewInject(R.id.company_address_tv)
    private EditText companyAddressEv;
    @ViewInject(R.id.oneself_known_layout)
    private LinearLayout oneselfKnownLayout;
    @ViewInject(R.id.add_company_image)
    private ImageView addCompanyImage;
    @ViewInject(R.id.onself_text_tv)
    private TextView userOnselfText;

    private static final int INFOLT_HINT_DATA=7;//公司介绍
    private String uid;
    private String pid;
    private LoadingDialog loadingDialog;
    private BMerchantValueBean bMerchantValueBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_information);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        initView();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (getIntent().getStringExtra("merchantFalg").equals("previewMerchant")){
            initData(uid);

        }
    }

    private void initView() {
        loadingDialog=new LoadingDialog(this,"保存数据.....");
        tailtReturnTv.setOnClickListener(this);
        if (getIntent().getStringExtra("merchantFalg").equals("addMerchant")) {
            tailtText.setText("添加");
        }else {
            tailtText.setText("编辑");
        }

        saveText.setVisibility(View.VISIBLE);
        saveText.setOnClickListener(this);
        saveText.setText("保存");
        addCompanyImage.setOnClickListener(this);
        oneselfKnownLayout.setOnClickListener(this);
        selectDatabase();

        if (getIntent().getStringExtra("merchantFalg").equals("previewMerchant")){
            initData(uid);

        }
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
                        companyNameEt.setText(bMerchantValueBean.getBEcompanyName());
                        companyPhoneEv.setText(bMerchantValueBean.getBEphone());
                        companyEmailEv.setText(bMerchantValueBean.getBEemail());
                        companyHttpEv.setText(bMerchantValueBean.getBEweb());
                        companyAddressEv.setText(bMerchantValueBean.getBEaddress());
                        userOnselfText.setText(bMerchantValueBean.getBEcompanyInfo());
                        userOnselfText.setTextColor(getResources().getColor(R.color.textColor242424));
                        loadingDialog.dismiss();


                    }


                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                showExitGameAlert("网络异常，请稍后操作!", MerchantInformationActivity.this);
                loadingDialog.dismiss();
                MyAppliction.showToast("网络异常，请稍后...");

            }
        });



    }

    //对话框
    public  void showExitGameAlert(String text, final Context app) {
        final AlertDialog dlg = new AlertDialog.Builder(app).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.tishi_exit_dialog);
        TextView tailte = (TextView) window.findViewById(R.id.tailte_tv);
        tailte.setText(text);
        // 关闭alert对话框架
        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                dlg.cancel();
            }
        });
    }




    //查询数据库
    private void selectDatabase() {
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            pid = cursor.getString(1);
            uid=cursor.getString(0);

        }

        cursor.close();
        db.close();



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:
                saveData();
                break;
            case R.id.add_company_image:
                Intent intent=new Intent(MerchantInformationActivity.this,MerchantAddPictrueActivity.class);
                if (getIntent().getStringExtra("merchantFalg").equals("previewMerchant")){
                    if (bMerchantValueBean!=null){
                        intent.putExtra("merchantPictrue",bMerchantValueBean);
                    }

                }

                startActivity(intent);
                break;
            case R.id.oneself_known_layout:
                Intent infoIntent = new Intent(MerchantInformationActivity.this, OneselfExperienceActivity.class);  //方法1
                infoIntent.putExtra("hintData","infoIntent");
                if (!TextUtils.isEmpty(userOnselfText.getText().toString())){
                    infoIntent.putExtra("content",userOnselfText.getText().toString());
                }
                startActivityForResult(infoIntent, INFOLT_HINT_DATA);
                break;

        }

    }

    private void saveData() {
        HttpUtils httpUtils=new HttpUtils();
        final RequestParams requestParams=new RequestParams();
        String companyName=companyNameEt.getText().toString();
        String companyPhone=companyPhoneEv.getText().toString();
        String companyEmail=companyEmailEv.getText().toString();
        String companyHttp=companyHttpEv.getText().toString();
        String companyAddress=companyAddressEv.getText().toString();
        String userOnself=userOnselfText.getText().toString();

        if (!TextUtils.isEmpty(uid)){
           requestParams.addBodyParameter("uid", uid);
            if (!TextUtils.isEmpty(companyName)){
                requestParams.addBodyParameter("BEcompanyName", companyName);
            if (!TextUtils.isEmpty(companyPhone)){
                requestParams.addBodyParameter("BEphone",companyPhone);
                if (!TextUtils.isEmpty(companyEmail)){
                    requestParams.addBodyParameter("BEemail",companyEmail);
                    if (!TextUtils.isEmpty(companyHttp)){
                        requestParams.addBodyParameter("BEweb",companyHttp);
                        if(!TextUtils.isEmpty(companyAddress)){
                            requestParams.addBodyParameter("BEaddress",companyAddress);
                            if (!TextUtils.isEmpty(userOnself)){
                                requestParams.addBodyParameter("BEcompanyInfo", userOnself);
                                loadingDialog.show();
                                httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getUpdateMerchant(), requestParams, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {

                                        update(uid,companyNameEt.getText().toString());
                                        MyAppliction.showToast("保存成功");
                                        loadingDialog.dismiss();
                                        finish();

                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        MyAppliction.showToast("网络异常，请稍后重试...");
                                        loadingDialog.dismiss();
                                    }
                                });
                            }else {
                                MyAppliction.showToast("请输入公司介绍");
                            }

                        }else {
                            MyAppliction.showToast("请输入详情地址");
                        }
                    }else {
                        MyAppliction.showToast("请输入公司官网");
                    }

                }else {
                    MyAppliction.showToast("请输入电子邮箱");
                }
            }else {
                MyAppliction.showToast("请输入公司电话号码");
            }
        }else {
            MyAppliction.showToast("请输入公司名称");

        }
        }












    }
    /**
     * 更新公司名字记录的
     */
    public void update(String uid,String name){
        SQLhelper sqLhelper= new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLhelper.COMPANYNAME, name);
        db.update(SQLhelper.tableName, contentValues,
                "uid=?",
                new String[]{uid});
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode==0){
           ArrayList<String> list= ((ArrayList<String>)data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE));
            if (list!=null&&list.size()!=0){
                selectedPicture = (ArrayList<String>) data
                        .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
                adapter.notifyDataSetChanged();
            }

            //Log.e("selectedPicture",selectedPicture.size()+"");
        }else*/ if (requestCode==INFOLT_HINT_DATA){
            if (data.getStringExtra("infoIntent").toString().equals("notData")){
                if (userOnselfText.getText().toString().equals("介绍一下自己")){
                    userOnselfText.setText("介绍一下自己");
                    userOnselfText.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                }else {
                    userOnselfText.setText(userOnselfText.getText().toString());
                    userOnselfText.setTextColor(getResources().getColor(R.color.textColor242424));
                }

            }else if (data.getStringExtra("infoIntent").toString().equals("data")){
                if (userOnselfText.getText().toString().equals("介绍一下自己")){
                    userOnselfText.setText("介绍一下自己");
                    userOnselfText.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                }else {
                    userOnselfText.setText(userOnselfText.getText().toString());
                    userOnselfText.setTextColor(getResources().getColor(R.color.textColor242424));
                }

            }else {
                userOnselfText.setText(data.getStringExtra("infoIntent").toString());
                userOnselfText.setTextColor(getResources().getColor(R.color.textColor242424));
            }

        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            hintKbTwo();
        }

        return super.onTouchEvent(event);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
