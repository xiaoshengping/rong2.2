package com.jeremy.Customer.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.MessageBean;
import com.jeremy.Customer.bean.ParmeBean;
import com.jeremy.Customer.bean.mine.RecruitmentHistoryValueBean;
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

import java.util.List;

/**
 * Created by xiaoshengping on 2015/6/17.
 */
public class RecruitmentHistoryAdapter extends AppBaseAdapter<RecruitmentHistoryValueBean> implements View.OnClickListener {
    private ViewHole viewHole;
    private String state;
    private PullToRefreshListView recruitmentHistoryLv;
    private  String jodId;
    private Button stateDialogButton;

    public RecruitmentHistoryAdapter(List<RecruitmentHistoryValueBean> data, Context context,PullToRefreshListView recruitmentHistoryLv) {
        super(data, context);
        this.recruitmentHistoryLv=recruitmentHistoryLv;
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
         if (convertView==null) {
             convertView = LayoutInflater.from(context).inflate(R.layout.recruitment_history_adapter_layout, parent, false);
              viewHole=new ViewHole(convertView);
             convertView.setTag(viewHole);

         }else {
             viewHole= (ViewHole) convertView.getTag();

         }
        intiData(position);
        return convertView;
    }

    private void intiData(int position) {
        viewHole.createTimeTv.setText(data.get(position).getWorkingTime());
        viewHole.recruitmentCompanyNameTv.setText(data.get(position).getCompanyName());
        viewHole.recruitmentTimeTv.setText("投递量: "+data.get(position).getApplyjobCount());
        state=data.get(position).getState();
        if (state.equals("0")){
            viewHole.merchantStateTv.setText("公开");
        }else if (state.equals("1")){
            viewHole.merchantStateTv.setText("保密");
        }
        viewHole.modificationButton.setOnClickListener(this);
        viewHole.refreshButton.setOnClickListener(this);
        viewHole.moreButton.setOnClickListener(this);
        jodId=data.get(position).getJobId()+"";



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modification_button:

                break;
            case R.id.refresh_button:
                 if (!TextUtils.isEmpty(jodId)){
                     refreshMerchantData(jodId);
                 }
                break;
            case R.id.more_button:
                showDialog();
                break;



        }



    }

    private void showDialog() {

        View view = LayoutInflater.from(context).inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = window.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        stateDialogButton= (Button) view.findViewById(R.id.picture_dialog_button);
        Button deleteDialogButton= (Button) view.findViewById(R.id.photograph_dialog_button);
        Button cancelDialogButton= (Button) view.findViewById(R.id.cancel_dialog_button);
        deleteDialogButton.setText("删除");
        if (state.equals("0")){
            stateDialogButton.setText("保密");
        }else if (state.equals("1")){
            stateDialogButton.setText("公开");
        }
        stateDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(jodId)||!TextUtils.isEmpty(state)){
                    if (state.equals("0")){
                        stateSaveData("职位已保密", "1", jodId);
                    }else if (state.equals("1")){
                        stateSaveData("职位已公开","0" , jodId);
                    }

                }
                dialog.dismiss();
            }
        });
        deleteDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(jodId)){
                    deleteMerchantData(jodId);
                }
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

    //保密或者公开
    private void stateSaveData(final String text, final String state,String  jobid) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("jobid",jobid);
        requestParams.addBodyParameter("state",state);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSaveStateMerchant(), requestParams,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<MessageBean> parmeBean= JSONObject.parseObject(responseInfo.result,new TypeReference<ParmeBean<MessageBean>>(){});
                if (parmeBean.getState().equals("success")){
                    if (parmeBean.getValue().getMessage().equals("success")){
                        MyAppliction.showToast(text);
                        if (state.equals("0")){
                            viewHole.merchantStateTv.setText("保密");
                        }else if (state.equals("1")){
                            viewHole.merchantStateTv.setText("公开");
                        }
                        notifyDataSetChanged();
                        recruitmentHistoryLv.setRefreshing();
                    }else {
                        MyAppliction.showToast("职位保存失败");

                    }
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }
    //删除招聘
    private void deleteMerchantData(String jobid) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("jobid",jobid);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getDeleteJob(), requestParams,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<MessageBean> parmeBean= JSONObject.parseObject(responseInfo.result,new TypeReference<ParmeBean<MessageBean>>(){});
                // Log.e("jfjfjfj",responseInfo.result);
                if (parmeBean.getState().equals("success")){
                    if (parmeBean.getValue().getMessage().equals("success")){
                        MyAppliction.showToast("删除招聘成功");
                        notifyDataSetChanged();
                        recruitmentHistoryLv.setRefreshing();
                    }else {
                        MyAppliction.showToast("删除招聘失败");

                    }
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });




    }


    //刷新招聘列表
    private void refreshMerchantData(String jobid) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("jobid",jobid);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRefreshMerchant(), requestParams,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<MessageBean> parmeBean= JSONObject.parseObject(responseInfo.result, new TypeReference<ParmeBean<MessageBean>>() {
                });
                if (parmeBean.getState().equals("success")){
                    if (parmeBean.getValue().getMessage().equals("success")){
                        MyAppliction.showToast("刷新职位成功");
                        recruitmentHistoryLv.setRefreshing();
                    }else {
                        MyAppliction.showToast("刷新职位失败");

                    }
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }


    private class ViewHole{
        @ViewInject(R.id.recruiment_time_tv)
        private TextView recruitmentTimeTv;
        @ViewInject(R.id.createTime_tv)
        private TextView createTimeTv;
        @ViewInject(R.id.application_JobName_tv)
        private TextView recruitmentCompanyNameTv;
        @ViewInject(R.id.merchant_state_tv)
        private TextView merchantStateTv;
        @ViewInject(R.id.modification_button)
        private Button modificationButton;
        @ViewInject(R.id.refresh_button)
        private Button refreshButton;
        @ViewInject(R.id.more_button)
        private Button moreButton;

        public ViewHole(View view) {

            ViewUtils.inject(this, view);
        }
    }
}
