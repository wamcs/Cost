package com.example.cost.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cost.R;
import com.example.cost.Utils.SystemBarManager;
import com.example.cost.Util;
import com.example.cost.UI.Widget.RevealCircleBackgroud;
import com.example.cost.datebase.BillDataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 此类为详细内容类
 */
public class BillDetail extends BaseActivity{
    private ImageButton editfab;
    private ImageButton deletebtn;
    private ImageButton returnbtn;
    private TextView contenttv;
    private TextView moneytv;
    private TextView labeltv;
    private TextView timetv;
    private TextView periodtv;
    private TextView statetv;
    private RevealCircleBackgroud circleBackgroud;
    private LinearLayout linearLayout;
    private LinearLayout backLayout;
    private BillDataHelper billDataHelper;
    private int billitemID;
    private Intent intent;
    private Intent change;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail);
        billDataHelper =new BillDataHelper(this,"allbill.db",1);
        billitemID=getIntent().getIntExtra("billitemID",1);
        init();
        initDate(billitemID);
        setListener();
        setupRevealRectangleBackgroud();

    }

    public void init(){
        editfab= (ImageButton) findViewById(R.id.bill_detail_fab);
        deletebtn= (ImageButton) findViewById(R.id.bill_detail_delete);
        returnbtn= (ImageButton) findViewById(R.id.bill_detail_return);
        contenttv= (TextView) findViewById(R.id.bill_detail_content);
        moneytv= (TextView) findViewById(R.id.bill_detail_money);
        labeltv= (TextView) findViewById(R.id.bill_detail_label);
        timetv= (TextView) findViewById(R.id.bill_detail_time);
        periodtv= (TextView) findViewById(R.id.bill_detail_period);
        statetv= (TextView) findViewById(R.id.bill_detail_state);
        linearLayout= (LinearLayout) findViewById(R.id.cost_activity_body_linearLayout);
        backLayout= (LinearLayout) findViewById(R.id.bill_detail_layout);
        circleBackgroud = (RevealCircleBackgroud) findViewById(R.id.bill_detail_reveal);
        intent=new Intent(this,BillWrite.class);
        intent.putExtra("billitemID", billitemID);
        change=new Intent();
        change.setAction("BILL_CHANGED");
    }

    //此方法用于数据加载
    public void initDate(int billitemID){
        Map<String,Object> map= billDataHelper.getBillItem(billitemID);
        String content=map.get("content").toString();
        int income=Integer.parseInt(map.get("income").toString());
        int pay=Integer.parseInt(map.get("pay").toString());
        String label=map.get("label").toString();
        int recycleperiod=Integer.parseInt(map.get("period").toString());
        int year=Integer.parseInt(map.get("year").toString());
        int month=Integer.parseInt(map.get("month").toString());
        int date=Integer.parseInt(map.get("date").toString());
        int color=Integer.parseInt(map.get("color").toString());
        linearLayout.setBackgroundColor(getResources().getColor(color));
        deletebtn.setBackgroundColor(getResources().getColor(color));
        returnbtn.setBackgroundColor(getResources().getColor(color));
        circleBackgroud.setPaintColor(color);
        SystemBarManager manager = new SystemBarManager(this);
        manager.setStatusBarTintEnabled(true);
        manager.setStatusBarTintColor(getResources().getColor(color));
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
                    .getString(billDataHelper.recycleTurnToString(recycleperiod)));
        else
            periodtv.setText(getResources()
                    .getString(billDataHelper.recycleTurnToString(recycleperiod)));
    }

    //几个控件的点击事件
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
                AlertDialog.Builder builder=new AlertDialog.Builder(BillDetail.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delele();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setMessage("您确认要删除吗").create().show();

            }
        });

        editfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                location[0] += v.getWidth() / 2;
                intent.putExtra("location", location);
                startActivity(intent);
                overridePendingTransition(0, 0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
            }
        });
    }

    //删除该条目
    private void delele(){
        Map<String,Object> billmap= billDataHelper.getBillItem(billitemID);
        billDataHelper.deleteBill(billitemID);
        if((int)billmap.get("period")!=Util.NOPERIOD)
        {
            int recycleid = billDataHelper.getRecycleID(billmap);
            Map<String ,Object> map=new HashMap<>();
            map.put("period",0);
            ArrayList<Integer> list= billDataHelper.getBillID(billmap);
            for(int i=0;i<list.size();i++)
                billDataHelper.updateBillRecycle(map,list.get(i));
            billDataHelper.deleteRecycle(recycleid);
        }
        sendBroadcast(change);
        finish();
    }

    //动画方法，实现扩散动画，以及一些初始动画
    private void setupRevealRectangleBackgroud(){
        circleBackgroud.setListener(new RevealCircleBackgroud.onStateListener() {
            @Override
            public void onStateChange(int state) {
                if (RevealCircleBackgroud.STATE_FILL_FINISHED == state) {

                        editfab.animate().translationX(0).translationY(0).setDuration(400)
                            .setInterpolator(new OvershootInterpolator(1.0f)).setStartDelay(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    editfab.setImageResource(R.mipmap.ic_edit_white_36dp);
                                }
                            }).start();
                    linearLayout.animate().setDuration(300).alpha(1.0f).setStartDelay(400)
                            .setListener(new AnimatorListenerAdapter() {

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    backLayout.animate().translationY(0).setDuration(400)
                                            .setStartDelay(100).start();

                                }
                            }).start();
                } else {
                    editfab.setTranslationY(backLayout.getHeight() - Util.dpToPx(40));
                    editfab.setTranslationX(Util.dpToPx(34));
                    editfab.setImageResource(R.mipmap.ic_add_white_36dp);
                    linearLayout.setAlpha(0.0f);

                    backLayout.setTranslationY(2 * backLayout.getHeight() + linearLayout.getHeight());

                }
            }
        });

        final int[] loaction=getIntent().getIntArrayExtra("location");
        circleBackgroud.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                circleBackgroud.getViewTreeObserver().removeOnPreDrawListener(this);
                circleBackgroud.startFromLocation(loaction);
                return true;
            }
        });
    }


}
