package com.example.cost.Controller.Base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.Base.ContextController;

import butterknife.ButterKnife;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public abstract class FragmentController extends ContextController {

    private Fragment mFragment;
    private View mView;

    public FragmentController(Fragment fragment, ActivityController controller, View view) {
        super(controller.getContext());
        mFragment = fragment;
        mView=view;
        ButterKnife.bind(this, view);
    }

    public void onCreated(int id){

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onDestroy() {

    }

    public void onSaveInstanceState(Bundle outState) {

    }


    public void onDestroyView() {

    }

    public FragmentManager getChildFragmentManager() {
        return mFragment.getChildFragmentManager();
    }

    public FragmentManager getFragmentManager() {
        return mFragment.getFragmentManager();
    }

    public View getRootView(){
        return mView;
    }
}
