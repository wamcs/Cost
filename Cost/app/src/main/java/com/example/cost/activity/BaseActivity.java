package com.example.cost.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cost.R;
import com.example.cost.SystemBarManager;

public abstract class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarManager manager = new SystemBarManager(this);
            manager.setStatusBarTintEnabled(true);
            manager.setStatusBarTintColor(getResources().getColor(R.color.style_color_primary_dark));
        }
    }
}
