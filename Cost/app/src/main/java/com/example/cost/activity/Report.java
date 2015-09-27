package com.example.cost.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cost.R;
import com.example.cost.contrl.ColorCompareView;
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
    private ColorCompareView yearCompare;
    private ColorCompareView monthCompare;
    private ColorCompareView weekCompare;
    private ColorCompareView dateCompare;
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
        setToolbar();
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
        yearCompare= (ColorCompareView) findViewById(R.id.activity_report_year);
        monthCompare= (ColorCompareView) findViewById(R.id.activity_report_month);
        weekCompare= (ColorCompareView) findViewById(R.id.activity_report_week);
        dateCompare= (ColorCompareView) findViewById(R.id.activity_report_date);
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
        dateCompare.setProportion(pay, income);
        dateCompare.setAnimation();
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
        weekIncomeTv.setText(income + "");
        weekPayTv.setText(pay + "");
        weekCompare.setProportion(pay, income);
        weekCompare.setAnimation();
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
        monthCompare.setProportion(pay, income);
        monthCompare.setAnimation();
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
        yearCompare.setProportion(pay,income);
        yearCompare.setAnimation();
        cursor.close();

    }
    public void setToolbar(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.activity_report_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("收支情况");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
