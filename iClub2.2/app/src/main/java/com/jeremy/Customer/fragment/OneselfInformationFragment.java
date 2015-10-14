package com.jeremy.Customer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.Customer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneselfInformationFragment extends Fragment {


    public OneselfInformationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_oneself_information, container, false);



        return view;
    }


}
