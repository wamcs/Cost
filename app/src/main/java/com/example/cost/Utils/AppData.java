package com.example.cost.Utils;

import android.content.Context;

/**
 * author:wamcs
 * date:2016/2/23
 * email:kaili@hustunique.com
 */
public class AppData {

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static String getPackageName(){
        return sContext.getPackageName();
    }

}

