package com.nexters.intersection.intersectionapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nexters.intersection.intersectionapp.ui.fragment.StartFragment;

public class StartFragmentAdapter extends FragmentPagerAdapter {
    private int mCount = 3;

    public StartFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return StartFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mCount;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//      return StartFragmentAdapter.CONTENT[position % CONTENT.length];
//    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}