package com.jeremy.Customer.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.MessageBean;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.ResumeValueBean;
import com.jeremy.Customer.http.ImageUtil;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.uilt.ModificationResumeActivity;
import com.jeremy.Customer.uilt.OneselfExperienceActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ModificationInformationFragment extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {



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
    @ViewInject(R.id.job_city_tv)
    private TextView jobCityTv;

    @ViewInject(R.id.sex_radio_group)
    private RadioGroup sexRadioGroup;
    @ViewInject(R.id.boy_radio_button)
    private RadioButton boyRadioButton;
    @ViewInject(R.id.girl_radio_button)
    private RadioButton girlRadioButton;
    @ViewInject(R.id.next_resume_tv)
    private TextView nextResumeTv;




    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private File screenshotFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private static final int INFOLT_HINT_DATA=7;//自我介绍
    private static final int EXPERIENCE_HINT_DATA=8;//工作经验

    private String sex;

    private int selectYear;
    private int selectMonthOfYear;
    private int selectDayOfMonth;
    private  int year1;
    private  int monthOfYear;
    private  int dayOfMonth;
    private String  age;
    private DatePickerDialog datePickerDialog;

    private ResumeValueBean resumeValueBeans;
    private LoadingDialog loadingDialog;
    private LoadingDialog loadingDialogOne;


    public ModificationInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_modification_information, container, false);
        ViewUtils.inject(this, view);
         init();
        return view;
    }

    private void init() {
        initView();


    }



    private void initView() {
        updataImage.setOnClickListener(this);
        oneselfLayout.setOnClickListener(this);
        workLayout.setOnClickListener(this);
        resumeAgeTv.setOnClickListener(this);
        sexRadioGroup.setOnCheckedChangeListener(this);
        nextResumeTv.setOnClickListener(this);
         loadingDialog=new LoadingDialog(getActivity(),"正在保存数据......");
        loadingDialogOne=new LoadingDialog(getActivity(),"正在加载数据......");

        //获取系统时间
        Calendar calendar = Calendar.getInstance();
        year1 = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DATE);
       resumeValueBeans=((ModificationResumeActivity) getActivity()).getResumeValueBeans();
        if (resumeValueBeans!=null){
            MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + resumeValueBeans.getUsericon(), usericonIV, MyAppliction.RoundedOptions);
            resumeZhName.setText(resumeValueBeans.getResumeZhName());

            if (resumeValueBeans.getResumeSex().equals(0)){
                boyRadioButton.setChecked(true);
            }else if(resumeValueBeans.getResumeSex().equals(1)){
                girlRadioButton.setChecked(true);
            }
            resumeAgeTv.setText(resumeValueBeans.getResumeAge()+"");
            resumeAgeTv.setTextColor(getResources().getColor(R.color.textColor242424));
            resumeJobName.setText(resumeValueBeans.getResumeJobCategoryName());
            jobCityTv.setText(resumeValueBeans.getResumeWorkPlace());
            phoneTextView.setText(resumeValueBeans.getResumeMobile());
            resumeQq.setText(resumeValueBeans.getResumeQq());
            resumeEmail.setText(resumeValueBeans.getResumeEmail());
            userOnselfText.setText(resumeValueBeans.getResumeInfo());
            userOnselfText.setTextColor(getResources().getColor(R.color.textColor242424));
            workExpexteText.setText(resumeValueBeans.getResumeWorkExperience());
            workExpexteText.setTextColor(getResources().getColor(R.color.textColor242424));
            loadingDialogOne.dismiss();
        }

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resume_age_tv:
                datePickerDialogData().show();
                break;
            case R.id.updata_image:
                showDialog();
                break;
            case R.id.oneself_known_layout:
                Intent infoIntent = new Intent(getActivity(), OneselfExperienceActivity.class);  //方法1
                infoIntent.putExtra("hintData","infoIntent");
                if (!TextUtils.isEmpty(userOnselfText.getText().toString())){
                    infoIntent.putExtra("content",userOnselfText.getText().toString());
                }
                startActivityForResult(infoIntent, INFOLT_HINT_DATA);
                break;
            case R.id.work_experience_layout:
                Intent workIntent = new Intent(getActivity(), OneselfExperienceActivity.class);  //方法1
                workIntent.putExtra("hintData","workIntent");
                if (!TextUtils.isEmpty(workExpexteText.getText().toString())){
                    workIntent.putExtra("content",workExpexteText.getText().toString());
                }
                startActivityForResult(workIntent, EXPERIENCE_HINT_DATA);
                break;



            case R.id.job_classfite_layout:


                break;
            case R.id.job_city_layout:

                break;
            case R.id.next_resume_tv:
                saveData();
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
            if (!TextUtils.isEmpty(resumeZhName.getText().toString())){
                requestParams.addBodyParameter("resumeZhName",resumeZhName.getText().toString());
                if (!TextUtils.isEmpty(sex)){
                    requestParams.addBodyParameter("resumeSex",sex);
                    if (selectYear!=0&&selectMonthOfYear!=0&&selectDayOfMonth!=0){
                        requestParams.addBodyParameter("birthday", selectYear + "-" + selectMonthOfYear + "-" + selectDayOfMonth);
                    }
                    if (!TextUtils.isEmpty(resumeJobName.getText().toString())){
                        requestParams.addBodyParameter("resumeJobName", resumeJobName.getText().toString());
                        if (!TextUtils.isEmpty(resumeQq.getText().toString())){
                            requestParams.addBodyParameter("resumeQq", resumeQq.getText().toString());
                            if (!TextUtils.isEmpty(resumeEmail.getText().toString())){
                                requestParams.addBodyParameter("resumeEmail", resumeEmail.getText().toString());
                               if (!TextUtils.isEmpty(userOnselfText.getText().toString())){
                                   requestParams.addBodyParameter("resumeInfo", userOnselfText.getText().toString());
                               if (!TextUtils.isEmpty(workExpexteText.getText().toString())){
                                       requestParams.addBodyParameter("resumeWorkExperience", workExpexteText.getText().toString());
                                   loadingDialog.show();
                                   httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getCompileResume(), requestParams, new RequestCallBack<String>() {
                                       @Override
                                       public void onSuccess(ResponseInfo<String> responseInfo) {
                                           if (!TextUtils.isEmpty(responseInfo.result)){
                                               ParmeBean<MessageBean> parmeBean=JSONObject.parseObject(responseInfo.result,new TypeReference<ParmeBean<MessageBean>>(){});
                                               if (parmeBean.getState().equals("success")){
                                                   if (parmeBean.getValue().getMessage().equals("success")){
                                                       loadingDialog.dismiss();
                                                      /* Bundle bundle=new Bundle();
                                                       Intent intent=new Intent(getActivity(), ResumeParticularsActivity.class);
                                                       intent.putExtra("",bundle);*/
                                                       getActivity().finish();

                                                   }

                                               }
                                           }



                                       }

                                       @Override
                                       public void onFailure(HttpException e, String s) {

                                       }
                                   });


                                   }else {
                                   MyAppliction.showToast("请输入工作经验");
                               }

                               }else {
                                   MyAppliction.showToast("请介绍一下自己");
                               }


                            }else {
                                MyAppliction.showToast("请输入电子邮箱");
                            }

                        }else {
                            MyAppliction.showToast("请输入QQ号码");
                        }

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


        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                age = (year1 - year)+"";
                //Log.e("age00000",age+"");
                if (Integer.valueOf(age)>0){
                    resumeAgeTv.setText(age);
                }else {
                    MyAppliction.showToast("亲,您设置的年龄要大于0哦!");
                }
                selectYear=year;
                selectMonthOfYear=monthOfYear;
                selectDayOfMonth =dayOfMonth;
            }
        }, year1, monthOfYear, dayOfMonth);
        return datePickerDialog;

    }
    private void showDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
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
                usericonIV.setImageResource(R.mipmap.ic_launcher);
            } else {
                Bitmap zoomBitmap = ImageUtil.zoomBitmap(photo, 100, 100);
                //获取圆角图片
                Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(zoomBitmap, 200.0f);
                //获取倒影图片
                //Bitmap reflectBitmap = ImageUtil.createReflectionImageWithOrigin(roundBitmap);
                usericonIV.setImageBitmap(roundBitmap);
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
            case INFOLT_HINT_DATA:
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

                break;
            case EXPERIENCE_HINT_DATA:
                if (data.getStringExtra("workIntent").toString().equals("notData")){
                    if (workExpexteText.getText().toString().equals("分享一下自己工作经验")){
                        workExpexteText.setText("分享一下自己工作经验");
                        workExpexteText.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        workExpexteText.setText(workExpexteText.getText().toString());
                        workExpexteText.setTextColor(getResources().getColor(R.color.textColor242424));
                    }
                }else if (data.getStringExtra("workIntent").toString().equals("data")){
                    if (workExpexteText.getText().toString().equals("分享一下自己工作经验")){
                        workExpexteText.setText("分享一下自己工作经验");
                        workExpexteText.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        workExpexteText.setText(workExpexteText.getText().toString());
                        workExpexteText.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else {
                    workExpexteText.setText(data.getStringExtra("workIntent").toString());
                    workExpexteText.setTextColor(getResources().getColor(R.color.textColor242424));
                }

                break;

        }
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
