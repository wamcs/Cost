package com.example.cost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cost.R;

import java.util.ArrayList;
import java.util.Map;
import android.widget.TextView;

/**
 * billwrite类中label点击后弹出窗口的adapter，回调方法返回label名
 */
    public class LabelChooseAdapter extends RecyclerView.Adapter<LabelChooseAdapter.ViewHolder>{

        private ArrayList<Map<String,Object>> arrayList;
        private Context context;
        private labelListener listener;

        public LabelChooseAdapter(Context context,ArrayList<Map<String,Object>> arrayList){
            this.context=context;
            this.arrayList=arrayList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            ViewHolder viewHolder;
            view= LayoutInflater.from(context).inflate(R.layout.view_label_choose_items,parent,false);
            viewHolder=new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.imageView.setBackgroundColor(context
                    .getResources().getColor(Integer.parseInt
                            (arrayList.get(position).get("color").toString())));
            holder.textView.setText(arrayList.get(position).get("label").toString());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.getLabel(arrayList.get(position).get("label").toString());
                    }
            });


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView imageView;
            private TextView textView;
            private LinearLayout linearLayout;
            public ViewHolder(View itemView) {
                super(itemView);
                imageView= (ImageView) itemView.findViewById(R.id.label_color);
                textView= (TextView) itemView.findViewById(R.id.label_text);
                linearLayout= (LinearLayout) itemView.findViewById(R.id.label_layout);
            }
        }
        public interface labelListener{
            void getLabel(String label);
        }

        public void setListener(labelListener listener){
            this.listener=listener;
        }
    }

