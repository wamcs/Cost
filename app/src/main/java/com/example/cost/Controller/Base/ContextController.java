package com.example.cost.Controller.Base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.WindowManager;

import com.example.cost.Controller.Base.BaseController;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public abstract class ContextController extends BaseController {

    private Context mContext;

    public ContextController(Context mContext){
        this.mContext=mContext;
    }

    public Context getContext(){
        return mContext;
    }

    public Drawable getDrawable (int resId){
        return mContext.getResources().getDrawable(resId);
    }

    public Resources getResource (){
        return mContext.getResources();
    }

    public WindowManager getWindowManager (){
        return (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public String getString (int resId){
        return mContext.getString(resId);
    }


}
