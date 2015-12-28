package com.example.cost.UI;

import android.os.Bundle;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.BillDetailActivityController;
import com.example.cost.R;
import com.example.cost.UI.Base.BaseActivity;

/**
 * author:wamcs
 * date:2015/12/28
 * email:kaili@hustunique.com
 */
public class BillDetailActivity extends BaseActivity {

    BillDetailActivityController mBillDetailActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail);
        mBillDetailActivityController=new BillDetailActivityController(this,getWindow().getDecorView());
    }

    @Override
    public ActivityController getController() {
        return mBillDetailActivityController;
    }
}
