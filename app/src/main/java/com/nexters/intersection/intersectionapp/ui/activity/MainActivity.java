package com.nexters.intersection.intersectionapp.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.model.Translation;
import com.nexters.intersection.intersectionapp.thread.MessageTask;
import com.nexters.intersection.intersectionapp.ui.map.MapBridge;
import com.nexters.intersection.intersectionapp.ui.map.MapBridgeType;
import com.nexters.intersection.intersectionapp.utils.BackPressCloseHandler;
import com.nexters.intersection.intersectionapp.utils.CommonUtils;
import com.nexters.intersection.intersectionapp.utils.IntersactionSession;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity {
    public static final String DAUM_MAP_URL = "http://map.daum.net/link/search/";
    public static final String COMMA_SEP = ",";

    private class State {
        public static final int NOT_SHOWING_RESULT = 1;
        public static final int SHOWING_RESULT = 2;
        int currentState;

        public void setCurrentState(int state) {
            this.currentState = state;
        }

        public int getCurrentState() {
            return this.currentState;
        }
    }

    private State state = new State();
    private int mMinFooterTranslation = 0;
    private int mMinHeaderTranslation = 0;
    private float mFooterHeight = 0f;
    private float mHeaderHeight = 0f;

    public MapBridge mapBridge;
    public WebView webView;
    private ImageButton mBtnIntersection;

    private SlidingUpPanelLayout mLayout;

    private RelativeLayout mFooterResult;

    // Social Share Button
    private ImageButton mBtnKakaoTalk, mBtnBand, mBtnMsg;

    private TextView mLikeCnt, mTransName, mTransAddress;
    private ImageView mToggleLike;

    private RelativeLayout mFooter;
    private LinearLayout mHeader;
    private Button mTutorial, mVersion, mContact;
    private ImageButton mMyLoc;

    private BackPressCloseHandler backPressCloseHandler;

    // action bar search
    private SearchView searchView;
    private ImageButton mBtnSearch, mBtnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initResource();
        initActionBar();
        initEvent();
    }

    public void onBackPressed() {
        if (state.getCurrentState() == State.NOT_SHOWING_RESULT) {
            backPressCloseHandler.onBackPressed();
        } else {
            mapBridge.reset();
            mLayout.hidePanel();
            mFooter.setVisibility(View.VISIBLE);
            state.setCurrentState(State.NOT_SHOWING_RESULT);
        }
    }

    public void initResource() {
        backPressCloseHandler = new BackPressCloseHandler(this);

        mFooterHeight = getResources().getDimension(R.dimen.footer_height);
        mHeaderHeight = getResources().getDimension(R.dimen.header_height);

        webView = (WebView) findViewById(R.id.web_view);
        mapBridge = new MapBridge(webView, targetHandler);
        mFooter = (RelativeLayout) findViewById(R.id.am_footer_rl);

        mHeader = (LinearLayout) findViewById(R.id.am_header_ll);
        mMyLoc = (ImageButton) mHeader.findViewById(R.id.am_header_myloc);

        mFooterResult = (RelativeLayout) findViewById(R.id.am_result_footer_rl);

        if (webView != null) {
            webView.loadUrl("file:///android_asset/map.html");
            webView.getSettings().setJavaScriptEnabled(true);
        }
        webView.addJavascriptInterface(mapBridge, "DaumApp");

        mBtnIntersection = (ImageButton) findViewById(R.id.am_btn_Intersection);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        mLayout.hidePanel();

        mLikeCnt = (TextView) mFooterResult.findViewById(R.id.am_tv_like_count);
        mTransName = (TextView) mFooterResult.findViewById(R.id.am_tv_name);
        mTransAddress = (TextView) mFooterResult.findViewById(R.id.am_tv_address);
        mToggleLike = (ImageView) mFooterResult.findViewById(R.id.am_toggle_btn_like);

        procSendMarkerCnt(0);

        mBtnKakaoTalk = (ImageButton) mFooterResult.findViewById(R.id.img_btn_kakaotalk);
        mBtnBand = (ImageButton) mFooterResult.findViewById(R.id.img_btn_band);
        mBtnMsg = (ImageButton) mFooterResult.findViewById(R.id.img_btn_msg);
    }

    public void initActionBar() {
        ActionBar actionbar = getSupportActionBar();
        LayoutInflater mInflater = LayoutInflater.from(this);
        View header = mInflater.inflate(R.layout.actionbar_custom, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.NO_GRAVITY
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setCustomView(header, params);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        searchView = (SearchView) findViewById(R.id.am_searchview);

        int searchPlateID = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateID);
        int searchIconId = searchView.getContext().getResources().
                getIdentifier("android:id/search_mag_icon", null, null);
        ImageView searchIcon = (ImageView) searchView.findViewById(searchIconId);

        searchIcon.setVisibility(View.GONE);
        searchView.onActionViewExpanded();

        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.WHITE);
        }
        //Button Resource initialize
        mBtnSetting = (ImageButton) header.findViewById(R.id.am_btn_top_bar_setting);
        mBtnSearch = (ImageButton) header.findViewById(R.id.am_btn_search);
    }

    public void initEvent() {
        mMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntersactionSession intersactionSession = IntersactionSession.getInstance(MainActivity.this);

                if (intersactionSession.getString(IntersactionSession.FIXED_LOCATION_LAT) != null
                        && intersactionSession.getString(IntersactionSession.FIXED_LOCATION_LNG) != null) {
                    double lat = Double.parseDouble(intersactionSession.getString(IntersactionSession.FIXED_LOCATION_LAT));
                    double lng = Double.parseDouble(intersactionSession.getString(IntersactionSession.FIXED_LOCATION_LNG));

                    mapBridge.moveLocation(lat, lng);
                }
            }
        });

        mBtnIntersection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapBridge.searchIntersection();
                state.setCurrentState(State.SHOWING_RESULT);
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
                        getTranslation(translation.getName(), null);
                    }
                });
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
            }
        });

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

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

            }
        });

        mBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procToggleToolbar();
            }
        });
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView mSubmitBtn;
                int submitBtnID = searchView.getContext().getResources().getIdentifier("android:id/search_go_btn", null, null);
                mSubmitBtn = (ImageView) searchView.findViewById(submitBtnID);
                mSubmitBtn.callOnClick();
            }
        });

        //Social Share Button
        mBtnKakaoTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareToKakaoTalk();
            }
        });
        mBtnBand.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ShareToBand();
            }
        });
        mBtnMsg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ShareToEmail();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //  Action Bar에서 SearchView를 보여주고 싶을때 사용하는 클래스입니다.
