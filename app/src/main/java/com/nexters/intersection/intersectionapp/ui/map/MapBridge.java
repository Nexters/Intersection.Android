package com.nexters.intersection.intersectionapp.ui.map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.nexters.intersection.intersectionapp.ui.activity.MainActivity;

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

    public void moveLocation(double lat, double lng){
        webView.loadUrl("javascript:moveLocation(" + lat + "," + lng + ")");
    }

    public void searchIntersection() {
        webView.loadUrl("javascript:searchIntersection()");
    }

    public void directSearch(String search){
        webView.loadUrl("javascript:directSearch('" + search + "')");
    }

    @JavascriptInterface
    public void getTranslation(final String name, final String address) {
        Message msg = new Message();
        Bundle bundle = msg.getData();
        bundle.putSerializable("type", MapBridgeType.Translation);
        bundle.putString("name", name);
        bundle.putString("address", address);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    @JavascriptInterface
    public void ToggleToolbar() {
        Message msg = new Message();
        Bundle bundle = msg.getData();
        bundle.putSerializable("type", MapBridgeType.ToggleToolbar);
        msg.setData(bundle);

        mHandler.sendMessage(msg);
    }

    @JavascriptInterface
    public void onScrollChangedCallback() {
        Log.d(MainActivity.class.getSimpleName(), "OnScroll");
        Log.d(MainActivity.class.getSimpleName(), "hideToolbar");

        Message msg = new Message();
        Bundle bundle = msg.getData();
        bundle.putSerializable("type", MapBridgeType.ScrollChangedCallback);
        msg.setData(bundle);

        mHandler.sendMessage(msg);
    }

    @JavascriptInterface
    public void fixMyLocation(final double lat, final double lng) {
        Log.d("test", "fixMyLocation: " + lat + " - " + lng);

        Message msg = new Message();
        Bundle bundle = msg.getData();
        bundle.putSerializable("type", MapBridgeType.FixedMyLocation);
        bundle.putDouble("lat", lat);
        bundle.putDouble("lng", lng);
        msg.setData(bundle);

        mHandler.sendMessage(msg);
    }


    @JavascriptInterface
    public void test(final String str) {
        Log.d("test", "test: " + str);
    }
}
