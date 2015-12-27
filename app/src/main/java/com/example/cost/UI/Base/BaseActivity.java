package com.example.cost.UI.Base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.Base.ControllerHolder;
import com.example.cost.R;
import com.example.cost.Utils.SystemBarManager;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public abstract class BaseActivity extends AppCompatActivity implements ControllerHolder {

    ActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //states bar change color to primary dark color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarManager manager = new SystemBarManager(this);
            manager.setStatusBarTintEnabled(true);
            manager.setStatusBarTintColor(getResources().getColor(R.color.style_color_primary_dark));
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller = getController();
        if (controller != null) {
            controller.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (controller != null) {
            controller.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (controller != null) {
            controller.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (controller != null) {
            controller.onStop();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != controller) {
            controller.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (controller != null) {
            controller.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != controller) {
            controller.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        if (null != controller) {
            controller.onBackPressed();
        }
    }


    public abstract ActivityController getController();
}
