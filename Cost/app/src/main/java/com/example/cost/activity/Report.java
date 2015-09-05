package com.example.cost.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.datebase.BillDateHelper;

import java.util.Calendar;

public class Report extends BaseActivity {

    private int billid;
    private TextView dateIncomeTv;
    private TextView datePayTv;
    private TextView weekIncomeTv;
    private TextView weekPayTv;
    private TextView monthIncomeTv;
    private TextView monthPayTv;
    private TextView yearIncomeTv;
    private TextView yearPayTv;
    private LinearLayout yearLayout;
    private LinearLayout monthLayout;
    private LinearLayout weekLayout;
    private LinearLayout dateLayout;
    private SQLiteDatabase liteDatabase;
    private int year;
    private int month;
    private int week;
    private int date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        billid=getIntent().getIntExtra("billid",1);
        BillDateHelper billDateHelper = new BillDateHelper(this, "allbill.db", 1);
        liteDatabase= billDateHelper.getWritableDatabase();
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        date=calendar.get(Calendar.DATE);
        week=calendar.get(Calendar.WEEK_OF_MONTH);
        init();
        getData();
    }

    public void init(){
        dateIncomeTv= (TextView) findViewById(R.id.activity_report_date_income);
        datePayTv= (TextView) findViewById(R.id.activity_report_date_pay);
        weekIncomeTv= (TextView) findViewById(R.id.activity_report_week_income);
        weekPayTv= (TextView) findViewById(R.id.activity_report_week_pay);
        monthIncomeTv= (TextView) findViewById(R.id.activity_report_month_income);
        monthPayTv= (TextView) findViewById(R.id.activity_report_month_pay);
        yearIncomeTv= (TextView) findViewById(R.id.activity_report_year_income);
        yearPayTv= (TextView) findViewById(R.id.activity_report_year_pay);
        yearLayout= (LinearLayout) findViewById(R.id.report_year_layout);
        monthLayout= (LinearLayout) findViewById(R.id.report_month_layout);
        dateLayout= (LinearLayout) findViewById(R.id.report_date_layout);
        weekLayout= (LinearLayout) findViewById(R.id.report_week_layout);
        yearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation(0,100,100,100);
            }
        });
        monthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation(0,0,100,100);
            }
        });
        weekLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation(0,0,0,100);
            }
        });
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation(0,0,0,0);
            }
        });

    }

    public void getData(){
        getDateData();
        getWeekData();
        getMonthData();
        getYearData();
    }

    public void getDateData(){
        Cursor cursor=liteDatabase.rawQuery("select income,pay from bill where billid="
                +billid+" and year="+year+" and month="+month+" and date="+date,null);
        int income=0;
        int pay=0;
        for(;cursor.moveToNext();cursor.isAfterLast()){
            income+=cursor.getInt(cursor.getColumnIndex("income"));
            pay+=cursor.getInt(cursor.getColumnIndex("pay"));
        }
        dateIncomeTv.setText(income + "");
        datePayTv.setText(pay + "");
        cursor.close();
    }

    public void getWeekData(){
        Cursor cursor=liteDatabase.rawQuery("select income,pay from bill where billid="
                +billid+" and year="+year+" and month="+month+" and week="+week,null);
        int income=0;
        int pay=0;
        for(;cursor.moveToNext();cursor.isAfterLast()){
            income+=cursor.getInt(cursor.getColumnIndex("income"));
            pay+=cursor.getInt(cursor.getColumnIndex("pay"));
        }
        weekIncomeTv.setText(income+"");
        weekPayTv.setText(pay+"");
        cursor.close();
    }

    public void getMonthData(){
        Cursor cursor=liteDatabase.rawQuery("select income,pay from bill where billid="
                +billid+" and year="+year+" and month="+month,null);
        int income=0;
        int pay=0;
        for(;cursor.moveToNext();cursor.isAfterLast()){
            income+=cursor.getInt(cursor.getColumnIndex("income"));
            pay+=cursor.getInt(cursor.getColumnIndex("pay"));
        }
        monthIncomeTv.setText(income+"");
        monthPayTv.setText(pay+"");
        cursor.close();
    }

    public void getYearData(){
        Cursor cursor=liteDatabase.rawQuery("select income,pay from bill where billid="
                + billid + " and year=" + year, null);
        int income=0;
        int pay=0;
        for(;cursor.moveToNext();cursor.isAfterLast()){
            income+=cursor.getInt(cursor.getColumnIndex("income"));
            pay+=cursor.getInt(cursor.getColumnIndex("pay"));
        }
        yearIncomeTv.setText(income + "");
        yearPayTv.setText(pay + "");
        cursor.close();
    }

    public void Animation(int yearDp,int monthDp,int weekDp,int dateDp){
        int yearPx= Util.dpToPx(yearDp);
        int monthPx= Util.dpToPx(monthDp);
        int weekPx= Util.dpToPx(weekDp);
        int datePx= Util.dpToPx(dateDp);
        Interpolator interpolator=new AccelerateInterpolator();
        yearLayout.animate().translationY(yearPx).setInterpolator(interpolator).setDuration(300)
                .setStartDelay(200).start();
        monthLayout.animate().translationY(monthPx).setInterpolator(interpolator).setDuration(300)
                .setStartDelay(200).start();
        weekLayout.animate().translationY(weekPx).setInterpolator(interpolator).setDuration(300)
                .setStartDelay(200).start();
        dateLayout.animate().translationY(datePx).setInterpolator(interpolator).setDuration(300)
                .setStartDelay(200).start();
    }

}