//        SearchView searchView = (SearchView) menu.findItem(R.id.am_searchview).getActionView();

//        searchView.setBackgroundResource(R.drawable.search_button_top_bar);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    // TODO MAP Method
    public void procScrollChangedCallback() {
        if (!(mFooter.getTranslationY() > 0)) { //Hide Footer
            mFooter.setTranslationY(mFooterHeight);
            mFooter.startAnimation(animateTopDown(-mFooterHeight, 300));
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

    public void procFixedMyLocation(double lat, double lng) {
        IntersactionSession intersactionSession = IntersactionSession.getInstance(this);

        intersactionSession.putString(IntersactionSession.FIXED_LOCATION_LAT, Double.toString(lat));
        intersactionSession.putString(IntersactionSession.FIXED_LOCATION_LNG, Double.toString(lng));
    }

    public void procSendMarkerCnt(int cnt) {

        if (cnt >= 2) {
            mBtnIntersection.setImageResource(R.drawable.button_intersection_r);
            mBtnIntersection.setEnabled(true);
        } else {
            mBtnIntersection.setImageResource(R.drawable.button_intersection);
            mBtnIntersection.setEnabled(false);
        }

    }

    // TODO Animate
    public Animation animateTopDown(float height, int duration) {
        Animation animation = new TranslateAnimation(0, 0, height, 0);
        animation.setDuration(duration);
        return animation;
    }

    public void getTranslation(String name, final String address) {
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
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("getTranslation", "this.getString(R.string.trans_like_list)");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Translation translation = null, preTranslation = null;
                int likeSrc = 0;

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

                    preTranslation = (Translation) mFooterResult.getTag(mFooterResult.getId());
                    if (preTranslation != null)
                        translation.setAddress(preTranslation.getAddress());
                    if (address != null)
                        translation.setAddress(address);

                    if (translation.getLikeStatus())
                        likeSrc = R.drawable.llike_icon_2;
                    else
                        likeSrc = R.drawable.llike_icon;
                    ImageView imageView = (ImageView) mFooterResult.findViewById(R.id.am_toggle_btn_like);
                    imageView.setImageResource(likeSrc);

                    mFooterResult.setTag(mFooterResult.getId(), translation);
                    mLikeCnt.setText("" + translation.getLikeCount());
                    mTransName.setText(translation.getName());
                    mTransAddress.setText(translation.getAddress());
                    mFooter.setVisibility(View.GONE);
                    mLayout.showPanel();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("post Json", "failure");
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
                case CloseFooter:
                    mLayout.hidePanel();
                    mFooter.setVisibility(View.VISIBLE);
                    break;
                case Toast:
                    String message = msg.getData().getString("msg");
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                case SendMarkerCnt:
                    int cnt = msg.getData().getInt("cnt");
                    procSendMarkerCnt(cnt);
                    break;
                case FixedMyLocation:
                    double lat = msg.getData().getDouble("lat");
                    double lng = msg.getData().getDouble("lng");
                    procFixedMyLocation(lat, lng);
                    break;
                case ScrollChangedCallback:
                    procScrollChangedCallback();
                    break;
                case ToggleToolbar:
                    procToggleToolbar();
                    break;
                case Translation:
                    String name = msg.getData().getString("name");
                    String address = msg.getData().getString("address");
                    getTranslation(name, address);
                    break;
            }
        }
    };

    /* TODO 메뉴 버튼 */
    public void Tutorial(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void Verinfo(View view) {
        Intent intent = new Intent(this, VerActivity.class);
        startActivity(intent);
    }

    public void Email(View view) {
       procEmail(new String[]{"jjungda@gmail.com"}, "", "");
    }

    public void procEmail(String[] emails, String title, String content){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");//gmail패키지 포함
        i.setType("plain/text");
        i.putExtra(Intent.EXTRA_EMAIL, emails);
        i.putExtra(Intent.EXTRA_SUBJECT, title);
        i.putExtra(Intent.EXTRA_TEXT, content);

        startActivity(i);
    }

    KakaoLink kakaoLink;
    KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;

    //TODO Social Share
    public void ShareToBand() {

        Translation translation = (Translation) mFooterResult.getTag(mFooterResult.getId());
        String url = DAUM_MAP_URL + translation.getName();
        String place_name = translation.getName();
        String text = "너와 나의 중간지점은?\n" + place_name + " 입니다.";

        PackageManager manager = this.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage("com.nhn.android.band");
        if (i == null) {
            // 밴드앱 설치되지 않은 경우 구글 플레이 설치페이지로 이동
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.band"));
            this.startActivity(intent);
            return;
        }
        String encodedText = null;
        try {
            encodedText = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
// "%ED%85%8C%EC%8A%A4%ED%8A%B8+%EB%B3%B8%EB%AC%B8"; // 글 본문 (utf-8 urlencoded)
        Uri uri = Uri.parse("bandapp://create/post?text=" + encodedText + "&route=" + "www.naver.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void ShareToKakaoTalk() {
        Translation translation = (Translation) mFooterResult.getTag(mFooterResult.getId());
        String url = DAUM_MAP_URL + translation.getName();
        String place_name = translation.getName();
        String text = "너와 나의 중간지점은?\n" + place_name + " 입니다.";

        try {
            kakaoLink = KakaoLink.getKakaoLink(this);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            sendKakaoTalkLink(text, url);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    private void sendKakaoTalkLink(String text, String link) {
        try {
            kakaoTalkLinkMessageBuilder.addText(text);
            // 웹싸이트에 등록한 "http://www.kakao.com"을 overwrite함. overwrite는 같은 도메인만 가능.
//            kakaoTalkLinkMessageBuilder.addWebLink("다음 지도로 이동하기", link);
//                kakaoTalkLinkMessageBuilder.addAppButton(getString(R.string.kakaolink_appbutton));
//                // 웹싸이트에 등록한 "http://www.kakao.com"으로 이동.

            kakaoTalkLinkMessageBuilder.addWebButton("다음 지도로 이동하기", link);
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), this);
        } catch (KakaoParameterException e) {
            alert(e.getMessage());
        }
    }

    public void ShareToEmail(){
        Translation translation = (Translation) mFooterResult.getTag(mFooterResult.getId());
        String place_name = translation.getName();
        String text = "너와 나의 중간지점은?\n" + place_name + " 입니다.";

        procEmail(new String[]{}, "", text);
    }

    private void alert(String message) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
    }
}
