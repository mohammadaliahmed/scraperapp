package com.appsinventiv.numberscraper.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.appsinventiv.numberscraper.NumberScraperApplication;

/**
 * Created by AliAh on 29/03/2018.
 */

public class SharedPrefs {
    Context context;

    private SharedPrefs() {

    }

    public static String getUsername() {
        return preferenceGetter("username");
    }

    public static void setUsername(String username) {
        preferenceSetter("username", username);
    }


    public static String getIsDemo() {
        return preferenceGetter("IsDemo");
    }

    public static void setIsDemo(String value) {
        preferenceSetter("IsDemo", value);
    }

    public static String getDemoCount() {
        return preferenceGetter("demoCount");
    }

    public static void setDemoCount(String value) {
        preferenceSetter("demoCount", value);
    }

    public static String getSplashImage() {
        return preferenceGetter("splash");
    }

    public static void setSplashImage(String value) {
        preferenceSetter("splash", value);
    }


    public static String getFeaturedAd() {
        return preferenceGetter("featured");
    }

    public static void setFeaturedAd(String value) {
        preferenceSetter("featured", value);
    }


    public static String getUserCity() {
        return preferenceGetter("city");
    }

    public static void setIsLoggedIn(String value) {

        preferenceSetter("isLoggedIn", value);
    }

    public static String getIsLoggedIn() {
        return preferenceGetter("isLoggedIn");
    }

    public static void setUserCity(String value) {

        preferenceSetter("city", value);
    }

    public static void setFcmKey(String fcmKey) {
        preferenceSetter("fcmKey", fcmKey);
    }

    public static String getFcmKey() {
        return preferenceGetter("fcmKey");
    }


    public static void preferenceSetter(String key, String value) {
        SharedPreferences pref = NumberScraperApplication.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String preferenceGetter(String key) {
        SharedPreferences pref;
        String value = "";
        pref = NumberScraperApplication.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        value = pref.getString(key, "");
        return value;
    }
}
