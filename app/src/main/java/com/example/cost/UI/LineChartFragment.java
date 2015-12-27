package com.example.cost.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cost.Controller.Base.FragmentController;
import com.example.cost.Controller.LineChartFragmentController;
import com.example.cost.R;
import com.example.cost.UI.Base.BaseFragment;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class LineChartFragment extends BaseFragment {

    LineChartFragmentController lineChartFragmentController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_linechart,container,false);
        lineChartFragmentController=new LineChartFragmentController(this,getActivityController(),view);
        return view;
    }

    @Override
    public FragmentController getController() {
        return lineChartFragmentController;
    }
}
