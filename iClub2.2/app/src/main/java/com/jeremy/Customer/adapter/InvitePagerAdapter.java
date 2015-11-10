package com.jeremy.Customer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xiaoshengping on 2015/11/6.
 */
public class InvitePagerAdapter extends PagerAdapter {

    private String[] titles = {"邀约历史", "接受邀约", "合作成功"};
    private List<Fragment> fragments;
    private FragmentManager manager;

    public InvitePagerAdapter(List<Fragment> fragments, FragmentManager manager) {
        this.fragments = fragments;
        this.manager = manager;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = fragments.get(position);
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragment, fragment.getClass().getName());
            transaction.commitAllowingStateLoss();
            manager.executePendingTransactions();
        }

        if (fragment.getView().getParent() == null) {
            container.addView(fragment.getView());
        }

        return fragment.getView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    /**
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles != null && titles.length > 0 ? titles[position] : super.getPageTitle(position);
    }


}
