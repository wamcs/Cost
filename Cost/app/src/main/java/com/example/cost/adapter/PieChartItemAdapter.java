package com.example.cost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cost.R;
import com.example.cost.contrl.CircleImageView;

import java.util.ArrayList;

public class PieChartItemAdapter extends RecyclerView.Adapter
        <PieChartItemAdapter.ChartViewHolder>{
    private Context context;
    private ArrayList<String> labelList;
    private ArrayList<Integer> moneyList;
    private ArrayList<Integer> colorList;
    private ArrayList<Integer> proportion;

    public PieChartItemAdapter(Context context,ArrayList<String> labelList
            ,ArrayList<Integer> moneyList,ArrayList<Integer> colorList
            , ArrayList<Integer> proportion){
        this.context=context;
        this.labelList=labelList;
        this.moneyList=moneyList;
        this.colorList=colorList;
        this.proportion=proportion;
    }
    @Override
    public void onBindViewHolder(ChartViewHolder holder, int position) {
        holder.circleImageView.setImageDrawable(context.getResources()
                .getDrawable(colorList.get(position)));
        holder.titletv.setText(labelList.get(position));
        holder.moneytv.setText(moneyList.get(position).toString());
        holder.proportiontv.setText(proportion.get(position).toString()+"%");
        holder.proportiontv.setTextColor(context.getResources()
                .getColor(colorList.get(position)));
    }


    @Override
    public ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.view_piechart_item,parent,false);
        ChartViewHolder viewHolder=new ChartViewHolder(view);
        viewHolder.titletv= (TextView) view.findViewById(R.id.table_pieitem_title);
        viewHolder.moneytv= (TextView) view.findViewById(R.id.table_pieitem_money);
        viewHolder.proportiontv= (TextView) view.findViewById(R.id.table_pieitem_proportion);
        viewHolder.circleImageView= (CircleImageView) view.findViewById(R.id.table_circle_view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

    class ChartViewHolder extends RecyclerView.ViewHolder{
        private TextView titletv;
        private TextView moneytv;
        private CircleImageView circleImageView;
        private TextView proportiontv;
        public ChartViewHolder(View itemView) {
            super(itemView);
        }
    }
}
