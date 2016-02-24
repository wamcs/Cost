package com.example.cost.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.R;
import com.example.cost.UI.AboutUsActivity;
import com.example.cost.UI.LabelSettingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public class SettingActivityController extends ActivityController {

    @Bind(R.id.default_tool_bar_textview)
    TextView mToolbarText;

    public SettingActivityController(AppCompatActivity activity, View view) {
        super(activity, view);
        ButterKnife.bind(this, view);
        mToolbarText.setText("设置");
    }

    @OnClick(R.id.default_tool_bar_imageview)
    void back(){
        finish();
    }

    @OnClick(R.id.about_us)
    void about(){
        Intent intent = new Intent(this.getActivity(), AboutUsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.label_setting)
    void labelSetting(){
        Intent intent = new Intent(this.getActivity(), LabelSettingActivity.class);
        startActivity(intent);
    }
}
