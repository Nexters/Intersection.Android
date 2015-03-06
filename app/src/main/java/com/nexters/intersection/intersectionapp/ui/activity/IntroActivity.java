package com.nexters.intersection.intersectionapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.model.User;
import com.nexters.intersection.intersectionapp.thread.MessageTask;
import com.nexters.intersection.intersectionapp.utils.CommonUtils;
import com.nexters.intersection.intersectionapp.utils.IntersactionSession;

import org.apache.http.Header;
import org.json.JSONObject;


public class IntroActivity extends Activity {
    private static int INTRO_LOADING_TIME = 2000;
    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);


        initResource();
    }

    private void initResource(){

        //skip();
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                endIntro();
            }
        }, INTRO_LOADING_TIME);

    }

    private void skip(){
        Intent intent = null;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void endIntro() {
        // 1. 처음일 경우, 2. 아닐 경우
        IntersactionSession intersactionSession = IntersactionSession.getInstance(this);
        boolean isVisited = intersactionSession.getBoolean(IntersactionSession.IS_VISITED);
        Intent intent = null;

        // 유저 등록
        addUser();

        intent = new Intent(this, MainActivity.class);
        if(isVisited)
            intent = new Intent(this, MainActivity.class);
        else
            intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    private void addUser(){
        User user = new User();
        user.pushYn = "N";
        user.phoneId = CommonUtils.getAndroidId(this);
        user.tokenId = "";

        final String path = this.getString(R.string.user_add);
        MessageTask.postJson(path, this, user, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(path, "response : " + response.toString());
            }
        });
    }

}
