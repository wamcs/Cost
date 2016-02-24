package com.example.cost.UI;

import android.os.Bundle;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.BillTableActivityController;
import com.example.cost.R;
import com.example.cost.UI.Base.BaseActivity;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class BillTableActivity extends BaseActivity {

    BillTableActivityController mBillTableActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        mBillTableActivityController=new BillTableActivityController(this,getWindow().getDecorView());
    }

    @Override
    public ActivityController getController() {
        return mBillTableActivityController;
    }
}
