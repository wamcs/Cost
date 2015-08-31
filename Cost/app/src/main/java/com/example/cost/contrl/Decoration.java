package com.example.cost.contrl;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class Decoration extends RecyclerView.ItemDecoration{

    private int space;

    public Decoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

    }
}
