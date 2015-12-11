package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.MerchantPicyureAdapter;
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

public class MerchantAddPictrueActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;

    @ViewInject(R.id.show_picture_text)
    private TextView showPictureText;
    @ViewInject(R.id.show_pciture_text_one)
    private TextView showPictureTextOne;
    @ViewInject(R.id.show_picture_gridview)
    private GridView showPictureGridview;
    @ViewInject(R.id.show_picture_layout)
    private LinearLayout showPictureLayout;
    @ViewInject(R.id.show_picture_one_layout)
    private LinearLayout showPictureOneLayout;

    private static final int REQUEST_PICK = 0;
    private GridView gridview;
    private GridAdapter adapter;
    private ArrayList<String> selectedPicture = new ArrayList<String>();

    private LoadingDialog loadingDialog;
    private String pid;
    private BMerchantValueBean bMerchantValueBean;
    private boolean isShowDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_add_pictrue);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        initView();



    }

    private void initView() {
        loadingDialog=new LoadingDialog(this,"正在上传.....");
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加");
        saveText.setVisibility(View.VISIBLE);
        saveText.setOnClickListener(this);
        saveText.setText("上传");
        intiPictureView();
        selectDatabase();
        bMerchantValueBean= (BMerchantValueBean) getIntent().getSerializableExtra("merchantPictrue");
        if (bMerchantValueBean!=null){
            final MerchantPicyureAdapter resumePictureAdapter=new MerchantPicyureAdapter(bMerchantValueBean.getBEpicture(),this);
            showPictureGridview.setAdapter(resumePictureAdapter);
            resumePictureAdapter.notifyDataSetChanged();
            if (bMerchantValueBean.getBEpicture().size()!=0){
                showPictureOneLayout.setVisibility(View.VISIBLE);

            }
            showPictureGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (isShowDelete) {
                        isShowDelete = false;
                    } else {
                        isShowDelete = true;
                    }
                    resumePictureAdapter.setIsShowDelete(isShowDelete);
                    return  true;
                }
            });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            ArrayList<String> list = ((ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE));
            if (list != null && list.size() != 0) {
                showPictureLayout.setVisibility(View.VISIBLE);
                selectedPicture = (ArrayList<String>) data
                        .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
                adapter.notifyDataSetChanged();
            }
        }
    }


    public void selectPicture(View view) {
        startActivityForResult(new Intent(this, SelectPictureActivity.class), REQUEST_PICK);


    }
    //查询数据库
    private void selectDatabase() {
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            pid = cursor.getString(1);


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
        }

    }

    private void saveData() {
        if (selectedPicture.size()!=0){
            // Log.e("添加商家信息", selectedPicture.size() + "");

            for (int i = 0; i <selectedPicture.size() ; i++) {
                HttpUtils httpUtils=new HttpUtils();
                RequestParams requestParams1=new RequestParams();
                requestParams1.addBodyParameter("pid",pid);
                requestParams1.addBodyParameter("BEicon_file",new File(selectedPicture.get(i)));
                loadingDialog.show();
                httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddBePicture(), requestParams1, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        MyAppliction.showToast("上传成功");
                        loadingDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        loadingDialog.dismiss();
                    }
                });


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
                convertView = new ImageView(MerchantAddPictrueActivity.this);
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                convertView.setLayoutParams(params);
            }
            ImageLoader.getInstance().displayImage("file://" + selectedPicture.get(position),
                    (ImageView) convertView);
            return convertView;
        }

    }


}
