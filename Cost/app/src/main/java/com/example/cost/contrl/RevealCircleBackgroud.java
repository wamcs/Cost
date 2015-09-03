package com.example.cost.contrl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class RevealCircleBackgroud extends View {

    public static final int STATE_NO_START=0;
    public static final int STATE_FILL_STRATED=1;
    public static final int STATE_FILL_FINISHED=2;

    private Paint fillPaint;
    private int state=STATE_NO_START;
    private ObjectAnimator animator;

    private int LAST_TIME=400;
    private int radius;

    private int startLocationX;
    private int startLocationY;

    private onStateListener listener;


    public RevealCircleBackgroud(Context context) {
        super(context);
        init();
    }

    public RevealCircleBackgroud(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RevealCircleBackgroud(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RevealCircleBackgroud(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        fillPaint=new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.WHITE);
    }

    public void setPaintColor(int Color){
        fillPaint.setColor(Color);
    }

    public void startFromLocation(int[] location){
        changeState(STATE_FILL_STRATED);
        startLocationX=location[0];
        startLocationY=location[1];
        animator=ObjectAnimator.ofInt(this,"radius",0,getWidth()+getHeight()).setDuration(LAST_TIME);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FILL_FINISHED);
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (STATE_FILL_FINISHED==state){
            canvas.drawRect(0,0,getWidth(),getHeight(),fillPaint);
        }
        else{
            canvas.drawCircle(startLocationX,startLocationY,radius,fillPaint);
        }
    }

    public void changeState(int state){
        if(this.state!=state){
            this.state=state;
            if(listener!=null){
                listener.onStateChange(state);
            }
        }

    }

    public void setListener(onStateListener listener) {
        this.listener = listener;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    public interface onStateListener{
        void onStateChange(int state);
    }

}
