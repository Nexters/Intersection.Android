package com.nexters.intersection.intersectionapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.nexters.intersection.intersectionapp.ui.fragment.StartFragment;

public class StartFragmentAdapter extends FragmentPagerAdapter {
    private int mCount = 8;

    public StartFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return StartFragment.newInstance(position+1);
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
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