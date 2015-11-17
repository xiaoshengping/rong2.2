package com.jeremy.Customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.ActivityBean;
import com.jeremy.Customer.bean.CommentBean;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.RecruitmentListBean;
import com.jeremy.Customer.bean.TalentValueBean;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.jeremy.Customer.view.RoundAngleImageView;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/9/22.
 */
public class RecommendListAdater extends BaseAdapter {

    private LayoutInflater mInflater;
    private BitmapUtils bitmapUtils;
    private ViewActivity viewActivity;
    private ViewTalents viewTalents;
    private ViewPosition viewPosition;
    private ViewComment viewComment;
    private int identi;
    private List<ActivityBean> activityData;
    public List<RecruitmentListBean> recruitmentListData;
    public List<TalentValueBean> talentValueBean;
    public List<CommentBean> commentDate;
    private int maxNumber = 0;

    private Context context;

    public RecommendListAdater(Context context, int identi) {
        this.mInflater = LayoutInflater.from(context);
        this.identi = identi;
    }

    //找工作列表
    public RecommendListAdater(Context context, int identi, List<RecruitmentListBean> data) {
        this.mInflater = LayoutInflater.from(context);
        this.identi = identi;
        recruitmentListData = data;
        maxNumber = data.size();
        this.context = context;
//        Toast.makeText(context, "get", Toast.LENGTH_LONG).show();
//        Toast.makeText(context, data.size() + "", Toast.LENGTH_LONG).show();
    }

    //找人才列表
    public RecommendListAdater(Context context, List<TalentValueBean> data, int identi) {
        this.mInflater = LayoutInflater.from(context);
        this.bitmapUtils = new BitmapUtils(context);
        this.identi = identi;
        talentValueBean = data;
        maxNumber = data.size();
//        Toast.makeText(context, data.size() + "", Toast.LENGTH_LONG).show();
    }
    //活动列表
    public RecommendListAdater(int identi,Context context, List<ActivityBean> data) {
        this.mInflater = LayoutInflater.from(context);
        this.bitmapUtils = new BitmapUtils(context);
        this.identi = identi;
        activityData = data;
        maxNumber = data.size();
//        Toast.makeText(context, data.size() + "", Toast.LENGTH_LONG).show();
    }
    //评论列表
    public RecommendListAdater(List<CommentBean> data, int identi ,Context context) {
        this.mInflater = LayoutInflater.from(context);
//        this.bitmapUtils = new BitmapUtils(context);
        this.identi = identi;
        commentDate = data;
        maxNumber = data.size();
//        Toast.makeText(context, data.size() + "", Toast.LENGTH_LONG).show();
    }

    public RecommendListAdater() {

//        Toast.makeText(context, data.size() + "", Toast.LENGTH_LONG).show();
    }



    public void setActivityBean(List<ActivityBean> datas){
        activityData = datas;
        maxNumber = datas.size();
    }

    public void setRecruitmentListData(List<RecruitmentListBean> datas){
        recruitmentListData = datas;
        maxNumber = datas.size();
    }
    public void setTalentValueBean(List<TalentValueBean> datas){
        talentValueBean = datas;
        maxNumber = datas.size();
    }
    public void setCommentBean(List<CommentBean> datas){
        commentDate = datas;
        maxNumber = datas.size();
    }

    @Override
    public int getCount() {
        return maxNumber;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        Toast.makeText(context, "getView", Toast.LENGTH_LONG).show();

        if (identi == Identification.ACTIVITY) {
            return activity(convertView,position);
        } else if (identi == Identification.TALENTS) {
            return talents(convertView, position);
        } else if (identi == Identification.PROSITION) {
//            Toast.makeText(context, "PROSITION", Toast.LENGTH_LONG).show();
            return position(convertView, position);
        }else if(identi == Identification.COMMENT){
            return comment(convertView,position);
        }else if(identi == Identification.HOTJOBS){
            return hotjobs(convertView, position);
        }

        return convertView;
    }

