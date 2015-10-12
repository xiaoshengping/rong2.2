package com.jeremy.Customer.citySelection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.bean.LoadingDialog;

import java.util.Arrays;


public class CitySelectionActivity extends Activity implements View.OnClickListener {

    /**
     * Called when the activity is first created.
     */
//	private static ArrayList<ProvinceBean> parentData = new ArrayList<ProvinceBean>();

    private SideBar indexBar;
    private WindowManager mWindowManager;
    private TextView mDialogText;
    //    private String PROVINCE_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getRegionProvince";
    private static String[] cityName;
    private String[] books;
    private ArrayAdapter<String> av;
    //    static int i;
    ListViewAdp lAdp;
    //    ProgressDialog prodialog;
    ListView lvContact;

    private TextView hot_city1, hot_city2, hot_city3, hot_city4, hot_city5, hot_city6, hot_city7, hot_city8, hot_city9;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);
        lvContact = (ListView) this.findViewById(R.id.lvContact);
        View header = View.inflate(CitySelectionActivity.this, R.layout.hot_city_slot_head, null);//头部内容
        hot_city1 = (TextView) header.findViewById(R.id.hot_city1);
        hot_city2 = (TextView) header.findViewById(R.id.hot_city2);
        hot_city3 = (TextView) header.findViewById(R.id.hot_city3);
        hot_city4 = (TextView) header.findViewById(R.id.hot_city4);
        hot_city5 = (TextView) header.findViewById(R.id.hot_city5);
        hot_city6 = (TextView) header.findViewById(R.id.hot_city6);
        hot_city7 = (TextView) header.findViewById(R.id.hot_city7);
        hot_city8 = (TextView) header.findViewById(R.id.hot_city8);
        hot_city9 = (TextView) header.findViewById(R.id.hot_city9);
        hot_city1.setOnClickListener(this);
        hot_city2.setOnClickListener(this);
        hot_city3.setOnClickListener(this);
        hot_city4.setOnClickListener(this);
        hot_city5.setOnClickListener(this);
        hot_city6.setOnClickListener(this);
        hot_city7.setOnClickListener(this);
        hot_city8.setOnClickListener(this);
        hot_city9.setOnClickListener(this);
        lvContact.addHeaderView(header);//添加头部
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        findView();
        getdata();
        // 设置右侧触摸监听
        indexBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // TODO Auto-generated method stub
                // 该字母首次出现的位置
                int position = lAdp.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lvContact.setSelection(position);
                }
            }
        });

    }


    private void initSearchBox() {

//        books = CitySelectionActivity.this.getString(R.string.city_name).split(",");

        av = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, cityName);
        AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        auto.setAdapter(av);

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Byte a= 42f4c528;
//                hot_city1.setText(av.getItem(position).toString().toLowerCase());
//                hot_city2.setText("广州".getBytes()+"");
//                hot_city3.setText(42f4c528);
//                hot_city4.setText("广州".getBytes()+"");
                returnCity(av.getItem(position).toString().toLowerCase());
            }
        });

    }


    Handler hand = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // lAdp.notifyDataSetChanged();// 动态更新ListView
                    loadingDialog.dismiss();
                    lAdp = new ListViewAdp(CitySelectionActivity.this, cityName);
                    lvContact.setAdapter(lAdp);

                    lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                TextView contactitem_nick = (TextView) view.findViewById(R.id.contactitem_nick);
                                returnCity(contactitem_nick.getText().toString());
                            }
                        }
                    });

                    // findView();
                    String Up[] = lAdp.first;
                    System.out.println("00000000" + Up);
                    initSearchBox();
                    break;

                default:
                    break;
            }

        }

    };

    //返回选择的值
    private void returnCity(String nameCity) {
        String a[] = CitySelectionActivity.this.getString(R.string.area).split(":" + nameCity)[0].split(",");
        Intent intent = new Intent();
        intent.putExtra("City", Integer.parseInt(a[a.length - 1]));
        intent.putExtra("CityName", nameCity);
                        /*给上一个Activity返回结果*/
        CitySelectionActivity.this.setResult(Identification.CITYSELECTION, intent);
                        /*结束本Activity*/
        CitySelectionActivity.this.finish();
    }

    private void findView() {

        // indexBar=new SideBar(this, lAdp.first);
        indexBar = (SideBar) findViewById(R.id.sideBar);
        // indexBar.setListView(lvContact);
        mDialogText = (TextView) LayoutInflater.from(this).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);

        indexBar.setTextView(mDialogText);
    }

    private static String[] sArea;
    private LoadingDialog loadingDialog;

    public void getdata() {

        loadingDialog = new LoadingDialog(this,"正在更新数据……");
        loadingDialog.show();




//        loadingDialog = new LoadingDialog(CitySelectionActivity.this,"正在更新数据……");
//        dialog2.setDetermine(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                recommend_list.setVisibility(View.GONE);
//                dialog2.dismiss();
//            }
//        });

//        loadingDialog.show();

//        System.out.println("1111111112");
//        prodialog = ProgressDialog.show(CitySelectionActivity.this, "",
//                "正在更新数据……", true, true);
//        System.out.println("1111111113");
        Thread thread = new Thread() {

            @Override
            public void run() {


//                cityName = CitySelectionActivity.this.getString(R.string.city_name).split(",");
                if (cityName == null) {
                    sArea = CitySelectionActivity.this.getString(R.string.area).split(",");
                    cityName = new String[sArea.length];
                    for (int i = 0; i < sArea.length; i++) {
                        cityName[i] = sArea[i].split(":")[1];
                    }
                    Arrays.sort(cityName, new PinyinComparator());
//				parentData.clear();
//				Domxml.getDomTools().aaa(parentData);
                }
                Message msg = hand.obtainMessage();
                msg.what = 1;
                msg.sendToTarget();
            }
        };
        thread.start();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hot_city1:
                returnCity(hot_city1.getText().toString());
                break;
            case R.id.hot_city2:
                returnCity(hot_city2.getText().toString());
                break;
            case R.id.hot_city3:
                returnCity(hot_city3.getText().toString());
                break;
            case R.id.hot_city4:
                returnCity(hot_city4.getText().toString());
                break;
            case R.id.hot_city5:
                returnCity(hot_city5.getText().toString());
                break;
            case R.id.hot_city6:
                returnCity(hot_city6.getText().toString());
                break;
            case R.id.hot_city7:
                returnCity(hot_city7.getText().toString());
                break;
            case R.id.hot_city8:
                returnCity(hot_city8.getText().toString());
                break;
            case R.id.hot_city9:
                returnCity(hot_city9.getText().toString());
                break;

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //这里重写返回键
            Intent intent = new Intent();
            intent.putExtra("City", -1);
            intent.putExtra("CityName", "无");
                        /*给上一个Activity返回结果*/
            CitySelectionActivity.this.setResult(Identification.RETURN, intent);
            CitySelectionActivity.this.finish();
            return true;
        }
        return false;

    }

}