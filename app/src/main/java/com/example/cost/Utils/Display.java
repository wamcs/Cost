package com.example.cost.Utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class Display {

    public static DisplayMetrics display(){
        if (null == AppData.getContext()){
            return null;
        }
        return AppData.getContext().getResources().getDisplayMetrics();
    }

    public static int width(){
        DisplayMetrics display=display();
        if (null == display){
            return  0;
        }
        return display.widthPixels;
    }

    public static int dpToPx(int Dp){
        return (int)(Dp* Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px){
        return (int)(px/ Resources.getSystem().getDisplayMetrics().density);
    }

}
