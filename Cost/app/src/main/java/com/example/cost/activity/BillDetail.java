package com.example.cost.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.datebase.BillDateHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillDetail extends BaseActivity{
    private ImageButton editfab;
    private ImageButton deletebtn;
    private ImageButton returnbtn;
    private ImageView backgroudiv;
    private TextView contenttv;
    private TextView moneytv;
    private TextView labeltv;
    private TextView timetv;
    private TextView periodtv;
    private TextView statetv;
    private BillDateHelper billDateHelper;
    private int billitemID;
    private Intent intent;
    private Intent change;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail);
        billDateHelper=new BillDateHelper(this,"allbill.db",1);
        billitemID=getIntent().getIntExtra("billitemID",1);
        init();
        initDate(billitemID);
        setListener();

    }

    public void init(){
        editfab= (ImageButton) findViewById(R.id.bill_detail_fab);
        deletebtn= (ImageButton) findViewById(R.id.bill_detail_delete);
        returnbtn= (ImageButton) findViewById(R.id.bill_detail_return);
        backgroudiv= (ImageView) findViewById(R.id.bill_detail_imageview);
        contenttv= (TextView) findViewById(R.id.bill_detail_content);
        moneytv= (TextView) findViewById(R.id.bill_detail_money);
        labeltv= (TextView) findViewById(R.id.bill_detail_label);
        timetv= (TextView) findViewById(R.id.bill_detail_time);
        periodtv= (TextView) findViewById(R.id.bill_detail_period);
        statetv= (TextView) findViewById(R.id.bill_detail_state);
        intent=new Intent(this,BillWrite.class);
        intent.putExtra("billitemID",billitemID);
        change=new Intent();
        change.setAction("BILL_CHANGED");
    }

    public void initDate(int billitemID){
            Map<String,Object> map=billDateHelper.getbillitem(billitemID);
            String content=map.get("content").toString();
            int income=Integer.parseInt(map.get("income").toString());
            int pay=Integer.parseInt(map.get("pay").toString());
            String label=map.get("label").toString();
            int recycleperiod=Integer.parseInt(map.get("period").toString());
            int year=Integer.parseInt(map.get("year").toString());
            int month=Integer.parseInt(map.get("month").toString());
            int date=Integer.parseInt(map.get("date").toString());
            timetv.setText(year+"/"+month+"/"+date);
            contenttv.setText(content);
            labeltv.setText(label);
            if(income==0){
                statetv.setText(getResources().getString(R.string.statepay));
                moneytv.setText(pay+"");
            }
            else {
                statetv.setText(getResources().getString(R.string.stateincome));
                moneytv.setText(income+"");
            }
            if(recycleperiod==Util.NOPERIOD)
                periodtv.setText(getResources()
                        .getString(billDateHelper.recycleTurntoString(recycleperiod)));
            else
                periodtv.setText(getResources()
                        .getString(billDateHelper.recycleTurntoString(recycleperiod)));
    }

    private void setListener(){
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> billmap=billDateHelper.getbillitem(billitemID);
                billDateHelper.deleteBill(billitemID);
                if((int)billmap.get("period")!=Util.NOPERIOD)
                {
                    int recycleid = billDateHelper.getRecycleID(billmap);
                    Map<String ,Object> map=new HashMap<>();
                    map.put("period",0);
                    ArrayList<Integer> list=billDateHelper.getBillID(billmap);
                    for(int i=0;i<list.size();i++)
                        billDateHelper.updateBillRecycle(map,list.get(i));
                    billDateHelper.deleterecycle(recycleid);
                }
                sendBroadcast(change);
                finish();
            }
        });

        editfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location=new int[2];
                v.getLocationOnScreen(location);
                location[0]+=v.getWidth()/2;
                intent.putExtra("location",location);
                startActivity(intent);
                overridePendingTransition(0,0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);
            }
        });
    }

}