    //活动
    private View activity(View view,int position) {
        if (view == null) {
            view = mInflater.inflate(R.layout.item_activity, null);
            viewActivity = new ViewActivity();

            viewActivity.activity_poster = (ImageView) view.findViewById(R.id.activity_poster);
            viewActivity.activity_name = (TextView) view.findViewById(R.id.activity_name);

            view.setTag(viewActivity);
        } else {
            viewActivity = (ViewActivity) view.getTag();
        }

        bitmapUtils.display(viewActivity.activity_poster, AppUtilsUrl.ImageBaseUrl + activityData.get(position).getImage());
        viewActivity.activity_name.setText(activityData.get(position).getTitle());

        return view;
    }

    //人才
    private View talents(View view, int position) {
        if (view == null) {
            view = mInflater.inflate(R.layout.item_talents, null);
            viewTalents = new ViewTalents();

            viewTalents.item_talents_head = (RoundAngleImageView) view.findViewById(R.id.item_talents_head);
            viewTalents.item_talents_name_tv = (TextView) view.findViewById(R.id.item_talents_name_tv);
            viewTalents.item_talents_age_tv = (TextView) view.findViewById(R.id.item_talents_age_tv);
            viewTalents.item_talents_rest_tv = (TextView) view.findViewById(R.id.item_talents_rest_tv);
            viewTalents.item_talents_sex_iv = (ImageView) view.findViewById(R.id.item_talents_sex_iv);

            view.setTag(viewTalents);
        } else {
            viewTalents = (ViewTalents) view.getTag();
        }
        if(talentValueBean.size()>0) {
            if (talentValueBean.get(position).getUsericon() != null) {
                bitmapUtils.display(viewTalents.item_talents_head, AppUtilsUrl.ImageBaseUrl + talentValueBean.get(position).getUsericon());
            }
            viewTalents.item_talents_name_tv.setText(talentValueBean.get(position).getResumeZhName());
            viewTalents.item_talents_age_tv.setText(talentValueBean.get(position).getResumeAge() + "岁");
            viewTalents.item_talents_rest_tv.setText(talentValueBean.get(position).getResumeJobCategoryName() + "    " + talentValueBean.get(position).getResumeWorkPlace());

            if (talentValueBean.get(position).getResumeSex() == 0) {
                viewTalents.item_talents_sex_iv.setImageResource(R.mipmap.man);
            } else {
                viewTalents.item_talents_sex_iv.setImageResource(R.mipmap.woman);
            }
        }

        return view;
    }

