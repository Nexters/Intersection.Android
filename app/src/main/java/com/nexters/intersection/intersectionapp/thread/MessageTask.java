package com.nexters.intersection.intersectionapp.thread;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.nexters.intersection.intersectionapp.R;

import org.apache.http.entity.StringEntity;

import java.net.URLEncoder;

/**
 * Created by BoBinLee on 2014-09-04.
 */
public class MessageTask {
    private static AsyncHttpClient asyncClient = new AsyncHttpClient();
    private static AsyncHttpClient syncClient = new SyncHttpClient();
    private static Gson gson = new Gson();

    public static void postJson(String path, Context context, Object reqParam, AsyncHttpResponseHandler responseHandler) {
        String url = context.getString(R.string.base_uri) + path;
        StringEntity jsonParams = null;

        try {
            jsonParams = new StringEntity(gson.toJson(reqParam));
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.d("postJson", "" + gson.toJson(reqParam));
        asyncClient.post(context, url, jsonParams, "application/json",
                responseHandler);
    }

    public static void postSyncJson(String path, Context context, Object reqParam, AsyncHttpResponseHandler responseHandler) {
        String url = context.getString(R.string.base_uri) + path;

        StringEntity jsonParams = null;
        try {
            jsonParams = new StringEntity(URLEncoder.encode(gson.toJson(reqParam), "utf-8"));
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.d("postSyncJson", "" + gson.toJson(reqParam));
        syncClient.post(context, url, jsonParams, "application/json",
                responseHandler);
    }
}
