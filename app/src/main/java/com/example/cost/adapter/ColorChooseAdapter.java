package com.example.cost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cost.R;


import java.util.ArrayList;

/**
 * label颜色自定义界面中gridview的adapter，
 * 加了回调方法用于在选择确认后的颜色消除
 */

public class ColorChooseAdapter extends BaseAdapter {

    private ArrayList<Integer> arrayList;
    private Context context;
    private final int mainColor= R.color.main_color_show;
    private final int shallowColor=R.color.shallow_color_show;
    private LinearLayout lastposition;
    private ColorListener listener;


    public ColorChooseAdapter(Context context,ArrayList<Integer> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
                    inflate(R.layout.item_color_choose, parent, false);
            viewholder = new viewHolder();
            viewholder.imageView =
                    (ImageView) convertView.findViewById(R.id.label_setting_activity_choose_color_item);
            viewholder.linearLayout=
                    (LinearLayout) convertView.findViewById(R.id.label_setting_activity_choose_layout_item);
            convertView.setTag(viewholder);
        }
        else{
            viewholder= (viewHolder) convertView.getTag();
        }
        viewholder.imageView.setBackgroundColor(context
                .getResources().getColor(arrayList.get(position)));
        viewholder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastposition != null)
                    lastposition.setBackgroundColor(context.getResources().
                            getColor(mainColor));
                viewholder.linearLayout.setBackgroundColor(context.getResources().
                        getColor(shallowColor));
                lastposition = viewholder.linearLayout;
                listener.getColor(arrayList.get(position));
            }
        });

        return convertView;
    }

    class viewHolder{
        public ImageView imageView;
        public LinearLayout linearLayout;
    }

    public interface ColorListener{
        void getColor(int color);
    }

    public void setListener(ColorListener listener){
        this.listener=listener;
    }
}
