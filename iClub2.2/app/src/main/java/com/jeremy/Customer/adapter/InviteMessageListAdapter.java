package com.jeremy.Customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import java.util.Map;

/**
 * Created by xiaoshengping on 2015/7/2.
 */
public class InviteMessageListAdapter extends  AppBaseAdapter<InviteMessgaeListValueBean> {

    private ViewHold viewHold;
    private String status;
    private PullToRefreshListView inviteMessageLv;

    // 用来记录按钮状态的Map
    public static Map<Integer, Boolean> isChecked;

    public InviteMessageListAdapter(List<InviteMessgaeListValueBean> data, Context context, PullToRefreshListView inviteMessageLv) {
        super(data, context);
        this.inviteMessageLv = inviteMessageLv;
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.invite_message_list_adapter_layout, parent, false);
            viewHold = new ViewHold(convertView);
            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();

        }
        inti(position);

        return convertView;
    }

    private void initButton() {
        // 初使化操作，默认都是false
        isChecked = new HashMap<Integer, Boolean>();
        for (int i = 0; i < data.size(); i++) {
            isChecked.put(i, false);
        }
    }

    private void inti(final int position) {
        initButton();
        viewHold.InviteResumeJobNameTv.setText(data.get(position).getInviteResume().getResumeJobName());
        status = data.get(position).getStatus();
        if (status.equals("0")) {
            viewHold.InviteStatusTv.setText("new");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor9a4274));
            // viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_icon);
        } else if (status.equals("2")) {
            viewHold.InviteStatusTv.setText("拒绝");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            // viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setVisibility(View.GONE);
            viewHold.refuseButtonTv.setText("删除");


        } else if (status.equals("6")) {
            viewHold.InviteStatusTv.setText("过期");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            //viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setVisibility(View.GONE);
            viewHold.refuseButtonTv.setText("删除");
        } else if (status.equals("5")) {
            viewHold.InviteStatusTv.setText("完成");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            //viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
        } else if (status.equals("1")) {
            viewHold.InviteStatusTv.setText("接受");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            // viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setText("成功");
            viewHold.refuseButtonTv.setText("失败");
        } else if (status.equals("3")) {
            viewHold.InviteStatusTv.setText("成功");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            //viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setText("评论");
            viewHold.refuseButtonTv.setText("删除");
        } else if (status.equals("4")) {
            viewHold.InviteStatusTv.setText("失败");
            viewHold.InviteStatusTv.setTextColor(context.getResources().getColor(R.color.textColor2f967a));
            //viewHold.InviteStatusTv.setBackgroundResource(R.mipmap.new_one_icon);
            viewHold.acceptButtonTv.setText("评论");
            viewHold.refuseButtonTv.setText("删除");
        }
        //viewHold.invitePalyTv.setText(data.get(position));
        viewHold.inviteTripTimeTv.setText(data.get(position).getTripTime());
        viewHold.InviteJobCategoryTv.setText(data.get(position).getInviteResume().getResumeWorkPlace());
        viewHold.InviteCompanyNameTv.setText(data.get(position).getInvitePerson().getBEcompanyName());

        viewHold.acceptButtonTv.setOnClickListener(new acceptButtonClick(position));


        viewHold.refuseButtonTv.setOnClickListener(new refuseButtonClick(position));


    }


    /*
        * 此为listview条目中的acceptButtonClick按钮点击事件的写法
        */
    class refuseButtonClick implements View.OnClickListener {

        private int position;

        public refuseButtonClick(int pos){  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == viewHold.refuseButtonTv.getId()){
                if (isChecked.get(position) == false){
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                    if (data.get(position).getStatus().equals("1")) {
                        adoptData(data.get(position).getInviteid(),"4");

                    } else if (data.get(position).getStatus().equals("0")) {
                        adoptData(data.get(position).getInviteid(),"2");

                    }else if (data.get(position).getStatus().equals("3") || data.get(position).getStatus().equals("4")) {
                        adoptData(data.get(position).getInviteid(),"998");
                        MyAppliction.showToast(position + "kkkkkk" + data.get(position).getStatus());
                    }else if (data.get(position).getStatus().equals("2")){
                        adoptData(data.get(position).getInviteid(),"998");
                    }else if (data.get(position).getStatus().equals("6")){
                        adoptData(data.get(position).getInviteid(),"998");
                    }


                } else if (isChecked.get(position) == true){
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }


    /*
         * 此为listview条目中的acceptButtonClick按钮点击事件的写法
         */
    class acceptButtonClick implements View.OnClickListener {

        private int position;

        public acceptButtonClick(int pos) {  // 在构造时将position传给它这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }

        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == viewHold.acceptButtonTv.getId()) {
                if (isChecked.get(position) == false) {
                    isChecked.put(position, true);   // 根据点击的情况来将其位置和相应的状态存入
                    if (data.get(position).getStatus().equals("1")) {
                        adoptData(data.get(position).getInviteid(), "3");

                    } else if (data.get(position).getStatus().equals("0")) {
                        adoptData(data.get(position).getInviteid(), "1");
                    } else if (data.get(position).getStatus().equals("3") || data.get(position).getStatus().equals("4")) {
                        Intent intent = new Intent(context, CommentGradeActivity.class);
                        intent.putExtra("inviteMessgaeListValueBeans", (Serializable) data.get(position));
                        intent.putExtra("falgeData", "SuccessfulInviteFragment");
                        context.startActivity(intent);
                        //MyAppliction.showToast("成功接受");
                    }

                    MyAppliction.showToast(position + "kkkkkk" + data.get(position).getStatus());
                    //Log.e("steta________", position + "");
                } else if (isChecked.get(position) == true) {
                    isChecked.put(position, false);  // 根据点击的情况来将其位置和相应的状态存入

                }
                notifyDataSetChanged();
            }
        }

    }





    private void adoptData(String inviteid, String status) {
        HttpUtils httpUtils =new HttpUtils();
        RequestParams requestParams=new RequestParams();
        Log.e("inviteid",inviteid);
        requestParams.addBodyParameter("inviteid",inviteid );
        requestParams.addBodyParameter("status", status);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAdoptAndRefuse(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // Log.e("responseInfo",responseInfo.result);
                if (responseInfo.result != null) {
                    inviteMessageLv.setRefreshing();
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
        @ViewInject(R.id.accept_refuse_layout)
        private RelativeLayout acceptRefuseLayout;
        @ViewInject(R.id.invite_paly_tv)
        private TextView invitePalyTv;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }

}
