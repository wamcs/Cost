package com.example.cost.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.datebase.BillDateHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;

/**
 * lineChart，MPAndroidChart中的表，具体方法参见文档
 */
public class LineChartFragment extends Fragment {

    private int billid;
    private LineChart lineChart;
    private BillDateHelper billDateHelper;
    private ImageButton lastMonth;
    private ImageButton nextMonth;
    private TextView nowMonth;
    private TextView income;
    private TextView pay;
    private int year;
    private int month;
    private ArrayList<Integer> incomeList=new ArrayList<>();
    private ArrayList<Integer> payList=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billid=getArguments().getInt("billid");
        billDateHelper=new BillDateHelper(getActivity(),"allbill.db",1);
        year=Util.year;
        month=Util.month;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_linechart,container,false);
        lineChart= (LineChart) view.findViewById(R.id.table_line_chart);
        lastMonth= (ImageButton) view.findViewById(R.id.table_last_month);
        nextMonth= (ImageButton) view.findViewById(R.id.table_next_month);
        nowMonth= (TextView) view.findViewById(R.id.table_month);
        income= (TextView) view.findViewById(R.id.table_income);
        pay= (TextView) view.findViewById(R.id.table_pay);
        initChart();
        nowMonth.setText(month + "月");
        income.setText(incomeList.get(month-1)+"");
        pay.setText(payList.get(month-1)+"");
        setListener();
        return view;
    }


    public void initChart(){
        lineChart.setDescription("");
        lineChart.setDrawBorders(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);

        lineChart.setData(getLinedate());


        XAxis xAxis=lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(0);
        xAxis.setSpaceBetweenLabels(5);


        YAxis leftAxis=lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value+"￥";
            }
        });

        YAxis rightAxis=lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend mlegend=lineChart.getLegend();
        mlegend.setFormSize(10f);
        mlegend.setForm(Legend.LegendForm.CIRCLE);
        mlegend.setTextColor(Color.BLACK);
        mlegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);

        lineChart.animateXY(2000,2500);
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
            incomeList.add(income);
            payList.add(pay);
            incomelist.add(new Entry(income,i));
            paylist.add(new Entry(pay,i));
            cursor.close();
        }


        LineDataSet incomeDate=new LineDataSet(incomelist,"收入");
        incomeDate.setLineWidth(2f);
        incomeDate.setCircleSize(4f);
        incomeDate.setDrawCubic(true);
        incomeDate.setColor(getResources().getColor(R.color.line_chart_income));
        incomeDate.setCircleColor(getResources().getColor(R.color.line_chart_income));
        incomeDate.setHighLightColor(getResources().getColor(R.color.line_chart_income));
        LineDataSet payDate=new LineDataSet(paylist,"支出");
        payDate.setLineWidth(2f);
        payDate.setCircleSize(4f);
        payDate.setDrawCubic(true);
        payDate.setColor(getResources().getColor(R.color.line_chart_pay));
        payDate.setCircleColor(getResources().getColor(R.color.line_chart_pay));
        payDate.setHighLightColor(getResources().getColor(R.color.line_chart_pay));

        ArrayList<LineDataSet> lineDataSets=new ArrayList<>();
        lineDataSets.add(incomeDate);
        lineDataSets.add(payDate);
        LineData lineData=new LineData(Util.months,lineDataSets);
        lineData.setDrawValues(false);
        lineData.setHighlightEnabled(false);

        return lineData;
    }

    public void setListener(){
        lastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.month==1){
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "前面没有le~~", Snackbar.LENGTH_SHORT).show();
                }else{
                    Util.month--;
                    refresh(false);
                }

            }
        });
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.month==12){
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "后面没有le~~", Snackbar.LENGTH_SHORT).show();
                }else{
                    Util.month++;
                    refresh(false);
                }

            }
        });
    }

    public void refresh(Boolean yearChange) {
        year= Util.year;
        month = Util.month;
        nowMonth.setText(month+"月");
        income.setText(incomeList.get(month-1)+"");
        pay.setText(payList.get(month - 1) + "");
        if (yearChange){
            incomeList.clear();
            payList.clear();
            initChart();
        }
    }
}
