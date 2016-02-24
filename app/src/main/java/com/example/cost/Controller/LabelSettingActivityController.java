package com.example.cost.Controller;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class LabelSettingActivityController extends ActivityController {

    @Bind(R.id.default_tool_bar_textview)
    TextView mToolbarText;

    public LabelSettingActivityController(AppCompatActivity activity, View view) {
        super(activity, view);
        ButterKnife.bind(this, view);
        mToolbarText.setText("标签设置");
    }

    @OnClick(R.id.default_tool_bar_imageview)
    void back(){
        finish();
    }
}
