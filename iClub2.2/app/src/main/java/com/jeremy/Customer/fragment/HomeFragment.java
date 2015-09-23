package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.Identification;
import com.jeremy.Customer.uilt.RecommenListActivity;
import com.jeremy.Customer.view.SlideShowView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    @ViewInject(R.id.advertisement_ll)
    private LinearLayout advertisement_ll;

    private TextView home_more1,home_more2,home_more3;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ViewUtils.inject(this, view);
        binding(view);
        initAdvertisement();
        return view;
    }

    private void binding(View view){
        home_more1 = (TextView)view.findViewById(R.id.home_more1);
        home_more2 = (TextView)view.findViewById(R.id.home_more2);
        home_more3 = (TextView)view.findViewById(R.id.home_more3);
        home_more1.setOnClickListener(this);
        home_more2.setOnClickListener(this);
        home_more3.setOnClickListener(this);
    }

    //初始化广告栏
    private void initAdvertisement(){
        SlideShowView ssv = new SlideShowView(getActivity());
        advertisement_ll.addView(ssv);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (getResources().getDimension(R.dimen.advertisement_height)));
        layoutParams.setMargins(0, 0, 0, 0);
        ssv.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_more1:
                TouchMore(Identification.ACTIVITY);
                break;
            case R.id.home_more2:
                TouchMore(Identification.TALENTS);
                break;
            case R.id.home_more3:
                TouchMore(Identification.PROSITION);
                break;
        }
    }

    private void TouchMore(int ident){
        Intent intent = new Intent();
        intent.setClass(getActivity(), RecommenListActivity.class);
        intent.putExtra("Ident", ident);
        startActivity(intent);
    }

}
