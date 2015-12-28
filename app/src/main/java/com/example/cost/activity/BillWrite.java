package com.example.cost.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.adapter.LabelChooseAdapter;
import com.example.cost.UI.Widget.RevealCircleBackgroud;
import com.example.cost.datebase.BillDateHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 编辑类，三种初始化情况，一为主界面fab点击进入，二为shoppinglist的点击进入
 * 三为更改
 */

public class BillWrite extends BaseActivity {

    private EditText getMoney;
    private EditText getRemark;
    private ImageButton cancel;
    private TextView getLabel;
    private TextView timetv;
    private TextView periodtv;
    private TextView statetv;
    private SwitchCompat periodswitch;
    private SwitchCompat stateswitch;
    private ImageButton confirm;
    private LinearLayout labellayout;
    private LinearLayout timelayout;
    private LinearLayout periodlayout;
    private LinearLayout backgroudlayout;
    private LinearLayout statelayout;
    private BillDateHelper billDateHelper;
    private RevealCircleBackgroud revealCircleBackgroud;
    private int money;
    private String content;
    private String label;
    private int year;
    private int month;
    private int date;
    private int times;
    private int recycleperiod;
    private static int billID;
    private static int billitemID;
    private Intent change;
    private String ShoppingName;
    private String reciveLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billwrite);
        billID=getSharedPreferences("billselect",
                Context.MODE_PRIVATE).getInt("selectedID",1);
        billitemID=getIntent().getIntExtra("billitemID",0);
        ShoppingName=getIntent().getStringExtra("ShoppingName");
        billDateHelper=new BillDateHelper(this,"allbill.db",1);
        init();
        setupRevealCircleBackgroud();
        initDate(billitemID);
        setListener();
    }


    public void init(){
        getMoney= (EditText) findViewById(R.id.billwrite_getmoney);
        getRemark= (EditText) findViewById(R.id.billwrite_getremark);
        cancel= (ImageButton) findViewById(R.id.billwrite_return);
        getLabel= (TextView) findViewById(R.id.billwrite_label);
        timetv= (TextView) findViewById(R.id.billwrite_time);
        periodtv= (TextView) findViewById(R.id.billwrite_period);
        statetv= (TextView) findViewById(R.id.billwrite_state);
        periodswitch= (SwitchCompat) findViewById(R.id.billwrite_period_switch);
        stateswitch= (SwitchCompat) findViewById(R.id.billwrite_state_switch);
        confirm= (ImageButton) findViewById(R.id.billwrite_confirm);
        labellayout= (LinearLayout) findViewById(R.id.billwrite_label_layout);
        timelayout= (LinearLayout) findViewById(R.id.billwrite_timelayout);
        periodlayout= (LinearLayout) findViewById(R.id.billwrite_period_layout);
        backgroudlayout= (LinearLayout) findViewById(R.id.billwrite_backgroud_layout);
        statelayout= (LinearLayout) findViewById(R.id.billwrite_state_layout);
        revealCircleBackgroud= (RevealCircleBackgroud) findViewById(R.id.billwrite_reveal);
        change=new Intent();
        change.setAction("BILL_CHANGED");
    }

    public void getBilldate(){
        money=Integer.parseInt(getMoney.getText().toString());
        content=getRemark.getText().toString();
        label=getLabel.getText().toString();

    }

    public void setListener(){
        timelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             popTime();

            }
        });
        labellayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popLabel();
            }
        });

        stateswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    statetv.setText(R.string.stateincome);
                else
                    statetv.setText(R.string.statepay);
            }
        });
        periodswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recycleperiod=Util.NOPERIOD;
                if(isChecked)
                    popPeriod();
                else
                    periodtv.setText(R.string.periodno);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getMoney.getText().toString().equals("")||
                        getRemark.getText().toString().equals("")){
                    Snackbar.make(findViewById(android.R.id.content),"输入为空，请重新输入",Snackbar.LENGTH_SHORT).show();
                }else
                    dateCofirm();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
    }

    //根据获取的billitemid的正负性判断哪种初始化情况，
    // 负数为shoppinglist，0为fab，正数为更改
    public void initDate(int billitemID){
        if (billitemID<=0){
            year=(int)billDateHelper.getTime().get("year");
            month=(int)billDateHelper.getTime().get("month");
            date=(int)billDateHelper.getTime().get("date");
            times=date+month*100+year*10000;
            recycleperiod=Util.NOPERIOD;
            getLabel.setText(billDateHelper.getLabel(1));
            timetv.setText(year+"/"+month+"/"+date);
            if(ShoppingName!=null)
                getRemark.setText(ShoppingName);
        }
        else {
            Map<String,Object> map=billDateHelper.getbillitem(billitemID);
            String content=map.get("content").toString();
            int income=Integer.parseInt(map.get("income").toString());
            int pay=Integer.parseInt(map.get("pay").toString());
            String label=map.get("label").toString();
            recycleperiod=Integer.parseInt(map.get("period").toString());
            year=Integer.parseInt(map.get("year").toString());
            month=Integer.parseInt(map.get("month").toString());
            date=Integer.parseInt(map.get("date").toString());
            times=Integer.parseInt(map.get("time").toString());
            timetv.setText(year+"/"+month+"/"+date);
            getRemark.setText(content);
            getLabel.setText(label);
            if(income==0){
                stateswitch.setChecked(false);
                statetv.setText(getResources().getString(R.string.statepay));
                getMoney.setText(pay+"");
                money=pay;
            }
            else {
                stateswitch.setChecked(true);
                statetv.setText(getResources().getString(R.string.stateincome));
                getMoney.setText(income+"");
                money=income;
            }
            if(recycleperiod==Util.NOPERIOD){
                periodswitch.setChecked(false);
                periodtv.setText(getResources()
                        .getString(billDateHelper.recycleTurntoString(recycleperiod)));
            }
            else {
                periodswitch.setChecked(true);
                periodtv.setText(getResources()
                        .getString(billDateHelper.recycleTurntoString(recycleperiod)));
            }
        }
    }

    //数据的最终确认，并根据billitemid分三种情况处理，同上
    public void dateCofirm(){
        getBilldate();
        Map<String,Object> map=new HashMap<>();
        map.put("content", content);
        if(stateswitch.isChecked()){
            map.put("income",money);
            map.put("pay",0);
        }
        else{
            map.put("income",0);
            map.put("pay",money);
        }
        map.put("label",label);
        map.put("color",billDateHelper.getColor(label));
        map.put("period",recycleperiod);
        map.put("year",year);
        map.put("month",month);
        map.put("date",date);
        map.put("week",billDateHelper.getTime().get("week"));
        map.put("time",times);
        map.put("billid",billID);
        map.put("day", 0);
        if(billitemID<=0) {
            billDateHelper.addbill(map);
            if (periodswitch.isChecked())
                billDateHelper.addrecycle(map);
        }
        else{
            Map<String,Object> billmap=billDateHelper.getbillitem(billitemID);
            if((int)billmap.get("period")==Util.NOPERIOD)
            {
                billDateHelper.updateBill(map,billitemID);
                ArrayList<Integer> list=billDateHelper.getBillID(billmap);
                for(int i=0;i<list.size();i++)
                    billDateHelper.updateBillRecycle(map,list.get(i));
                if(periodswitch.isChecked())
                    billDateHelper.addrecycle(map);
            }
            else {
                int recycleid = billDateHelper.getRecycleID(billmap);
                billDateHelper.updateBill(map,billitemID);
                ArrayList<Integer> list=billDateHelper.getBillID(billmap);
                for(int i=0;i<list.size();i++)
                    billDateHelper.updateBillRecycle(map,list.get(i));
                if (periodswitch.isChecked())
                    billDateHelper.updateRecycle(map, recycleid);
                else
                    billDateHelper.deleterecycle(recycleid);
            }
        }
        sendBroadcast(change);
        finish();
        overridePendingTransition(0,0);
    }

    //动画
    public void setupRevealCircleBackgroud() {
        revealCircleBackgroud.setListener(new RevealCircleBackgroud.onStateListener() {
            @Override
            public void onStateChange(int state) {
                if (RevealCircleBackgroud.STATE_FILL_FINISHED == state) {
                    backgroudlayout.animate().translationY(0)
                            .setDuration(800).setStartDelay(0).start();
                    labellayout.animate().translationX(0).setInterpolator(new OvershootInterpolator(1.f))
                            .setDuration(800).setStartDelay(200).start();
                    timelayout.animate().translationX(0).setInterpolator(new OvershootInterpolator(1.f))
                            .setDuration(800).setStartDelay(300).start();
                    periodlayout.animate().translationX(0).setInterpolator(new OvershootInterpolator(1.f))
                            .setDuration(800).setStartDelay(400).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            periodswitch.animate().alpha(1.0f).setStartDelay(200)
                                    .setDuration(300).start();
                        }
                    }).start();
                    statelayout.animate().translationX(0).setInterpolator(new OvershootInterpolator(1.f))
                            .setDuration(800).setStartDelay(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            stateswitch.animate().alpha(1.0f).setStartDelay(100)
                                    .setDuration(300).start();
                        }
                    }).start();
                } else {
                    int backHeight = getResources().getDimensionPixelSize(R.dimen.activity_billwrite_height);
                    backgroudlayout.setTranslationY(-Util.dpToPx(backHeight));
                    labellayout.setTranslationX(-Util.width);
                    periodlayout.setTranslationX(-Util.width);
                    timelayout.setTranslationX(2 * Util.width);
                    statelayout.setTranslationX(2 * Util.width);
                    periodswitch.setAlpha(0.0f);
                    stateswitch.setAlpha(0.0f);
                }
            }
        });
        final int[] location = getIntent().getIntArrayExtra("location");
        revealCircleBackgroud.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                revealCircleBackgroud.getViewTreeObserver().removeOnPreDrawListener(this);
                revealCircleBackgroud.startFromLocation(location);
                return true;
            }
        });

    }

    //下面三个pop是周期标签及时间窗口的弹出
    public void popPeriod(){
        final PopupMenu pop = new PopupMenu(BillWrite.this,periodswitch);
        getMenuInflater().inflate(R.menu.bill_peroid_popmenu, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.period_month:
                            recycleperiod = Util.PRO_MONTH;
                            periodtv.setText(R.string.periodmonth);
                            pop.dismiss();
                            break;
                        case R.id.period_week:
                            recycleperiod = Util.PRO_WEEK;
                            periodtv.setText(R.string.periodweek);
                            pop.dismiss();
                            break;
                        case R.id.period_day:
                            recycleperiod = Util.PRO_DAY;
                            periodtv.setText(R.string.periodday);
                            pop.dismiss();
                            break;
                        case R.id.period_twoweek:
                            recycleperiod = Util.TWO_WEEK;
                            periodtv.setText(R.string.periodtwoweek);
                            pop.dismiss();
                            break;
                        default:
                            periodswitch.setChecked(false);
                            pop.dismiss();

                    }
                    return true;
                }
        });
        pop.show();

    }

    public void popLabel(){
        AlertDialog.Builder builder=
                new AlertDialog.Builder(BillWrite.this);
        View view= LayoutInflater.
                from(BillWrite.this).inflate(R.layout.layout_label_choose,null);
        RecyclerView recyclerView= (RecyclerView)
                view.findViewById(R.id.label_choose_recyclerview);
        final TextView textView= (TextView) view.findViewById(R.id.label_choose_title);
        Button button= (Button) view.findViewById(R.id.label_choose_button);
        LabelChooseAdapter adapter=new LabelChooseAdapter(BillWrite.this,billDateHelper.getLabelColor());
        adapter.setListener(new LabelChooseAdapter.labelListener() {
            @Override
            public void getLabel(String label) {
                textView.setText("标签:"+label);
                reciveLabel = label;
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(BillWrite.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        final Dialog dialog=builder.create();
        Window window=dialog.getWindow();
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        DisplayMetrics displayMetrics=
                getResources().getDisplayMetrics();
        layoutParams.width=(int)(displayMetrics.widthPixels*0.95);
        layoutParams.height=(int)(displayMetrics.heightPixels*0.6);
        window.setAttributes(layoutParams);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reciveLabel!=null) {
                    getLabel.setText(reciveLabel);
                    dialog.cancel();
                    reciveLabel=null;
                }
                else
                    Snackbar.make(findViewById(android.R.id.content),
                            "嘛，选一个咯", Snackbar.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void popTime(){
        AlertDialog.Builder datadialogbuilder=
                new AlertDialog.Builder(BillWrite.this);
        View view= getLayoutInflater().inflate(R.layout.view_dialog_datepicker,null);
        final DatePicker datePicker=
                (DatePicker) view.findViewById(R.id.view_dialog_datePicker);
        datadialogbuilder.setView(view);
        datePicker.setCalendarViewShown(false);
        datadialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                year=datePicker.getYear();
                month=datePicker.getMonth()+1;
                date=datePicker.getDayOfMonth();
                times=date+month*100+year*10000;
                timetv.setText(year+"/"+month+"/"+date);
                dialog.cancel();
            }
        });
        datadialogbuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        datadialogbuilder.create().show();
    }
}
