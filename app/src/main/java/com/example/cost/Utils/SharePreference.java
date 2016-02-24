package com.example.cost.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author:wamcs
 * date:2016/2/24
 * email:kaili@hustunique.com
 */
public class SharePreference {

    private static final String DEFAULT_NAME="setting";

    private static SharedPreferences preferences=null;

    public static String getString(String key, String defValue) {
        checkDefault();
        return preferences.getString(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        checkDefault();
        return preferences.getBoolean(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        checkDefault();
        return preferences.getFloat(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        checkDefault();
        return preferences.getInt(key, defValue);
    }

    public static void put(String key, Object value){
        checkDefault();
        SharedPreferences.Editor editor=preferences.edit();
        if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }else if (value instanceof String){
            editor.putString(key, (String) value);
        }else if (value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }else if (value instanceof Float){
            editor.putFloat(key, (Float) value);
        }else{
            throw new IllegalArgumentException("ShaPrefer illegal argument");
        }
        editor.apply();
    }

    private static void checkDefault(){
        if(preferences == null){
            preferences = AppData.getContext().getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        }
    }
}
