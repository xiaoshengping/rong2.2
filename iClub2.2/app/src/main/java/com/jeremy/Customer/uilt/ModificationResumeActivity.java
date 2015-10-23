package com.jeremy.Customer.uilt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.fragment.ModificationInformationFragment;
import com.jeremy.Customer.fragment.ModificationProductionFragment;
import com.jeremy.Customer.fragment.ModificationResumeTabAdapter;
import com.jeremy.Customer.http.ImageUtil;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.CustomImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ModificationResumeActivity extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    private ModificationProductionFragment modificationProductionFragment;
    private ModificationInformationFragment modificationInformationFragment;

    @ViewInject(R.id.resume_radioGroup)
    private RadioGroup resumeRadioGroup;
    @ViewInject(R.id.oneself_informaction_rb)
    private RadioButton oneselfInformactionRb;
    @ViewInject(R.id.message_layout)
    private LinearLayout messageLayout;

    @ViewInject(R.id.usericon_background_iv)
    private CustomImageView customImageView;
    @ViewInject(R.id.resumeZhName_Et)
    private EditText resumeZhNameEt;
    @ViewInject(R.id.sex_radio_group)
    private RadioGroup sexRadioGroup;
    @ViewInject(R.id.boy_radio_button)
    private RadioButton boyRadioButton;
    @ViewInject(R.id.girl_radio_button)
    private RadioButton girlRadioButton;
    @ViewInject(R.id.resume_age_et)
    private TextView resumeAgeTv;
    @ViewInject(R.id.job_category_et)
    private EditText jobCategoryEt;
    @ViewInject(R.id.job_category_name_et)
    private EditText jobCategoryNameEt;
    @ViewInject(R.id.resume_address_et)
    private EditText resumeAddressEt;
    @ViewInject(R.id.return_tv)
    private TextView returnTv;
    @ViewInject(R.id.save_resume_tv)
    private TextView saveResumeTv;


    private String position;
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private File screenshotFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private ResumeValueBean resumeValueBeans;
    private String sex;

    private int selectYear;
    private int selectMonthOfYear;
    private int selectDayOfMonth;
    private  int year1;
    private  int monthOfYear;
    private  int dayOfMonth;
    private String  age;
    private DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_resume);
        ViewUtils.inject(this);
        init();


    }

    private void init() {
        initView();


    }

    private void initView() {
        resumeRadioGroup.setOnCheckedChangeListener(this);
        oneselfInformactionRb.setChecked(true);
        returnTv.setOnClickListener(this);
        customImageView.setOnClickListener(this);
        sexRadioGroup.setOnCheckedChangeListener(this);
        List<Fragment> listFragment=new ArrayList<>();
        resumeAgeTv.setOnClickListener(this);
        saveResumeTv.setOnClickListener(this);
        modificationInformationFragment=new ModificationInformationFragment();
        modificationProductionFragment=new ModificationProductionFragment();

        listFragment.add(modificationInformationFragment);
        listFragment.add(modificationProductionFragment);
        resumeValueBeans= (ResumeValueBean) getIntent().getSerializableExtra("resumeValueBeans");
        String positions=getIntent().getStringExtra("position");
        ModificationResumeActivity.this.setPosition(positions);
        ModificationResumeTabAdapter fragmentInviteTabAdapter=new ModificationResumeTabAdapter(ModificationResumeActivity.this,listFragment,R.id.resume_fragment_layout,resumeRadioGroup);
           if (resumeValueBeans!=null){
               MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl+resumeValueBeans.getUsericon(),customImageView,MyAppliction.RoundedOptions);
               resumeZhNameEt.setText(resumeValueBeans.getResumeZhName());

               if (resumeValueBeans.getResumeSex().equals(0)){
                   boyRadioButton.setChecked(true);
               }else if(resumeValueBeans.getResumeSex().equals(1)){
                   girlRadioButton.setChecked(true);
               }
               resumeAgeTv.setText(resumeValueBeans.getResumeAge()+"");
               //jobCategoryEt.setText(resumeValueBeans.getResumeJobCategory());
               jobCategoryNameEt.setText(resumeValueBeans.getResumeJobCategoryName());
               resumeAddressEt.setText(resumeValueBeans.getResumeWorkPlace());
           }
        //获取系统时间
        Calendar calendar = Calendar.getInstance();
        year1 = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DATE);

    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.boy_radio_button:
                sex="0";
                break;
            case R.id.girl_radio_button:
                sex="1";
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.return_tv:
                finish();
                break;
            case R.id.save_resume_tv:
                 saveData();
                break;
            case R.id.usericon_background_iv:
                showDialog();
                break;
            case R.id.resume_age_et:
                datePickerDialogData().show();
                break;



        }
    }

    private void saveData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        String  touXiangPath = screenshotFile.getAbsolutePath();
        if (!TextUtils.isEmpty(resumeValueBeans.getResumeid()+"")){
            requestParams.addBodyParameter("resumeid", resumeValueBeans.getResumeid() + "");
             if (screenshotFile.exists()){
                 requestParams.addBodyParameter("usericon",new File(touXiangPath));
             }
            if (!TextUtils.isEmpty(resumeZhNameEt.getText().toString())){
                requestParams.addBodyParameter("resumeZhName",resumeZhNameEt.getText().toString());
               if (!TextUtils.isEmpty(sex)){
                   requestParams.addBodyParameter("resumeSex",sex);
                   if (selectYear!=0&&selectMonthOfYear!=0&&selectDayOfMonth!=0){
                       requestParams.addBodyParameter("birthday", selectYear + "-" + selectMonthOfYear + "-" + selectDayOfMonth);
                   }
                    if (!TextUtils.isEmpty(jobCategoryNameEt.getText().toString())){
                        requestParams.addBodyParameter("resumeJobName",jobCategoryNameEt.getText().toString());

                        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCompileResume(),requestParams, new RequestCallBack<String >() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.e("jjdjjff",responseInfo.result);
                                finish();
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }
                        });
                    }else {
                       MyAppliction.showToast("请输入职业名称");

                    }
               }
            }else {
                MyAppliction.showToast("请输入姓名");
            }
        }







    }

    //日期
    public DatePickerDialog datePickerDialogData() {


        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                age = (year1 - year)+"";
                //Log.e("age00000",age+"");
                if (Integer.valueOf(age)>0){
                    resumeAgeTv.setText(age);
                }else {
                    Toast.makeText(ModificationResumeActivity.this, "亲,您设置的年龄要大于0哦!", Toast.LENGTH_LONG).show();
                }
                selectYear=year;
                selectMonthOfYear=monthOfYear;
                selectDayOfMonth =dayOfMonth;
            }
        }, year1, monthOfYear, dayOfMonth);
        return datePickerDialog;

    }
    private void showDialog() {
        View view = this.getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = this.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button pictureDialogButton= (Button) view.findViewById(R.id.picture_dialog_button);
        Button photographDialogButton= (Button) view.findViewById(R.id.photograph_dialog_button);
        Button cancelDialogButton= (Button) view.findViewById(R.id.cancel_dialog_button);
        pictureDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                //intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        photographDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 指定调用相机拍照后照片的储存路径
                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(tempFile));
                startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
                dialog.dismiss();
            }
        });
        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 将进行剪裁后的图片传递到下一个界面上
    private void sentPicToNext(Intent picdata) {
        Bundle bundle = picdata.getExtras();

        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            if (photo == null) {
                customImageView.setImageResource(R.mipmap.ic_launcher);
            } else {
                Bitmap zoomBitmap = ImageUtil.zoomBitmap(photo, 100, 100);
                //获取圆角图片
                Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(zoomBitmap, 200.0f);
                //获取倒影图片
                //Bitmap reflectBitmap = ImageUtil.createReflectionImageWithOrigin(roundBitmap);
                customImageView.setImageBitmap(roundBitmap);
//                设置文本内容为    图片绝对路径和名字
                //text.setText(tempFile.getAbsolutePath());
                //Log.e("tempFile",tempFile.getAbsolutePath());


                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(screenshotFile);
                    if (null != fos) {
                        photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();

                        //  Toast.makeText(ResumeActivity.this, "截屏文件已保存至SDCard/AndyDemo/ScreenImage/下", Toast.LENGTH_LONG).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] photodata = baos.toByteArray();
                //System.out.println(photodata.toString());

            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
                startPhotoZoom(Uri.fromFile(tempFile));
                break;
            case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null)
                    startPhotoZoom(data.getData());
                break;
            case PHOTO_REQUEST_CUT:// 返回的结果
                if (data != null)
                    // setPicToView(data);
                    sentPicToNext(data);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
