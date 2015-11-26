package com.jeremy.Customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.MerchantInviteValueBean;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.uilt.CommentGradeActivity;
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
 * Created by xiaoshengping on 2015/7/10.
 */
public class MerchantInviteListAdapter extends AppBaseAdapter<MerchantInviteValueBean>{

       private ViewHodle viewHodle;
    // 用来记录按钮状态的Map
    public static Map<Integer, Boolean> isChecked;
     private  String invite;
   private  PullToRefreshListView merchantInviteMessageLv;

    public MerchantInviteListAdapter(List<MerchantInviteValueBean> data, Context context,PullToRefreshListView merchantInviteMessageLv) {
        super(data, context);
        this.merchantInviteMessageLv=merchantInviteMessageLv;


    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.merchant_invite_list_layout,parent,false);
            viewHodle=new ViewHodle(convertView);
            convertView.setTag(viewHodle);
        }else {
            viewHodle= (ViewHodle) convertView.getTag();
        }

          inti(position);
        return convertView;
    }

    private void inti(int position) {
        initButton();
        MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + data.get(position).getInviteResume().getUsericon(), viewHodle.talentImageIv, MyAppliction.RoundedOptions);
        viewHodle.talentNameTv.setText(data.get(position).getInviteResume().getResumeZhName());
        viewHodle.talentTime.setText(data.get(position).getTripTime());
        viewHodle.talentJobTv.setText(data.get(position).getInviteResume().getResumeJobName());
        String status=data.get(position).getBeStatus();
         invite=data.get(position).getInviteid();
        if (status.equals("0")){
            viewHodle.talentStateTv.setText("new");
            viewHodle.talentStateTv.setTextColor(context.getResources().getColor(R.color.textColor9a4274));
            viewHodle.acceptMerchantBt.setVisibility(View.GONE);

        }else if (status.equals("2")){
            viewHodle.talentStateTv.setText("拒绝");
            viewHodle.talentStateTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            viewHodle.acceptMerchantBt.setVisibility(View.GONE);

        }else if (status.equals("6")){
            viewHodle.talentStateTv.setText("过期");
            viewHodle.talentStateTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            viewHodle.acceptMerchantBt.setVisibility(View.GONE);

        }else if (status.equals("5")){
            viewHodle.talentStateTv.setText("完成");
            viewHodle.talentStateTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));

        }else if (status.equals("1")){
            viewHodle.talentStateTv.setText("接受");
            viewHodle.talentStateTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            viewHodle.rejectMerchantBt.setText("失败");

        }else if (status.equals("3")){
            viewHodle.talentStateTv.setText("成功");
            viewHodle.talentStateTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            viewHodle.acceptMerchantBt.setText("评论");

        }else if (status.equals("4")){
            viewHodle.talentStateTv.setText("失败");
            viewHodle.talentStateTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            viewHodle.acceptMerchantBt.setText("评论");

        }

        if ((data.get(position).getInviteResume().getResumeSex())==0){
            viewHodle.talentSexIv.setBackgroundResource(R.mipmap.man_icon);
        }else if ((data.get(position).getInviteResume().getResumeSex())==1){
            viewHodle.talentSexIv.setBackgroundResource(R.mipmap.woman_icon);

        }
        viewHodle.talentAgeTv.setText(data.get(position).getInviteResume().getResumeAge() + "岁");
        viewHodle.acceptMerchantBt.setOnClickListener(new apoctButtonClick(position));
        viewHodle.rejectMerchantBt.setOnClickListener(new deleteButtonClick(position));



    }


    private void initButton() {
        // 初使化操作，默认都是false
        isChecked = new HashMap<Integer, Boolean>();
        for (int i = 0; i < data.size(); i++){
            isChecked.put(i, false);
        }
    }
    /*
        * 此为listview条目中的apoctButtonClick按钮点击事件的写法
        */
    class deleteButtonClick implements View.OnClickListener {

        private int position;

        public deleteButtonClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHodle.rejectMerchantBt.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                   if (data.size()!=0){


                    if (!TextUtils.isEmpty(data.get(position).getBeStatus())){

                    if (data.get(position).getBeStatus().equals("6")) {
                        adoptData(data.get(position).getInviteid(),"998");

                    } else if (data.get(position).getBeStatus().equals("2")) {
                        adoptData(data.get(position).getInviteid(),"998");
                    } else if (data.get(position).getBeStatus().equals("3") || data.get(position).getBeStatus().equals("4")) {
                        adoptData(data.get(position).getInviteid(),"998");
                        //MyAppliction.showToast("成功接受");
                    }else if (data.get(position).getBeStatus().equals("1")){
                        adoptData(data.get(position).getInviteid(),"4");
                    }else if (data.get(position).getBeStatus().equals("0")){
                        adoptData(data.get(position).getInviteid(),"998");
                    }

                   }
                   }else {
                       MyAppliction.showToast("网络异常");
                   }
                    //MyAppliction.showToast(position + "kkkkkk" + data.get(position).getInviteid());
                    //Log.e("steta________", position + "");
                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }


    /*
          * 此为listview条目中的apoctButtonClick按钮点击事件的写法
          */
    class apoctButtonClick implements View.OnClickListener {

        private int position;

        public apoctButtonClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHodle.acceptMerchantBt.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                   if (!TextUtils.isEmpty(data.get(position).getBeStatus())){


                    if (data.get(position).getBeStatus().equals("1")) {
                        adoptData(data.get(position).getInviteid(),"3");

                    } else if (data.get(position).getBeStatus().equals("0")) {
                        adoptData(data.get(position).getInviteid(),"1");
                    } else if (data.get(position).getBeStatus().equals("3") || data.get(position).getBeStatus().equals("4")) {
                        Intent intent=new Intent(context, CommentGradeActivity.class);
                        intent.putExtra("inviteMessgaeListValueBeans",data.get(position) );
                        intent.putExtra("falgeData", "merchantSuccessfulInviteFragment");
                        context.startActivity(intent);
                        //MyAppliction.showToast("成功接受");
                    }
                   }else {
                       MyAppliction.showToast("网络异常");
                   }
                    //MyAppliction.showToast(position + "kkkkkk" + data.get(position).getStatus());
                    //Log.e("steta________", position + "");
                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }

    private void adoptData(String invite, String status) {
        HttpUtils httpUtils =new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("inviteid",invite);
        requestParams.addBodyParameter("status", status);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getModificationMerchant(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // Log.e("responseInfo",responseInfo.result);
                if (responseInfo.result != null) {
                    merchantInviteMessageLv.setRefreshing();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailure", s);
            }
        });


    }
    private class  ViewHodle{
        @ViewInject(R.id.talent_list_image)
        private ImageView talentImageIv;
        @ViewInject(R.id.talent_name)
        private TextView talentNameTv;
        @ViewInject(R.id.talent_sex)
        private ImageView talentSexIv;
        @ViewInject(R.id.talent_age_list)
        private TextView talentAgeTv;
        @ViewInject(R.id.talent_job)
        private TextView talentJobTv;
        @ViewInject(R.id.talent_time)
        private TextView talentTime;
        @ViewInject(R.id.talent_state_tv)
        private TextView talentStateTv;
        @ViewInject(R.id.accept_merchant_bt)
        private Button acceptMerchantBt;
        @ViewInject(R.id.reject_merchant_bt)
        private Button rejectMerchantBt;


        public ViewHodle(View view) {
            ViewUtils.inject(this, view);

        }
    }

}
