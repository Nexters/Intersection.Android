package com.nexters.intersection.intersectionapp.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.ui.adapter.StartFragmentAdapter;
import com.viewpagerindicator.PageIndicator;

public class StartActivity extends FragmentActivity {
    StartFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        mAdapter = new StartFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (PageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
