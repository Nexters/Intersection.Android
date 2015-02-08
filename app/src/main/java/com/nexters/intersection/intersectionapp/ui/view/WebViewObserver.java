package com.nexters.intersection.intersectionapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by daehyun on 15. 2. 4..
 */
//Daum Map에서는 안먹힌다는걸 알앗음...
//API event중에 bounds_changed event로 Callback 시켜줘야함;
public class WebViewObserver extends WebView {

    private onScrollChangedCallback mOnScrollChangedCallback;
    //    Declare Contstructor
    public WebViewObserver(Context context) {
        super(context);
    }
    public WebViewObserver(Context context,AttributeSet attrs){
        super(context, attrs);
    }
    public WebViewObserver(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }
    //    API Ver 21
//    public WebViewObserver(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
//        super(context,attrs,defStyleAttr, defStyleRes);
//    }
    public WebViewObserver(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing){
        super(context,attrs,defStyleAttr,privateBrowsing);
    }

    //    Scroll Event
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        Log.d(WebViewObserver.class.getSimpleName(),"scroll >");
        super.onScrollChanged(l, t, oldl, oldt);

        mOnScrollChangedCallback.onScroll(l,t, oldl, oldt);
    }
    public onScrollChangedCallback getOnScrollCallBack(){
        return mOnScrollChangedCallback;
    }
    public void setOnScrollCallBack(onScrollChangedCallback scrollCallBack) {
        this.mOnScrollChangedCallback = scrollCallBack;
    }
    public static interface onScrollChangedCallback {
        public void onScroll(int l,int t, int oldl,int oldt);
    };


}