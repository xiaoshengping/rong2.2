package com.jeremy.Customer.uilt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.Identification;


public class JobChoiceActivity extends Activity implements View.OnClickListener {

    private TextView job1,job2,job3,job4,job5,job6,job7,job8,job9,job10,job11,job12,job13,job14;
    private TextView job15,job16,job17,job18,job19,job20,job21,job22,job23,job24,job25,job26,job27;
    private TextView job28,job29,job30,job31,job32,job33,job34,job35,job36,job37;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_choice);
        init();
    }
    private void init(){
        job1 = (TextView)findViewById(R.id.job1);
        job2 = (TextView)findViewById(R.id.job2);
        job3 = (TextView)findViewById(R.id.job3);
        job4 = (TextView)findViewById(R.id.job4);
        job5 = (TextView)findViewById(R.id.job5);
        job6 = (TextView)findViewById(R.id.job6);
        job7 = (TextView)findViewById(R.id.job7);
        job8 = (TextView)findViewById(R.id.job8);
        job9 = (TextView)findViewById(R.id.job9);
        job9 = (TextView)findViewById(R.id.job9);
        job10 = (TextView)findViewById(R.id.job10);
        job11 = (TextView)findViewById(R.id.job11);
        job12 = (TextView)findViewById(R.id.job12);
        job13 = (TextView)findViewById(R.id.job13);
        job14 = (TextView)findViewById(R.id.job14);
        job15 = (TextView)findViewById(R.id.job15);
        job16 = (TextView)findViewById(R.id.job16);
        job17 = (TextView)findViewById(R.id.job17);
        job18 = (TextView)findViewById(R.id.job18);
        job19 = (TextView)findViewById(R.id.job19);
        job20 = (TextView)findViewById(R.id.job20);
        job21 = (TextView)findViewById(R.id.job21);
        job22 = (TextView)findViewById(R.id.job22);
        job23 = (TextView)findViewById(R.id.job23);
        job24 = (TextView)findViewById(R.id.job24);
        job25 = (TextView)findViewById(R.id.job25);
        job26 = (TextView)findViewById(R.id.job26);
        job27 = (TextView)findViewById(R.id.job27);
        job28 = (TextView)findViewById(R.id.job28);
        job29 = (TextView)findViewById(R.id.job29);
        job30 = (TextView)findViewById(R.id.job30);
        job31 = (TextView)findViewById(R.id.job31);
        job32 = (TextView)findViewById(R.id.job32);
        job33 = (TextView)findViewById(R.id.job33);
        job34 = (TextView)findViewById(R.id.job34);
        job35 = (TextView)findViewById(R.id.job35);
        job36 = (TextView)findViewById(R.id.job36);
        job37 = (TextView)findViewById(R.id.job37);

        job1.setOnClickListener(this);
        job2.setOnClickListener(this);
        job3.setOnClickListener(this);
        job4.setOnClickListener(this);
        job5.setOnClickListener(this);
        job6.setOnClickListener(this);
        job7.setOnClickListener(this);
        job8.setOnClickListener(this);
        job9.setOnClickListener(this);
        job10.setOnClickListener(this);
        job11.setOnClickListener(this);
        job12.setOnClickListener(this);
        job13.setOnClickListener(this);
        job14.setOnClickListener(this);
        job15.setOnClickListener(this);
        job16.setOnClickListener(this);
        job17.setOnClickListener(this);
        job18.setOnClickListener(this);
        job19.setOnClickListener(this);
        job20.setOnClickListener(this);
        job21.setOnClickListener(this);
        job22.setOnClickListener(this);
        job23.setOnClickListener(this);
        job24.setOnClickListener(this);
        job25.setOnClickListener(this);
        job26.setOnClickListener(this);
        job27.setOnClickListener(this);
        job28.setOnClickListener(this);
        job29.setOnClickListener(this);
        job30.setOnClickListener(this);
        job31.setOnClickListener(this);
        job32.setOnClickListener(this);
        job32.setOnClickListener(this);
        job33.setOnClickListener(this);
        job34.setOnClickListener(this);
        job35.setOnClickListener(this);
        job36.setOnClickListener(this);
        job37.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String name = null;
        switch (v.getId()){
            case R.id.job1:
                name = job1.getText().toString();
                break;
            case R.id.job2:
                name = job2.getText().toString();
                break;
            case R.id.job3:
                name = job3.getText().toString();
                break;
            case R.id.job4:
                name = job4.getText().toString();
                break;
            case R.id.job5:
                name = job5.getText().toString();
                break;
            case R.id.job6:
                name = job6.getText().toString();
                break;
            case R.id.job7:
                name = job7.getText().toString();
                break;
            case R.id.job8:
                name = job8.getText().toString();
                break;
            case R.id.job9:
                name = job9.getText().toString();
                break;
            case R.id.job10:
                name = job10.getText().toString();
                break;
            case R.id.job11:
                name = job11.getText().toString();
                break;
            case R.id.job12:
                name = job12.getText().toString();
                break;
            case R.id.job13:
                name = job13.getText().toString();
                break;
            case R.id.job14:
                name = job14.getText().toString();
                break;
            case R.id.job15:
                name = job15.getText().toString();
                break;
            case R.id.job16:
                name = job16.getText().toString();
                break;
            case R.id.job17:
                name = job17.getText().toString();
                break;
            case R.id.job18:
                name = job18.getText().toString();
                break;
            case R.id.job19:
                name = job19.getText().toString();
                break;
            case R.id.job20:
                name = job20.getText().toString();
                break;
            case R.id.job21:
                name = job21.getText().toString();
                break;
            case R.id.job22:
                name = job22.getText().toString();
                break;
            case R.id.job23:
                name = job23.getText().toString();
                break;
            case R.id.job24:
                name = job24.getText().toString();
                break;
            case R.id.job25:
                name = job25.getText().toString();
                break;
            case R.id.job26:
                name = job26.getText().toString();
                break;
            case R.id.job27:
                name = job27.getText().toString();
                break;
            case R.id.job28:
                name = job28.getText().toString();
                break;
            case R.id.job29:
                name = job29.getText().toString();
                break;
            case R.id.job30:
                name = job30.getText().toString();
                break;
            case R.id.job31:
                name = job31.getText().toString();
                break;
            case R.id.job32:
                name = job32.getText().toString();
                break;
            case R.id.job33:
                name = job33.getText().toString();
                break;
            case R.id.job34:
                name = job34.getText().toString();
                break;
            case R.id.job35:
                name = job35.getText().toString();
                break;
            case R.id.job36:
                name = job36.getText().toString();
                break;
            case R.id.job37:
                name = job37.getText().toString();
                break;
        }
        String a[] = JobChoiceActivity.this.getString(R.string.position).split(":" + name)[0].split(",");
        Intent intent = new Intent();
        intent.putExtra("Job", Integer.parseInt(a[a.length - 1]));
        intent.putExtra("JobName", name);
                        /*给上一个Activity返回结果*/
        JobChoiceActivity.this.setResult(Identification.JOBCHOICE, intent);
                        /*结束本Activity*/
        JobChoiceActivity.this.finish();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //这里重写返回键
            Intent intent = new Intent();
            intent.putExtra("Job", -1);
            intent.putExtra("JobName", "无");
                        /*给上一个Activity返回结果*/
            JobChoiceActivity.this.setResult(Identification.RETURN, intent);
            JobChoiceActivity.this.finish();
            return true;
        }
        return false;

    }

    public void back(View v) {
        Intent intent = new Intent();
        intent.putExtra("Job", -1);
        intent.putExtra("JobName", "无");
                        /*给上一个Activity返回结果*/
        JobChoiceActivity.this.setResult(Identification.RETURN, intent);
        JobChoiceActivity.this.finish();
    }

}

