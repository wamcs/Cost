package com.example.cost;


import android.content.res.Resources;
import android.util.Log;

/**
 * 此类为一些数据的集合类
 */

public class Util {
    public final static int PRO_DAY=1;
    public final static int PRO_WEEK=2;
    public final static int TWO_WEEK=3;
    public final static int PRO_MONTH=4;
    public final static int NOPERIOD=0;

    public static boolean isInitialize=false;

    public static int year;
    public static int month;

    public static int width;

    public final static int[] colors={R.color.label_color1,R.color.label_color2,R.color.label_color3,
            R.color.label_color4,R.color.label_color5};
    public final static String[] label={"食物","交通","服装","娱乐","医疗"};

    public final static int[] selectingColors= {
            R.color.label_color6,R.color.label_color7,R.color.label_color8,
            R.color.label_color9,R.color.label_color10,R.color.label_color11,
            R.color.label_color12,R.color.label_color13, R.color.label_color14,
            R.color.label_color15,R.color.label_color16,R.color.label_color17,
            R.color.label_color18,R.color.label_color19,R.color.label_color20,
            R.color.label_color21,R.color.label_color22,R.color.label_color23,
            };

    public final static int[] billCover={
            R.color.bill_cover_unchecked1,R.color.bill_cover_unchecked2,
            R.color.bill_cover_unchecked3,R.color.bill_cover_unchecked4,
            R.color.bill_cover_unchecked5,
    };

    public final static int[] billCoverSure={
            R.color.bill_cover_checked1,R.color.bill_cover_checked2,
            R.color.bill_cover_checked3,R.color.bill_cover_checked4,
            R.color.bill_cover_checked5,
    };

    public final static String[] months=
            {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    public static int dpToPx(int Dp){
        return (int)(Dp* Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px){
        return (int)(px/ Resources.getSystem().getDisplayMetrics().density);
    }
}
