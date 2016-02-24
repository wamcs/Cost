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
import com.example.cost.R;
import com.example.cost.activity.BillWrite;
import com.example.cost.UI.Widget.CircleImageView;
import com.example.cost.datebase.BillDataHelper;

import java.util.ArrayList;

/**
 * shoppinglist的adapter，last的用处在于可编辑状态与不可编辑状态的切换
 */

public class ShopingAdpter extends BaseAdapter {

    private ArrayList<String> namelist;
    private ArrayList<Integer> idlist;
    private Context context;
    private ViewHolder last;
    private int lastposition;
    private BillDataHelper billDataHelper;
    private Connection connection;

    public ShopingAdpter(Context context,ArrayList<String> namelist,ArrayList<Integer> idlist){
        this.context=context;
        this.namelist=namelist;
        this.idlist=idlist;
        billDataHelper =new BillDataHelper(context,"allbill.db",1);
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
                    .inflate(R.layout.item_shooping_list_view, null);
            viewHolder=new ViewHolder();
            viewHolder.editText= (EditText) convertView.
                    findViewById(R.id.item_shopping_list_edittext);
            viewHolder.imageButton= (ImageButton) convertView
                    .findViewById(R.id.item_shopping_list_imagebutton);
            viewHolder.circleImageView= (CircleImageView) convertView
                    .findViewById(R.id.item_shopping_list_radio);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.editText.setText(namelist.get(position));
        viewHolder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (last != null && lastposition != position) {
                    last.editText.setFocusableInTouchMode(false);
                    last.editText.setFocusable(false);
                    last.imageButton.setVisibility(View.INVISIBLE);
                    last.editText.setCursorVisible(false);
                    billDataHelper.updateShopping(last.editText.getText().toString(),
                            idlist.get(lastposition));
                    initData();
                    notifyDataSetChanged();
                }
                last = viewHolder;
                lastposition = position;
                viewHolder.editText.setFocusable(true);
                viewHolder.editText.setFocusableInTouchMode(true);
                viewHolder.editText.setCursorVisible(true);
            }

        });
        viewHolder.editText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (last != null && lastposition != position)
                    last.imageButton.setVisibility(View.INVISIBLE);
                last = viewHolder;
                lastposition = position;
                viewHolder.imageButton.setVisibility(View.VISIBLE);
                return true;
            }
        });
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billDataHelper.deleteShopping(idlist.get(position));
                initData();
                notifyDataSetChanged();
            }
        });
        viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BillWrite.class);
                intent.putExtra("ShoppingName", viewHolder.editText.getText().toString());
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                intent.putExtra("location", location);
                context.startActivity(intent);
                connection.change(idlist.get(position));
            }
        });
        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN&&last!=null&&lastposition<idlist.size()){
                    billDataHelper.updateShopping(last.editText.getText().toString(),
                            idlist.get(lastposition));
                    last.editText.setCursorVisible(false);
                    last.editText.setFocusableInTouchMode(false);
                    last.editText.setFocusable(false);
                    last.imageButton.setVisibility(View.INVISIBLE);
                    initData();
                    notifyDataSetChanged();
                    last=null;
                }
                return false;
            }
        });

        return convertView;
    }

    public class ViewHolder{
        private CircleImageView circleImageView;
        private EditText editText;
        private ImageButton imageButton;
    }

    public void initData(){
        if(!namelist.isEmpty())
            namelist.clear();
        if(!idlist.isEmpty())
            idlist.clear();
        SQLiteDatabase db= billDataHelper.getWritableDatabase();
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
