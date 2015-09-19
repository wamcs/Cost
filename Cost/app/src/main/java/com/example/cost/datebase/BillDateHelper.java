package com.example.cost.datebase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cost.R;
import com.example.cost.Util;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BillDateHelper extends SQLiteOpenHelper{

    private final String CREATE_TABLE="create table billdirc(_id integer primary " +
            "key autoincrement,name text,coverpicture integer,backgroud integer)";

    private final String CREATE_BILLTABLE="create table bill(_id integer primary key " +
            "autoincrement,content text,income integer,pay integer,label text" +
            ",color integer,period Integer,year integer,month integer,date integer," +
            "week integer,time integer,billid integer)";
    //周期使用正负数来标记。负数为月，正数为天
    private final String CREATE_RECYCLETABLE="create table recycle(_id integer " +
            "primary key autoincrement,content text,income integer,pay integer,label text" +
            ",color integer,period integer,day integer,billid integer)";
    private final String CREATE_SHOPPINGTABLE="create table shopping(_id integer " +
            "primary key autoincrement,name text)";
    private final String CREATE_LABELCOLOR="create table labelcolor(_id integer " +
            "primary key autoincrement,label text,color integer)";

    private Context context;
    public BillDateHelper(Context context, String name,int version) {
        super(context, name, null, version);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_BILLTABLE);
        db.execSQL(CREATE_RECYCLETABLE);
        db.execSQL(CREATE_SHOPPINGTABLE);
        db.execSQL(CREATE_LABELCOLOR);
        for(int i=0;i< Util.label.length;i++)
            db.execSQL("insert into labelcolor values(null,'"+Util.label[i]+"',"+Util.colors[i]+")");
        db.execSQL("insert into billdirc values(null,'新建账本',0,0)");
        context.getSharedPreferences("billselect",
                Context.MODE_PRIVATE).edit().putInt("selectedID",1)
                .commit();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Map<String,Object> getTime(){
        Map<String,Object> map=new HashMap<>();
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int date=calendar.get(Calendar.DATE);
        int week=calendar.get(Calendar.WEEK_OF_MONTH);
        int times=date+month*100+year*10000;
        map.put("year",year);
        map.put("month",month);
        map.put("date",date);
        map.put("week",week);
        map.put("time",times);
        return map;
    }

    public int getColor(String label){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select  color from labelcolor where label='" + label + "'", null);
        cursor.moveToNext();
        int color=cursor.getInt(cursor.getColumnIndex("color"));
        cursor.close();
        return color;
    }

    public int getcolor(String label){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select distinct color from bill where label='" + label + "'", null);
        cursor.moveToNext();
        int color=cursor.getInt(cursor.getColumnIndex("color"));
        cursor.close();
        return color;
    }

    public ArrayList<Map<String,Object>> getLabelColor(){
        ArrayList<Map<String,Object>> list=new ArrayList<>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from labelcolor", null);
        for(;cursor.moveToNext();cursor.isAfterLast()){
            Map<String,Object> map=new HashMap<>();
            String label=cursor.getString(cursor.getColumnIndex("label"));
            int color=cursor.getInt(cursor.getColumnIndex("color"));
            map.put("label",label);
            map.put("color",color);
            list.add(map);
        }
        cursor.close();
        return list;

    }

    public void addlabel(String label,int color){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("insert into labelcolor values (null,'" + label + "'," + color + ")");
    }

    public int getlabelID(String label){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select _id from labelcolor where label='"+label+"'",null);
        cursor.moveToNext();
        int id=cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        return id;
    }

    public void deletelabel(int id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("delete from labelcolor where _id=" + id);
    }

    public void updatelabel(String label,int id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("update labelcolor set label='" + label + "' where _id=" + id);
    }

    public void modificateLabel(String proLabel,String nowLabel){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("update bill set label='"+nowLabel+"' where label='"+proLabel+"'");
        db.execSQL("update recycle set label='"+nowLabel+"' where label='"+proLabel+"'");
    }

    public void updatecolor(int color,int id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("update labelcolor set color="+color+" where _id="+id);
    }

    public Map<String,Object> getbillitem(int billitemID){
        Map<String,Object> map=new HashMap<>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from bill where _id="+billitemID,null);
        cursor.moveToNext();
        String content=cursor.getString(cursor.getColumnIndex("content"));
        int income=cursor.getInt(cursor.getColumnIndex("income"));
        int pay=cursor.getInt(cursor.getColumnIndex("pay"));
        String label=cursor.getString(cursor.getColumnIndex("label"));
        int color=cursor.getInt(cursor.getColumnIndex("color"));
        int period=cursor.getInt(cursor.getColumnIndex("period"));
        int year=cursor.getInt(cursor.getColumnIndex("year"));
        int month=cursor.getInt(cursor.getColumnIndex("month"));
        int date=cursor.getInt(cursor.getColumnIndex("date"));
        int week=cursor.getInt(cursor.getColumnIndex("week"));
        int time=cursor.getInt(cursor.getColumnIndex("time"));
        int billid=cursor.getInt(cursor.getColumnIndex("billid"));
        map.put("content",content);
        map.put("income",income);
        map.put("pay",pay);
        map.put("label",label);
        map.put("color",color);
        map.put("period",period);
        map.put("year",year);
        map.put("month",month);
        map.put("date",date);
        map.put("week",week);
        map.put("time",time);
        map.put("billid",billid);
        cursor.close();
        return map;
    }

    public Map<String,Object> getrecycle(int ID){
        Map<String,Object> map=new HashMap<>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from recycle where _id="+ID,null);
        cursor.moveToNext();
        String content=cursor.getString(cursor.getColumnIndex("content"));
        int income=cursor.getInt(cursor.getColumnIndex("income"));
        int pay=cursor.getInt(cursor.getColumnIndex("pay"));
        String label=cursor.getString(cursor.getColumnIndex("label"));
        int color=cursor.getInt(cursor.getColumnIndex("color"));
        int period=cursor.getInt(cursor.getColumnIndex("period"));
        int billid=cursor.getInt(cursor.getColumnIndex("billid"));
        map.put("content",content);
        map.put("income",income);
        map.put("pay",pay);
        map.put("label",label);
        map.put("color",color);
        map.put("period",period);
        map.put("billid",billid);
        cursor.close();
        return map;
    }

    public ArrayList<Map<String,Integer>> getrecycle(){
        ArrayList<Map<String,Integer>> list=new ArrayList<>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from recycle ",null);
        for (;cursor.moveToNext();cursor.isAfterLast()) {
            Map<String,Integer> map=new HashMap<>();
            int period = cursor.getInt(cursor.getColumnIndex("period"));
            int day= cursor.getInt(cursor.getColumnIndex("day"));
            int id=cursor.getInt(cursor.getColumnIndex("_id"));
            map.put("period", period);
            map.put("day", day);
            map.put("id", id);
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public int getRecycleID(Map<String,Object> map){
        SQLiteDatabase db=getWritableDatabase();
        String content=map.get("content").toString();
        int income=Integer.parseInt(map.get("income").toString());
        int pay=Integer.parseInt(map.get("pay").toString());
        String label=map.get("label").toString();
        int period=Integer.parseInt(map.get("period").toString());
        int billid=Integer.parseInt(map.get("billid").toString());
        Cursor cursor=db.rawQuery("select _id from recycle where content='" + content + "' and income="
                + income + " and pay=" + pay + " and label='" + label + "' and period=" + period
                + " and billid=" + billid,null);
        Log.e("TAG", cursor.toString());
        cursor.moveToNext();
        int id=cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        return id;
    }

    public ArrayList<Integer> getBillID(Map<String,Object> map){
        SQLiteDatabase db=getWritableDatabase();
        ArrayList<Integer> list=new ArrayList<>();
        String content=map.get("content").toString();
        int income=Integer.parseInt(map.get("income").toString());
        int pay=Integer.parseInt(map.get("pay").toString());
        String label=map.get("label").toString();
        int color=Integer.parseInt(map.get("color").toString());
        int billid=Integer.parseInt(map.get("billid").toString());
        Cursor cursor=db.rawQuery("select _id from bill where content='" + content + "' and income="
                + income + " and pay=" + pay + " and label='" + label + "' and color=" + color
                + " and billid=" + billid,null);
        for (;cursor.moveToNext();cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            list.add(id);
        }
        cursor.close();
        return list;
    }

    public void updateShopping(String name,int id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("update shopping set name='"+name+"' where _id="+id);
    }

    public void addShopping(){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("insert into shopping values(null,'新建')");
    }

    public void addbill(Map<String,Object> map){
        SQLiteDatabase db=getWritableDatabase();
        String content=map.get("content").toString();
        int income=Integer.parseInt(map.get("income").toString());
        int pay=Integer.parseInt(map.get("pay").toString());
        String label=map.get("label").toString();
        int color=Integer.parseInt(map.get("color").toString());
        int period=Integer.parseInt(map.get("period").toString());
        int year=Integer.parseInt(map.get("year").toString());
        int month=Integer.parseInt(map.get("month").toString());
        int date=Integer.parseInt(map.get("date").toString());
        int week=Integer.parseInt(map.get("week").toString());
        int time=Integer.parseInt(map.get("time").toString());
        int billid=Integer.parseInt(map.get("billid").toString());
        db.execSQL("insert into bill values(null,'" + content + "',"
                + income + "," + pay + ",'" + label + "'," + color + "," + period
                + "," + year + "," + month + "," + date + "," + week + "," + time
                + "," + billid + ")");
    }

    public void addrecycle(Map<String,Object> map){
        SQLiteDatabase db=getWritableDatabase();
        String content=map.get("content").toString();
        int income=Integer.parseInt(map.get("income").toString());
        int pay=Integer.parseInt(map.get("pay").toString());
        String label=map.get("label").toString();
        int color=Integer.parseInt(map.get("color").toString());
        int period=Integer.parseInt(map.get("period").toString());
        int billid=Integer.parseInt(map.get("billid").toString());
        int day=Integer.parseInt(map.get("day").toString());
        db.execSQL("insert into recycle values(null,'" + content + "',"
                + income + "," + pay + ",'" + label + "'," + color + "," + period
                + "," + day +  ","+ billid + ")");
    }

    public void updateBill(Map<String,Object> map,int id){
        SQLiteDatabase db=getWritableDatabase();
        String content=map.get("content").toString();
        int income=Integer.parseInt(map.get("income").toString());
        int pay=Integer.parseInt(map.get("pay").toString());
        String label=map.get("label").toString();
        int color=Integer.parseInt(map.get("color").toString());
        int period=Integer.parseInt(map.get("period").toString());
        int year=Integer.parseInt(map.get("year").toString());
        int month=Integer.parseInt(map.get("month").toString());
        int date=Integer.parseInt(map.get("date").toString());
        int week=Integer.parseInt(map.get("week").toString());
        int time=Integer.parseInt(map.get("time").toString());
               db.execSQL("update bill set content='" + content + "', income="
                + income + ", pay=" + pay + ", label='" + label + "', color=" + color
                + ", period=" + period + ", year=" + year + ", month=" + month + ", date="
                + date + ", week=" + week + ", time=" + time
                +  " where _id="+id);
    }

    public void updateBillRecycle(Map<String,Object> map,int id){
        SQLiteDatabase db=getWritableDatabase();
        int period=Integer.parseInt(map.get("period").toString());
        db.execSQL("update bill set period=" + period + " where _id="+id);
    }

    public void updateRecycle(Map<String,Object> map,int id){
        SQLiteDatabase db=getWritableDatabase();
        String content=map.get("content").toString();
        int income=Integer.parseInt(map.get("income").toString());
        int pay=Integer.parseInt(map.get("pay").toString());
        String label=map.get("label").toString();
        int color=Integer.parseInt(map.get("color").toString());
        int period=Integer.parseInt(map.get("period").toString());
               db.execSQL("update recycle set content='" + content + "', income="
                + income + ", pay=" + pay + ", label='" + label + "', color=" + color
                + ", period=" + period + " where _id="+id);
    }

    public void deleterecycle(int id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("delete from recycle where _id="+id);
    }

    public void deleteBill(int id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("delete from bill where _id="+id);
    }

    public void deleteShopping(int id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("delete from shopping where _id="+id);
    }

    public int recycleTurntoString(int recycle){
        int period=R.string.periodno;
        switch (recycle){
            case Util.PRO_DAY:
                period= R.string.periodday;
                break;
            case Util.PRO_MONTH:
                period= R.string.periodmonth;
                break;
            case Util.PRO_WEEK:
                period= R.string.periodweek;
                break;
            case Util.TWO_WEEK:
                period= R.string.periodtwoweek;
                break;
            case Util.NOPERIOD:
                period= R.string.periodno;
                break;
        }
        return period;
    }


}
