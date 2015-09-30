package com.jeremy.Customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.InviteMessgaeListValueBean;
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiaoshengping on 2015/7/2.
 */
public class InviteMessageListAdapter extends  AppBaseAdapter<InviteMessgaeListValueBean> {

    private ViewHold viewHold;
    private String status;
    private int positions;
    private String inviteid;
    private static HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();

    public InviteMessageListAdapter(List<InviteMessgaeListValueBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.invite_message_list_adapter_layout,parent,false);
            viewHold=new ViewHold(convertView) ;
           convertView.setTag(viewHold);

        }else {
            viewHold= (ViewHold) convertView.getTag();

        }

        positions=position;
        inti(position);
        return convertView;
    }

    private void inti(final int position) {
        viewHold.InviteResumeJobNameTv.setText(data.get(position).getInviteResume().getResumeJobName());
        status=data.get(position).getStatus();
        inviteid=data.get(position).getInviteid();
        if (status.equals("0")){
            viewHold.InviteStatusTv.setText("new");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor9a4274));
           // viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_icon);
            viewHold.acceptButtonTv.setText("接受");
            viewHold.refuseButtonTv.setText("拒绝");

        }else if (status.equals("2")){
            viewHold.InviteStatusTv.setText("拒绝");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
           // viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setVisibility(View.GONE);
            viewHold.refuseButtonTv.setVisibility(View.GONE);

        }else if (status.equals("6")){
            viewHold.InviteStatusTv.setText("过期");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            //viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setVisibility(View.GONE);
            viewHold.refuseButtonTv.setVisibility(View.GONE);
        }else if (status.equals("5")){
            viewHold.InviteStatusTv.setText("完成");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            //viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
        }else if (status.equals("1")){
            viewHold.InviteStatusTv.setText("接受");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
           // viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setText("成功");
            viewHold.refuseButtonTv.setText("失败");
        }else if (status.equals("3")){
            viewHold.InviteStatusTv.setText("成功");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            //viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setText("评论");
            viewHold.refuseButtonTv.setText("删除");
        }else if (status.equals("4")){
            viewHold.InviteStatusTv.setText("失败");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            //viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setText("评论");
            viewHold.refuseButtonTv.setText("删除");
        }
        viewHold.inviteTripTimeTv.setText(data.get(position).getTripTime());
        viewHold.InviteJobCategoryTv.setText(data.get(position).getInviteResume().getResumeWorkPlace());
        viewHold.InviteCompanyNameTv.setText(data.get(position).getInvitePerson().getBEcompanyName());
       /* viewHold.popupButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){


                }else {


                }
            }
        });*/


        viewHold.acceptButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getStatus().equals("1")) {
                    adoptData("3");

                } else if (data.get(position).getStatus().equals("0")) {
                    adoptData("1");
                } else if (data.get(position).getStatus().equals("3") || data.get(position).getStatus().equals("4")) {
                    Intent intent=new Intent(context, CommentGradeActivity.class);
                    intent.putExtra("inviteMessgaeListValueBeans", (Serializable) data.get(position));
                    intent.putExtra("falgeData", "SuccessfulInviteFragment");
                    context.startActivity(intent);
                    MyAppliction.showToast("成功接受");
                }

                MyAppliction.showToast(position+"kkkkkk"+data.get(position).getStatus());
            }
        });

        viewHold.refuseButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getStatus().equals("1")) {
                    adoptData("4");

                } else if (data.get(position).getStatus().equals("0")) {
                    adoptData("2");

                }else if (data.get(position).getStatus().equals("3") || data.get(position).getStatus().equals("4")) {
                    // Intent intent=new Intent(context,);

                    MyAppliction.showToast("成功接受");
                }
                MyAppliction.showToast("jsjfhfhfss");
            }
        });

    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        InviteMessageListAdapter.isSelected = isSelected;
    }
    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accept_button_tv:
                if (data.get(positions).getStatus().equals("1")){
                    adoptData("3");

                }else if (status.equals("0")){
                    adoptData("1");
                }else if (status.equals("3")||status.equals("4")){
                   // Intent intent=new Intent(context,);
                    MyAppliction.showToast("成功接受");
                }

                MyAppliction.showToast(status);
                break;
            case R.id.refuse_button_tv:
                if (status.equals("1")){
                    adoptData("4");

                }else if (status.equals("0")){
                    adoptData("2");

                }
                MyAppliction.showToast("jsjfhfhfss");
                break;

        }




    }*/


    private void adoptData(String status) {
        HttpUtils httpUtils =new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("inviteid",inviteid );
        requestParams.addBodyParameter("status", status);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAdoptAndRefuse(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // Log.e("responseInfo",responseInfo.result);
                if (responseInfo.result != null) {
                    /*MyAppliction.showToast("成功接受");
                    viewHold.acceptButtonTv.setVisibility(View.GONE);
                    viewHold.refuseButtonTv.setVisibility(View.GONE);
                    viewHold.deleteButtonTv.setVisibility(View.VISIBLE);*/
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onFailure", s);
            }
        });


    }

    private class ViewHold {
        @ViewInject(R.id.resumeJobName_invite_tv)
        private TextView InviteResumeJobNameTv;
        @ViewInject(R.id.invite_status_tv)
        private TextView InviteStatusTv;
        @ViewInject(R.id.invite_tripTime_tv)
        private TextView inviteTripTimeTv;
        @ViewInject(R.id.resumeJobCategory_invite_tv)
        private TextView InviteJobCategoryTv;
        @ViewInject(R.id.BEcompanyName_tv)
        private TextView InviteCompanyNameTv;
        @ViewInject(R.id.accept_button_tv)
        private TextView acceptButtonTv;
        @ViewInject(R.id.refuse_button_tv)
        private Button refuseButtonTv;
        @ViewInject(R.id.delete_button_tv)
        private Button deleteButtonTv;
        @ViewInject(R.id.popup_button)
        private CheckBox popupButton;
        @ViewInject(R.id.accept_refuse_layout)
        private RelativeLayout acceptRefuseLayout;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }

}
