package com.example.cost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.example.cost.R;
import com.example.cost.Util;

import java.util.ArrayList;

public class GridviewAdapter extends BaseAdapter{

    private int[] cover=Util.billCover;//封面图案集合
    private int[] checked=Util.billCoverSure;//点击后图案集合
    private ArrayList<Integer> ischecked;
    private LayoutInflater layoutInflater;
    private Context context;
    private int lastposition=0;//被选中图案，默认为第一个
    private int coverposition=-1;

    public GridviewAdapter(Context context){
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
        ischecked = new ArrayList<>();
        ischecked.add(1);
        for(int i=0;i<4;i++)
            ischecked.add(0);
    }
    public GridviewAdapter(Context context,int lastposition){
        this.context=context;
        this.lastposition=lastposition;
        this.coverposition=lastposition;
        this.layoutInflater=LayoutInflater.from(context);
        ischecked = new ArrayList<>();
        for(int i=0;i<5;i++)
            ischecked.add(0);
        ischecked.set(lastposition,1);
    }
    @Override
    public int getCount() {
        return cover.length;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        viewHolder viewHolder ;
        //优化
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.item_book_list_dialog,
                    null);
            viewHolder=new viewHolder();
            viewHolder.itembtn= (ImageButton) convertView.findViewById(R.id.item_book_list_dialog_button);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder= (GridviewAdapter.viewHolder) convertView.getTag();
        //选择。如果被选中封面设为选中封面，没有设为普通封面
        if(ischecked.get(position)==0){
            viewHolder.itembtn.setBackgroundColor(context.getResources().
                    getColor(cover[position]));
        }
        else {
            viewHolder.itembtn.setBackgroundColor(context.getResources().
                    getColor(checked[position]));
        }
        //设置监听，改变选择状态
        viewHolder.itembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ischecked.set(lastposition,0);
                ischecked.set(position,1);
                lastposition=position;
                coverposition=position;
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public final class viewHolder{
       public ImageButton itembtn;
    }
    public int getCoverposition(){
        return coverposition;}
}
