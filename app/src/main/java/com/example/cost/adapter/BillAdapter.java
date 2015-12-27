package com.example.cost.adapter;

import android.app.Activity;
import android.content.Context;

import com.example.cost.Util;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.cost.R;
import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Map;

/**
 *由于使用recyclerview嵌套的形式，此为外部recyclerview的adapter，用于加载条目的日期划分
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolderBody>{

    private Map<Integer,ArrayList<Map<String,Object>>> date;
    private Context context;
    private ArrayList<Integer> time;
    private Activity activity;
    public BillAdapter(Context context, ArrayList<Integer> time,
                       Map<Integer, ArrayList<Map<String, Object>>> date, Activity activity){
        this.context=context;
        this.date=date;
        this.time=time;
        this.activity=activity;
    }

    @Override
    public ViewHolderBody onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolderBody viewHolder;
        view=LayoutInflater.from(context).inflate(R.layout.view_activity_body,parent,false);
        viewHolder=new ViewHolderBody(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBody viewHolderBody, int position) {
        ArrayList<Map<String, Object>> recycledate=date.get(time.get(position));
        int date=time.get(position)%100;
        int month=(time.get(position)%10000)/100;
        viewHolderBody.month.setText(month+"月");
        viewHolderBody.date.setText(date + "");
        LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(android.support.v7.widget.LinearLayoutManager.VERTICAL);
        viewHolderBody.recyclerView.setLayoutManager(layoutManager);
        BodyItemAdapter bodyItemAdapter=new BodyItemAdapter(context,recycledate,activity);
        viewHolderBody.recyclerView.setAdapter(bodyItemAdapter);
        if(!Util.isInitialize)
            onStratAnimation(viewHolderBody,position);
    }


    @Override
    public int getItemCount() {
        return time.size();
    }

    public class ViewHolderBody extends RecyclerView.ViewHolder {
        private TextView month;
        private TextView date;
        private RecyclerView recyclerView;
        private LinearLayout linearLayout;
        public ViewHolderBody(View itemView) {
            super(itemView);
            month= (TextView) itemView.findViewById(R.id.view_activity_body_month);
            date= (TextView) itemView.findViewById(R.id.view_activity_body_data);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.view_activity_body_recyclerview);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }

    private void onStratAnimation(ViewHolderBody viewHolderBody,int position){
        viewHolderBody.linearLayout.setTranslationX(Util.width * 2);
        viewHolderBody.linearLayout.animate().setStartDelay(400).translationX(0)
                .setStartDelay(700+position*100).setInterpolator(new OvershootInterpolator(1.f))
                .start();
    }
}
