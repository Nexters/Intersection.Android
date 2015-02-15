package com.nexters.intersection.intersectionapp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.model.Translation;
import com.nexters.intersection.intersectionapp.thread.MessageTask;
import com.nexters.intersection.intersectionapp.ui.view.WebViewObserver;
import com.nexters.intersection.intersectionapp.utils.BackPressCloseHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity {
    private int mMinFooterTranslation = 0;
    private int mMinHeaderTranslation = 0;
    private float mFooterHeight = 0f;
    private float mHeaderHeight = 0f;

    public MapBridge mapBridge;
    public WebViewObserver webView;
    private ButtonFloat mBtnSearch;
    private ButtonFloat mBtnSearch2;
    private SlidingUpPanelLayout mLayout;
    private RelativeLayout mFooter;
    private LinearLayout mHeader;
    //    private int mMinFooterTranslation=0 ;
//    private static int mFooterHeight = 0;
    //    public ListView webList;
//    public EditText originEditText, destinationEditText;
//    public Button searchBtn;
//    public TextView markText;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initResource();
        initEvent();

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void initResource() {
        mFooterHeight = getResources().getDimension(R.dimen.footer_height);
        mHeaderHeight = getResources().getDimension(R.dimen.header_height);

        webView = (WebViewObserver) findViewById(R.id.web_view);
        mapBridge = new MapBridge(webView, targetHandler);
        mFooter = (RelativeLayout) findViewById(R.id.am_footer_rl);
        mHeader = (LinearLayout) findViewById(R.id.am_header_ll);

        if (webView != null) {
            webView.loadUrl("file:///android_asset/daum1.html");
            webView.getSettings().setJavaScriptEnabled(true);
        }
        webView.addJavascriptInterface(mapBridge, "DaumApp");

        mBtnSearch = (ButtonFloat) findViewById(R.id.am_btn_search);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.hidePanel();
//        webList = (ListView) findViewById(R.id.web_list);
//        originEditText = (EditText) findViewById(R.id.origin_edit_text);
//        destinationEditText = (EditText) findViewById(R.id.destination_edit_text);
//        searchBtn = (Button) findViewById(R.id.search_btn);
//        markText = (TextView) findViewById(R.id.mark_text);
    }

    public void initEvent() {
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.showPanel();
                mFooter.setVisibility(View.GONE);
                mapBridge.test();
//                String origin, destination;
//                origin = originEditText.getText().toString();
//                destination = destinationEditText.getText().toString();
//                mapBridge.search2Mark(origin, destination);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                mapBridge.test();
//                mapBridge.firstTest();
//                mapBridge.search2Mark("봉화산역", "봉화산역");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getSupportActionBar().setTitle(R.string.app_name);

        //getSupportActionBar().setCustomView(R.layout.bottom_menu_main);
        //getSupportActionBar().setDisplayShowCustomEnabled(true);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        MenuItemCompat.setActionView(item, R.layout.activity_main);

        View view = (View) menu.findItem(R.id.action_search).getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                // Execute when actionbar's item is touched
                int mMinHeaderTranslation = 0;
                float mHeaderHeight = getResources().getDimension(R.dimen.header_height);
                switch (view2.getId()) {
                    case R.id.am_btn_search_cancel:
                        mLayout.hidePanel();
                        mFooter.setVisibility(View.VISIBLE);
                        break;
                    default:
                        if (mHeader.getTranslationY() < 0) { //Header가 숨겨져 잇다면
                            mHeader.setTranslationY(mMinHeaderTranslation);
                        } else {
                            mHeader.setTranslationY(-mHeaderHeight);
                        }
                        break;

                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void onClick(View view) {
        int mMinHeaderTranslation = 0;
        float mHeaderHeight = getResources().getDimension(R.dimen.header_height);
        switch (view.getId()) {
            case R.id.am_btn_search_cancel:
                mLayout.hidePanel();
                mFooter.setVisibility(View.VISIBLE);
                break;
            default:
                if (mHeader.getTranslationY() < 0) { //Header가 숨겨져 잇다면
                    mHeader.setTranslationY(mMinHeaderTranslation);
                } else {
                    mHeader.setTranslationY(-mHeaderHeight);
                }
                break;

        }
    }

    // TODO MAP Method
    public void procScrollChangedCallback() {
        if (!(mFooter.getTranslationY() > 0)) { //Hide Footer
            mFooter.setTranslationY(mFooterHeight);
            Animation animation = new TranslateAnimation(0, 0, -mFooterHeight, 0);
            animation.setDuration(300);
            mFooter.startAnimation(animation);
        }
        if (mHeader.getTranslationY() >= 0) { //Hide Header
            mHeader.setTranslationY(-mHeaderHeight);
            Animation animation = new TranslateAnimation(0, 0, mHeaderHeight, 0);
            animation.setDuration(300);
            mHeader.startAnimation(animation);
        }
    }

    public void procToggleToolbar() {
        if (mFooter.getVisibility() != View.GONE) { // 검색 결과 화면이 아닐시에
            //Toggle Footer
            if (mFooter.getTranslationY() > 0) {
                //Footer가 가려진 상태
                Log.d(MainActivity.class.getSimpleName(), "show");
                mFooter.setTranslationY(mMinFooterTranslation);
                Animation animation = new TranslateAnimation(0, 0, mFooterHeight, 0);
                animation.setDuration(300);
                mFooter.startAnimation(animation);
            } else {
                //Footer가 보이는 상태
                Log.d(MainActivity.class.getSimpleName(), "hide");
                mFooter.setTranslationY(mFooterHeight);
                Animation animation = new TranslateAnimation(0, 0, -mFooterHeight, 0);
                animation.setDuration(300);
                mFooter.startAnimation(animation);
            }
        } else { //검색결과 화면일시에
            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                Log.d("Hidden Panel State", mLayout.getPanelState().name());
                mLayout.hidePanel();

            } else {
                Log.d("!Hidden Panel State", mLayout.getPanelState().name());
                mLayout.showPanel();
            }
        }
    }

    public void getTranslation(String name) {
        ArrayList<String> list = new ArrayList<String>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        final String path = this.getString(R.string.trans_like_list);

        try {
            list.add(URLEncoder.encode(name, "utf-8"));
        } catch(Exception ex){
            ex.printStackTrace();
        }
        hashMap.put("names", list);

        MessageTask.postJson(path, this, hashMap, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Translation translation = null;

                if (response.length() > 0) {
                    try {
                        String json = response.getJSONObject(0).toString();
                        translation = (new Gson()).fromJson(response.getJSONObject(0).toString(), Translation.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.d("getTranslation", translation.toString());
            }
        });
    }

    // TODO MAP Bridge
    Handler targetHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MapBrigeType mapBridgeType = (MapBrigeType) msg.getData().getSerializable("type");
//            Log.d("type", "type");

            switch (mapBridgeType) {
                case ScrollChangedCallback:
                    procScrollChangedCallback();
                    break;
                case ToggleToolbar:
                    procToggleToolbar();
                    break;
                case Translation:
                    String name = msg.getData().getString("name");
                    getTranslation(name);
                    break;
            }
        }
    };
}
