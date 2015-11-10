package com.jeremy.Customer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.Customer.R;
import com.lidroid.xutils.ViewUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModificationInformationFragment extends Fragment implements View.OnClickListener {








    public ModificationInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_modification_information, container, false);
        ViewUtils.inject(this, view);
         init();
        return view;
    }

    private void init() {
        initView();


    }



    private void initView() {




    }





    @Override
    public void onClick(View v) {

    }


}
