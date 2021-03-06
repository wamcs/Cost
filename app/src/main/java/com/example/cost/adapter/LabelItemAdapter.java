package com.example.cost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.cost.R;
import com.example.cost.datebase.BillDataHelper;

import java.util.ArrayList;
import java.util.Map;

public class LabelItemAdapter extends RecyclerView.Adapter<LabelItemAdapter.ViewHolder>{

    private ArrayList<Map<String,Object>> arrayList;
    private Context context;
    private LabelItemAdapter.ViewHolder last;
    private int lastposition;
    private String lastlabel;
    private BillDataHelper billDataHelper;


    public LabelItemAdapter(Context context,ArrayList<Map<String,Object>> arrayList){
        this.context=context;
        this.arrayList=arrayList;
        billDataHelper =new BillDataHelper(context,"allbill.db",1);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        view= LayoutInflater.from(context).inflate(R.layout.item_label_show,parent,false);
        viewHolder=new ViewHolder(view);
        parent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && last != null) {
                        billDataHelper.updatelabel(last.editText.getText().toString(),
                                billDataHelper.getlabelID(lastlabel));
                        billDataHelper.modificateLabel(lastlabel, last.editText.getText().toString());
                        last.editText.setFocusableInTouchMode(false);
                        last.editText.setFocusable(false);
                        last.editText.setCursorVisible(false);
                        initData();
                        notifyDataSetChanged();
                        last = null;
                    }
                    return false;
                }
            });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imageView.setBackgroundColor(context
                .getResources().getColor(Integer.parseInt
                        (arrayList.get(position).get("color").toString())));
        holder.editText.setText(arrayList.get(position).get("label").toString());
        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (last != null && lastposition != position) {
                    last.editText.setFocusableInTouchMode(false);
                    last.editText.setFocusable(false);
                    last.editText.setCursorVisible(false);
                    billDataHelper.updatelabel(last.editText.getText().toString(),
                            billDataHelper.getlabelID(lastlabel));
                    billDataHelper.modificateLabel(lastlabel, last.editText.getText().toString());
                    initData();
                    notifyDataSetChanged();
                }
                last = holder;
                lastposition = position;
                lastlabel = holder.editText.getText().toString();
                holder.editText.setFocusable(true);
                holder.editText.setFocusableInTouchMode(true);
                holder.editText.setCursorVisible(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void initData(){
        if(arrayList!=null)
            arrayList.clear();
        arrayList= billDataHelper.getLabelColor();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private EditText editText;
        public ViewHolder(final View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.label_setting_activity_label_color);
            editText= (EditText) itemView.findViewById(R.id.label_setting_activity_label_edittext);

        }
    }

}
