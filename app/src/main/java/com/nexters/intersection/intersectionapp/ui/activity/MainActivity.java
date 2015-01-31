package com.nexters.intersection.intersectionapp.ui.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nexters.intersection.intersectionapp.R;

public class MainActivity extends ActionBarActivity {
    public MapBridge mapBridge;
    public WebView webView;
    //    public ListView webList;
//    public EditText originEditText, destinationEditText;
//    public Button searchBtn;
//    public TextView markText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initResource();
        initEvent();
    }

    public void initResource() {
        webView = (WebView) findViewById(R.id.web_view);
        mapBridge = new MapBridge(webView);

        if (webView != null) {
            webView.loadUrl("file:///android_asset/daum1.html");
            webView.getSettings().setJavaScriptEnabled(true);
        }
        webView.addJavascriptInterface(mapBridge, "DaumApp");

//        webList = (ListView) findViewById(R.id.web_list);
//        originEditText = (EditText) findViewById(R.id.origin_edit_text);
//        destinationEditText = (EditText) findViewById(R.id.destination_edit_text);
//        searchBtn = (Button) findViewById(R.id.search_btn);
//        markText = (TextView) findViewById(R.id.mark_text);
    }

    public void initEvent() {
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String origin, destination;
////                origin = originEditText.getText().toString();
////                destination = destinationEditText.getText().toString();
////                mapBridge.search2Mark(origin, destination);
//            }
//        });
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
//<<<<<<< HEAD
//        getSupportActionBar().setCustomView(R.layout.bottom_menu_main);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//
//=======
        getMenuInflater().inflate(R.menu.menu_main, menu);

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


    public static class MapBridge {
        public WebView webView;

        public MapBridge(WebView wv) {
            webView = wv;
        }

        public void test() {
            webView.loadUrl("javascript:test()");
        }

        @JavascriptInterface
        public void test(final String str) {
            Log.d("test", "test: " + str);
        }
    }
}
