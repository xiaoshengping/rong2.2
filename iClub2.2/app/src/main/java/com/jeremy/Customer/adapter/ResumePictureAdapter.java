package com.jeremy.Customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumePicture;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by xiaoshengping on 2015/10/26.
 */
public class ResumePictureAdapter extends AppBaseAdapter<ResumePicture> {


    private ViewHodle viewHodle;
    public ResumePictureAdapter(List<ResumePicture> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
               if (convertView==null){
                  convertView= LayoutInflater.from(context).inflate(R.layout.resume_picture_adapter_layout,null);
                   viewHodle=new ViewHodle(convertView) ;
                   convertView.setTag(viewHodle);

               }else {
                   viewHodle= (ViewHodle) convertView.getTag();

               }
        MyAppliction.imageLoader.displayImage(AppUtilsUrl.ImageBaseUrl+data.get(position).getPath(),viewHodle.showPictureIv,MyAppliction.options);


        return convertView;
    }


    private class  ViewHodle{
        @ViewInject(R.id.show_picture_iv)
        private ImageView showPictureIv;


        public ViewHodle(View view) {
            ViewUtils.inject(this, view);
        }
    }



}
