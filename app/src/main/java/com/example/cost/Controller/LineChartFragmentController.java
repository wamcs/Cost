package com.example.cost.Controller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.Base.FragmentController;
import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.Utils.DataBaseHelper;
import com.example.cost.Utils.FinalNumber;
import com.example.cost.Utils.TimeHelper;
import com.example.cost.datebase.BillDataHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class LineChartFragmentController extends FragmentController {

    @Bind(R.id.line_chart_fragment_line_chart)
    LineChart lineChart;
    @Bind(R.id.line_chart_fragment_month_textview)
    TextView MonthTextview;
    @Bind(R.id.line_chart_fragment_income_textview)
    TextView IncomeTextview;
    @Bind(R.id.line_chart_fragment_pay_textview)
    TextView PayTextview;

    private int billId;
    private int year;
    private int month;
    private BillDataHelper dataHelper;
    private ArrayList<Integer> incomeList = new ArrayList<>();
    private ArrayList<Integer> payList = new ArrayList<>();


    public LineChartFragmentController(Fragment fragment, ActivityController controller, View view) {
        super(fragment, controller, view);
        ButterKnife.bind(this, view);
        init();
    }

    private void init(){
        year= TimeHelper.getYear();
        month= TimeHelper.getMonth();
        dataHelper= DataBaseHelper.getInstance();
        initChart();
        MonthTextview.setText(month + "月");
        IncomeTextview.setText(incomeList.get(month - 1) + "");
        PayTextview.setText(payList.get(month - 1) + "");
    }

    @Override
    public void onCreated(int id) {
        super.onCreated(id);
        billId=id;
    }

    public void initChart() {
        lineChart.setDescription("");
        lineChart.setDrawBorders(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);

        lineChart.setData(getLinedate());


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(0);
        xAxis.setSpaceBetweenLabels(5);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value + "￥";
            }
        });

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend mlegend = lineChart.getLegend();
        mlegend.setFormSize(10f);
        mlegend.setForm(Legend.LegendForm.CIRCLE);
        mlegend.setTextColor(Color.BLACK);
        mlegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);

        lineChart.animateXY(2000, 2500);
    }

    public LineData getLinedate() {
        SQLiteDatabase liteDatabase = dataHelper.getWritableDatabase();
        ArrayList<Entry> incomelist = new ArrayList<>();
        ArrayList<Entry> paylist = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int income = 0;
            int pay = 0;
            Cursor cursor = liteDatabase.rawQuery("select income,pay from bill where billid="
                    + billId + " and year=" + year + " and month=" + (i + 1), null);
            for (; cursor.moveToNext(); cursor.isAfterLast()) {
                income += cursor.getInt(cursor.getColumnIndex("income"));
                pay += cursor.getInt(cursor.getColumnIndex("pay"));
            }
            incomeList.add(income);
            payList.add(pay);
            incomelist.add(new Entry(income, i));
            paylist.add(new Entry(pay, i));
            cursor.close();
        }


        LineDataSet incomeDate = new LineDataSet(incomelist, "收入");
        incomeDate.setLineWidth(2f);
        incomeDate.setCircleSize(4f);
        incomeDate.setDrawCubic(true);
        incomeDate.setColor(getResource().getColor(R.color.line_chart_income));
        incomeDate.setCircleColor(getResource().getColor(R.color.line_chart_income));
        incomeDate.setHighLightColor(getResource().getColor(R.color.line_chart_income));
        LineDataSet payDate = new LineDataSet(paylist, "支出");
        payDate.setLineWidth(2f);
        payDate.setCircleSize(4f);
        payDate.setDrawCubic(true);
        payDate.setColor(getResource().getColor(R.color.line_chart_pay));
        payDate.setCircleColor(getResource().getColor(R.color.line_chart_pay));
        payDate.setHighLightColor(getResource().getColor(R.color.line_chart_pay));

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(incomeDate);
        lineDataSets.add(payDate);
        LineData lineData = new LineData(FinalNumber.months, lineDataSets);
        lineData.setDrawValues(false);
        lineData.setHighlightEnabled(false);

        return lineData;
    }

    @OnClick(R.id.line_chart_fragment_last_month_button)
    void last(){
        if (month == 1) {
            Snackbar.make(getRootView(), "前面没有le~~", Snackbar.LENGTH_SHORT).show();
        } else {
            month--;
            refresh(false);
        }
    }

    @OnClick(R.id.line_chart_fragment_next_month_button)
    void next(){
        if (month == 12) {
            Snackbar.make(getRootView(), "后面没有le~~", Snackbar.LENGTH_SHORT).show();
        } else {
            month++;
            refresh(false);
        }
    }

    public void refresh(Boolean yearChange) {
        MonthTextview.setText(month + "月");
        IncomeTextview.setText(incomeList.get(month - 1) + "");
        PayTextview.setText(payList.get(month - 1) + "");
        if (yearChange) {
            incomeList.clear();
            payList.clear();
            initChart();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
