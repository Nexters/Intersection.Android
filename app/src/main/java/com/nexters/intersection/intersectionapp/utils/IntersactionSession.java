package com.nexters.intersection.intersectionapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BoBinLee on 2014-08-01.
 * 카카오톡, 페이스북, 서버로그인 부분
 */
public class IntersactionSession {
	public static final String IS_VISITED = "IS_VISIT";

	private static IntersactionSession mIntersactionSession;
	private SharedPreferences mPref;
	private SharedPreferences.Editor mEditor;
	private Context mContext;

	// Shared pref mode
	private int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "SessionPref";

	public IntersactionSession(Context context) {
		this.mContext = context;
		mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		mEditor = mPref.edit();
	}

	public static IntersactionSession getInstance(Context context) {
		if(mIntersactionSession == null)
			mIntersactionSession = new IntersactionSession(context);
		return mIntersactionSession;
	}

	public void putString(String key, String value){
		// Storing name in pref
		mEditor.putString(key, value);
		// commit changes
		mEditor.commit();
	}
	
	public void putBoolean(String key, boolean value){
		// Storing name in pref
		mEditor.putBoolean(key, value);
		// commit changes
		mEditor.commit();
	}
	
	public void putInt(String key, int value){
		// Storing name in pref
		mEditor.putInt(key, value);
		// commit changes
		mEditor.commit();
	}

    public boolean getBoolean(String key){
        return mPref.getBoolean(key, false);
    }
}
