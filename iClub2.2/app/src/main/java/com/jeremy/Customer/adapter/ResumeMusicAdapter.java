package com.jeremy.Customer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeMusic;
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

import static com.jeremy.Customer.R.id;

/**
 * Created by xiaoshengping on 2015/7/16.
 */
public class ResumeMusicAdapter extends AppBaseAdapter<ResumeMusic> {
    private ViewHodle viewHodle;
    private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示
    public ResumeMusicAdapter(List<ResumeMusic> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.resume_music_adapter_layout,null);
            viewHodle=new ViewHodle(convertView) ;
            convertView.setTag(viewHodle);

        }else {
            viewHodle= (ViewHodle) convertView.getTag();

        }
      inti(position);

        return convertView;
    }

    private void inti(final int position) {
        viewHodle.showMusicTextView.setText(data.get(position).getTitle());
        viewHodle.daleteMarkView.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);//设置删除按钮是否显示
        viewHodle.daleteMarkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideoData(data.get(position).getResumeid() + "", position, data.get(position).getResumemusicid() + "");

            }
        });


    }
    private void deleteVideoData(String resumeid, final int position,String musicid) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid", resumeid);
        requestParams.addBodyParameter("musicid", musicid);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getDeleteMusic(), requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    MyAppliction.showToast("删除成功");
                    data.remove(position);
                    notifyDataSetChanged();


                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                MyAppliction.showToast("网络出错了,删除失败...");
            }
        });




    }
    public void setIsShowDelete(boolean isShowDelete){
        this.isShowDelete=isShowDelete;
        notifyDataSetChanged();
    }



    private class  ViewHodle{
        @ViewInject(id.show_music_tv)
        private TextView showMusicTextView;
        @ViewInject(R.id.delete_markView)
        private ImageView daleteMarkView;

        public ViewHodle(View view) {
            ViewUtils.inject(this, view);
        }
    }


}
