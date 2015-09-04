package com.example.cost.adapter;

import android.app.Activity;
import android.content.Context;

import com.example.cost.Util;
import com.example.cost.contrl.Decoration;
import com.example.cost.contrl.LinerLayoutManager;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import com.example.cost.R;

import java.util.ArrayList;
import java.util.Map;


public class BillAdater extends RecyclerView.Adapter<BillAdater.ViewHolderBody>{

    private Map<Integer,ArrayList<Map<String,Object>>> date;
    private Context context;
    private ArrayList<Integer> time;
    private Activity activity;
    public BillAdater(Context context,ArrayList<Integer> time,
                      Map<Integer,ArrayList<Map<String,Object>>> date,Activity activity){
        this.context=context;
        this.date=date;
        this.time=time;
        this.activity=activity;
    }

    @Override
    public ViewHolderBody onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolderBody viewHolder;
        view=LayoutInflater.from(context).inflate(R.layout.view_activity_body,null);
        viewHolder=new ViewHolderBody(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBody viewHolderBody, int position) {
        ArrayList<Map<String, Object>> recycledate=date.get(time.get(position));
        int date=time.get(position)%100;
        int month=(time.get(position)%10000)/100;
        viewHolderBody.month.setText(month+"月");
        viewHolderBody.date.setText(date+"");
        LinerLayoutManager linearLayoutManager=new LinerLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewHolderBody.recyclerView.setLayoutManager(linearLayoutManager);
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
        private CardView cardView;
        public ViewHolderBody(View itemView) {
            super(itemView);
            month= (TextView) itemView.findViewById(R.id.view_activity_body_month);
            date= (TextView) itemView.findViewById(R.id.view_activity_body_data);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.view_activity_body_recyclerview);
            cardView= (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    private void onStratAnimation(ViewHolderBody viewHolderBody,int position){
        viewHolderBody.cardView.setTranslationX(Util.width*2);
        viewHolderBody.cardView.animate().setStartDelay(400).translationX(0)
                .setStartDelay(700+position*100).setInterpolator(new OvershootInterpolator(1.f))
                .start();
    }
}
