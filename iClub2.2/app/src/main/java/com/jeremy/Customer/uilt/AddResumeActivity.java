package com.jeremy.Customer.uilt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.bean.mine.SaveResumeValueBean;
import com.jeremy.Customer.http.ImageUtil;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddResumeActivity extends ActionBarActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {

      @ViewInject(R.id.tailt_return_tv)
      private TextView tailtReturnTv;
      @ViewInject(R.id.tailt_text)
      private TextView tailtText;

    //上传头像
    @ViewInject(R.id.updata_image)
    private LinearLayout updataImage;
    @ViewInject(R.id.touxiang_image)
    private ImageView usericonIV;
    //自我介绍
    @ViewInject(R.id.oneself_known_layout)
    private LinearLayout oneselfLayout;
    private String userInfo;
    @ViewInject(R.id.onself_text_tv)
    private TextView userOnselfText;

    //工作经历
    @ViewInject(R.id.work_experience_layout)
    private LinearLayout workLayout;
    private String userWork;
    @ViewInject(R.id.work_experience_text_tv)
    private TextView workExpexteText;

    //个人信息
    @ViewInject(R.id.resumeZhName_et)
    private EditText resumeZhName;
    @ViewInject(R.id.resumeJobName_et)
    private EditText resumeJobName;
    @ViewInject(R.id.resumeQq_et)
    private EditText resumeQq;
    @ViewInject(R.id.resumeEmail_et)
    private EditText resumeEmail;

    @ViewInject(R.id.phone_textView_tv)
    private TextView phoneTextView;
    @ViewInject(R.id.touxiang_image)
    private ImageView touXiangIv;
    @ViewInject(R.id.resume_age_tv)
    private TextView resumeAgeTv;

    private String userName;
    private  String sex = null;
    private String userJobName;
    private  String userQq;
    private  String userEmail;
    private  String touXiangPath;
    @ViewInject(R.id.sex_radio_group)
    private RadioGroup sexRadioGroup;
    @ViewInject(R.id.boy_radio_button)
    private RadioButton boyRadioButton;
    @ViewInject(R.id.girl_radio_button)
    private RadioButton girlRadioButton;

    private String mobile;
    private   String uid=null;

    //下一步
    @ViewInject(R.id.next_layout)
    private LinearLayout nextLayout;
    @ViewInject(R.id.next_resume_tv)
    private TextView nextTextView;

    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private File screenshotFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private HttpUtils httpUtils;

    private ResumeValueBean resumeValueBean;
    private String resumeNuber;
    private RequestParams compileRequestParams;
    private RequestParams requestParams;
    private DatePickerDialog datePickerDialog;
    private  int year1;
    private  int monthOfYear;
    private  int dayOfMonth;
    private String  age;
    private int selectYear;
    private int selectMonthOfYear;
    private int selectDayOfMonth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resume);
        ViewUtils.inject(this);
        inti();

    }

    private void inti() {
        intiView();



    }

    private void intiView() {
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加简历");
        requestParams = new RequestParams();
        httpUtils = new HttpUtils();
        updataImage.setOnClickListener(this);
        oneselfLayout.setOnClickListener(this);
        workLayout.setOnClickListener(this);
        resumeAgeTv.setOnClickListener(this);
        sexRadioGroup.setOnCheckedChangeListener(this);
        nextTextView.setOnClickListener(this);
        boyRadioButton.setChecked(true);
        //获取系统时间
        Calendar calendar = Calendar.getInstance();
        year1 = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DATE);
        //resumeValueBeanData();   //列表数据
        selectDatabase();       //查询数据库
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.resume_age_tv:
                datePickerDialogData().show();
                break;
            case R.id.updata_image:
                showDialog();
                break;
            case R.id.oneself_known_layout:
                /*Intent infoIntent = new Intent(AddResumeActivity.this, ResumeMessageActivity.class);
                if (resumeValueBean!=null){
                    infoIntent.putExtra("caseData", "oneself");
                    infoIntent.putExtra("resumeInfoData",resumeValueBean);
                }else {
                    infoIntent.putExtra("caseData", "oneselfNo");
                }

                startActivityForResult(infoIntent, 7);*/
                break;
            case R.id.work_experience_layout:
                /*Intent workIntent = new Intent(AddResumeActivity.this, ResumeMessageActivity.class);

                if (resumeValueBean!=null){
                    workIntent.putExtra("caseData", "wor k");
                    workIntent.putExtra("resumeInfoData",resumeValueBean);
                } else{
                    workIntent.putExtra("caseData", "workNo");

                }
                startActivityForResult(workIntent, 8);*/

                break;



            case R.id.job_classfite_layout:
              /*  Intent intentClassfite = new Intent(AddResumeActivity.this, SelectedCityOrPositionActivity.class);  //方法1
                intentClassfite.putExtra("Status", areaBean.POSITION);
                intentClassfite.putExtra("Company",-1);
                startActivityForResult(intentClassfite, 12);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_not);*/
                break;
            case R.id.job_city_layout:
               /* Intent intentCity = new Intent(AddResumeActivity.this, SelectedCityOrPositionActivity.class);  //方法1
                intentCity.putExtra("Status", areaBean.PROVINCE);
                intentCity.putExtra("Company",-1);
                startActivityForResult(intentCity, 12);
                overridePendingTransition(R.anim.in_from_buttom, R.anim.out_to_not);*/
                break;
            case R.id.next_resume_tv:
                intiSaveData();
                break;


        }

        }

    private void intiSaveData() {
        userName = resumeZhName.getText().toString();
        userJobName = resumeJobName.getText().toString();
        userQq = resumeQq.getText().toString();
        userEmail = resumeEmail.getText().toString();
        touXiangPath = screenshotFile.getAbsolutePath();
            if (!tempFile.exists() || TextUtils.isEmpty(userName)
                    || TextUtils.isEmpty(userJobName) || TextUtils.isEmpty(userQq) || TextUtils.isEmpty(userEmail)
                    || TextUtils.isEmpty(userInfo) || TextUtils.isEmpty(userWork)
                    ||selectYear==0||selectMonthOfYear==0||selectDayOfMonth==0) {
                MyAppliction.showExitGameAlert("您填写的信息不全或错误", AddResumeActivity.this);
            } else {
                requestParams = new RequestParams();
                requestParams.addBodyParameter("resumeSex", sex);
                requestParams.addBodyParameter("uid", uid);
                //requestParams.addBodyParameter("resumeWorkExperience", userWork);
                //requestParams.addBodyParameter("resumeInfo", userInfo);
                requestParams.addBodyParameter("resumeEmail", userEmail);
                requestParams.addBodyParameter("resumeQq", userQq);
                requestParams.addBodyParameter("resumeJobName", userJobName);
                requestParams.addBodyParameter("resumeZhName", userName);
                requestParams.addBodyParameter("usericon", new File(touXiangPath));
                //requestParams.addBodyParameter("resumeJobCategory", job_classfite_num + "");
                //requestParams.addBodyParameter("resumeCityId", job_city_num + "");
                requestParams.addBodyParameter("resumeMobile", mobile);
                requestParams.addBodyParameter("birthday", selectYear + "-" + selectMonthOfYear + "-" + selectDayOfMonth);

                httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddResume(), requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        // Log.e("onSuccess", responseInfo.result);
                        String result = responseInfo.result;
                        if (result != null) {
                            ParmeBean<SaveResumeValueBean> parmeBean = JSONObject.parseObject(result, new TypeReference<ParmeBean<SaveResumeValueBean>>() {
                            });
                            if (parmeBean.getState().equals("success")) {
                                SaveResumeValueBean saveValueBean = parmeBean.getValue();
                                if (saveValueBean.getMessage().equals("success")) {
                                    MyAppliction.showToast("提交数据成功");
                                } else {
                                    MyAppliction.showToast(saveValueBean.getMessage());
                                }
                                /*Intent intent = new Intent(AddResumeActivity.this, NextResumeActivity.class);
                                intent.putExtra("resumeid", saveValueBean.getResumeid());
                                startActivity(intent);*/

                                finish();


                            }
                        }

                        //

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                        MyAppliction.showToast("网络出错了");

                    }
                });
            }


    }



    //查询数据库
    private void selectDatabase() {
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);
            mobile=cursor.getString(5);
        }
        if (!TextUtils.isEmpty(mobile)){


            phoneTextView.setText(mobile);

        }
        cursor.close();
        db.close();



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
                touXiangIv.setImageResource(R.mipmap.ic_launcher);
            } else {
                Bitmap zoomBitmap = ImageUtil.zoomBitmap(photo, 100, 100);
                //获取圆角图片
                Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(zoomBitmap, 200.0f);
                //获取倒影图片
                //Bitmap reflectBitmap = ImageUtil.createReflectionImageWithOrigin(roundBitmap);
                touXiangIv.setImageBitmap(roundBitmap);
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
    //日期
    public  DatePickerDialog datePickerDialogData() {


        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                age = (year1 - year)+"";
                //Log.e("age00000",age+"");
                if (Integer.valueOf(age)>0){
                    resumeAgeTv.setText(age);
                }else {
                    Toast.makeText(AddResumeActivity.this, "亲,您设置的年龄要大于0哦!", Toast.LENGTH_LONG).show();
                }
                selectYear=year;
                selectMonthOfYear=monthOfYear;
                selectDayOfMonth =dayOfMonth;
            }
        }, year1, monthOfYear, dayOfMonth);
        return datePickerDialog;

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
}