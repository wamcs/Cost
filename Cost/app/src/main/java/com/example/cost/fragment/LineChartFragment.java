package com.example.cost.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.activity.BillTable;
import com.example.cost.datebase.BillDateHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class LineChartFragment extends Fragment {

    private int billid;
    private LineChart lineChart;
    private BillDateHelper billDateHelper;
    private int year;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billid=getArguments().getInt("billid");
        billDateHelper=new BillDateHelper(getActivity(),"allbill.db",1);
        year=Util.year;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_linechart,container,false);
        lineChart= (LineChart) view.findViewById(R.id.table_line_chart);
        initChart();
        return view;
    }


    public void initChart(){
        lineChart.setDescription("");
        lineChart.setDrawBorders(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        lineChart.setBackgroundColor(getResources().getColor(R.color.line_chart_backgroud));

        lineChart.setData(getLinedate());
        Legend mlegend=lineChart.getLegend();

        mlegend.setFormSize(6f);
        mlegend.setForm(Legend.LegendForm.CIRCLE);
        mlegend.setTextColor(Color.WHITE);

        lineChart.animateXY(1000,1500);
    }

    public LineData getLinedate(){
        SQLiteDatabase liteDatabase=billDateHelper.getWritableDatabase();
        ArrayList<Entry> incomelist=new ArrayList<>();
        ArrayList<Entry> paylist=new ArrayList<>();
        for(int i=0;i<12;i++){
            int income=0;
            int pay=0;
            Cursor cursor=liteDatabase.rawQuery("select income,pay from bill where billid="
                    +billid+" and year="+year+" and month="+(i+1),null);
            for(;cursor.moveToNext();cursor.isAfterLast()){
                income+=cursor.getInt(cursor.getColumnIndex("income"));
                pay+=cursor.getInt(cursor.getColumnIndex("pay"));
            }
            incomelist.add(new Entry(income,i));
            paylist.add(new Entry(pay,i));
            cursor.close();
        }


        LineDataSet incomeDate=new LineDataSet(incomelist,"收入");
        incomeDate.setLineWidth(1.5f);
        incomeDate.setCircleSize(2.5f);
        incomeDate.setColor(getResources().getColor(R.color.line_chart_income));
        incomeDate.setCircleColor(getResources().getColor(R.color.line_chart_income));
        incomeDate.setHighLightColor(getResources().getColor(R.color.line_chart_income));
        LineDataSet payDate=new LineDataSet(paylist,"支出");
        payDate.setLineWidth(1.5f);
        payDate.setCircleSize(2.5f);
        payDate.setColor(getResources().getColor(R.color.line_chart_pay));
        payDate.setCircleColor(getResources().getColor(R.color.line_chart_pay));
        payDate.setHighLightColor(getResources().getColor(R.color.line_chart_pay));

        ArrayList<LineDataSet> lineDataSets=new ArrayList<>();
        lineDataSets.add(incomeDate);
        lineDataSets.add(payDate);

        LineData lineData=new LineData(Util.months,lineDataSets);

        return lineData;
    }
    /*@Override
    public void refresh() {
        year=Util.year;
        initChart();
    }
    */

}
