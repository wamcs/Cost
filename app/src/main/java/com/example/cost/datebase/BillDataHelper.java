package com.example.cost.datebase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.Utils.SharePreference;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BillDataHelper extends SQLiteOpenHelper{

    /**
     * billdirc:账本集
     * name：账本名
     * coverpicture：账本封面颜色
     * backgroud：背景色（未用）
     */

    private final String CREATE_TABLE="create table billdirc(_id integer primary " +
            "key autoincrement,name text,coverpicture integer,backgroud integer)";

    /**
     * bill：账本具体内容表
     * content：所记的内容
     * income：收入
     * pay：支出
     * label：标签
     * color：标签对应颜色
     * period：周期
     * year，month，date，week：所记时的时间（用于周期记）
     * time：上述时间（年月日）的数字集合，为一8位数
     * billid：所属账本的id
     *
     */

    private final String CREATE_BILLTABLE="create table bill(_id integer primary key " +
            "autoincrement,content text,income integer,pay integer,label text" +
            ",color integer,period Integer,year integer,month integer,date integer," +
            "week integer,time integer,billid integer)";
    //周期使用正负数来标记。负数为月，正数为天
    /**
     * recycle：周期的记录
     *
     */
    private final String CREATE_RECYCLETABLE="create table recycle(_id integer " +
            "primary key autoincrement,content text,income integer,pay integer,label text" +
            ",color integer,period integer,day integer,billid integer)";

    /**
     * shopping：shoppinglist的记录表
     *
     */
    private final String CREATE_SHOPPINGTABLE="create table shopping(_id integer " +
            "primary key autoincrement,name text)";

    /**
     * labelcolor：标签与颜色的对应表
     */
    private final String CREATE_LABELCOLOR="create table labelcolor(_id integer " +
            "primary key autoincrement,label text,color integer)";

    /**
     * colors:备选颜色的表
     */
    private final String CREATE_SELECTCOLOR="create table colors(_id integer " +
            "primary key autoincrement,color integer)";

    private Context context;
    public BillDataHelper(Context context, String name, int version) {
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
        db.execSQL(CREATE_SELECTCOLOR);
        //后两个查询表的初始化
        for(int i=0;i< Util.label.length;i++)
            db.execSQL("insert into labelcolor values(null,'"+Util.label[i]+"',"+Util.colors[i]+")");
        for(int i=0;i<Util.selectingColors.length;i++)
            db.execSQL("insert into colors values(null,"+Util.selectingColors[i]+")");
        db.execSQL("insert into billdirc values(null,'新建账本',0,0)");
        SharePreference.put("selectedID", 1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //获取现在的时间
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

    //从labelcolor里获取标签对应颜色
    public int getColor(String label){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select  color from labelcolor where label='" + label + "'", null);
        cursor.moveToNext();
        int color=cursor.getInt(cursor.getColumnIndex("color"));
        cursor.close();
        return color;
    }

    //从bill里获取标签对应颜色
    public int getColorFromBill(String label){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select distinct color from bill where label='" + label + "'", null);
        cursor.moveToNext();
        int color=cursor.getInt(cursor.getColumnIndex("color"));
        cursor.close();
        return color;
    }

    //获取所有备选颜色，用于adapter数据的调用
    public ArrayList<Integer> getAllColors(){
        ArrayList<Integer> list=new ArrayList<>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from colors", null);
        for(;cursor.moveToNext();cursor.isAfterLast())
            list.add(cursor.getInt(cursor.getColumnIndex("color")));
        cursor.close();
        return list;
    }


    public int getColorID(int color){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select _id from colors where color=" + color, null);
        cursor.moveToNext();
        int id=cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        return id;
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

    public void deleteColor(int id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("delete from colors where _id=" + id);
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

    public String getLabel(int id){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select label from labelcolor where _id=" + id, null);
        cursor.moveToNext();
        String label=cursor.getString(cursor.getColumnIndex("label"));
        cursor.close();
        return label;

    }

    //获取单项的详细信息
    public Map<String,Object> getBillItem(int billitemID){
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

    //获取所给id的recycle表中的项
    public Map<String,Object> getRecycle(int ID){
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

    public ArrayList<Map<String,Integer>> getRecycle(){
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

    //获取对应map的id，用于更新
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
                + " and billid=" + billid, null);
        for (;cursor.moveToNext();cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            list.add(id);
        }
        cursor.close();
        return list;
    }

    public void updateShopping(String name,int id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("update shopping set name='" + name + "' where _id=" + id);
    }

    public void addShopping(){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("insert into shopping values(null,'新建')");
    }

    public void addBill(Map<String, Object> map){
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

    public void addRecycle(Map<String, Object> map){
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

    public void deleteRecycle(int id){
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

    public int recycleTurnToString(int recycle){
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

    public Map<String,Integer> getDataOfIncomeAndPay(int billId ,int year){
        return  getDataOfIncomeAndPay(billId,year,-1,-1,-1);
    }

    public Map<String,Integer> getDataOfIncomeAndPay(int billId ,int year ,int month){
        return getDataOfIncomeAndPay(billId,year,month,-1,-1);
    }

    public Map<String,Integer> getWeekDataOfIncomeAndPay(int billId ,int year ,int month ,int week){
        return getDataOfIncomeAndPay(billId,year,month,week,-1);
    }

    public Map<String,Integer> getDateDataOfIncomeAndPay(int billId ,int year ,int month ,int date){
        return getDataOfIncomeAndPay(billId, year, month, -1, date);
    }

    private Map<String,Integer> getDataOfIncomeAndPay(int billId ,int year ,int month ,int week ,int date){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor;

        if ( billId < 1){
            throw new IllegalArgumentException("bill ID is lesser than 1");
        }else if ( year < 0){
            throw new IllegalArgumentException("year error");
        }


        if(month == -1){
            cursor=db.rawQuery("select income,pay from bill where billid="
                    + billId + " and year=" + year, null);
        }else if (week == -1 && date ==-1){
            cursor=db.rawQuery("select income,pay from bill where billid="
                    +billId+" and year="+year+" and month="+month,null);
        }else if (week != -1){
            cursor=db.rawQuery("select income,pay from bill where billid="
                    +billId+" and year="+year+" and month="+month+" and week="+week,null);
        }else {
            cursor=db.rawQuery("select income,pay from bill where billid="
                    +billId+" and year="+year+" and month="+month+" and date="+date,null);
        }

        int income=0;
        int pay=0;
        for(;cursor.moveToNext();cursor.isAfterLast()){
            income+=cursor.getInt(cursor.getColumnIndex("income"));
            pay+=cursor.getInt(cursor.getColumnIndex("pay"));
        }

        Map<String,Integer> map=new HashMap<>();
        map.put("income",income);
        map.put("pay",pay);
        cursor.close();
        return map;
    }


}
