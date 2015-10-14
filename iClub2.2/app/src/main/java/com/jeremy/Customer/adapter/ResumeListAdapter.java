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
import com.jeremy.Customer.bean.mine.ResumeValueBean;
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
 * Created by xiaoshengping on 2015/6/11.
 */
public class ResumeListAdapter extends AppBaseAdapter<ResumeValueBean> implements View.OnClickListener {

    private  ViewHolde viewHolde;
    private int resumeId;
    private String state;
    private Button stateDialogButton;
    private PullToRefreshListView resumeListLv;

    private int positions;
    public ResumeListAdapter(List<ResumeValueBean> data, Context context,PullToRefreshListView resumeListLv) {
        super(data, context);
        this.resumeListLv=resumeListLv;
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.resume_list_adapter, parent, false);
             viewHolde=new ViewHolde(convertView);
            convertView.setTag(viewHolde);
        }else {
          viewHolde= (ViewHolde) convertView.getTag();

        }
        inti(position);

        return convertView;
    }

    private void inti(int position) {
        positions=position;
        viewHolde.resumeJobNameTv.setText(data.get(position).getResumeJobName());
        viewHolde.createTimeTv.setText(data.get(position).getCreateTime());
        viewHolde.updateTimeTv.setText("浏览量: " + data.get(position).getResumeViewCount());
        resumeId= data.get(position).getResumeid();
         state=data.get(position).getState()+"";
        if (state.equals("0")){
             viewHolde.resumeStateTv.setText("公开");
         }else if (state.equals("1")){
             viewHolde.resumeStateTv.setText("保密");
         }
       viewHolde.modificationButton.setOnClickListener(this);
        viewHolde.refreshButton.setOnClickListener(this);
        viewHolde.moreButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modification_button:
                break;
            case R.id.refresh_button:
                if (!TextUtils.isEmpty(Integer.toString(resumeId))){
                    refreshResumeData(Integer.toString(resumeId));
                    //Log.e("refresh_button","--------"+positions);
                }
                break;
            case R.id.more_button:
                showDialog();
                //Log.e("more_button", "--------" + positions);
                break;


        }


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
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
                if (!TextUtils.isEmpty(resumeId+"")||!TextUtils.isEmpty(state)){
                    if (state.equals("0")){
                        stateSaveData("简历已保密", "1", resumeId + "");
                    }else if (state.equals("1")){
                        stateSaveData("简历已公开","0" ,  resumeId + "");
                    }

                }
                //Log.e("stateDialogButton", "--------" + positions);
                dialog.dismiss();
            }
        });
        deleteDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteResumeData(resumeId+"");
                //Log.e("deleteDialogButton", "--------" + positions);
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

    private void deleteResumeData(String resumeId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid",resumeId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getDeleteResume(), requestParams,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<MessageBean> parmeBean= JSONObject.parseObject(responseInfo.result,new TypeReference<ParmeBean<MessageBean>>(){});
                // Log.e("jfjfjfj",responseInfo.result);
                if (parmeBean.getState().equals("success")){
                    if (parmeBean.getValue().getMessage().equals("success")){
                        MyAppliction.showToast("删除简历成功");
                        notifyDataSetChanged();
                        resumeListLv.setRefreshing();
                    }else {
                        MyAppliction.showToast("删除简历失败");

                    }
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });




    }

    //删除简历



        //保密或者公开
    private void stateSaveData(final String text, final String state,String  resumeId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid",resumeId);
        requestParams.addBodyParameter("state",state);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSaveStateResume(), requestParams,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<MessageBean> parmeBean= JSONObject.parseObject(responseInfo.result,new TypeReference<ParmeBean<MessageBean>>(){});
               // Log.e("jfjfjfj",responseInfo.result);
                if (parmeBean.getState().equals("success")){
                    if (parmeBean.getValue().getMessage().equals("success")){
                        MyAppliction.showToast(text);
                        if (state.equals("0")){
                            viewHolde.resumeStateTv.setText("保密");
                        }else if (state.equals("1")){
                            viewHolde.resumeStateTv.setText("公开");
                        }
                        notifyDataSetChanged();
                        resumeListLv.setRefreshing();
                    }else {
                        MyAppliction.showToast("简历保存失败");

                    }
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }

    //刷新简历
    private void refreshResumeData(String resumeId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid",resumeId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getRefreshResume(), requestParams,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ParmeBean<MessageBean> parmeBean= JSONObject.parseObject(responseInfo.result,new TypeReference<ParmeBean<MessageBean>>(){});
               // Log.e("jfjfjfj",responseInfo.result);
                if (parmeBean.getState().equals("success")){
                    if (parmeBean.getValue().getMessage().equals("success")){
                        MyAppliction.showToast("刷新简历成功");
                        resumeListLv.setRefreshing();
                    }else {
                        MyAppliction.showToast("刷新简历失败");

                    }
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }

    private class  ViewHolde{

        @ViewInject(R.id.resume_JobName_tv)
        private TextView resumeJobNameTv;
         @ViewInject(R.id.createTime_tv)
        private TextView createTimeTv;
         @ViewInject(R.id.updateTime_tv)
        private TextView updateTimeTv;
        @ViewInject(R.id.resume_state_tv)
        private TextView resumeStateTv;
        @ViewInject(R.id.modification_button)
        private Button modificationButton;
        @ViewInject(R.id.refresh_button)
        private Button refreshButton;
        @ViewInject(R.id.more_button)
        private Button moreButton;

        public ViewHolde(View view) {
            ViewUtils.inject(this, view);
        }
    }

}
