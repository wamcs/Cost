package com.example.cost.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.fragment.LineChartFragment;
import com.example.cost.fragment.PieCharFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class BillTable extends BaseActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton lastButton;
    private ImageButton nextButton;
    private TextView dateNumber;
    private int billid;
    private Fragment lineChart;
    private Fragment pieChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        billid=getIntent().getIntExtra("billid",1);
        Calendar calendar = Calendar.getInstance();
        Util.year = calendar.get(Calendar.YEAR);
        Util.month = calendar.get(Calendar.MONTH) + 1;
        init();
        initView();
    }

    public void init(){
        tabLayout= (TabLayout) findViewById(R.id.table_tablayout);
        viewPager= (ViewPager) findViewById(R.id.table_viewpager);
        lastButton= (ImageButton) findViewById(R.id.table_last);
        nextButton= (ImageButton) findViewById(R.id.table_next);
        dateNumber= (TextView) findViewById(R.id.table_date_number);
        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(Util.month==1){
                        Util.year--;
                        Util.month=12;
                        ((linerRefresh) lineChart).refresh();
                        ((pieRefresh) pieChart).refresh();
                    }else{
                        Util.month--;
                        ((pieRefresh) pieChart).refresh();
                    }
                dateNumber.setText(Util.year+"年"+Util.month+"月");
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(Util.month==12){
                        Util.year++;
                        Util.month=1;
                        ((linerRefresh) lineChart).refresh();
                        ((pieRefresh) pieChart).refresh();
                    }else{
                        Util.month++;
                        ((pieRefresh) pieChart).refresh();
                    }
                dateNumber.setText(Util.year+"年"+Util.month+"月");
            }
        });
        dateNumber.setText(Util.year+"年"+Util.month+"月");
    }

    public void initView(){
        Bundle bundle=new Bundle();
        bundle.putInt("billid",billid);
        lineChart = new LineChartFragment();
        lineChart.setArguments(bundle);
        pieChart = new PieCharFragment();
        pieChart.setArguments(bundle);
        final ArrayList<Fragment> list=new ArrayList<>();
        list.add(pieChart);
        list.add(lineChart);
        FragmentPagerAdapter adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        viewPager.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText("饼状图"),true);
        tabLayout.addTab(tabLayout.newTab().setText("折线图"),false);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
        TabLayout.TabLayoutOnPageChangeListener listener=
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        viewPager.addOnPageChangeListener(listener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public interface linerRefresh{
        void refresh();
    }
    public interface pieRefresh{
        void refresh();
    }

}
