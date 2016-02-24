package com.example.cost.Controller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.Base.FragmentController;
import com.example.cost.R;
import com.example.cost.Utils.DataBaseHelper;
import com.example.cost.Utils.TimeHelper;
import com.example.cost.adapter.PieChartItemAdapter;
import com.example.cost.contrl.RecyclerItemDivider;
import com.example.cost.datebase.BillDataHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class PieChartFragmentController extends FragmentController {

    @Bind(R.id.pie_chart_fragment_pie_chart)
    PieChart PieChart;
    @Bind(R.id.pie_chart_fragment_translate_title_textview)
    TextView TranslateTitleTextview;
    @Bind(R.id.pie_chart_fragment_translate_money_textview)
    TextView TranslateMoneyTextview;
    @Bind(R.id.pie_chart_fragment_translate_button)
    LinearLayout TranslateButton;
    @Bind(R.id.pie_chart_fragment_last_button)
    ImageButton LastButton;
    @Bind(R.id.pie_chart_fragment_next_button)
    ImageButton NextButton;
    @Bind(R.id.pie_chart_fragment_recyclerview)
    RecyclerView Recyclerview;

    private boolean isShowAll = false;
    private ArrayList<String> labelList = new ArrayList<>();
    private ArrayList<Integer> moneyList = new ArrayList<>();
    private ArrayList<Integer> colorList = new ArrayList<>();
    private ArrayList<Integer> proportion = new ArrayList<>();
    private int year;
    private int month;
    private int billId;
    private BillDataHelper dataHelper;

    public PieChartFragmentController(Fragment fragment, ActivityController controller, View view) {
        super(fragment, controller, view);
        ButterKnife.bind(this, view);
        init();
    }

    private void init(){
        year= TimeHelper.getYear();
        month= TimeHelper.getMonth();
        dataHelper= DataBaseHelper.getInstance();
        initView();
    }

    public void initView() {
        if (isShowAll)
            TranslateTitleTextview.setText("全账本");
        else
            TranslateTitleTextview.setText(year + "年" + month + "月");
        PieChart.setHoleColorTransparent(true);
        PieChart.setHoleRadius(90f);

        PieChart.setDescription("");
        PieChart.setDrawCenterText(false);
        PieChart.setNoDataText(year + "年" + month + "月");
        PieChart.setDrawHoleEnabled(true);
        PieChart.setDrawSliceText(false);
        PieChart.setDrawMarkerViews(false);

        PieChart.setRotationAngle(90);
        PieChart.setRotationEnabled(true);
        PieChart.setUsePercentValues(true);

        PieChart.setData(getPieDate());
        PieChartItemAdapter adapter = new PieChartItemAdapter(getContext(), labelList
                , moneyList, colorList, proportion);
        Recyclerview.addItemDecoration(new RecyclerItemDivider(getContext()));
        Recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Recyclerview.setAdapter(adapter);

        Legend legend = PieChart.getLegend();
        legend.setEnabled(false);

        PieChart.animateXY(1000, 1000);
    }

    @OnClick(R.id.pie_chart_fragment_translate_button)
    void translate(){
        isShowAll = !isShowAll;
        initView();
        if (isShowAll) {
            LastButton.setVisibility(View.INVISIBLE);
            NextButton.setVisibility(View.INVISIBLE);
        } else {
            LastButton.setVisibility(View.VISIBLE);
            NextButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.pie_chart_fragment_last_button)
    void last(){
        if (month == 1) {
            year--;
            month = 12;
            initView();
        } else {
            month--;
            initView();
        }
    }

    @OnClick(R.id.pie_chart_fragment_next_button)
    void next(){
        if (month == 12) {
            year++;
            month = 1;
            initView();
        } else {
            month++;
            initView();
        }
    }

    private PieData getPieDate() {
        labelList = new ArrayList<>();
        moneyList = new ArrayList<>();
        colorList = new ArrayList<>();
        proportion = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Entry> yValues = new ArrayList<>();
        SQLiteDatabase liteDatabase = dataHelper.getWritableDatabase();
        PieDataSet pieDataSet;
        if (!isShowAll) {
            Cursor label = liteDatabase.rawQuery("select distinct label from bill where billid="
                    + billId + " and year=" + year + " and month=" + month + " and pay>0", null);
            for (; label.moveToNext(); label.isAfterLast())
                labelList.add(label.getString(label.getColumnIndex("label")));
            if (labelList.isEmpty())
                TranslateButton.setVisibility(View.INVISIBLE);
            else
                TranslateButton.setVisibility(View.VISIBLE);
            label.close();
            for (int i = 0; i < labelList.size(); i++) {
                colorList.add(dataHelper.getColorFromBill(labelList.get(i)));
                colors.add(getResource().getColor(dataHelper.getColorFromBill(labelList.get(i))));
                Cursor pay = liteDatabase.rawQuery("select pay from bill where label='"
                        + labelList.get(i) + "' and billid=" + billId + " and year=" + year +
                        " and month=" + month, null);
                int money = 0;
                for (; pay.moveToNext(); pay.isAfterLast())
                    money += pay.getInt(pay.getColumnIndex("pay"));
                pay.close();
                moneyList.add(money);
                yValues.add(new Entry((float) money, i));
            }
            pieDataSet = new PieDataSet(yValues, month + "月");
        } else {
            Cursor label = liteDatabase.rawQuery("select distinct label from bill where billid="
                    + billId, null);
            for (; label.moveToNext(); label.isAfterLast())
                labelList.add(label.getString(label.getColumnIndex("label")));
            if (labelList.isEmpty())
                TranslateButton.setVisibility(View.INVISIBLE);
            else
                TranslateButton.setVisibility(View.VISIBLE);
            label.close();
            for (int i = 0; i < labelList.size(); i++) {
                colorList.add(dataHelper.getColorFromBill(labelList.get(i)));
                colors.add(getResource().getColor(dataHelper.getColorFromBill(labelList.get(i))));
                Cursor pay = liteDatabase.rawQuery("select pay from bill where label='"
                        + labelList.get(i) + "' and billid=" + billId, null);
                int money = 0;
                for (; pay.moveToNext(); pay.isAfterLast())
                    money += pay.getInt(pay.getColumnIndex("pay"));
                pay.close();
                moneyList.add(money);
                yValues.add(new Entry((float) money, i));
            }
            pieDataSet = new PieDataSet(yValues, "全账本");
        }
        if (!moneyList.isEmpty()) {
            int money = 0;
            for (int i = 0; i < moneyList.size(); i++)
                money += moneyList.get(i);
            for (int i = 0; i < moneyList.size(); i++) {
                if ((moneyList.get(i) * 1000 / money) % 10 >= 5)
                    proportion.add(moneyList.get(i) * 100 / money + 1);
                else
                    proportion.add(moneyList.get(i) * 100 / money);
            }
            TranslateMoneyTextview.setText(money + "");
        }
        pieDataSet.setColors(colors);
        pieDataSet.setDrawValues(false);
        pieDataSet.setValueTextSize(10f);
        return new PieData(labelList, pieDataSet);
    }


    @Override
    public void onCreated(int id) {
        super.onCreated(id);
        billId=id;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
