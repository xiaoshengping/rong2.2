package com.jeremy.Customer.bean;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;


/**
 * Created by Administrator on 2015/6/4.
 */
public class MyDialog extends Dialog {
    private TextView tips_tv;
    private LinearLayout register_or_cancel;
    private TextView register;
    private TextView cancel;
    private EditText search_import_et;
    private TextView searchbox_seek_tv;

    public MyDialog(Context context,int type,int markedWords) {
        super(context, R.style.MyDialog);
        setCustomDialog();
    }

    //
    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_my, null);

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
    public void setOnPositiveListener(View.OnClickListener listener){
        register.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener){
        cancel.setOnClickListener(listener);
    }

    /**
     * 确定键监听器
     * @param listener
     */
    public void setSeek_ib(View.OnClickListener listener){

//        seek_ib.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setEliminate_tv(View.OnClickListener listener){
        searchbox_seek_tv.setOnClickListener(listener);
    }


}

