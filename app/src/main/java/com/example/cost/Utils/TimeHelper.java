package com.example.cost.Utils;

import java.util.Calendar;

/**
 * author:wamcs
 * date:2016/2/24
 * email:kaili@hustunique.com
 */
public class TimeHelper {
    private static Calendar calendar=null;

    public static int getYear(){
        checkDefault();
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(){
        checkDefault();
        return calendar.get(Calendar.MONTH)+1;
    }

    public static int getWeek(){
        checkDefault();
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public static int getDate(){
        checkDefault();
        return calendar.get(Calendar.DATE);
    }

    private static void checkDefault(){
        if (calendar == null){
            calendar=Calendar.getInstance();
        }
    }
}
