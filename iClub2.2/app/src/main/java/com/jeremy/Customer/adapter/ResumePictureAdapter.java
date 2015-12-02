package com.jeremy.Customer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumePicture;
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
 * Created by xiaoshengping on 2015/10/26.
 */
public class ResumePictureAdapter extends AppBaseAdapter<ResumePicture> {


    private ViewHodle viewHodle;
    private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示
    public ResumePictureAdapter(List<ResumePicture> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
               if (convertView==null){
                  convertView= LayoutInflater.from(context).inflate(R.layout.resume_picture_adapter_layout,null);
                   viewHodle=new ViewHodle(convertView) ;
                   convertView.setTag(viewHodle);

               }else {
                   viewHodle= (ViewHodle) convertView.getTag();

               }
        MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl + data.get(position).getPath(), viewHodle.showPictureIv, MyAppliction.options);
        viewHodle.daleteMarkView.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);//设置删除按钮是否显示
        viewHodle.daleteMarkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePictrueData(data.get(position).getResumeid()+"", position,data.get(position).getResumepictureid()+"");

            }
        });

        return convertView;
    }

    private void deletePictrueData(String resumeid, final int position,String pictureId) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("resumeid", resumeid);
        requestParams.addBodyParameter("id", pictureId);
       // Log.e("hsdhhd",resumeid+"jfjfjjf"+pictureId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getDeletePicture(), requestParams, new RequestCallBack<String>() {
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
        @ViewInject(R.id.show_picture_iv)
        private ImageView showPictureIv;
        @ViewInject(R.id.delete_markView)
        private ImageView daleteMarkView;

        public ViewHodle(View view) {
            ViewUtils.inject(this, view);
        }
    }



}
