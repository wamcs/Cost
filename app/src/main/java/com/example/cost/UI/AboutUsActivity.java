package com.example.cost.UI;

import android.os.Bundle;

import com.example.cost.Controller.AboutUsActivityController;
import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.R;
import com.example.cost.UI.Base.BaseActivity;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class AboutUsActivity extends BaseActivity {

    AboutUsActivityController mAboutUsActivityController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        mAboutUsActivityController=new AboutUsActivityController(this,getWindow().getDecorView());
    }

    @Override
    public ActivityController getController() {
        return mAboutUsActivityController;
    }
}