    //职位
    private View position(View view, int position) {
        if (view == null) {
            view = mInflater.inflate(R.layout.item_position, null);
            viewPosition = new ViewPosition();

            viewPosition.item_position_name_tv = (TextView) view.findViewById(R.id.item_position_name_tv);
            viewPosition.item_position_salary_tv = (TextView) view.findViewById(R.id.item_position_salary_tv);
            viewPosition.item_position_time_tv = (TextView) view.findViewById(R.id.item_position_time_tv);
            viewPosition.item_position_site_tv = (TextView) view.findViewById(R.id.item_position_site_tv);
            viewPosition.item_company_name_tv = (TextView)view.findViewById(R.id.item_company_name_tv);

            view.setTag(viewPosition);
        } else {
            viewPosition = (ViewPosition) view.getTag();
        }

//        if(position<recruitmentListData.size()) {

        viewPosition.item_company_name_tv.setText(recruitmentListData.get(position).getCompanyName());

        if (recruitmentListData.get(position).getPosition() == null) {
        } else {
            if (recruitmentListData.get(position).getPosition().equals("")) {
            } else {
                viewPosition.item_position_name_tv.setText(recruitmentListData.get(position).getPosition());
            }
        }
        if (recruitmentListData.get(position).getWorkPay() == null) {
        } else {
            if (recruitmentListData.get(position).getWorkPay().equals("")) {
            } else {
                viewPosition.item_position_salary_tv.setText(recruitmentListData.get(position).getWorkPay());
            }
        }
        if (recruitmentListData.get(position).getPuttime() == null) {
        } else {
            if (recruitmentListData.get(position).getPuttime().equals("")) {
            } else {
                viewPosition.item_position_time_tv.setText(recruitmentListData.get(position).getPuttime().toString().substring(5,10));
            }
        }
        if (recruitmentListData.get(position).getWorkPlace() == null) {
        } else {
            if (recruitmentListData.get(position).getWorkPlace().equals("")) {
            } else {
                viewPosition.item_position_site_tv.setText(recruitmentListData.get(position).getWorkPlace());
            }
        }
//        }

        return view;
    }
    //热门职位
    private View hotjobs(View view, int position) {
        if (view == null) {
            view = mInflater.inflate(R.layout.item_hotjobs, null);
            viewPosition = new ViewPosition();

            viewPosition.item_position_name_tv = (TextView) view.findViewById(R.id.item_position_name_tv);
            viewPosition.item_position_salary_tv = (TextView) view.findViewById(R.id.item_position_salary_tv);
            viewPosition.item_position_time_tv = (TextView) view.findViewById(R.id.item_position_time_tv);
            viewPosition.item_position_site_tv = (TextView) view.findViewById(R.id.item_position_site_tv);

            view.setTag(viewPosition);
        } else {
            viewPosition = (ViewPosition) view.getTag();
        }

        if(position<recruitmentListData.size()) {

//        viewPosition.item_company_name_tv.setText(recruitmentListData.get(position).getCompanyName());

        if (recruitmentListData.get(position).getPosition() == null) {
        } else {
            if (recruitmentListData.get(position).getPosition().equals("")) {
            } else {
                viewPosition.item_position_name_tv.setText(recruitmentListData.get(position).getPosition());
            }
        }
        if (recruitmentListData.get(position).getWorkPay() == null) {
        } else {
            if (recruitmentListData.get(position).getWorkPay().equals("")) {
            } else {
                viewPosition.item_position_salary_tv.setText(recruitmentListData.get(position).getWorkPay());
            }
        }
        if (recruitmentListData.get(position).getPuttime() == null) {
        } else {
            if (recruitmentListData.get(position).getPuttime().equals("")) {
            } else {
                viewPosition.item_position_time_tv.setText(recruitmentListData.get(position).getPuttime().toString().substring(5,10));
            }
        }
        if (recruitmentListData.get(position).getWorkPlace() == null) {
        } else {
            if (recruitmentListData.get(position).getWorkPlace().equals("")) {
            } else {
                viewPosition.item_position_site_tv.setText(recruitmentListData.get(position).getWorkPlace());
            }
        }
        }

        return view;
    }

    //评价
    private View comment(View view, int position){
        if (view == null) {
            view = mInflater.inflate(R.layout.item_comment, null);
            viewComment = new ViewComment();
            viewComment.comment_text_tv = (TextView)view.findViewById(R.id.comment_text_tv);
            viewComment.comment_unit_tv = (TextView)view.findViewById(R.id.comment_unit_tv);
            viewComment.comment_time_tv = (TextView)view.findViewById(R.id.comment_time_tv);

            view.setTag(viewTalents);
        } else {
            viewTalents = (ViewTalents) view.getTag();
        }
        if(commentDate.size()>0) {
            viewComment.comment_text_tv.setText(commentDate.get(position).getBody());
            if (commentDate.get(position).getCompanyName()==null||commentDate.get(position).getCompanyName().equals("")){
                viewComment.comment_unit_tv.setText(commentDate.get(position).getNickname());
            }else {
                viewComment.comment_unit_tv.setText(commentDate.get(position).getCompanyName());
            }
            viewComment.comment_time_tv.setText(commentDate.get(position).getTime());
        }

        return view;
    }

    public class ViewActivity {
        private ImageView activity_poster;
        private TextView activity_name;
    }

    public class ViewTalents {
        private RoundAngleImageView item_talents_head;
        private TextView item_talents_name_tv, item_talents_age_tv, item_talents_rest_tv;
        private ImageView item_talents_sex_iv;
    }

    public class ViewPosition {
        private TextView item_position_name_tv, item_position_salary_tv, item_position_time_tv, item_position_site_tv,item_company_name_tv;

    }

    public class ViewComment {
        private TextView comment_text_tv, comment_unit_tv, comment_time_tv;

    }

}
