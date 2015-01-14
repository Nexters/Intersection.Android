package com.nexters.intersection.intersectionapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.core.SessionManager;


public class IntroActivity extends Activity {
    private static int INTRO_LOADING_TIME = 1000;
    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);

        initResource();
    }

    private void initResource(){
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                endIntro();
            }
        }, INTRO_LOADING_TIME);
    }

    private void endIntro() {
        // 1. 처음일 경우, 2. 아닐 경우
        SessionManager sessionManager = SessionManager.getInstance(this);
        boolean isVisited = sessionManager.getBoolean(SessionManager.IS_VISITED);
        Intent intent = null;

        isVisited = false;
        if(isVisited)
            intent = new Intent(this, MainActivity.class);
        else
            intent = new Intent(this, StartActivity.class);

        startActivity(intent);
        finish();
    }

}
