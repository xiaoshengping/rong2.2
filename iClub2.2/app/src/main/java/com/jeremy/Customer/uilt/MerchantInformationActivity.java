package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.LoadingDialog;
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
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;

public class MerchantInformationActivity extends ActionBarActivity implements View.OnClickListener {

    private static final int REQUEST_PICK = 0;
    private GridView gridview;
    private GridAdapter adapter;
    private ArrayList<String> selectedPicture = new ArrayList<String>();


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

    private void initView() {
        loadingDialog=new LoadingDialog(this,"保存数据.....");
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加商家信息");
        saveText.setVisibility(View.VISIBLE);
        saveText.setOnClickListener(this);
        saveText.setText("保存");
        addCompanyImage.setOnClickListener(this);
        oneselfKnownLayout.setOnClickListener(this);
        selectDatabase();
        intiPictureView();
        if (getIntent().getStringExtra("merchantFalg").equals("previewMerchant")){
          BMerchantValueBean bMerchantValueBean= (BMerchantValueBean) getIntent().getSerializableExtra("bMerchantValueBean");
           if (bMerchantValueBean!=null){
               companyNameEt.setText(bMerchantValueBean.getBEcompanyName());
               companyPhoneEv.setText(bMerchantValueBean.getBEphone());
               companyEmailEv.setText(bMerchantValueBean.getBEemail());
               companyHttpEv.setText(bMerchantValueBean.getBEweb());
               companyAddressEv.setText(bMerchantValueBean.getBEaddress());
               userOnselfText.setText(bMerchantValueBean.getBEcompanyInfo());
               userOnselfText.setTextColor(getResources().getColor(R.color.textColor242424));
           }

        }
    }
    private void intiPictureView() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new GridAdapter();
        gridview.setAdapter(adapter);
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
                startActivityForResult(new Intent(MerchantInformationActivity.this, SelectPictureActivity.class), REQUEST_PICK);
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
           requestParams.addBodyParameter("uid",uid);
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

                                         if (selectedPicture.size()!=0){
                                            // Log.e("添加商家信息", selectedPicture.size() + "");

                                             for (int i = 0; i <selectedPicture.size() ; i++) {
                                                 HttpUtils httpUtils=new HttpUtils();
                                                 RequestParams requestParams1=new RequestParams();
                                                 requestParams1.addBodyParameter("pid",pid);
                                                 requestParams1.addBodyParameter("BEicon_file",new File(selectedPicture.get(i)));

                                                 httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddBePicture(), requestParams1, new RequestCallBack<String>() {
                                                     @Override
                                                     public void onSuccess(ResponseInfo<String> responseInfo) {
                                                         Log.e("添加商家信息", responseInfo.result);
                                                         MyAppliction.showToast("保存成功");
                                                         loadingDialog.dismiss();
                                                         finish();
                                                     }

                                                     @Override
                                                     public void onFailure(HttpException e, String s) {
                                                         loadingDialog.dismiss();
                                                     }
                                                 });


                                             }


                                         }else {
                                             MyAppliction.showToast("保存成功");
                                             finish();
                                         }
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        Log.e("添加商家信息onFailure", s);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
           ArrayList<String> list= ((ArrayList<String>)data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE));
            if (list!=null&&list.size()!=0){
                selectedPicture = (ArrayList<String>) data
                        .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
                adapter.notifyDataSetChanged();
            }

            //Log.e("selectedPicture",selectedPicture.size()+"");
        }else if (requestCode==INFOLT_HINT_DATA){
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
    class GridAdapter extends BaseAdapter {
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(250, 250);

        @Override
        public int getCount() {
            return selectedPicture.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ImageView(MerchantInformationActivity.this);
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                convertView.setLayoutParams(params);
            }
            ImageLoader.getInstance().displayImage("file://" + selectedPicture.get(position),
                    (ImageView) convertView);
            return convertView;
        }

    }

}
