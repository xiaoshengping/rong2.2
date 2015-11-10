package com.jeremy.Customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumePicture;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/9/25.
 */
public class PictureAdapter extends BaseAdapter {

    private ViewPicture viewPicture;
    private LayoutInflater mInflater;
    private List<ResumePicture> resumePicture;
    private Context mContext;
    private int wi;

    private BitmapUtils bitmapUtils;
    private int maxNum = 0;

    public PictureAdapter(Context context,List<ResumePicture> resumePicture,int w){
        this.mInflater = LayoutInflater.from(context);
        this.resumePicture = resumePicture;
        bitmapUtils=new BitmapUtils(context);
        mContext = context;
        wi = w;
        if (resumePicture.size() > 3) {
            maxNum = 3;
        } else {
            maxNum = resumePicture.size();
        }
    }
    public PictureAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        this.resumePicture = resumePicture;
        bitmapUtils=new BitmapUtils(context);
    }

    public void setMaxNum() {
        if (maxNum == 3) {
            maxNum = resumePicture.size();
        } else if (resumePicture.size() > 3) {
            maxNum = 3;
        } else {
            maxNum = resumePicture.size();
        }
    }

    @Override
    public int getCount() {
        return maxNum;
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_picture, null);
            convertView.setLayoutParams(new GridView.LayoutParams(wi, wi));
            viewPicture = new ViewPicture();
            viewPicture.picture_iv = (ImageView)convertView.findViewById(R.id.picture_iv);
            convertView.setTag(viewPicture);
        } else {
            viewPicture = (ViewPicture) convertView.getTag();
        }

        bitmapUtils.display(viewPicture.picture_iv, AppUtilsUrl.ImageBaseUrl + resumePicture.get(position).getPath());
//        viewPicture.picture_iv.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) mContext);
//        ViewTreeObserver vto = viewPicture.picture_iv.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
////                int height = viewPicture.picture_iv.getMeasuredHeight();
//                int width = viewPicture.picture_iv.getMeasuredWidth();
//                viewPicture.picture_iv.setLayoutParams(new LinearLayout.LayoutParams(wi, wi));
//                return true;
//            }
//        });
//
//        int w = viewPicture.picture_iv.getWidth();
//        viewPicture.picture_iv.setLayoutParams(new LinearLayout.LayoutParams(w,w));

        return convertView;
    }

    public class ViewPicture{
        private ImageView picture_iv;
    }

}
