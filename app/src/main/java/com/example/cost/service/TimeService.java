package com.example.cost.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.cost.Util;
import com.example.cost.datebase.BillDataHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * 用于实现周期功能的service
 */

public class TimeService extends Service {
    private BillDataHelper billDataHelper;
    private ArrayList<Map<String,Integer>> list;
    private static boolean isTimeChange=false;
    private static boolean isMonthChange=false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        billDataHelper =new BillDataHelper(this,"allbill.db",1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(isTimeChange) {
            //获取recycle表中全部信息
            list= billDataHelper.getRecycle();
            //对四类周期循环做处理
            for (int i = 0; i < list.size(); i++) {
                switch (list.get(i).get("period")){
                    case Util.PRO_DAY:
                        add(list.get(i).get("id"));
                        break;
                    case Util.PRO_MONTH:
                        if(isMonthChange){
                            add(list.get(i).get("id"));
                            isMonthChange=false;
                        }
                        break;
                    case Util.PRO_WEEK:
                        update(list.get(i).get("id"),i,7);
                        break;
                    case Util.TWO_WEEK:
                        update(list.get(i).get("id"),i,14);
                        break;
                }

            }
            isTimeChange=false;
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public void add(int id){
        Map<String,Object> map;
        map= billDataHelper.getRecycle(id);
        map.putAll(billDataHelper.getTime());
        billDataHelper.addBill(map);
    }

    public void update(int id,int position,int days){
        SQLiteDatabase db= billDataHelper.getWritableDatabase();
        int day=list.get(position).get("day");
        if(day==days-1){
            add(id);
            db.execSQL("update recycle set day=0 where _id="+id);
        }
        else
            db.execSQL("update recycle set day="+(day+1)+" where _id="+id);
    }

    /**
     * 用于接收时间改变信息的广播
     */
    public static class TimeChangedBroadReceiver extends BroadcastReceiver{

     @Override
     public void onReceive(Context context, Intent intent) {
         Calendar calendar = Calendar.getInstance();
         Log.e("TAG","onReceive "+calendar.get(Calendar.MONTH)+" "+calendar.get(Calendar.DATE));
         int month = calendar.get(Calendar.MONTH) + 1;
         isTimeChange = true;
         //利用sharePerferences记录上次时间，并进行对比，决定是否调用service
         int getmonth = context.getSharedPreferences("monthChange", Context.MODE_PRIVATE)
                 .getInt("month", 0);
         if (getmonth == 0)
             context.getSharedPreferences("monthChange", Context.MODE_PRIVATE)
                     .edit().putInt("month", month).commit();
         if (getmonth != month) {
             isMonthChange = true;
             context.getSharedPreferences("monthChange", Context.MODE_PRIVATE)
                     .edit().putInt("month", month).commit();
         }
         Intent service = new Intent(context, TimeService.class);
         context.startService(service);
     }
 }

}
