package com.jeremy.Customer.bean;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.Customer.R;


/**
 * Created by Administrator on 2015/6/4.
 */
public class MyDialog extends Dialog {
    private TextView hint,cancel,parting_line,determine;
    private String tisp;

    public MyDialog(Context context,int type,int markedWords) {
        super(context, R.style.MyDialog);
        setCustomDialog(type, markedWords);
    }

    //
    private void setCustomDialog(int type,int markedWords) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_my, null);
        hint = (TextView)mView.findViewById(R.id.hint);
        cancel = (TextView)mView.findViewById(R.id.cancel);
        parting_line = (TextView)mView.findViewById(R.id.parting_line);
        determine = (TextView)mView.findViewById(R.id.determine);

        if(markedWords == Identification.NETWORKANOMALY){
            tisp = "网络异常，请稍后重试！";
        }

        if (type == Identification.TOOLTIP){
            cancel.setVisibility(View.GONE);
            parting_line.setVisibility(View.GONE);
            determine.setText("确认");
            if(markedWords == Identification.NETWORKANOMALY){
                hint.setText(tisp);
            }
        }

        super.setContentView(mView);
    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     * @param listener
     */
    public void setDetermine(View.OnClickListener listener){
        determine.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setCancel(View.OnClickListener listener){
        cancel.setOnClickListener(listener);
    }

}

