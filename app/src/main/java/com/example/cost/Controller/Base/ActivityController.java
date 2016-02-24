package com.example.cost.Controller.Base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.example.cost.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public abstract class ActivityController extends ContextController {

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    private AppCompatActivity mActivity;

    private View mView;

    public ActivityController(AppCompatActivity activity,View view) {
        super(activity);
        this.mActivity=activity;
        this.mView=view;
        if (isToolBarEnable()) {
            ButterKnife.bind(this, view);
            initToolBar();
        }

    }

    private void initToolBar(){
        mActivity.setSupportActionBar(toolbar);
        ActionBar actionBar=mActivity.getSupportActionBar();
        if (null != actionBar){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected boolean isToolBarEnable(){
        return true;
    }

    public ActionBar getActionBar(){
        return mActivity.getSupportActionBar();

    }

    public AppCompatActivity getActivity(){
        return mActivity;
    }

    public Window getWindow(){
        return mActivity.getWindow();
    }

    public void onPostCreate() {

    }

    public void onStart() {

    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {

    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public void onDestroy() {

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onBackPressed() {

    }

    public void overridePendingTransition(int enterAnim, int exitAnim) {
        mActivity.overridePendingTransition(enterAnim, exitAnim);
    }

    public void startActivity(Intent intent){
        mActivity.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

    public void finish() {
        mActivity.finish();
    }

    public Intent getIntent() {
        return mActivity.getIntent();
    }

    public View getRootView(){
        return  mView;
    }




}
