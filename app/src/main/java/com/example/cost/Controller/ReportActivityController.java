package com.example.cost.Controller;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.R;
import com.example.cost.UI.Widget.ColorCompareView;
import com.example.cost.Utils.DataBaseHelper;
import com.example.cost.Utils.TimeHelper;
import com.example.cost.datebase.BillDataHelper;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class ReportActivityController extends ActivityController {

    @Bind(R.id.activity_report_date)
    ColorCompareView activityReportDate;
    @Bind(R.id.activity_report_date_income)
    TextView activityReportDateIncome;
    @Bind(R.id.activity_report_date_pay)
    TextView activityReportDatePay;
    @Bind(R.id.activity_report_week)
    ColorCompareView activityReportWeek;
    @Bind(R.id.activity_report_week_income)
    TextView activityReportWeekIncome;
    @Bind(R.id.activity_report_week_pay)
    TextView activityReportWeekPay;
    @Bind(R.id.activity_report_month)
    ColorCompareView activityReportMonth;
    @Bind(R.id.activity_report_month_income)
    TextView activityReportMonthIncome;
    @Bind(R.id.activity_report_month_pay)
    TextView activityReportMonthPay;
    @Bind(R.id.activity_report_year)
    ColorCompareView activityReportYear;
    @Bind(R.id.activity_report_year_income)
    TextView activityReportYearIncome;
    @Bind(R.id.activity_report_year_pay)
    TextView activityReportYearPay;
    @Bind(R.id.default_tool_bar_textview)
    TextView mToolbarText;

    private int billId;
    private int year;
    private int month;
    private int week;
    private int date;
    private BillDataHelper dataHelper;

    public ReportActivityController(AppCompatActivity activity, View view) {
        super(activity, view);
        ButterKnife.bind(this, view);
        init();
        loadData();

    }

    private void init(){
        mToolbarText.setText("收支情况");
        billId = getIntent().getIntExtra("billid", 1);
        dataHelper= DataBaseHelper.getInstance();
        year= TimeHelper.getYear();
        month=TimeHelper.getMonth();
        week=TimeHelper.getWeek();
        date=TimeHelper.getDate();
    }

    private void loadData(){
        Map<String,Integer> map;
        int income=0;
        int pay=0;

        map=dataHelper.getDataOfIncomeAndPay(billId,year);
        income=map.get("income");
        pay=map.get("pay");
        activityReportYearIncome.setText(income + "");
        activityReportYearPay.setText(pay + "");
        activityReportYear.setProportion(income , pay);
        activityReportYear.setAnimation();

        map=dataHelper.getDataOfIncomeAndPay(billId ,year ,month);
        income=map.get("income");
        pay=map.get("pay");
        activityReportMonthIncome.setText(income + "");
        activityReportMonthPay.setText(pay + "");
        activityReportMonth.setProportion(income , pay);
        activityReportMonth.setAnimation();

        map=dataHelper.getWeekDataOfIncomeAndPay(billId, year, month, week);
        income=map.get("income");
        pay=map.get("pay");
        activityReportWeekIncome.setText(income + "");
        activityReportWeekPay.setText(pay + "");
        activityReportWeek.setProportion(income , pay);
        activityReportWeek.setAnimation();

        map=dataHelper.getDateDataOfIncomeAndPay(billId, year, month, date);
        income=map.get("income");
        pay=map.get("pay");
        activityReportDateIncome.setText(income + "");
        activityReportDatePay.setText(pay + "");
        activityReportDate.setProportion(income , pay);
        activityReportDate.setAnimation();

    }

    @OnClick(R.id.default_tool_bar_imageview)
    void back(){
        finish();
    }


}
