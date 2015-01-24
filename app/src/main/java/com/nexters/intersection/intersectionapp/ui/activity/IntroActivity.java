package com.nexters.intersection.intersectionapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.utils.IntersactionSession;


public class IntroActivity extends Activity {
    private static int INTRO_LOADING_TIME = 3000;
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
//        ImageView logoGif = (ImageView)findViewById(R.id.logoGif);

    }

    private void endIntro() {
        // 1. 처음일 경우, 2. 아닐 경우
        IntersactionSession intersactionSession = IntersactionSession.getInstance(this);
        boolean isVisited = intersactionSession.getBoolean(IntersactionSession.IS_VISITED);
        Intent intent = null;

        if(isVisited)
            intent = new Intent(this, MainActivity.class);
        else {
            intersactionSession.putBoolean(IntersactionSession.IS_VISITED, true);
            intent = new Intent(this, StartActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
