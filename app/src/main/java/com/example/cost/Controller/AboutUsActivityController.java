package com.example.cost.Controller;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.cost.Controller.Base.ActivityController;

import butterknife.ButterKnife;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class AboutUsActivityController extends ActivityController{

    public AboutUsActivityController(AppCompatActivity activity, View view) {
        super(activity, view);
        ButterKnife.bind(this, view);
    }

    private void initToolbar(){
        ActionBar actionBar=getActionBar();

    }
}
