package com.example.cost.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cost.Util;
import com.example.cost.adapter.BillAdapter;
import com.example.cost.adapter.ShopingAdpter;
import com.example.cost.contrl.RecyclerItemDivider;
import com.example.cost.datebase.BillDateHelper;
import com.example.cost.R;
import com.example.cost.adapter.NagivationAdapter;
import com.example.cost.service.TimeService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Cost extends BaseActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private BillDateHelper billDateHelper;
    private ListView listView;
    private RecyclerView recyclerView;
    private TextView titletv;
    private TextView incometv;
    private TextView paytv;
    private ImageView backgroud;
    private LinearLayout rightLayout;
    private LinearLayout leftlayout;
    private LinearLayout layout;
    private ImageButton fab;
    private LinearLayout incomelayout;
    private LinearLayout paylayout;
    private Intent tobillwrite;
    private int id;
    private ImageButton shoppingadd;
    private ListView shoppinglist;
    private static Boolean isChanged=false;
    private Handler handler;
    private ExecutorService es= Executors.newCachedThreadPool();
    private ArrayList<Integer> time;
    private  Map<Integer,ArrayList<Map<String,Object>>> date;
    private ArrayList<String> namelist;
    private ArrayList<Integer> idlist;
    private String title;
    private String pay;
    private String income;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);
        billDateHelper=new BillDateHelper(this,"allbill.db",1);
        id=this.getSharedPreferences("billselect",
                Context.MODE_PRIVATE).getInt("selectedID",1);
        Intent intent =new Intent(this, TimeService.TimeChangedBroadReceiver.class);
        intent.setAction("TIME_CHANGE");
        Calendar calendar=Calendar.getInstance();
        //calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,
        //        calendar.get(Calendar.DATE)+1,8,0,0);
        PendingIntent sender=
                PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),24*60*60*1000, sender);
        init();
        StartAnimation();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Cost.this,Report.class);
                int billid=getSharedPreferences("billselect",
                        Context.MODE_PRIVATE).getInt("selectedID",1);
                intent.putExtra("billid",billid);
                startActivity(intent);
            }
        });
        onCreateShopping();
        es.execute(new BaseThread());
        onCreateNavigation();
        onCreateFab();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.isInitialize=true;
            }
        },1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isChanged) {
            es.execute(new BaseThread());
            isChanged = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Util.isInitialize=false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(billDateHelper!=null)
            billDateHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.first:
                drawerLayout.openDrawer(rightLayout);
                break;
            case R.id.second:
                Intent billTable=new Intent(Cost.this,BillTable.class);
                int billid=this.getSharedPreferences("billselect",
                        Context.MODE_PRIVATE).getInt("selectedID",1);
                billTable.putExtra("billid", billid);
                startActivity(billTable);
                break;
            case R.id.setting:
                Intent setting=new Intent(Cost.this,Setting.class);
                startActivity(setting);
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(leftlayout);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&(drawerLayout.isDrawerOpen(rightLayout)
        ||drawerLayout.isDrawerOpen(leftlayout))){
                drawerLayout.closeDrawer(rightLayout);
                drawerLayout.closeDrawer(leftlayout);
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_BACK&&!drawerLayout.isDrawerOpen(rightLayout)
                &&!drawerLayout.isDrawerOpen(leftlayout)){
            this.finish();
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    public void init(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawlayout);
        listView= (ListView) findViewById(R.id.view_ngv_lv);
        recyclerView= (RecyclerView) findViewById(R.id.bill_rv);
        titletv= (TextView) findViewById(R.id.view_activity_head_title);
        incometv= (TextView) findViewById(R.id.view_activity_income);
        paytv= (TextView) findViewById(R.id.view_activity_pay);
        backgroud= (ImageView) findViewById(R.id.view_activity_head_iv);
        rightLayout= (LinearLayout) findViewById(R.id.rightlayout);
        leftlayout= (LinearLayout) findViewById(R.id.leftlayout);
        layout= (LinearLayout) findViewById(R.id.activity_head_layout);
        fab= (ImageButton) findViewById(R.id.fab_btn_main);
        shoppingadd= (ImageButton) findViewById(R.id.view_shoppinglist_addbtn);
        shoppinglist= (ListView) findViewById(R.id.view_shoppinglist_listview);
        incomelayout= (LinearLayout) findViewById(R.id.activity_head_income_layout);
        paylayout= (LinearLayout) findViewById(R.id.activity_head_pay_layout);
        tobillwrite=new Intent(this,BillWrite.class);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        handler=new Handler();
        Util.width=getResources().getDisplayMetrics().widthPixels;
    }

    public void onCreateFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] location=new int[2];
                v.getLocationOnScreen(location);
                location[0]+=v.getWidth()/2;
                tobillwrite.putExtra("location", location);
                startActivity(tobillwrite);
                overridePendingTransition(0, 0);
            }
        });
    }

    public void onCreateShopping(){
        namelist=new ArrayList<>();
        idlist=new ArrayList<>();
        initData();
        final ShopingAdpter adpter=new ShopingAdpter(this,namelist,idlist);
        adpter.setConnection(new ShopingAdpter.Connection() {
            @Override
            public void change(int id) {
                    billDateHelper.deleteShopping(id);
                    initData();
                    adpter.notifyDataSetChanged();

            }
        });
        shoppingadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billDateHelper.addShopping();
                initData();
                adpter.notifyDataSetChanged();
            }
        });
        shoppinglist.setAdapter(adpter);
    }
    //侧边菜单栏实现
    public void onCreateNavigation(){
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_menu_white_24dp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NagivationAdapter nagivationAdapter = new NagivationAdapter(Cost.this);
        nagivationAdapter.setMainChanged(new NagivationAdapter.MainChanged() {
            @Override
            public void changed(int ID) {
                id=ID;
                es.execute(new BaseThread());
            }
        });
        nagivationAdapter.setTitleChanged(new NagivationAdapter.TitleChanged() {
            @Override
            public void changed(String name) {
                titletv.setText(name);

            }
        });
        listView.setDividerHeight(0);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(nagivationAdapter);
    }

    public void onCreateMain(int id){
        time=getTime(id);
        date=getDateMap(id,time);
        title=getTitle(id);
        income=getTotalIncome(id)+"";
        pay=getTotalPay(id) + "";
    }

    public Map<Integer,ArrayList<Map<String,Object>>> getDateMap(int id,ArrayList<Integer> time){
        Map<Integer,ArrayList<Map<String,Object>>> date=new HashMap<>();
        int length=time.size();
        for(int i=0;i<length;i++){
            date.put(time.get(i),getDateList(time.get(i),id));
        }
        return date;
    }

    public ArrayList<Integer> getTime(int id){
        ArrayList<Integer> time=new ArrayList<>();
        SQLiteDatabase db=billDateHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select distinct time from bill where billid="+id+" order by time desc", null);
        for(;cursor.moveToNext();cursor.isAfterLast()){
            time.add(cursor.getInt(cursor.getColumnIndex("time")));
        }

        cursor.close();
        return time;
    }

    public ArrayList<Map<String,Object>> getDateList(int time,int id){
        ArrayList<Map<String,Object>> datelist=new ArrayList<>();
        SQLiteDatabase db=billDateHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from bill where time="+time+" and billid="+id,null);
        for (;cursor.moveToNext();cursor.isAfterLast()){
            Map<String,Object> map=new HashMap<>();
            int ID = cursor.getInt(cursor.getColumnIndex("_id"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            int income=cursor.getInt(cursor.getColumnIndex("income"));
            int pay=cursor.getInt(cursor.getColumnIndex("pay"));
            String label=cursor.getString(cursor.getColumnIndex("label"));
            int color=cursor.getInt(cursor.getColumnIndex("color"));
            String period=cursor.getString(cursor.getColumnIndex("period"));
            int billid=cursor.getInt(cursor.getColumnIndex("billid"));
            map.put("ID",ID);
            map.put("content",content);
            map.put("income",income);
            map.put("pay",pay);
            map.put("label",label);
            map.put("period",period);
            map.put("color",color);
            map.put("billid",billid);
            datelist.add(map);
        }
        cursor.close();
        return datelist;
    }

    public String getTitle(int id){
        SQLiteDatabase db=billDateHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select name from billdirc where _id="+id,null);
        cursor.moveToNext();
        String name = cursor.getString(cursor.getColumnIndex("name"));
        cursor.close();
        return name;
    }

    public int getTotalIncome(int id){
        SQLiteDatabase db=billDateHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select income from bill where billid="+id,null);
        int income=0;
        for(;cursor.moveToNext();cursor.isAfterLast())
            income+=cursor.getInt(cursor.getColumnIndex("income"));
        cursor.close();
        return income;

    }

    public int getTotalPay(int id){
        SQLiteDatabase db=billDateHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select pay from bill where billid="+id,null);
        int pay=0;
        for(;cursor.moveToNext();cursor.isAfterLast())
            pay+=cursor.getInt(cursor.getColumnIndex("pay"));
        cursor.close();
        return pay;
    }

    public void initData(){
        if(!namelist.isEmpty())
        namelist.clear();
        if(!idlist.isEmpty())
        idlist.clear();
        SQLiteDatabase db=billDateHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from shopping",null);
        for(;cursor.moveToNext();cursor.isAfterLast()){
            namelist.add(cursor.getString(cursor.getColumnIndex("name")));
            idlist.add(cursor.getInt(cursor.getColumnIndex("_id")));
        }
        cursor.close();
    }

    public class BaseThread implements Runnable{

        @Override
        public void run() {
            onCreateMain(id);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    titletv.setText(title);
                    incometv.setText(income);
                    paytv.setText(pay);
                    recyclerView.setAdapter(new BillAdapter(Cost.this,time,date,Cost.this));
                }
            });
        }
    }

    public static class BillChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            isChanged=true;
        }
    }

    public void StartAnimation(){
        fab.setTranslationY(2*getResources().getDimensionPixelOffset(R.dimen.fab_size));
        int actionbarSize = Util.dpToPx(56);
        DisplayMetrics displayMetrics=this.getResources().getDisplayMetrics();
        int width=displayMetrics.widthPixels;
        backgroud.setTranslationX(-width);
        titletv.setTranslationX(-width);
        incomelayout.setTranslationX(-width);
        paylayout.setTranslationX(-width);
        toolbar.setTranslationY(-actionbarSize);
        toolbar.animate()
                .setDuration(300)
                .translationY(0)
                .setStartDelay(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fab.animate().setDuration(300).translationY(0)
                                .setStartDelay(700).setInterpolator(new OvershootInterpolator(1.f))
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        Util.isInitialize = true;
                                    }
                                })
                                .start();
                    }
                })
                .start();
        backgroud.animate()
                .setDuration(300)
                .translationX(0)
                .setStartDelay(600)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        titletv.animate().setDuration(300).translationX(0)
                               .setStartDelay(200).setInterpolator(new OvershootInterpolator(1.f))
                               .start();
                        incomelayout.animate().setDuration(500).translationX(0)
                                .setStartDelay(300).setInterpolator(new OvershootInterpolator(1.f))
                                .start();
                        paylayout.animate().setDuration(500).translationX(0)
                                .setStartDelay(350).setInterpolator(new OvershootInterpolator(1.f))
                                .start();
                        RecyclerItemDivider divider=new RecyclerItemDivider(Cost.this);
                        divider.setmItemSize(2);
                        divider.setmPaintColor(R.color.cost_divider);
                        recyclerView.addItemDecoration(divider);
                    }
                })
                .start();
    }


}


