package com.jeremy.Customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.uilt.LoginActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    @ViewInject(R.id.touxiang_iv)
    private ImageView touXiangIv;


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_mine, container, false);
        //TextView textView= (TextView) getActivity().findViewById(R.id.text);
        ViewUtils.inject(this, view);

         intiView();
        return view;
    }

    private void intiView() {
        touXiangIv.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.touxiang_iv:
                Intent intent =new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;



        }
    }
}
