package com.example.cost.Base;

import android.app.Application;

import com.example.cost.Utils.AppData;

/**
 * author:wamcs
 * date:2016/2/23
 * email:kaili@hustunique.com
 */
public class CostApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppData.init(this);
    }
}
