package com.example.cost.contrl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 用于实现嵌套recyclerview的内部recyclerview的高度自适应（有bug，已使用github上的库）
 */

public class LayoutManager extends android.support.v7.widget.LinearLayoutManager {
    public LayoutManager(Context context) {
        super(context);
    }
    private int[] mMeasuredDimension=new int[2];

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);



        int width = 0;
        int height = 0;
        for (int i = 0; i < getItemCount(); i++) {
            measureScrapChild(recycler, i,
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    mMeasuredDimension);

            if (getOrientation() == HORIZONTAL) {
                width = width + mMeasuredDimension[0];
                if (i == 0) {
                    height = mMeasuredDimension[1];
                }
            } else {
                height = height + mMeasuredDimension[1];
                if (i == 0) {
                    width = mMeasuredDimension[0];
                }
            }
        }


        switch (widthMode) {
            case View.MeasureSpec.EXACTLY:
                width = widthSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        switch (heightMode) {
            case View.MeasureSpec.EXACTLY:
                height = heightSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }
        setMeasuredDimension(width, height);
    }
    private void measureScrapChild(RecyclerView.Recycler recycler,int position,int widthSpec,
                                   int heightSpec,int[] measuredDimension){
        View view=recycler.getViewForPosition(position);
        RecyclerView.LayoutParams p= (RecyclerView.LayoutParams) view.getLayoutParams();
        int childWidthSpec= ViewGroup.getChildMeasureSpec(widthSpec,
                getPaddingLeft()+getPaddingRight(), p.width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                getPaddingTop() + getPaddingBottom(), p.height);
        view.measure(childWidthSpec,childHeightSpec);
        measuredDimension[0]=view.getMeasuredWidth()+p.leftMargin+p.rightMargin;
        measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
        Log.e("TAG", "measureScrapChild "+position+"\nheight"+measuredDimension[1]+"\nBottomMargin"+p.bottomMargin
        +"\nTopMargin"+p.topMargin+"\nviewHeight"+p.height);
        recycler.recycleView(view);
    }
}
