package com.example.cost.Utils;

import android.content.Context;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class AppDate {

    private static Context mContext;

    public static void init(Context context){
       mContext=context.getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }

}
