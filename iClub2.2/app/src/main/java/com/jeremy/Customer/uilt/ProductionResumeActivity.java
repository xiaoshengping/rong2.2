package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

public class ProductionResumeActivity extends ActionBarActivity implements View.OnClickListener {
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
    @ViewInject(R.id.add_picture_tv)
    private TextView addPictureTv;
    @ViewInject(R.id.add_video_tv)
    private TextView addVideoTv;
    @ViewInject(R.id.add_music_tv)
    private TextView addMusicTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_resume);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        intiPictureView();
        initView();



    }

    private void initView() {
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加作品");
        saveText.setVisibility(View.VISIBLE);
        saveText.setText("完成");
        addPictureTv.setOnClickListener(this);
        addVideoTv.setOnClickListener(this);
        addMusicTv.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.save_text:

                break;
            case R.id.add_picture_tv:
               // Log.e("jjsdjfhfh",selectedPicture.get(0)+selectedPicture.get(1)+selectedPicture.get(2)+selectedPicture.get(3));
                break;
            case R.id.add_video_tv:

                break;
            case R.id.add_music_tv:

                break;


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

    public void selectPicture(View view) {
        startActivityForResult(new Intent(this, SelectPictureActivity.class), REQUEST_PICK);


    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            selectedPicture = (ArrayList<String>) data
                    .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
            adapter.notifyDataSetChanged();
        }
    }


    class GridAdapter extends BaseAdapter {
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(100, 100);

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
                convertView = new ImageView(ProductionResumeActivity.this);
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                convertView.setLayoutParams(params);
            }
            ImageLoader.getInstance().displayImage("file://" + selectedPicture.get(position),
                    (ImageView) convertView);
            return convertView;
        }

    }

}
