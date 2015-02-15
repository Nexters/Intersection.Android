package com.nexters.intersection.intersectionapp;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import dalvik.annotation.TestTargetClass;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public void macTest(){
//        Log.d("test", "Test : eth0 " + CommonUtils.getMACAddress("eth0")); // show value
//        Log.d("test", "Test : wlan0 " + CommonUtils.getMACAddress("wlan0")); // show null
    }



}