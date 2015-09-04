package com.example.cost.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.example.cost.R;
import com.example.cost.activity.BillWrite;
import com.example.cost.datebase.BillDateHelper;

import java.util.ArrayList;

public class ShopingAdpter extends BaseAdapter {

    private ArrayList<String> namelist;
    private ArrayList<Integer> idlist;
    private Context context;
    private ViewHolder last;
    private int lastposition;
    private BillDateHelper billDateHelper;
    private Connection connection;

    public ShopingAdpter(Context context,ArrayList<String> namelist,ArrayList<Integer> idlist){
        this.context=context;
        this.namelist=namelist;
        this.idlist=idlist;
        billDateHelper=new BillDateHelper(context,"allbill.db",1);
    }
    @Override
    public int getCount() {
        return namelist.size();
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
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context)
                    .inflate(R.layout.view_shoppinglist_item, null);
            viewHolder=new ViewHolder();
            viewHolder.editText= (EditText) convertView.
                    findViewById(R.id.view_shoppinglist_edittext);
            viewHolder.imageButton= (ImageButton) convertView
                    .findViewById(R.id.view_shoppinglist_imagebutton);
            viewHolder.radioButton= (RadioButton) convertView
                    .findViewById(R.id.view_shoppinglist_radio);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.editText.setText(namelist.get(position));
        viewHolder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(last!=null&&lastposition!=position){
                    last.editText.setFocusableInTouchMode(false);
                    last.editText.setFocusable(false);
                    last.imageButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        viewHolder.editText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(last!=null){
                    last.editText.setFocusableInTouchMode(false);
                    last.editText.setFocusable(false);
                    last.imageButton.setVisibility(View.INVISIBLE);
                }
                last=viewHolder;
                lastposition=position;
                viewHolder.editText.setFocusable(true);
                viewHolder.editText.setFocusableInTouchMode(true);
                viewHolder.imageButton.setVisibility(View.VISIBLE);
                return true;
            }
        });
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billDateHelper.deleteShopping(idlist.get(position));
                initData();
                notifyDataSetChanged();
            }
        });
        viewHolder.radioButton.setChecked(false);
        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, BillWrite.class);
                intent.putExtra("ShoppingName",viewHolder.editText.getText().toString());
                int[] location=new int[2];
                v.getLocationOnScreen(location);
                intent.putExtra("location", location);
                context.startActivity(intent);
                connection.change(idlist.get(position));
            }
        });
        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN&&last!=null){
                    billDateHelper.updateShopping(last.editText.getText().toString(),idlist.get(lastposition));
                    last.editText.setFocusableInTouchMode(false);
                    last.editText.setFocusable(false);
                    last.imageButton.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        return convertView;
    }

    public class ViewHolder{
        private RadioButton radioButton;
        private EditText editText;
        private ImageButton imageButton;
    }

    public void initData(){
        if(!namelist.isEmpty())
            namelist.clear();
        if(!idlist.isEmpty())
            idlist.clear();
        SQLiteDatabase db=billDateHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from shopping",null);
        for(;cursor.moveToNext();cursor.isAfterLast()){
            namelist.add(cursor.getString(cursor.getColumnIndex("name")));
            idlist.add(cursor.getInt(cursor.getColumnIndex("_id")));
        }
        cursor.close();
    }



    public interface Connection{
        void change(int id);
    }

    public void setConnection(Connection connection){
        this.connection=connection;
    }
}
