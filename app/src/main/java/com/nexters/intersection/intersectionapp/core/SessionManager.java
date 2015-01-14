package com.nexters.intersection.intersectionapp.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by BoBinLee on 2014-08-01.
 * 카카오톡, 페이스북, 서버로그인 부분
 */
public class SessionManager {
	public static final String IS_VISITED = "IS_VISIT";

	private static SessionManager mSessionManager;
	private SharedPreferences mPref;
	private SharedPreferences.Editor mEditor;
	private Context mContext;

	// Shared pref mode
	private int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "SessionPref";

	public SessionManager(Context context) {
		this.mContext = context;
		mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		mEditor = mPref.edit();
	}

	public static SessionManager getInstance(Context context) {
		if(mSessionManager == null)
			mSessionManager = new SessionManager(context);
		return mSessionManager;
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
