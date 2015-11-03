package com.jeremy.Customer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.CommentcountValueBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by xiaoshengping on 2015/10/30.
 */
public class CommentCountAdapter extends AppBaseAdapter<CommentcountValueBean> {
       private ViewHolde viewHolde;

    public CommentCountAdapter(List<CommentcountValueBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
         if (convertView==null){
             convertView= LayoutInflater.from(context).inflate(R.layout.comment_count_adapter_layout, parent, false);
             viewHolde=new ViewHolde(convertView);
             convertView.setTag(viewHolde);

         }else {
             viewHolde= (ViewHolde) convertView.getTag();
         }
          init(position);

        return convertView;
    }

    private void init(int position) {

        viewHolde.commentNeirongTv.setText(data.get(position).getBody());
        if (!TextUtils.isEmpty(data.get(position).getCompanyName())){
            viewHolde.commentBeTv.setText("来自"+data.get(position).getCompanyName());
        }else {
            viewHolde.commentBeTv.setText("来自无知");
        }

        viewHolde.commentTimeTv.setText(data.get(position).getTime());

    }

    private class ViewHolde{
        @ViewInject(R.id.comment_neirong_tv)
        private TextView commentNeirongTv;
        @ViewInject(R.id.comment_be_tv)
        private TextView commentBeTv;
        @ViewInject(R.id.comment_time_tv)
        private TextView commentTimeTv;

        public ViewHolde(View view) {
            ViewUtils.inject(this,view);
        }
    }

}
