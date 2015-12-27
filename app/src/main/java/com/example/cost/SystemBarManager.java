package com.example.cost;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 实现状态栏透明的类，所用方法为测量状态栏的高度，使其透明，
 * 并在状态栏处添加一个view，高度为状态栏高度，颜色为主背景色
 */

public class SystemBarManager {

    public static final int DEFAULT_TINT_COLOR = 0x99000000;

    private final SystemBarConfig mConfig;
    private boolean mStatusBarAvailable;
    private View mStatusBarTintView;

    public SystemBarManager(Activity activity) {

        Window win = activity.getWindow();
        ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int[] attrs = {android.R.attr.windowTranslucentStatus};
            TypedArray a = activity.obtainStyledAttributes(attrs);
            try {
                mStatusBarAvailable = a.getBoolean(0, false);
            } finally {
                a.recycle();
            }
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((winParams.flags & bits) != 0) {
                mStatusBarAvailable = true;
            }
        }

        mConfig = new SystemBarConfig(activity);

        if (mStatusBarAvailable) {
            setupStatusBarView(activity, decorViewGroup);
        }

    }

    public void setStatusBarTintEnabled(boolean enabled) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    public void setStatusBarTintColor(int color) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundColor(color);
        }
    }

    private void setupStatusBarView(Context context, ViewGroup decorViewGroup) {
        mStatusBarTintView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mConfig.getStatusBarHeight());
        params.gravity = Gravity.TOP;
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mStatusBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mStatusBarTintView);
    }

    public static class SystemBarConfig {

        private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
        private final int mStatusBarHeight;

        private SystemBarConfig(Activity activity) {
            Resources res = activity.getResources();
            mStatusBarHeight = getInternalDimensionSize(res, STATUS_BAR_HEIGHT_RES_NAME);
        }

        private int getInternalDimensionSize(Resources res, String key) {
            int result = 0;
            int resourceId = res.getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
            return result;
        }

        public int getStatusBarHeight() {
            return mStatusBarHeight;
        }
    }
}
