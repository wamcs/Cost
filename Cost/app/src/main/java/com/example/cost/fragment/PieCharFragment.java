package com.example.cost.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.activity.BillTable;
import com.example.cost.datebase.BillDateHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.Calendar;

public class PieCharFragment extends Fragment implements BillTable.pieRefresh{

    private int billid;
    private PieChart pieChart;
    private BillDateHelper billDateHelper;
    private boolean isShowAll = false;
    private ImageButton lastButton;
    private ImageButton nextButton;
    private ImageButton imageButton;
    private TextView dateNumber;
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
        imageButton = (ImageButton) view.findViewById(R.id.table_trans_btn);
        lastButton= (ImageButton) getActivity().findViewById(R.id.table_last);
        nextButton= (ImageButton) getActivity().findViewById(R.id.table_next);
        dateNumber= (TextView) getActivity().findViewById(R.id.table_date_number);
        initView();
        imageButton.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }


    public void initView() {
        if(isShowAll)
            dateNumber.setText("全账本");
        else
            dateNumber.setText(year+"年"+month+"月");
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(64f);

        pieChart.setDescription("");
        pieChart.setDrawCenterText(false);
        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);

        pieChart.setData(getPieDate());

        Legend legend=pieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        legend.setFormSize(5f);

        pieChart.animateXY(1000,1000);
    }


    private PieData getPieDate() {
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        SQLiteDatabase liteDatabase = billDateHelper.getWritableDatabase();
        PieDataSet pieDataSet;
        if (!isShowAll) {
            Cursor label = liteDatabase.rawQuery("select distinct label from bill where billid="
                    + billid + " and year=" + year + " and month=" + month, null);
            for (; label.moveToNext(); label.isAfterLast())
                xValues.add(label.getString(label.getColumnIndex("label")));
            if(xValues.isEmpty())
                imageButton.setVisibility(View.INVISIBLE);
            else
                imageButton.setVisibility(View.VISIBLE);
            label.close();
            for (int i = 0; i < xValues.size(); i++) {
                colors.add(getResources().getColor(billDateHelper.getcolor(xValues.get(i))));
                Cursor pay = liteDatabase.rawQuery("select pay from bill where label='"
                        + xValues.get(i) + "' and billid=" + billid + " and year=" + year +
                        " and month=" + month, null);
                int money = 0;
                for (; pay.moveToNext(); pay.isAfterLast())
                    money += pay.getInt(pay.getColumnIndex("pay"));
                yValues.add(new Entry((float) money, i));
            }
            pieDataSet = new PieDataSet(yValues, month+"月");
        } else {
            Cursor label = liteDatabase.rawQuery("select distinct label from bill where billid="
                    + billid, null);
            for (; label.moveToNext(); label.isAfterLast())
                xValues.add(label.getString(label.getColumnIndex("label")));
            if(xValues.isEmpty())
                imageButton.setVisibility(View.INVISIBLE);
            else
                imageButton.setVisibility(View.VISIBLE);
            label.close();
            for (int i = 0; i < xValues.size(); i++) {
                colors.add(getResources().getColor(billDateHelper.getcolor(xValues.get(i))));
                Cursor pay = liteDatabase.rawQuery("select pay from bill where label='"
                        + xValues.get(i) + "' and billid=" + billid, null);
                int money = 0;
                for (; pay.moveToNext(); pay.isAfterLast())
                    money += pay.getInt(pay.getColumnIndex("pay"));
                yValues.add(new Entry((float) money, i));
            }
            pieDataSet = new PieDataSet(yValues, "全账本");
        }
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(10f);

        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    @Override
    public void refresh() {
        year= Util.year;
        month = Util.month;
        initView();
    }
}
