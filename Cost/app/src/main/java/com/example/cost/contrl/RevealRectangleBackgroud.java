package com.example.cost.contrl;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class RevealRectangleBackgroud extends View {

    public static int STATE_NO_START=0;
    public static int STATE_FILL_START=1;
    public static int STATE_FILL_FINISH=2;

    private int startPositionY;

    private Paint fillPaint;
    ObjectAnimator animator;

    private int state=STATE_NO_START;
    private int height;
    private onStateListener listener;

    public RevealRectangleBackgroud(Context context) {
        super(context);
        init();
    }

    public RevealRectangleBackgroud(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RevealRectangleBackgroud(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RevealRectangleBackgroud(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init(){
        fillPaint=new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
    }

    public void startFromPosition(int location){
        changeState(STATE_FILL_START);
        startPositionY=location;
        if(location<getHeight()-location)
            animator=ObjectAnimator.ofFloat(this,"height", 0, getHeight() - location);
        else
            animator=ObjectAnimator.ofFloat(this,"height",0,location);
        animator.setDuration(400);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0,startPositionY-height,getWidth(),startPositionY+height,fillPaint);
    }

    public void changeState(int state){
        if(this.state!=state){
            this.state=state;
            if(listener!=null)
                listener.onStateChange(state);
        }
    }

    public void setPaintColor(int color){
        fillPaint.setColor(getResources().getColor(color));
    }

    public void setListener(onStateListener listener) {
        this.listener = listener;
    }

    public interface onStateListener{
        void onStateChange(int state);
    }
}
