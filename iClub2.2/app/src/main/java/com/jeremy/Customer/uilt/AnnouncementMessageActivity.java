package com.jeremy.Customer.uilt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.InfomationAdapter;
import com.jeremy.Customer.bean.ArtistParme;
import com.jeremy.Customer.bean.mine.InformationValueBean;
import com.jeremy.Customer.http.MyAppliction;
import com.jeremy.Customer.url.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class AnnouncementMessageActivity extends ActionBarActivity implements View.OnClickListener {

    @ViewInject(R.id.tailt_return_tv)
    private Button tailtReturnButton;
    @ViewInject(R.id.tailt_text)
    private TextView tailtText;
    @ViewInject(R.id.information_lv)
    private ListView informationListv;
    private List<InformationValueBean> informationValueBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        ViewUtils.inject(this);
        inti();
    }

    private void inti() {
        intiView();
        intiData();

    }

    private void intiView() {
        tailtText.setText("公告消息");
        tailtReturnButton.setOnClickListener(this);



    }
    private void intiData() {
        HttpUtils httpUtils=new HttpUtils();
        String informationUrl= AppUtilsUrl.getInformationList("0", "30");
        httpUtils.send(HttpRequest.HttpMethod.POST, informationUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //Log.e("jsjdfjff",result);
                ArtistParme<InformationValueBean> artistParme = JSONObject.parseObject(result, new TypeReference<ArtistParme<InformationValueBean>>() {
                });
                informationValueBeans = artistParme.getValue();
                InfomationAdapter infomationAdapter = new InfomationAdapter(informationValueBeans, AnnouncementMessageActivity.this);
                informationListv.setAdapter(infomationAdapter);
                infomationAdapter.notifyDataSetChanged();
                informationListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(AnnouncementMessageActivity.this, DetailedInformationActivity.class);
                        intent.putExtra("informationValueBeans", informationValueBeans.get(position));
                        startActivity(intent);


                    }
                });

            }

            @Override
            public void onFailure(HttpException e, String s) {
                MyAppliction.showToast("网络异常...");
            }
        });




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tailt_return_tv:
                finish();
                break;


        }
    }
}
