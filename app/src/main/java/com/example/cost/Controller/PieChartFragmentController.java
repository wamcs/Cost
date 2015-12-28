package com.example.cost.Controller;

import android.support.v4.app.Fragment;
import android.view.View;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.Base.FragmentController;

import butterknife.ButterKnife;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class PieChartFragmentController extends FragmentController {

    public PieChartFragmentController(Fragment fragment, ActivityController controller, View view) {
        super(fragment, controller, view);
        ButterKnife.bind(this, view);
    }
}
