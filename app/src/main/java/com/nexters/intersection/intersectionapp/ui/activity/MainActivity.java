package com.nexters.intersection.intersectionapp.ui.activity;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.ui.map.MapBridgeType;
import com.nexters.intersection.intersectionapp.model.Translation;
import com.nexters.intersection.intersectionapp.thread.MessageTask;
import com.nexters.intersection.intersectionapp.ui.map.MapBridge;
import com.nexters.intersection.intersectionapp.ui.view.WebViewObserver;
import com.nexters.intersection.intersectionapp.utils.BackPressCloseHandler;
import com.nexters.intersection.intersectionapp.utils.CommonUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apache.http.Header;
import org.json.JSONArray;

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

    private RelativeLayout mFooterResult;
    private TextView mLikeCnt, mTransName;
    private Button mSelectedTransCancel, mSelectedTransDone;
    private ImageView mToggleLike;

    private RelativeLayout mFooter;

    private LinearLayout mHeader;
    private Button mMyLoc, mTutorial, mVersion, mContact;

    private BackPressCloseHandler backPressCloseHandler;

    // action bar search
    private TextView mSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initResource();
        initEvent();
    }

    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public void initResource() {
        backPressCloseHandler = new BackPressCloseHandler(this);

        mFooterHeight = getResources().getDimension(R.dimen.footer_height);
        mHeaderHeight = getResources().getDimension(R.dimen.header_height);

        webView = (WebViewObserver) findViewById(R.id.web_view);
        mapBridge = new MapBridge(webView, targetHandler);
        mFooter = (RelativeLayout) findViewById(R.id.am_footer_rl);

        mHeader = (LinearLayout) findViewById(R.id.am_header_ll);
        mMyLoc = (Button) mHeader.findViewById(R.id.am_header_myloc);

        mFooterResult = (RelativeLayout) findViewById(R.id.am_result_footer_rl);

        if (webView != null) {
            webView.loadUrl("file:///android_asset/daum1.html");
            webView.getSettings().setJavaScriptEnabled(true);
        }
        webView.addJavascriptInterface(mapBridge, "DaumApp");

        mBtnSearch = (ButtonFloat) findViewById(R.id.am_btn_search);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.hidePanel();

        mLikeCnt = (TextView) mFooterResult.findViewById(R.id.am_tv_like_count);
        mTransName = (TextView) mFooterResult.findViewById(R.id.am_tv_result);
        mToggleLike = (ImageView) mFooterResult.findViewById(R.id.am_toggle_btn_like);

        mSelectedTransDone = (Button) mFooterResult.findViewById(R.id.am_btn_search_done);
        mSelectedTransCancel = (Button) mFooterResult.findViewById(R.id.am_btn_search_cancel);
    }

    public void initEvent() {
        mMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.getMyLocation(MainActivity.this, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d("MyLocationListener", "Latitudine = " + location.getLatitude() + "Longitudine = " + location.getLongitude());
                        mapBridge.moveLocation(location.getLatitude(), location.getLongitude());
                    }
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) { }
                    @Override
                    public void onProviderEnabled(String provider) { }
                    @Override
                    public void onProviderDisabled(String provider) { }
                });
            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapBridge.searchIntersection();
            }
        });

        mToggleLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Translation translation = (Translation) mFooterResult.getTag(mFooterResult.getId());
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                String path = MainActivity.this.getString(R.string.toggle_like);

                hashMap.put("transNo", translation.getTransNo());
                hashMap.put("phoneId", "" + CommonUtils.getAndroidId(MainActivity.this));

                MessageTask.postJson(path, MainActivity.this, hashMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        Log.d("mToggleLike", translation.getName());
                        getTranslation(translation.getName());
                    }
                });
            }
        });
        mSelectedTransDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mSelectedTransCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.hidePanel();
                mFooter.setVisibility(View.VISIBLE);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportActionBar().setTitle(R.string.app_name);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //  Action Bar에서 SearchView를 보여주고 싶을때 사용하는 클래스입니다.
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                mapBridge.directSearch(query);
                return true;
            }
        });

        //getSupportActionBar().setCustomView(R.layout.bottom_menu_main);
        //getSupportActionBar().setDisplayShowCustomEnabled(true);
//        MenuItemCompat.setActionView(item, R.layout.activity_main);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    // TODO MAP Method
    public void procScrollChangedCallback() {
        if (!(mFooter.getTranslationY() > 0)) { //Hide Footer
            mFooter.setTranslationY(mFooterHeight);
            mFooter.startAnimation(animateTopDown(mFooterHeight, 300));
        }
        if ((mHeader.getTranslationY() >= 0)) { //Hide Header
            mHeader.setTranslationY(-mHeaderHeight);
            mHeader.startAnimation(animateTopDown(mHeaderHeight, 300));
        }
    }

    public void procToggleToolbar() {
        //Toggle Footer
        if (mFooter.getTranslationY() > 0) {
            //Footer가 가려진 상태
            Log.d(MainActivity.class.getSimpleName(), "show");
            mHeader.setTranslationY(mMinHeaderTranslation);
            mHeader.startAnimation(animateTopDown(-mHeaderHeight, 300));

            mFooter.setTranslationY(mMinFooterTranslation);
            mFooter.startAnimation(animateTopDown(mFooterHeight, 300));
        } else {
            //Footer가 보이는 상태
            Log.d(MainActivity.class.getSimpleName(), "hide");
            mHeader.setTranslationY(-mHeaderHeight);
            mHeader.startAnimation(animateTopDown(mHeaderHeight, 300));

            mFooter.setTranslationY(mFooterHeight);
            mFooter.startAnimation(animateTopDown(-mFooterHeight, 300));
        }
    }

    // TODO Animate
    public Animation animateTopDown(float height, int duration){
        Animation animation = new TranslateAnimation(0, 0, height, 0);
        animation.setDuration(duration);
        return animation;
    }

    public void getTranslation(String name) {
        ArrayList<String> list = new ArrayList<String>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        final String path = this.getString(R.string.trans_like_list);

        try {
            list.add(URLEncoder.encode(name, "utf-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        hashMap.put("names", list);
        hashMap.put("phoneId", CommonUtils.getAndroidId(this));

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

                if (translation != null) {
                    Log.d("getTranslation", translation.toString());
                    mFooterResult.setTag(mFooterResult.getId(), translation);
                    mLikeCnt.setText("" + translation.getLikeCount());
                    mTransName.setText(translation.getName());

                    mFooter.setVisibility(View.GONE);
                    mLayout.showPanel();
                }
            }
        });
    }

    // TODO MAP Bridge
    Handler targetHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MapBridgeType mapBridgeType = (MapBridgeType) msg.getData().getSerializable("type");
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

    /* tmp */
  /*  public void onClick(View view) {
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
    }*/
}