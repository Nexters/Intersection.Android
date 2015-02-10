package com.nexters.intersection.intersectionapp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gc.materialdesign.views.ButtonFloat;
import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.ui.view.WebViewObserver;
import com.nexters.intersection.intersectionapp.utils.BackPressCloseHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends ActionBarActivity {
    private final Handler mHandler = new Handler();

    public MapBridge mapBridge;
    public WebViewObserver webView;
    private ButtonFloat mBtnSearch;
    private SlidingUpPanelLayout mLayout;
//    private RelativeLayout mFooter;
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
        webView = (WebViewObserver) findViewById(R.id.web_view);
        mapBridge = new MapBridge(webView);
        mFooter = (RelativeLayout)findViewById(R.id.am_footer_rl);
//        mFooter = (LinearLayout)findViewById(R.id.am_search_ll);
        mHeader = (LinearLayout)findViewById(R.id.am_header_ll);

        if (webView != null) {
            webView.loadUrl("file:///android_asset/daum1.html");
            webView.getSettings().setJavaScriptEnabled(true);
        }
        webView.addJavascriptInterface(mapBridge, "DaumApp");

        mBtnSearch = (ButtonFloat)findViewById(R.id.am_btn_search);

        mLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
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
                mBtnSearch.hide();
                mLayout.showPanel();
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
//        webView.setOnScrollCallBack(new WebViewObserver.onScrollChangedCallback() {
//            @Override
//            public void onScroll(int l, int t, int oldl, int oldt) {
//                Log.d(MainActivity.class.getSimpleName(), "OnScroll");
//                int diff = oldt - t ;
//                //float newY = mWebViewObserver.getScrollY();
//                if(diff<=0){
//                    //Scroll Down
//                    Log.d(MainActivity.class.getSimpleName(), "hideToolbar");
//                    mFooterHeight = Math.max(mFooterHeight + diff, -mMinFooterTranslation);
//                }
//                else {
//                    //Scroll UP
//                    Log.d(MainActivity.class.getSimpleName(), "showToolbar");
//                    mFooterHeight = Math.min(Math.max(mFooterHeight + diff, -mMinFooterTranslation), 0);
//                }
//                mFooter.setTranslationY(-mFooterHeight);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getSupportActionBar().setTitle(R.string.app_name);

        //getSupportActionBar().setCustomView(R.layout.bottom_menu_main);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){
        int mMinHeaderTranslation = 0;
       // 0과 음수의 사이

        float mHeaderHeight = getResources().getDimension(R.dimen.header_height);
        if(mHeader.getTranslationY() < 0 ) { //Header가 숨겨져 잇다면
            mHeader.setTranslationY(mMinHeaderTranslation);
        } else {
            mHeader.setTranslationY(-mHeaderHeight);
        }


    }

    public class MapBridge {
        public WebView webView;
        private int mMinFooterTranslation = 0;
        private int mMinHeaderTranslation = 0;
        private float mFooterHeight = getResources().getDimension(R.dimen.footer_height);
        private float mHeaderHeight = getResources().getDimension(R.dimen.header_height);
        public MapBridge(WebView wv) {
            webView = wv;
        }

        public void test() {
            webView.loadUrl("javascript:test()");
        }

        @JavascriptInterface
        public void ToggleToolbar() {
            mHandler.post(new Runnable() {
                public void run() {
                    //Toggle Footer
                    if(mFooter.getTranslationY() > 0) {
                        //Footer가 가려진 상태
                        Log.d(MainActivity.class.getSimpleName(), "show");
                        mFooter.setTranslationY(mMinFooterTranslation);
                        Animation animation = new TranslateAnimation(0,0,mFooterHeight,0);
                        animation.setDuration(300);
                        mFooter.startAnimation(animation);
                    }
                    else {
                        //Footer가 보이는 상태
                        Log.d(MainActivity.class.getSimpleName(), "hide");
                        mFooter.setTranslationY(mFooterHeight);
                        Animation animation = new TranslateAnimation(0,0, -mFooterHeight,0);
                        animation.setDuration(300);
                        mFooter.startAnimation(animation);

                    }
                }
            });
        }
        @JavascriptInterface
        public void onScrollChangedCallback() {
            Log.d(MainActivity.class.getSimpleName(), "OnScroll");
            Log.d(MainActivity.class.getSimpleName(), "hideToolbar");


            mHandler.post(new Runnable() {
                public void run() {
                    if(!(mFooter.getTranslationY() > 0)) {
                        mFooter.setTranslationY(mFooterHeight);
                        Animation animation = new TranslateAnimation(0, 0, -mFooterHeight, 0);
                        animation.setDuration(300);
                        mFooter.startAnimation(animation);
                    }
                    if(mHeader.getTranslationY() >= 0 ) {
                        mHeader.setTranslationY(-mHeaderHeight);
                        Animation animation = new TranslateAnimation(0, 0, mHeaderHeight, 0);
                        animation.setDuration(300);
                        mHeader.startAnimation(animation);

                    }

                }
            });

        }
        @JavascriptInterface
        public void test(final String str) {
            Log.d("test", "test: " + str);
        }
    }
}
