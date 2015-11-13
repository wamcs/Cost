package com.example.cost.contrl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cost.R;

/**
 * 所有recyclerview的item分割线
 */
public class RecyclerItemDivider extends RecyclerView.ItemDecoration{
    private Context context;
    private int mItemSize = 2 ;
    private Paint mPaint;

    public RecyclerItemDivider(Context context){
        this.context=context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        mPaint.setColor(context.getResources().getColor(R.color.backgroud));
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        onDrawVertical(c, parent);
    }

    public void onDrawVertical(Canvas canvas,RecyclerView parent){
        final int left=parent.getPaddingLeft();
        final int right=parent.getMeasuredWidth()-parent.getPaddingRight();
        final int childrenNum=parent.getChildCount();
        for(int i=0;i<childrenNum;i++){
            final View view=parent.getChildAt(i);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) view.getLayoutParams();
            final int top=view.getBottom()+params.bottomMargin;
            final int bootom=top+mItemSize;
            canvas.drawRect(left,top,right,bootom,mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0,0,0,mItemSize);
    }

    public void setmPaintColor(int color){
        mPaint.setColor(context.getResources().getColor(color));
    }

    public void setmItemSize(int size){
        this.mItemSize=size;
    }
}
