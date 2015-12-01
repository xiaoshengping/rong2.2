package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.ResumePictureAdapter;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.MyGridView;
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

public class AddPictureActivity extends ActionBarActivity implements View.OnClickListener {
    private static final int REQUEST_PICK = 0;
    private MyGridView gridview;
    private GridAdapter adapter;
    private ArrayList<String> selectedPicture = new ArrayList<String>();

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;

    @ViewInject(R.id.show_picture_gridview)
    private MyGridView showPictureGridView;
    @ViewInject(R.id.show_picture_text)
    private LinearLayout showPcitureText;
    @ViewInject(R.id.show_pciture_text_one)
    private LinearLayout showPcitureTextOne;


    private ResumeValueBean resumeValueBean;
    private LoadingDialog loadingDialog;
    private boolean isShowDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        intiPictureView();
        initView();


    }

    private void initView() {
        loadingDialog = new LoadingDialog(this,"正在上传图片……");
        resumeValueBean= (ResumeValueBean) getIntent().getSerializableExtra("resumeValueBean");
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加图片");
        saveText.setVisibility(View.VISIBLE);
        saveText.setOnClickListener(this);
        saveText.setText("上传");
        if (resumeValueBean!=null){

            final ResumePictureAdapter resumePictureAdapter=new ResumePictureAdapter(resumeValueBean.getResumePicture(),this);
            showPictureGridView.setAdapter(resumePictureAdapter);
            resumePictureAdapter.notifyDataSetChanged();
            if (resumeValueBean.getResumePicture().size()!=0){
                showPcitureTextOne.setVisibility(View.VISIBLE);
            }
            showPictureGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        gridview = (MyGridView) findViewById(R.id.gridview);
        adapter = new GridAdapter();
        gridview.setAdapter(adapter);
    }
    public void selectPicture(View view) {
        startActivityForResult(new Intent(this, SelectPictureActivity.class), REQUEST_PICK);


    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            showPcitureText.setVisibility(View.VISIBLE);
            selectedPicture = (ArrayList<String>) data
                    .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:
                if (getIntent().getSerializableExtra("fagle").equals("modifactionResume")){
                    if (!TextUtils.isEmpty(resumeValueBean.getResumeid()+"")&&selectedPicture.size()!=0){
                        for (int i = 0; i <selectedPicture.size() ; i++) {
                            savePictureData(resumeValueBean.getResumeid()+"",selectedPicture.get(i));
                        }
                    }else {
                        MyAppliction.showExitGameAlert("你还没有选择照片", AddPictureActivity.this);
                    }

                }else if (getIntent().getSerializableExtra("fagle").equals("productionResume")){
                    if (!TextUtils.isEmpty(getIntent().getStringExtra("resumeid").toString())&&selectedPicture.size()!=0){
                        for (int i = 0; i <selectedPicture.size() ; i++) {
                            savePictureData(getIntent().getStringExtra("resumeid").toString(),selectedPicture.get(i));
                        }
                    }else {
                        MyAppliction.showExitGameAlert("你还没有选择照片", AddPictureActivity.this);
                    }
                }


                break;
        }
    }

    private void savePictureData(String resumeid,String pictureFile) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid",resumeid);
        requestParams.addBodyParameter("picture",new File(pictureFile));
        loadingDialog.show();
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddPicture(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                loadingDialog.dismiss();
                finish();


            }

            @Override
            public void onFailure(HttpException e, String s) {
                loadingDialog.dismiss();
                MyAppliction.showToast("网络异常...");
            }
        });



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
                convertView = new ImageView(AddPictureActivity.this);
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                convertView.setLayoutParams(params);
            }

            ImageLoader.getInstance().displayImage("file://" + selectedPicture.get(position),
                    (ImageView) convertView);
            return convertView;
        }

    }
}
