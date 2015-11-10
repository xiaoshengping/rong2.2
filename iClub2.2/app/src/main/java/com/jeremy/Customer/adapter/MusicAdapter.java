package com.jeremy.Customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.mine.ResumeMusic;

import java.util.List;

/**
 * Created by Administrator on 2015/9/25.
 */
public class MusicAdapter extends BaseAdapter {

    private ViewMusic viewMusic;
    private LayoutInflater mInflater;
    private List<ResumeMusic> resumeMusic;
    private Context mContext;
    private int maxNum = 0;

    public MusicAdapter(Context context,List<ResumeMusic> resumeMusic){
        this.mInflater = LayoutInflater.from(context);
        this.resumeMusic = resumeMusic;
        mContext = context;
        if (resumeMusic.size() > 2) {
            maxNum = 2;
        } else {
            maxNum = resumeMusic.size();
        }
    }

    public void setMaxNum() {
        if (maxNum == 2) {
            maxNum = resumeMusic.size();
        } else if (resumeMusic.size() > 2) {
            maxNum = 2;
        } else {
            maxNum = resumeMusic.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_music, null);
            viewMusic = new ViewMusic();
            viewMusic.music_tv = (TextView)convertView.findViewById(R.id.music_tv);

            convertView.setTag(viewMusic);
        } else {
            viewMusic = (ViewMusic) convertView.getTag();
        }

        viewMusic.music_tv.setText(resumeMusic.get(position).getTitle());
//        viewMusic.music_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MusicActivity.class);  //方法1
//                intent.putExtra("url", AppUtilsUrl.ImageBaseUrl + resumeMusic.get(position).getPath());
//                intent.putExtra("musicName", resumeMusic.get(position).getTitle());
//                intent.putExtras(intent);
//                mContext.startActivity(intent);
//                mContext.overridePendingTransition(R.anim.out_to_not,R.anim.music_in);
//            }
//        });

        return convertView;
    }

    public class ViewMusic{
        private TextView music_tv;
    }

}
