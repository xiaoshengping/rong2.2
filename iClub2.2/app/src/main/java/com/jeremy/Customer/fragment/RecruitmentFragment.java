package com.jeremy.Customer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.adapter.RecommendListAdater;
import com.lidroid.xutils.ViewUtils;

import static com.jeremy.Customer.bean.Identification.PROSITION;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecruitmentFragment extends Fragment {

    private ListView recommend_list;

    public RecruitmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruitment, container, false);
        ViewUtils.inject(this, view);
        intiProsition(view);
        return view;

    }

    //初始化职位列表
    private void intiProsition(View view) {
        recommend_list = (ListView)view.findViewById(R.id.recommend_list);
        RecommendListAdater adater = new RecommendListAdater(getActivity(), PROSITION);
        recommend_list.setAdapter(adater);
    }


}
