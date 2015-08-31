package com.example.cost;


import android.content.res.Resources;

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
            R.color.label_color4,R.color.label_color5,};
    public final static String[] label={"食物","交通","服装","娱乐","医疗"};

    public final static String[] months=
            {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    public static int dpToPx(int Dp){
        return (int)(Dp* Resources.getSystem().getDisplayMetrics().density);
    }
}
