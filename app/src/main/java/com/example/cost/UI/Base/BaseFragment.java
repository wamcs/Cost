package com.example.cost.UI.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.cost.Controller.Base.ActivityController;
import com.example.cost.Controller.Base.BaseController;
import com.example.cost.Controller.Base.ContextController;
import com.example.cost.Controller.Base.ControllerHolder;
import com.example.cost.Controller.Base.FragmentController;

/**
 * author:wamcs
 * date:2015/12/27
 * email:kaili@hustunique.com
 */
public abstract class BaseFragment extends Fragment implements ControllerHolder {


    private ActivityController mActivityController;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null !=getController()){
            if (getArguments() != null) {
                getController().onCreated(getArguments().getInt("billid"));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ControllerHolder) {
            mActivityController = (ActivityController) ((ControllerHolder) context).getController();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ControllerHolder");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != getController()) {
            getController().onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != getController()) {
            getController().onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != getController()) {
            getController().onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != getController()) {
            getController().onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != getController()) {
            getController().onDestroyView();
        }
    }

    public ActivityController getActivityController() {
        if (mActivityController == null) {
            throw new RuntimeException("Must be called after Fragment.attach()");
        }
        return mActivityController;
    }

    public abstract FragmentController getController();


}
