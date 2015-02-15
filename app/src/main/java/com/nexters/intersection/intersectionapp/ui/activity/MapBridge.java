package com.nexters.intersection.intersectionapp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by BoBinLee on 2015-02-15.
 */
public class MapBridge {
    private WebView webView;
    private Handler mHandler;

    public MapBridge(WebView wv, Handler handler) {
        webView = wv;
        mHandler = handler;
    }

    public void test() {
        webView.loadUrl("javascript:test()");
    }

    @JavascriptInterface
    public void getTranslation(final String name) {
        Message msg = new Message();
        Bundle bundle = msg.getData();
        bundle.putSerializable("type", MapBrigeType.Translation);
        bundle.putString("name", name);
        msg.setData(bundle);

        mHandler.sendMessage(msg);
    }

    @JavascriptInterface
    public void ToggleToolbar() {
        Message msg = new Message();
        Bundle bundle = msg.getData();
        bundle.putSerializable("type", MapBrigeType.ToggleToolbar);
        msg.setData(bundle);

        mHandler.sendMessage(msg);
    }

    @JavascriptInterface
    public void onScrollChangedCallback() {
        Log.d(MainActivity.class.getSimpleName(), "OnScroll");
        Log.d(MainActivity.class.getSimpleName(), "hideToolbar");

        Message msg = new Message();
        Bundle bundle = msg.getData();
        bundle.putSerializable("type", MapBrigeType.ScrollChangedCallback);
        msg.setData(bundle);

        mHandler.sendMessage(msg);
    }

    @JavascriptInterface
    public void test(final String str) {
        Log.d("test", "test: " + str);
    }
}
