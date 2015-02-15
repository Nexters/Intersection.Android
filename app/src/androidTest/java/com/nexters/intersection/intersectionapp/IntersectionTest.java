package com.nexters.intersection.intersectionapp;

import android.test.AndroidTestCase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nexters.intersection.intersectionapp.model.User;

import junit.framework.TestCase;

import java.lang.reflect.Type;

/**
 * Created by BoBinLee on 2015-02-15.
 */
public class IntersectionTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testConvertJson() {
        User user = new User();

        user.userNo = 1;
        user.tokenId = "test";
        user.phoneId = "phoneId";
        user.pushYn = "Y";

        Gson gson = new Gson();
        String str = gson.toJson(user);
        Log.d("testConvertJson - ", str);

        Log.d("testConvertJson : ", (gson.fromJson(str, User.class)).toString());
    }
}
