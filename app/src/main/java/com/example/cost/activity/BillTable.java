package com.example.cost.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.cost.R;
import com.example.cost.UI.LineChartFragment;
import com.example.cost.UI.PieChartFragment;
import com.example.cost.Util;

import java.util.ArrayList;
import java.util.Calendar;

public class BillTable extends BaseActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
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


    }

    public void initView(){
        Bundle bundle=new Bundle();
        bundle.putInt("billid",billid);
        lineChart = new LineChartFragment();
        lineChart.setArguments(bundle);
        pieChart = new PieChartFragment();
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

}
