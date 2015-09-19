package com.example.cost.contrl;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.cost.R;
import com.example.cost.Util;

public class ColorCompareView  extends RelativeLayout{

    private ImageView payImageView;
    private ImageView incomeImageView;

    public ColorCompareView(Context context) {
        super(context);
        init(context);
    }

    public ColorCompareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorCompareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorCompareView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.view_compare_color,this,false);
        payImageView= (ImageView) view.findViewById(R.id.view_compare_pay);
        incomeImageView=(ImageView) view.findViewById(R.id.view_compare_income);
        addView(view);
    }

    public void setProportion(int pay,int income){
        int sum=pay+income;
        LayoutParams payParams = (LayoutParams) payImageView.getLayoutParams();
        LayoutParams incomeParams = (LayoutParams) incomeImageView.getLayoutParams();
        if(sum!=0) {
            payParams.width = (Util.width - Util.dpToPx(35)) * pay / sum;
            incomeParams.width = (Util.width - Util.dpToPx(35)) * income / sum;
            Log.e("TAG", "payparams " + payParams.width + " incomeparams " + incomeParams.width);
        }
        else{
            payParams.width = (Util.width - Util.dpToPx(35))/2;
            incomeParams.width = (Util.width - Util.dpToPx(35))/2;
        }
        payImageView.setLayoutParams(payParams);
        incomeImageView.setLayoutParams(incomeParams);
    }

    public void setAnimation() {
        payImageView.setTranslationX(Util.width*2);
        incomeImageView.setTranslationX(-Util.width);
        payImageView.animate().translationX(0).setDuration(1000)
                .setInterpolator(new AccelerateInterpolator()).start();
        incomeImageView.animate().translationX(0).setDuration(1000)
                .setInterpolator(new AccelerateInterpolator()).start();

    }
}
