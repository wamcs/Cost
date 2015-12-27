package com.example.cost.UI;

import android.os.Bundle;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.BillWriteActivityController;
import com.example.cost.R;
import com.example.cost.UI.Base.BaseActivity;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class BillWriteActivity extends BaseActivity {

    BillWriteActivityController mBillWriteActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billwrite);
        mBillWriteActivityController=new BillWriteActivityController(this,getWindow().getDecorView());
    }

    @Override
    public ActivityController getController() {
        return mBillWriteActivityController;
    }
}
