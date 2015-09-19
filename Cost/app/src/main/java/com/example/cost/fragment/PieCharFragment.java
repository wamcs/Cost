package com.example.cost.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.adapter.PieChartItemAdapter;
import com.example.cost.datebase.BillDateHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;


public class PieCharFragment extends Fragment {

    private int billid;
    private PieChart pieChart;
    private BillDateHelper billDateHelper;
    private boolean isShowAll = false;
    private ImageButton lastButton;
    private ImageButton nextButton;
    private LinearLayout button;
    private TextView titletv;
    private TextView moneytv;
    private RecyclerView recyclerView;
    private ArrayList<String> labelList=new ArrayList<>();
    private ArrayList<Integer> moneyList=new ArrayList<>();
    private ArrayList<Integer> colorList=new ArrayList<>();
    private ArrayList<Integer> proportion=new ArrayList<>();
    private int year;
    private int month;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billid = getArguments().getInt("billid");
        billDateHelper = new BillDateHelper(getActivity(), "allbill.db", 1);
        year= Util.year;
        month = Util.month;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_piechart, container, false);
        pieChart = (PieChart) view.findViewById(R.id.table_pie_chart);
        button = (LinearLayout) view.findViewById(R.id.table_trans_btn);
        lastButton= (ImageButton) view.findViewById(R.id.table_last);
        nextButton= (ImageButton) view.findViewById(R.id.table_next);
        titletv= (TextView) view.findViewById(R.id.table_trans_title);
        moneytv=(TextView) view.findViewById(R.id.table_trans_money);
        recyclerView= (RecyclerView) view.findViewById(R.id.table_recyclerview);
        initView();
        initListener();
        return view;
    }


    public void initView() {
        if(isShowAll)
            titletv.setText("全账本");
        else
            titletv.setText(year+"年"+month+"月");
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(64f);


        pieChart.setDescription("");
        pieChart.setDrawCenterText(false);
        pieChart.setNoDataText(year+"年"+month+"月");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setDrawSliceText(false);
        pieChart.setDrawMarkerViews(false);

        pieChart.setRotationAngle(90);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);

        pieChart.setData(getPieDate());
        PieChartItemAdapter adapter=new PieChartItemAdapter(getActivity(),labelList
                ,moneyList,colorList,proportion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        Legend legend=pieChart.getLegend();
        legend.setEnabled(false);

        pieChart.animateXY(1000, 1000);
    }

    public void initListener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowAll = !isShowAll;
                initView();
                if (isShowAll) {
                    lastButton.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.INVISIBLE);
                } else {
                    lastButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });
        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.month==1){
                    Util.year--;
                    Util.month=12;
                    //((linerRefresh) lineChart).refresh();
                    refresh();
                }else{
                    Util.month--;
                    refresh();
                }

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.month==12){
                    Util.year++;
                    Util.month=1;
                    //((linerRefresh) lineChart).refresh();
                    refresh();
                }else{
                    Util.month++;
                    refresh();
                }

            }
        });

    }


    private PieData getPieDate() {
        labelList=new ArrayList<>();
        moneyList=new ArrayList<>();
        colorList=new ArrayList<>();
        proportion=new ArrayList<>();
        ArrayList<Integer> colors=new ArrayList<>();
        ArrayList<Entry> yValues = new ArrayList<>();
        SQLiteDatabase liteDatabase = billDateHelper.getWritableDatabase();
        PieDataSet pieDataSet;
        if (!isShowAll) {
            Cursor label = liteDatabase.rawQuery("select distinct label from bill where billid="
                    + billid + " and year=" + year + " and month=" + month, null);
            for (; label.moveToNext(); label.isAfterLast())
                labelList.add(label.getString(label.getColumnIndex("label")));
            if(labelList.isEmpty())
                button.setVisibility(View.INVISIBLE);
            else
                button.setVisibility(View.VISIBLE);
            label.close();
            for (int i = 0; i < labelList.size(); i++) {
                colorList.add(billDateHelper.getcolor(labelList.get(i)));
                colors.add(getResources().getColor(billDateHelper.getcolor(labelList.get(i))));
                Cursor pay = liteDatabase.rawQuery("select pay from bill where label='"
                        + labelList.get(i) + "' and billid=" + billid + " and year=" + year +
                        " and month=" + month, null);
                int money = 0;
                for (; pay.moveToNext(); pay.isAfterLast())
                    money += pay.getInt(pay.getColumnIndex("pay"));
                pay.close();
                moneyList.add(money);
                yValues.add(new Entry((float) money, i));
            }
            pieDataSet = new PieDataSet(yValues, month+"月");
        } else {
            Cursor label = liteDatabase.rawQuery("select distinct label from bill where billid="
                    + billid, null);
            for (; label.moveToNext(); label.isAfterLast())
                labelList.add(label.getString(label.getColumnIndex("label")));
            if(labelList.isEmpty())
                button.setVisibility(View.INVISIBLE);
            else
                button.setVisibility(View.VISIBLE);
            label.close();
            for (int i = 0; i < labelList.size(); i++) {
                colorList.add(billDateHelper.getcolor(labelList.get(i)));
                colors.add(getResources().getColor(billDateHelper.getcolor(labelList.get(i))));
                Cursor pay = liteDatabase.rawQuery("select pay from bill where label='"
                        + labelList.get(i) + "' and billid=" + billid, null);
                int money = 0;
                for (; pay.moveToNext(); pay.isAfterLast())
                    money += pay.getInt(pay.getColumnIndex("pay"));
                pay.close();
                moneyList.add(money);
                yValues.add(new Entry((float) money, i));
            }
            pieDataSet = new PieDataSet(yValues, "全账本");
        }
        if(!moneyList.isEmpty()){
            int money=0;
            for(int i=0;i<moneyList.size();i++)
                money+=moneyList.get(i);
            for(int i=0;i<moneyList.size();i++){
                if(moneyList.get(i)*100%money>=50)
                    proportion.add(moneyList.get(i)*100/money+1);
                else
                    proportion.add(moneyList.get(i) * 100 / money);
            }
            moneytv.setText(money+"");
        }
        pieDataSet.setColors(colors);
        pieDataSet.setDrawValues(false);
        pieDataSet.setValueTextSize(10f);
        return new PieData(labelList, pieDataSet);
    }

    public void refresh() {
        year= Util.year;
        month = Util.month;
        initView();
    }



}
