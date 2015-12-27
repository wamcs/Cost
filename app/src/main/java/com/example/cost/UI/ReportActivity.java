package com.example.cost.UI;

import android.os.Bundle;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.ReportActivityController;
import com.example.cost.R;
import com.example.cost.UI.Base.BaseActivity;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class ReportActivity extends BaseActivity {

    ReportActivityController mReportActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mReportActivityController=new ReportActivityController(this,getWindow().getDecorView());
    }

    @Override
    public ActivityController getController() {
        return mReportActivityController;
    }
}
