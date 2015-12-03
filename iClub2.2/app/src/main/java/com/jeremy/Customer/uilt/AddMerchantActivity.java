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
    private List<String> experienceList=new ArrayList<>();
    private List<String> positionList=new ArrayList<>();
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
        addExperienceList();
        addPositionList();
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

        }





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


        if (resultCode == Identification.CITYSELECTION) {
            Bundle bundle = data.getExtras();
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
            Bundle bundle = data.getExtras();
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
            if (getIntent().getStringExtra("fagle").equals("addMerchant")){
                   seleteProfessionClass(job);
            }


        }

        if (resultCode != Identification.RETURN) {
//            initRecruitmentListData(citynum, jobnum, offset);
//            recommend_list.onRefreshComplete();
//            recommend_list.setRefreshing(true);adsgadga
        }

    }

    private void seleteProfessionClass(int job) {
        if (experienceList.size()!=0&&positionList.size()!=0) {
            if (job == 1) {
                experienceRequireTv.setText(experienceList.get(0).toString());
                workDescribeTv.setText(positionList.get(0).toString());

            } else if (job == 2) {
                experienceRequireTv.setText(experienceList.get(1).toString());
                workDescribeTv.setText(positionList.get(1).toString());

            } else if (job == 3) {
                experienceRequireTv.setText(experienceList.get(2).toString());
                workDescribeTv.setText(positionList.get(2).toString());
            } else if (job == 4) {
                experienceRequireTv.setText(experienceList.get(3).toString());
                workDescribeTv.setText(positionList.get(3).toString());
            } else if (job == 5) {
                experienceRequireTv.setText(experienceList.get(4).toString());
                workDescribeTv.setText(positionList.get(3).toString());
            } else if (job == 6) {
                experienceRequireTv.setText(experienceList.get(5).toString());
                workDescribeTv.setText(positionList.get(5).toString());
            } else if (job == 7) {
                experienceRequireTv.setText(experienceList.get(6).toString());
                workDescribeTv.setText(positionList.get(6).toString());
            } else if (job == 8) {
                experienceRequireTv.setText(experienceList.get(7).toString());
                workDescribeTv.setText(positionList.get(7).toString());
            } else if (job == 9) {
                experienceRequireTv.setText(experienceList.get(8).toString());
                workDescribeTv.setText(positionList.get(8).toString());
            } else if (job == 10) {
                experienceRequireTv.setText(experienceList.get(9).toString());
                workDescribeTv.setText(positionList.get(9).toString());
            } else if (job == 11) {
                experienceRequireTv.setText(experienceList.get(10).toString());
                workDescribeTv.setText(positionList.get(10).toString());
            } else if (job == 12) {
                experienceRequireTv.setText(experienceList.get(11).toString());
                workDescribeTv.setText(positionList.get(11).toString());

            } else if (job == 13) {
                experienceRequireTv.setText(experienceList.get(12).toString());
                workDescribeTv.setText(positionList.get(12).toString());
            } else if (job == 14) {
                experienceRequireTv.setText(experienceList.get(13).toString());
                workDescribeTv.setText(positionList.get(13).toString());
            } else if (job == 15) {
                experienceRequireTv.setText(experienceList.get(14).toString());
                workDescribeTv.setText(positionList.get(14).toString());
            } else if (job == 16) {
                experienceRequireTv.setText(experienceList.get(15).toString());
                workDescribeTv.setText(positionList.get(15).toString());
            } else if (job == 17) {
                experienceRequireTv.setText(experienceList.get(16).toString());
                workDescribeTv.setText(positionList.get(16).toString());
            } else if (job == 18) {
                experienceRequireTv.setText(experienceList.get(17).toString());
                workDescribeTv.setText(positionList.get(17).toString());
            } else if (job == 19) {
                experienceRequireTv.setText(experienceList.get(18).toString());
                workDescribeTv.setText(positionList.get(18).toString());
            } else if (job == 20) {
                experienceRequireTv.setText(experienceList.get(19).toString());
                workDescribeTv.setText(positionList.get(19).toString());
            } else if (job == 21) {
                experienceRequireTv.setText(experienceList.get(20).toString());
                workDescribeTv.setText(positionList.get(20).toString());
            } else if (job == 22) {
                experienceRequireTv.setText(experienceList.get(21).toString());
                workDescribeTv.setText(positionList.get(21).toString());
            } else if (job == 23) {
                experienceRequireTv.setText(experienceList.get(22).toString());
                workDescribeTv.setText(positionList.get(22).toString());
            } else if (job == 24) {
                experienceRequireTv.setText(experienceList.get(23).toString());
                workDescribeTv.setText(positionList.get(23).toString());
            } else if (job == 25) {
                experienceRequireTv.setText(experienceList.get(24).toString());
                workDescribeTv.setText(positionList.get(24).toString());
            } else if (job == 26) {
                experienceRequireTv.setText(experienceList.get(25).toString());
                workDescribeTv.setText(positionList.get(25).toString());
            } else if (job == 27) {
                experienceRequireTv.setText(experienceList.get(26).toString());
                workDescribeTv.setText(positionList.get(26).toString());
            } else if (job == 28) {
                experienceRequireTv.setText(experienceList.get(27).toString());
                workDescribeTv.setText(positionList.get(27).toString());
            } else if (job == 29) {
                experienceRequireTv.setText(experienceList.get(28).toString());
                workDescribeTv.setText(positionList.get(28).toString());
            } else if (job == 30) {
                experienceRequireTv.setText(experienceList.get(29).toString());
                workDescribeTv.setText(positionList.get(29).toString());
            } else if (job == 31) {
                experienceRequireTv.setText(experienceList.get(30).toString());
                workDescribeTv.setText(positionList.get(30).toString());
            } else if (job == 32) {
                experienceRequireTv.setText(experienceList.get(31).toString());
                workDescribeTv.setText(positionList.get(31).toString());
            } else if (job == 33) {
                experienceRequireTv.setText(experienceList.get(32).toString());
                workDescribeTv.setText(positionList.get(32).toString());
            } else if (job == 34) {
                experienceRequireTv.setText(experienceList.get(33).toString());
                workDescribeTv.setText(positionList.get(33).toString());
            } else if (job == 35) {
                experienceRequireTv.setText(experienceList.get(34).toString());
                workDescribeTv.setText(positionList.get(34).toString());
            } else if (job == 36) {
                experienceRequireTv.setText(experienceList.get(35).toString());
                workDescribeTv.setText(positionList.get(35).toString());
            }
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
            requestParams.addBodyParameter("jobRequirements", experienceRequireTv.getText().toString());
            requestParams.addBodyParameter("jobInfo",workDescribeTv.getText().toString() );

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

                    MyAppliction.showToast("网络异常，保存失败...");
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
            requestParams.addBodyParameter("jobRequirements",merchantInfo );
            requestParams.addBodyParameter("jobInfo", merchantWork);
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
                    MyAppliction.showToast("网络异常，保存失败...");
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

    private void addPositionList() {
       positionList.add("1、影视、戏剧、广告及专题片剧本的前期内容策划;\n" +
               "2、编写剧本;\n" +
               "3、协调导演对剧本作出修改,使其更适合银幕表现");
        positionList.add("1、根据美工师整体造型设计意图，完成影视片等道具设计；\n" +
                "2、组织制作影视片所需要的道具及陈设；\n" +
                "3、组织、收集或装置、置换道具。\n" +
                "4、负责编制道具预算");
        positionList.add("1、负责视频后期的剪辑及制作工作\n" +
                "2、能熟练使用After Effect、Adobe Premiere、Photoshop等影视合成编辑软件,能应用3dmax软件进行三维包装效果的制作；\n" +
                "3、能够根据脚本独立剪辑画面并进行后期合成包装，完成导演的意图和构想；\n" +
                "4、根据导演要求完成动画影片的校色，剪接，输出等工作合作专业精神。");
        positionList.add("１、负责配音;\n" +
                " 2、能组织和独立承担各类节目的播音或主持任务;\n" +
                "3、较好地完成岗位职责目标工作量。");
        positionList.add("1、根据美工师整体造型设计意图，完成影视片等道具设计；\n" +
                "2、组织制作影视片所需要的道具及陈设；\n" +
                "3、组织、收集或装置、置换道具。\n" +
                "4、负责编制道具预算");
        positionList.add("1、根据要求完成人物的化妆、发型、服装、配饰等整体形象设计;\n" +
                "2、协助摄影师完成拍摄工作.");
        positionList.add("1、能很熟练运用摇臂,升降等辅助器材,对构图和设计,有很高艺术追求和导演执行力;\n" +
                "2、能高效带领团队并与团队紧密沟通协作，认真负责,内外沟通畅通,无阻碍;\n" +
                "3、组建剧组或活动召开剧组会议，讲解、分派工作、制定场记表，依照排期表&场记表执行拍摄工作;\n" +
                "4、监督后期制作工作，根据不同情况反馈修改影片小样并提交成片.");
        positionList.add("1、根据活动方案和场地进行灯光编程;\n" +
                "2、负责演出或拍摄场地灯光设备的管理和操作;\n" +
                "3、协助活动人员制定不同的灯光布置方案，根据需要进行实时的调整和编排;\n" +
                "4、对灯光设备妥善保管存放");
        positionList.add("1、跟随艺员的全程通告，并保管好艺员演出所需物品;\n" +
                "2、根据艺员通告制定相应工作的行程计划");
        positionList.add("1、负责对音乐进行作词、填词;\n" +
                "2、根据要求进行相应的词曲创作;\n" +
                "3、音乐类产品策划,能从专业角度给产品提建议");
        positionList.add("1、有音乐基础、乐理知识、编曲制作经验;音乐类相关专业本科以上学历者优先忧虑\n" +
                "2、拥有良好的用户角度思考能力,有较高的音乐素养,熟悉多种音乐类型和风格,想象力丰富;\n" +
                "3、熟悉整个编曲流程的操作步骤,能熟练使用业内各种编曲软件,独立完成谱曲、编曲工作;\n" +
                "4、有成熟的作品优先考虑");
        positionList.add("1、根据作品的创作意图,完成作品音乐、音响、效果、对话等的录音工作；\n" +
                "2、根据配设画面的要求进行前期录音,同期录音和后期录音；\n" +
                "3、进行作品最终的混录工作");
        positionList.add("1、负责现场演出时乐器的演奏,可选自己最为熟悉的乐器;\n" +
                "2、协调乐队对歌曲方面进行编排调整;");
        positionList.add("1、独立完成唱片发行前的准备、制作、修正等;\n" +
                "2、协调编曲人员对歌曲进行编曲,制定发行版本;\n" +
                "3、对唱片封面和内容进行设计;\n" +
                "4、唱片制作完成后登记著作权;\n" +
                "5、唱片面市后的宣传");
        positionList.add("1、为影视作品制定合适的背景音乐;\n" +
                "2、根据要求制作出相应的音效;\n" +
                "3、对音频作品文件的管理");
        positionList.add("1、对音乐作品进行后期处理,使音乐作品趋于完美;\n" +
                "2、协调录音师完成对音乐作品的录制工作");
        positionList.add("1、负责日常录音；\n" +
                "2、音频文件剪辑、制作 ；\n" +
                "3、录音室日常设备维护与文件整理 ;\n" +
                "4、根据要求创作项目所需要的配乐和音效");
        positionList.add("1、服从导演安排,担当拍摄过程中场景的组成部分,如路人\n" +
                "2、演出效果好者可长期合作");
        positionList.add("1、根据公司要求进行驻场演出、商业演出和团队巡演;\n" +
                "2、配合团队排练,保证演出质量;\n" +
                "3、对演出舞蹈编排提出建议,协助节目创作");
        positionList.add("1、根据公司要求进行驻场演出、商业演出和团队巡演;\n" +
                "2、配合团队排练,保证演出质量,积极配合团队创作出高质量作品;");
        positionList.add("1、在活动中提供魔术表演;\n" +
                "2、与现场观众进行活动,调动观众情绪");
        positionList.add("1、各类大、小型活动的主持人工作;\n" +
                "2、能从专业角度对活动方案提出建议,能独立设计活动节目方案并执行;\n" +
                "3、按时安质完成领导安排的采、编、播工作;");
        positionList.add("1、负责会场礼仪接待工作;\n" +
                "2、负责会场礼仪举牌工作;");
        positionList.add("1、负责活动现场音乐播放和音控设备的管理;\n" +
                "2、负责对活动节目进行音乐编排,进行相关音乐创作;");
        positionList.add("1、进行个人杂技项目表演;\n" +
                "2、配合团队演出,使整个活动效果达到最佳;\n" +
                "3、妥善保管相关杂技道具");
        positionList.add("1、根据公司要求进行公众演讲;\n" +
                "2、根据项目要求撰写讲稿");
        positionList.add("1、随队演奏或独立演奏钢琴曲目;\n" +
                "2、曲目乐谱的校对及排版");
        positionList.add("1、负责乐团演奏的现场演奏指挥;\n" +
                "2、对乐团演出曲目进行读谱,听谱;\n" +
                "3、组织乐团进行演出项目排练,达到最佳演奏效果");
        positionList.add("1、随队演奏或独立演奏小提琴曲目;\n" +
                "2、乐谱的校对及排版");
        positionList.add("1、配合团队表演或独自表演芭蕾舞;\n" +
                "2、对演出舞蹈编排进行协助");
        positionList.add("1、在项目活动中表演国标舞;\n" +
                "2、对演出舞蹈进行编排;\n" +
                "3、与拍档进行排练,以达到最佳表演效果");
        positionList.add("1、配合团队或个人进行各种管弦乐的演奏;\n" +
                "2、配合团队完成管弦乐部分的乐曲编排;");
        positionList.add("1、负责声乐视唱方面的教学工作\n" +
                "2、从基础教起,让学生掌握乐理和视唱");
        positionList.add("1、配合团队或个人进行打击乐的演奏;\n" +
                "2、配合团队完成民族打击乐的乐曲编排;");
        positionList.add("1、配合团队或个人进行吉他的演奏;\n" +
                "2、配合团队完成民族吉他的乐曲编排;");
        positionList.add("1、配合团队或个人进行各种民族乐器的演奏;\n" +
                "2、配合团队完成民族乐器部分的乐曲编排;");



    }

    private void addExperienceList() {
        experienceList.add("1、影视编剧、中文或相关专业,三年以上相关经验；\n" +
                "2、想象力丰富，思维敏捷、活跃，文字功底好,能独立撰写剧本、制作脚本及分镜头剧本；\n" +
                "3、对电影电视剧画面语言、角色性格、剧情推进有良好的驾驽能力；\n" +
                "4、性格开朗、善于沟通,具备良好的职业道德及团队精神；\n" +
                "5、有文学创作经验,文字功底较强,想象力丰富；");
        experienceList.add("1、3-5年以上广告摄影工作经验，熟练使用数码单反相机，灯光等各种摄影器材，热爱摄影，时尚触觉敏锐；熟练服装人像摄影，妆面拍摄，产品拍摄。\n" +
                "2、熟悉网拍的工作，对电商行业发展有独到的见解和执行能力;\n" +
                "3、工作认真细致、有责任感、注重效率、能吃苦耐劳、抗压能力强；\n" +
                "4、具有较好的组织能力与沟通能力");
        experienceList.add("1、具备相关工作资格证,两年或以上后期制作经验;\n" +
                "3、具备良好的后期制作功底，能独立创意及制作平面、影视广告；\n" +
                "4、表达及沟通能力强、熟悉影视广告流程，良好的合作意识，能够承受较强的工作压力，保质保量按时完成工作任务；");
        experienceList.add("1．大专以上学历，播音主持专业优先,一年或以上配音工作经验\n" +
                "2．形象气质佳，普通话、粤语准确规范、清晰流畅、音质清脆有力，语言表达能力强，幽默风趣，善于调动现场氛围。\n" +
                "3．具备极强的应变能力，能灵活处理各类突发事件。\n" +
                "4．系统掌握播音基础理论知识和专业研究能力。");
        experienceList.add("1、大专以上学历,艺术设计相关专业优先,一年或以上道具组工作经验;\n" +
                "2、熟悉影视制作流程，有一定舞台艺术鉴赏能力;\n" +
                "3、思维活跃有创意，在工作中勇于尝试;\n" +
                "4、性格开朗,善于沟通,具备良好职业道德和团队精神");
        experienceList.add("1、接受过专业化妆学校的培训，有一定的化妆技术基础;\n" +
                "2、个人形象气质佳,人品好，热爱彩妆行业;\n" +
                "3、性格开朗、善于与人沟通，有团队合作精神;\n" +
                "4、具备时尚触角,对流行色彩有自己的见解");
        experienceList.add("1.5年以上相关摄像工作经验，熟悉影片整体流程（策划、脚本编写、前期筹备、现场拍摄执行、后期编辑包装），具有较强的现场执行及后期经验； \n" +
                "2.熟练操作Final及AE、PS； \n" +
                "3.熟悉影视表现手法，擅长策划，通晓制作流程；具有摄制广告片、企业宣传片、产品专题片等相关工作经验和作品优先； \n" +
                "4.较强的剧本分析能力和文字功底；具有美术功底、较强的创意表现能力及审美能力、艺术的感受能力； \n" +
                "5.能根据企业要求，指挥制作团队创造性地、高效地完成影视制作； \n" +
                "6.良好的领导能力、项目控制能力、统筹协调能力和团队精神； \n" +
                "7.适应能力强，敬业，能承受工作压力");
        experienceList.add("1、接受过正规院校培训,有相关工作证书;\n" +
                "2、两年或以上相关工作经验，从事过舞台灯光编程，有过大型晚会或者灯光秀的编程经验;\n" +
                "3、熟悉珍珠触摸老虎控台或市面上常见控台，对舞台灯光的排布和编程有独到的理解;\n" +
                "4、性格开朗、善于与人沟通协调，有团队合作精神;");
        experienceList.add("1、学历不限;\n" +
                "2、擅长与人沟通，语言表达能力强，能吃苦耐劳，工作积极主动、勤奋好学;");
        experienceList.add("1、懂乐理,有良好的乐感;\n" +
                "2、有一定的语言造诣,能完美地糅合音乐与文字;\n" +
                "3、有一年以上相关创作经验优先");
        experienceList.add("1、有音乐基础、乐理知识、编曲制作经验;音乐类相关专业本科以上学历者优先忧虑\n" +
                "2、拥有良好的用户角度思考能力,有较高的音乐素养,熟悉多种音乐类型和风格,想象力丰富;\n" +
                "3、熟悉整个编曲流程的操作步骤,能熟练使用业内各种编曲软件,独立完成谱曲、编曲工作;\n" +
                "4、有成熟的作品优先考虑");
        experienceList.add("1.2年以上录音棚工作经验;\n" +
                "2.有扎实丰富的乐理素质和较高音乐赏析能力；\n" +
                "3.熟练操作Protools软件及各种混音插件；\n" +
                "4.对于录音工作有热情，具备良好的协调沟通能力和团队合作意识；");
        experienceList.add("1、熟悉各种中、西乐器的演奏;\n" +
                "2、有团队合作精神,能以整体演奏效果最佳为先;\n" +
                "3、有一年或以上乐队演奏经验者优先;");
        experienceList.add("1、热爱音乐,懂乐理,对音乐市场有充分的了解;两年或以上音乐制作工作经验;有过大型唱片制作经验者优先考虑;\n" +
                "2、熟悉整个唱片制作流程,能熟练对每个步骤进行操作;\n" +
                "3、对流行音乐有着自己独特见解,善于发掘好音乐");
        experienceList.add("1、熟练掌握sonar、cubase、nuendo等主流制作软件，熟悉软件效果器；\n" +
                "3、具有较高的音乐素质、较强的音乐悟性和音效的制作水平；\n" +
                "4、有影视制作配乐经验");
        experienceList.add("1、两年以上音乐后期制作工作经验;\n" +
                "2、熟悉音乐后期处理的完整流程;\n" +
                "3、熟悉业内aa、melo、ee等后期处理软件的操作使用;\n" +
                "4、有责任心,性格开朗,善于沟通协调");
        experienceList.add("1、大专或以上学历，音乐音频相关专业；\n" +
                "2、熟练掌握sonar、cubase、nuendo等主流制作软件，熟悉软件效果器；\n" +
                "3、具有较高的音乐素质、较强的音乐悟性和音效的制作水平；\n" +
                "4、有影视制作配乐经验");
        experienceList.add("1、身体健康,吃苦耐劳,服从现场指导安排;\n" +
                "2、有责任感,拍摄过程中按照剧本要求,不得擅自改动;\n" +
                "3、有表演经验者优先");
        experienceList.add("1、27岁以下,女身高158以上,男身高170以上,热爱舞蹈,形象气质佳;\n" +
                "2、相关专业院校毕业或有丰富舞台经验者优先;\n" +
                "3、能服从和配合团队,");
        experienceList.add("1、五官端正,形象气质佳,自信大方,热爱舞台演出,有强烈的表演欲;\n" +
                "2、相关专业院校毕业或有丰富舞台经验者,精通流行通俗、民谣、美声唱法者优先;\n" +
                "3、能服从团队分配,沟通协调能力强");
        experienceList.add("1、接受过正规魔术培训,有一年以上魔术表演经验;有独创魔术者优先考虑;\n" +
                "2、身体健康,外形端正;\n" +
                "3、能临场发挥,调动观众情绪");
        experienceList.add("1、国语、粤语标准流利,主持或演艺相关专业,本科以上学历;\n" +
                "2、外貌端正、时尚、气质佳,男身高175cm以上,女身高165cm以上;\n" +
                "3、有一年或以上大型活动、晚会主持经验,能配合编导完善节目构思;\n" +
                "4、具有亲和力,性格活泼开朗,善于沟通,具有团队合作精神、较强的应变能力和现场管控能力");
        experienceList.add("1、男女不限,27岁以下,男身高1米7以上,女身高1米58以上;\n" +
                "2、身材匀称,整体感好,形象气质佳;\n" +
                "3、为人有礼貌,性格开朗,有亲和力,热情耐心;\n" +
                "4、能吃苦耐劳,服从安排");
        experienceList.add("1、有一年以上相关工作经验;\n" +
                "2、男女不限,外形端正,无不良嗜好;\n" +
                "3、有相关音控设备操作经验和技能,乐感好;\n" +
                "4、责任心强,守时守信");
        experienceList.add("1、一年以上杂技表演经验,有个人特色杂技项目的优先考虑;\n" +
                "2、身体健康,品行端正,有团队合作精神,服从管理;");
        experienceList.add("1、有丰富的公众演讲经验,高校专家型教师者优先;\n" +
                "2、男女不限,相貌端正,身形匀称,有沉稳的气质;\n" +
                "3、富有激情具有优秀的演讲口才，煽动力、亲和力强，应变能力强;\n" +
                "4、具有高超的演讲能力、渲染能力及掌控现场能力");
        experienceList.add("1、相关专业证明,非专业6级证书以上;\n" +
                "2、业务素质良好,沟通能力强，有舞台演出经历及相关表演经验者优先;\n" +
                "3、外貌端正,气质佳;\n" +
                "4、有团队合作精神,服从分配工作");
        experienceList.add("1、多年乐团指挥经验;\n" +
                "2、有强烈的责任心和团队合作精神;\n" +
                "3、有高超的指挥技术和乐团带队经验者优先");
        experienceList.add("1、相关专业证明,非专业5级证书以上;\n" +
                "2、业务素质良好,沟通能力强，有舞台演出经历及相关表演经验者优先;\n" +
                "3、外貌端正,气质佳;\n" +
                "4、有团队合作精神,服从分配工作");
        experienceList.add("1、女性,相貌端正,整体感好,形象气质佳;\n" +
                "2、相关专业学院或培训机构毕业,有一年或以上芭蕾舞演出经验;\n" +
                "3、熟悉各种芭蕾舞曲的表演技巧;\n" +
                "4、能够从专业角度去对舞蹈进行编排");
        experienceList.add("1、男女不限,男身高175以上,女身高165以上,相貌端正,整体感好,形象气质佳;\n" +
                "2、相关专业学院或培训机构毕业,有一年或以上国标舞演出经验;\n" +
                "3、熟悉国标舞舞步技巧;\n" +
                "4、能够从专业角度去对舞蹈进行编排");
        experienceList.add("1、相关专业院校或培训机构毕业,有演出经验者优先;\n" +
                "2、男女不限,形象气质佳,无不良嗜好;\n" +
                "3、熟悉各种管弦乐器的演奏,能服从团队安排进行演奏;\n" +
                "4、配合团队对演奏乐曲进行编排校对");
        experienceList.add("1、相关专业本科以上学历,视唱能力高;\n" +
                "2、熟悉各种调式和调性;\n" +
                "3、具有一定的音乐理论知识,能灵活运用,有一定的视唱造诣");
        experienceList.add("1、熟练打击乐演奏,具有一定打击乐表演经验\n" +
                "2、有随乐团表演经验者优先");
        experienceList.add("1、有一定的吉他演奏演出经验;\n" +
                "2、仪表端正,有气质,大方得体;");
        experienceList.add("1、热爱传统音乐文化\n" +
                "2、熟悉一门或以上传统民族乐器演奏,又一定的演出经验");




    }




}
