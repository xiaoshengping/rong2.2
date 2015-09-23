package com.jeremy.Customer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;

/**
 * Created by Administrator on 2015/9/22.
 */
public class MyInputBox extends LinearLayout{

    private TextView  textView;
    private EditText editText;

    public MyInputBox(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.input_box, this);
        textView=(TextView)findViewById(R.id.input_box_text);
        editText=(EditText)findViewById(R.id.input_box_edit);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyInputBox);
        String title_name_text = array.getString(R.styleable.MyInputBox_input_box_text);
        String input_box_edit = array.getString(R.styleable.MyInputBox_input_box_edit);
        textView.setText(title_name_text.substring(0,1)+"     "+title_name_text.substring(1));
        editText.setHint(input_box_edit);
    }

    public void setTextViewText(String text) {
        textView.setText(text);
    }
    public String getEditText() {
        return editText.getText().toString();
    }

}