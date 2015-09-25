package com.jeremy.Customer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.RecommendListAdater;
import com.jeremy.Customer.bean.Identification;
import com.lidroid.xutils.ViewUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalentFragment extends Fragment {

    private ListView recommend_list;

    public TalentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talent, container, false);
        ViewUtils.inject(this, view);
        intiTalents(view);
        return view;

    }

    //初始化人才列表
    private void intiTalents(View view) {
        recommend_list = (ListView)view.findViewById(R.id.recommend_list);
        RecommendListAdater adater = new RecommendListAdater(getActivity(), Identification.TALENTS);
        recommend_list.setAdapter(adater);
    }

}
