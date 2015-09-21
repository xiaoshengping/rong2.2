package com.jeremy.Customer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jeremy.Customer.R;
import com.jeremy.Customer.view.SlideShowView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @ViewInject(R.id.advertisement_ll)
    private LinearLayout advertisement_ll;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ViewUtils.inject(this, view);
        initAdvertisement();
        return view;
    }

    //初始化广告栏
    private void initAdvertisement(){
        SlideShowView ssv = new SlideShowView(getActivity());
        advertisement_ll.addView(ssv);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (getResources().getDimension(R.dimen.advertisement_heitht)));
        layoutParams.setMargins(0, 0, 0, 0);
        ssv.setLayoutParams(layoutParams);
    }


}
