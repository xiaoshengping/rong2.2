package com.jeremy.Customer.uilt;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.LoadingDialog;
import com.jeremy.Customer.bean.mine.RecruitmentHistoryValueBean;
import com.jeremy.Customer.citySelection.CitySelectionActivity;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMerchantActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.tailt_return_tv)
    private TextView tailtReturnTv;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.save_text)
    private TextView saveText;

    //添加招聘
    @ViewInject(R.id.position_edit)
    private EditText positionEdit;
    @ViewInject(R.id.workPay_edit)
    private EditText workPayEdit;
    @ViewInject(R.id.recruitingNumbers_edit)
    private EditText recruitingNumbersEdit;
    @ViewInject(R.id.profession_classification_tv)
    private TextView professionClassfitionTv;  //职位类别
    @ViewInject(R.id.work_address_tv)
    private TextView workAddressTv;  //工作地点
    @ViewInject(R.id.experience_require_tv)
    private TextView experienceRequireTv;
    @ViewInject(R.id.work_describe_tv)
    private TextView  workDescribeTv;
    @ViewInject(R.id.working_Hours_edit)
    private TextView workingHoursEdit;
    @ViewInject(R.id.working_Time_edit)
    private TextView workingTimeTt;

    @ViewInject(R.id.jobRequirements_layout)
    private LinearLayout jobRequirementsLayout;
    @ViewInject(R.id.jobInfo_layout)
    private LinearLayout jobInfoLayout;

    private  String professionClassfition;//职位类别
    private  String workAddress;    //工作地点


    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;


    private RecruitmentHistoryValueBean recruitmentHistoryValueBean;
    private  String uid=null;
    private Intent intent;
    private HttpUtils httpUtils;
    private RequestParams requestParams;
    private static final int INFOLT_HINT_DATA=7;//工作要求
    private static final int EXPERIENCE_HINT_DATA=8;//职位描述
    /*private AreaBean areaBean = new AreaBean();
    private int job_classfite_num = -1;//职业类别
    private int job_city_num = -1;//工作地点*/
    private DatePickerDialog datePickerDialog;
    private  int year1;
    private  int monthOfYear;
    private  int dayOfMonth;
    private String  age;
    private int selectYear;
    private int selectMonthOfYear;
    private int selectDayOfMonth;

    private String workingHours;
    private String workingTime;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_merchant);
        ViewUtils.inject(this);
        init();
    }

    private void init() {

        intiView();
        SQLhelper sqLhelper = new SQLhelper(this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        cursor.close();
        db.close();
        initSpinnt();

    }

   private void initSpinnt() {
        //数据
        data_list = new ArrayList<String>();
        data_list.add("全职");
        data_list.add("1天内");
        data_list.add("3天内");
        data_list.add("5天内");
        data_list.add("7天内");
        data_list.add("15天内");
        data_list.add("30天内");
        data_list.add("30天以上");
    }

    private void intiView() {
        workingTimeTt.setOnClickListener(this);
        loadingDialog=new LoadingDialog(this,"保存数据.....");
        httpUtils=new HttpUtils();
        intent=getIntent();
        tailtReturnTv.setOnClickListener(this);
        tailtText.setText("添加招聘");
        saveText.setText("保存");
        saveText.setOnClickListener(this);
        saveText.setVisibility(View.VISIBLE);
        jobRequirementsLayout.setOnClickListener(this);
        jobInfoLayout.setOnClickListener(this);
        professionClassfitionTv.setOnClickListener(this);
        workAddressTv.setOnClickListener(this);
        workingHoursEdit.setOnClickListener(this);
        recruitmentHistoryValueBean= (RecruitmentHistoryValueBean) intent.getSerializableExtra("recruitmentHistoryValueBean");
        if (recruitmentHistoryValueBean!=null){
            workAddressTv.setText(recruitmentHistoryValueBean.getWorkPlace());
            workAddressTv.setTextColor(getResources().getColor(R.color.textColor242424));
            positionEdit.setText(recruitmentHistoryValueBean.getPosition());
            if (!TextUtils.isEmpty(recruitmentHistoryValueBean.getJobRequirements())){
                experienceRequireTv.setText(recruitmentHistoryValueBean.getJobRequirements());
                experienceRequireTv.setTextColor(getResources().getColor(R.color.textColor242424));
            }else {
                experienceRequireTv.setText("写一下经验要求哦(必填)");
                experienceRequireTv.setTextColor(getResources().getColor(R.color.hunTextColor));
            }
            if (!TextUtils.isEmpty(recruitmentHistoryValueBean.getJobInfo())){
                workDescribeTv.setText(recruitmentHistoryValueBean.getJobInfo());
                workDescribeTv.setTextColor(getResources().getColor(R.color.textColor242424));
            }else {
                workDescribeTv.setText("写一下职位描述哦(必填)");
                workDescribeTv.setTextColor(getResources().getColor(R.color.hunTextColor));
            }
            professionClassfitionTv.setText(recruitmentHistoryValueBean.getJobcategory()+"");
            workingTimeTt.setText(recruitmentHistoryValueBean.getWorkingTime());
            workingHoursEdit.setText(recruitmentHistoryValueBean.getWorkingHours());
            workPayEdit.setText(recruitmentHistoryValueBean.getWorkPay());
            recruitingNumbersEdit.setText(recruitmentHistoryValueBean.getRecruitingNumbers());
        }
        //获取系统时间
        Calendar calendar = Calendar.getInstance();
        year1 = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DATE);

        if (selectYear!=0&&selectMonthOfYear!=0&&selectDayOfMonth!=0){
            workingTimeTt.setText(selectYear+"-"+(selectMonthOfYear+1)+"-"+selectDayOfMonth);
            workingTimeTt.setTextColor(getResources().getColor(R.color.textColor242424));

        }/*else {
            workingTimeTt.setText(year1+"-"+monthOfYear+"-"+dayOfMonth);
            workingTimeTt.setTextColor(getResources().getColor(R.color.textColor242424));
        }*/





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jobRequirements_layout:
                Intent infoIntent = new Intent(AddMerchantActivity.this, OneselfExperienceActivity.class);  //方法1
                infoIntent.putExtra("hintData","infoIntent");
                if (!TextUtils.isEmpty(experienceRequireTv.getText().toString())){
                    infoIntent.putExtra("content",experienceRequireTv.getText().toString());
                }
                startActivityForResult(infoIntent, INFOLT_HINT_DATA);
                break;
            case R.id.jobInfo_layout:
                Intent workIntent = new Intent(AddMerchantActivity.this, OneselfExperienceActivity.class);  //方法1
                workIntent.putExtra("hintData","workIntent");
                if (!TextUtils.isEmpty(workDescribeTv.getText().toString())){
                    workIntent.putExtra("content",workDescribeTv.getText().toString());
                }
                startActivityForResult(workIntent, EXPERIENCE_HINT_DATA);
                break;
            case R.id.save_text:
                if (getIntent().getStringExtra("fagle").equals("addMerchant")){
                    intiData();
                }else if (getIntent().getStringExtra("fagle").equals("editMerchant")){
                    intiEditData();
                }

                break;
            case R.id.tailt_return_tv:
                finish();
                break;
            case R.id.profession_classification_tv:
                Intent intent = new Intent(AddMerchantActivity.this, JobChoiceActivity.class);  //方法1
                startActivityForResult(intent, 0);

                break;
            case R.id.work_address_tv:
                Intent intent1 = new Intent(AddMerchantActivity.this, CitySelectionActivity.class);  //方法1
                startActivityForResult(intent1, 0);

                break;
            case R.id.working_Time_edit:
                datePickerDialogData().show();
                break;
            case R.id.working_Hours_edit:
                new AlertView(null, null, null, null,new String[]{"全职", "1天内", "3天内", "5天内", "7天内", "15天内",
                                "30天内", "30天内以上"},
                        this, AlertView.Style.Alert, new OnItemClickListener(){
                    public void onItemClick(Object o,int position){
                        workingHours= data_list.get(position);
                        workingHoursEdit.setText(data_list.get(position));
                    }

                    }).show();
                break;

        }
    }


    //日期
    public DatePickerDialog datePickerDialogData() {


        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                workingTimeTt.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                workingTimeTt.setTextColor(getResources().getColor(R.color.textColor242424));

                selectYear=year;
                selectMonthOfYear=monthOfYear;
                selectDayOfMonth =dayOfMonth;
            }
        }, year1, monthOfYear, dayOfMonth);
        return datePickerDialog;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INFOLT_HINT_DATA:
                if (data.getStringExtra("infoIntent").toString().equals("notData")){
                    if (experienceRequireTv.getText().toString().equals("写一下经验要求哦(必填)")){
                        experienceRequireTv.setText("写一下经验要求哦(必填)");
                        experienceRequireTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        experienceRequireTv.setText(experienceRequireTv.getText().toString());
                        experienceRequireTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }
                }else if (data.getStringExtra("infoIntent").toString().equals("data")){
                    if (experienceRequireTv.getText().toString().equals("写一下经验要求哦(必填)")){
                        experienceRequireTv.setText("写一下经验要求哦(必填)");
                        experienceRequireTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        experienceRequireTv.setText(experienceRequireTv.getText().toString());
                        experienceRequireTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else {
                    experienceRequireTv.setText(data.getStringExtra("infoIntent").toString());
                    experienceRequireTv.setTextColor(getResources().getColor(R.color.textColor242424));
                }

                break;
            case EXPERIENCE_HINT_DATA:
                if (data.getStringExtra("workIntent").toString().equals("notData")){
                    if (workDescribeTv.getText().toString().equals("写一下职位描述哦(必填)")){
                        workDescribeTv.setText("写一下职位描述哦(必填)");
                        workDescribeTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        workDescribeTv.setText(workDescribeTv.getText().toString());
                        workDescribeTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }
                }else if (data.getStringExtra("workIntent").toString().equals("data")){
                    if (workDescribeTv.getText().toString().equals("写一下职位描述哦(必填)")){
                        workDescribeTv.setText("写一下职位描述哦(必填)");
                        workDescribeTv.setTextColor(getResources().getColor(R.color.editTextPromptColor));
                    }else {
                        workDescribeTv.setText(workDescribeTv.getText().toString());
                        workDescribeTv.setTextColor(getResources().getColor(R.color.textColor242424));
                    }

                }else {
                    workDescribeTv.setText(data.getStringExtra("workIntent").toString());
                    workDescribeTv.setTextColor(getResources().getColor(R.color.textColor242424));
                }

                break;
        }

        Bundle bundle = data.getExtras();
        if (resultCode == Identification.CITYSELECTION) {
         /*获取Bundle中的数据，注意类型和key*/
            int city = bundle.getInt("City");
            String cName = bundle.getString("CityName");
            if (city >= 0) {
                if (city != 0) {
                    workAddressTv.setText(cName);
                    workAddressTv.setTextColor(getResources().getColor(R.color.textColor242424));
                } else {
                    workAddressTv.setText("选择城市");
                }
                workAddress = city+"";

            }
        } else if (resultCode == Identification.JOBCHOICE) {
            int job = bundle.getInt("Job");
            String pName = bundle.getString("JobName");
//        if(job>=0&&job!=10){
            if (job != 0) {
                professionClassfitionTv.setText(pName);
                professionClassfitionTv.setTextColor(getResources().getColor(R.color.textColor242424));
            } else {

                professionClassfitionTv.setText("选择职位");
            }
            professionClassfition = job+"";

        }

        if (resultCode != Identification.RETURN) {
//            initRecruitmentListData(citynum, jobnum, offset);
//            recommend_list.onRefreshComplete();
//            recommend_list.setRefreshing(true);adsgadga
        }

    }

    private void intiEditData() {
        RequestParams requestParams=new RequestParams();


        String position=positionEdit.getText().toString();
        String workPay=workPayEdit.getText().toString();
        String recruitingNumbers=recruitingNumbersEdit.getText().toString();
        if (!TextUtils.isEmpty(position)&&!TextUtils.isEmpty(workPay)&&!TextUtils.isEmpty(recruitingNumbers)
                &&!TextUtils.isEmpty(workDescribeTv.getText().toString())
                &&!TextUtils.isEmpty(experienceRequireTv.getText().toString())){
            requestParams.addBodyParameter("jobid",recruitmentHistoryValueBean.getJobId()+"");
            requestParams.addBodyParameter("position",position);
            requestParams.addBodyParameter("workPay",workPay);
            requestParams.addBodyParameter("recruitingNumbers", recruitingNumbers);
            requestParams.addBodyParameter("jobRequirements", workDescribeTv.getText().toString());
            requestParams.addBodyParameter("jobInfo", experienceRequireTv.getText().toString());

            if (selectYear!=0&&selectMonthOfYear!=0&&selectDayOfMonth!=0){
                workingTime=selectYear+"-"+selectMonthOfYear+"-"+selectDayOfMonth;
                requestParams.addBodyParameter("workingTime", workingTime);
            }
            if (!TextUtils.isEmpty(workingHours)){
                requestParams.addBodyParameter("workingHours", workingHours);
            }else {
                requestParams.addBodyParameter("workingHours", recruitmentHistoryValueBean.getWorkingHours());
            }

            if (!TextUtils.isEmpty(professionClassfition)){
                requestParams.addBodyParameter("jobCategory", professionClassfition);
            }
            if (!TextUtils.isEmpty(workAddress)){
                requestParams.addBodyParameter("cityid", workAddress);
            }

            loadingDialog.show();
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getEditJod(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    // Log.e("result",responseInfo.result);
                    if (responseInfo.result!=null){
                        MyAppliction.showToast("保存数据成功");
                        loadingDialog.dismiss();
                        finish();
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {

                    MyAppliction.showToast("网络请求超时");
                    loadingDialog.dismiss();
                }
            });


        } else {
            MyAppliction.showExitGameAlert("您输入的内容不全", AddMerchantActivity.this);

        }






    }



    private void intiData() {
        RequestParams requestParams = new RequestParams();
        workingTime=selectYear+"-"+selectMonthOfYear+"-"+selectDayOfMonth;

        String position = positionEdit.getText().toString();
        String workPay = workPayEdit.getText().toString();
        String recruitingNumbers = recruitingNumbersEdit.getText().toString();
        String merchantInfo=experienceRequireTv.getText().toString();
        String merchantWork=workDescribeTv.getText().toString();
        if (!TextUtils.isEmpty(position) && !TextUtils.isEmpty(workPay) && !TextUtils.isEmpty(merchantWork)
                && !TextUtils.isEmpty(merchantInfo) && !TextUtils.isEmpty(recruitingNumbers)&&!TextUtils.isEmpty(professionClassfition)
                &&!TextUtils.isEmpty(workAddress)) {
            requestParams.addBodyParameter("uid",uid);
            requestParams.addBodyParameter("position", position);
            requestParams.addBodyParameter("workPay", workPay);
            requestParams.addBodyParameter("recruitingNumbers", recruitingNumbers);
            requestParams.addBodyParameter("jobRequirements", merchantWork);
            requestParams.addBodyParameter("jobInfo", merchantInfo);
            requestParams.addBodyParameter("workingHours", workingHours);
            if (!TextUtils.isEmpty(workingTime)){
                requestParams.addBodyParameter("workingTime", workingTime);
            }else {
                requestParams.addBodyParameter("workingTime", year1+"-"+monthOfYear+"-"+dayOfMonth);
            }
            if (!TextUtils.isEmpty(professionClassfition)){
                requestParams.addBodyParameter("jobCategory", professionClassfition);
            }
           if (!TextUtils.isEmpty(workAddress)){
               requestParams.addBodyParameter("cityid", workAddress);
           }
            loadingDialog.show();
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAddJod(), requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (responseInfo.result != null) {
                        MyAppliction.showToast("保存数据成功");
                        loadingDialog.dismiss();
                        finish();

                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    loadingDialog.dismiss();

                }
            });


        } else {

            MyAppliction.showExitGameAlert("您输入的内容不全", AddMerchantActivity.this);

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
