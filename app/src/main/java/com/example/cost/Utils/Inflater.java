package com.example.cost.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author:wamcs
 * date:2016/2/24
 * email:kaili@hustunique.com
 */
public class Inflater {
    public static View inflate(int resId,ViewGroup root,boolean attach){
        return LayoutInflater.from(AppData.getContext()).inflate(resId,root,attach);
    }
}
