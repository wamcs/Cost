package com.example.cost.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.contrl.CircleImageView;

public class ChooseAdapter extends BaseAdapter{
    private Context context;
    private final int mainColor= R.color.main_color;
    private final int shallowColor=R.color.shallow_color;
    private CircleImageView lastposition;
    private positionListener listener;
    public ChooseAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {return Util.colors.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewHolder viewholder;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.view_choose_item, parent, false);
            viewholder = new viewHolder();
            viewholder.imageview = (CircleImageView)
                    convertView.findViewById(R.id.lable_choose_item);
            convertView.setTag(viewholder);
        }
        else{
            viewholder= (viewHolder) convertView.getTag();
        }
            viewholder.imageview.setImageDrawable(context.getResources().getDrawable(Util.colors[position]));
            viewholder.imageview.setBackgroundColor(context.getResources().getColor(mainColor));
            viewholder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastposition != null)
                        lastposition.setBackgroundColor(context.getResources().
                                getColor(mainColor));
                    viewholder.imageview.setBackgroundColor(context.getResources().
                            getColor(shallowColor));
                    lastposition = viewholder.imageview;
                    listener.getPosition(position);
                }
            });

        return convertView;
    }

    public class viewHolder{
        public CircleImageView imageview;
    }

    public interface positionListener{
        void getPosition(int position);
    }

    public void setListener(positionListener listener){
        this.listener=listener;
    }
}
