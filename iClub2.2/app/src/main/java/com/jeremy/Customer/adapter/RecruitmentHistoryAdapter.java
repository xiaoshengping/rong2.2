package com.jeremy.Customer.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
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
import com.jeremy.Customer.uilt.AddMerchantActivity;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoshengping on 2015/6/17.
 */
public class RecruitmentHistoryAdapter extends AppBaseAdapter<RecruitmentHistoryValueBean>  {
    private ViewHole viewHole;
    private String state;
    private PullToRefreshListView recruitmentHistoryLv;
    private Button stateDialogButton;
    // 用来记录按钮状态的Map
    public static Map<Integer, Boolean> isChecked;

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
    private void initButton() {
        // 初使化操作，默认都是false
        isChecked = new HashMap<Integer, Boolean>();
        for (int i = 0; i < data.size(); i++){
            isChecked.put(i, false);
        }
    }
    private void intiData(int position) {
        initButton();
        viewHole.createTimeTv.setText(data.get(position).getWorkingTime());
        viewHole.recruitmentCompanyNameTv.setText(data.get(position).getCompanyName());
        viewHole.recruitmentTimeTv.setText("投递量: "+data.get(position).getApplyjobCount());
        state=data.get(position).getState();
        if (state.equals("0")){
            viewHole.merchantStateTv.setText("公开");
        }else if (state.equals("1")){
            viewHole.merchantStateTv.setText("保密");
        }
        viewHole.refreshButton.setOnClickListener(new refreshClick(position));
        viewHole.moreButton.setOnClickListener(new moreClick(position));
        viewHole.modificationButton.setOnClickListener(new modifictionClick(position));


    }


    class refreshClick implements View.OnClickListener {

        private int position;

        public refreshClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHole.refreshButton.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                    if (!TextUtils.isEmpty(data.get(position).getJobId()+"")){
                        refreshMerchantData(data.get(position).getJobId()+"");
                    }

                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }

    class moreClick implements View.OnClickListener {

        private int position;

        public moreClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHole.moreButton.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                    showDialog(position);
                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }

    class modifictionClick implements View.OnClickListener {

        private int position;

        public modifictionClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHole.modificationButton.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                    Intent intent=new Intent(context,AddMerchantActivity.class);
                    intent.putExtra("recruitmentHistoryValueBean", data.get(position));
                    intent.putExtra("fagle","editMerchant");
                    context.startActivity(intent);
                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }
    private void showDialog(final int position) {

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
        if (data.get(position).getState().equals("0")){
            stateDialogButton.setText("保密");
        }else if (data.get(position).getState().equals("1")){
            stateDialogButton.setText("公开");
        }
        stateDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(data.get(position).getJobId()+"")||!TextUtils.isEmpty(data.get(position).getState())){
                    if (data.get(position).getState().equals("0")){
                        stateSaveData("职位已保密", "1", data.get(position).getJobId()+"");
                    }else if (data.get(position).getState().equals("1")){
                        stateSaveData("职位已公开","0" , data.get(position).getJobId()+"");
                    }

                }
                dialog.dismiss();
            }
        });
        deleteDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(data.get(position).getJobId()+"")){
                    deleteMerchantData(data.get(position).getJobId()+"");
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
        Log.e("jobid", jobid);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSaveStateMerchant(), requestParams,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<MessageBean> parmeBean= JSONObject.parseObject(responseInfo.result,new TypeReference<ParmeBean<MessageBean>>(){});
                if (parmeBean.getState().equals("success")){
                    if (parmeBean.getValue().getMessage().equals("success")){
                        MyAppliction.showToast(text);
                        if (text.equals("职位已公开")){
                            viewHole.merchantStateTv.setText("公开");
                        }else if (text.equals("职位已保密")){
                            viewHole.merchantStateTv.setText("保密");
                        }

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
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getDeleteJob(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<MessageBean> parmeBean = JSONObject.parseObject(responseInfo.result, new TypeReference<ParmeBean<MessageBean>>() {
                });
                // Log.e("jfjfjfj",responseInfo.result);
                if (parmeBean.getState().equals("success")) {
                    if (parmeBean.getValue().getMessage().equals("success")) {
                        MyAppliction.showToast("删除招聘成功");
                        notifyDataSetChanged();
                        recruitmentHistoryLv.setRefreshing();

                    } else {
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
        //MyAppliction.showToast("" + jobid);
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
