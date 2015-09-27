package com.example.cost.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cost.Util;
import com.example.cost.datebase.BillDateHelper;
import com.example.cost.R;
import com.example.cost.contrl.CreateDialog;
import java.util.ArrayList;
import java.util.Map;

public class NagivationAdapter extends BaseAdapter {

    private ArrayList<String> namelist = new ArrayList<>();
    private ArrayList<Integer> coverlist = new ArrayList<>();
    private ArrayList<Integer> ID=new ArrayList<>();
    private int[] coverpictures = Util.billCover;
    private BillDateHelper billDateHelper;
    private SQLiteDatabase db;
    private Context context;
    private LayoutInflater layoutInflater;
    private MainChanged mainChanged;
    private TitleChanged titleChanged;
    private viewHolder lastposition;

    public NagivationAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        billDateHelper = new BillDateHelper(context, "allbill.db", 1);
        initBasedate();
    }

    @Override
    public int getCount() {
        return namelist.size()+1;
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
    public int getItemViewType(int position) {
        if(position==0)
            return 0;
        else
            return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position)==0) {

            convertView = layoutInflater.inflate(R.layout.view_nagitivation_addbtn, null);
            ImageButton bt = (ImageButton) convertView.findViewById(R.id.ngv_btn);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CreateDialog createDialog = new CreateDialog(context);
                    createDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    createDialog.show();
                    createDialog.setListenerInterface(new CreateDialog.listenerInterface() {
                        @Override
                        public void onconfrim() {
                            Map map = createDialog.getDate();
                            String name = map.get("name").toString();
                            int cover = Integer.parseInt(map.get("cover").toString());
                            db = billDateHelper.getWritableDatabase();
                            db.execSQL("insert into billdirc values(null,'" + name + "'," + cover + ",0)");
                            initBasedate();
                            context.getSharedPreferences("billselect",
                                    Context.MODE_PRIVATE).edit().putInt("selectedID", ID.get(ID.size()-1))
                                    .commit();
                            mainChanged.changed(ID.get(ID.size()-1));
                            notifyDataSetChanged();
                            createDialog.cancel();
                        }

                        @Override
                        public void oncancel() {
                            createDialog.cancel();
                        }
                    });
                }
            });
        }
        else {
            final viewHolder viewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.view_nagivation_item, null);
                viewHolder = new viewHolder();
                viewHolder.delbutton = (ImageButton) convertView.findViewById(R.id.ngv_item_del);
                viewHolder.modbutton = (ImageButton) convertView.findViewById(R.id.ngv_item_mod);
                viewHolder.itembutton = (ImageButton) convertView.findViewById(R.id.ngv_item_iv);
                viewHolder.title = (TextView) convertView.findViewById(R.id.nav_item_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (NagivationAdapter.viewHolder) convertView.getTag();
            }
            viewHolder.title.setText(namelist.get(position - 1));
            viewHolder.itembutton.setBackgroundColor(context.getResources().getColor(
                    coverpictures[coverlist.get(position - 1)]));
            viewHolder.modbutton.setAlpha(0.0f);
            viewHolder.modbutton.setVisibility(View.INVISIBLE);
            viewHolder.modbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CreateDialog dialog = new CreateDialog(context, coverlist.get(position - 1));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                    dialog.setListenerInterface(new CreateDialog.listenerInterface() {
                        @Override
                        public void onconfrim() {
                            Map map = dialog.getDate();
                            String name = map.get("name").toString();
                            int cover = Integer.parseInt(map.get("cover").toString());
                            modBasedate(name, cover, ID.get(position - 1));
                            initBasedate();
                            notifyDataSetChanged();
                            titleChanged.changed(name);
                            dialog.cancel();
                        }

                        @Override
                        public void oncancel() {
                            dialog.cancel();
                        }
                    });
                }
            });
            viewHolder.delbutton.setAlpha(0.0f);
            viewHolder.delbutton.setVisibility(View.INVISIBLE);
            viewHolder.delbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (namelist.size() == 1) {
                        new AlertDialog.Builder(context).
                                setMessage("这是默认账本不可删除但可重置,需要重置吗")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                modBasedate("新建账本", 0, ID.get(position - 1));
                                db.execSQL("delete from bill where billid=" + ID.get(position - 1));
                                db.execSQL("delete from recycle where billid=" + ID.get(position - 1));
                                mainChanged.changed(ID.get(position - 1));
                                dialog.cancel();
                                initBasedate();
                                notifyDataSetChanged();
                            }
                        }).show();
                    } else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delBasedate(ID.get(position - 1));
                                db.execSQL("delete from bill where billid=" + ID.get(position - 1));
                                db.execSQL("delete from recycle where billid=" + ID.get(position - 1));
                                int pos=context.getSharedPreferences("billselect",
                                        Context.MODE_PRIVATE).getInt("selectedID",1);
                                if(pos==ID.get(position - 1)) {
                                    if (ID.size() == position) {
                                        mainChanged.changed(ID.get(position - 2));
                                        context.getSharedPreferences("billselect",
                                                Context.MODE_PRIVATE).edit().putInt("selectedID", ID.get(position - 2))
                                                .commit();
                                    } else {
                                        mainChanged.changed(ID.get(position));
                                        context.getSharedPreferences("billselect",
                                                Context.MODE_PRIVATE).edit().putInt("selectedID", ID.get(position))
                                                .commit();
                                    }
                                }

                                initBasedate();
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setMessage("您确认要删除吗").create().show();
                    }

                }
                                                    }
            );
                viewHolder.itembutton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(lastposition!=null) {
                                    lastposition.delbutton.setAlpha(0.0f);
                                    lastposition.modbutton.setAlpha(0.0f);
                                    lastposition.modbutton.setVisibility(View.INVISIBLE);
                                    lastposition.delbutton.setVisibility(View.INVISIBLE);
                                }
                                context.getSharedPreferences("billselect",
                                        Context.MODE_PRIVATE).edit().putInt("selectedID", ID.get(position - 1))
                                        .commit();
                                mainChanged.changed(ID.get(position - 1));
                            }
                        }
                );
                viewHolder.itembutton.setOnLongClickListener(
                        new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                if(lastposition!=null) {
                                    lastposition.delbutton.setAlpha(0.0f);
                                    lastposition.modbutton.setAlpha(0.0f);
                                    lastposition.modbutton.setVisibility(View.INVISIBLE);
                                    lastposition.delbutton.setVisibility(View.INVISIBLE);
                                }
                                lastposition=viewHolder;
                                viewHolder.modbutton.setVisibility(View.VISIBLE);
                                viewHolder.delbutton.setVisibility(View.VISIBLE);
                                viewHolder.modbutton.animate().setDuration(200).alpha(1.0f).start();
                                viewHolder.delbutton.animate().setDuration(200).alpha(1.0f).start();

                                return true;
                            }
                        }
                );
            }
        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN&&lastposition!=null) {
                    lastposition.delbutton.setAlpha(0.0f);
                    lastposition.modbutton.setAlpha(0.0f);
                    lastposition.modbutton.setVisibility(View.INVISIBLE);
                    lastposition.delbutton.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
            return convertView;
    }


    public class viewHolder {
        private ImageButton itembutton;
        private ImageButton modbutton;
        private ImageButton delbutton;
        private TextView title;
    }

    public interface MainChanged{
        void changed(int ID);
    }

    public void setMainChanged(MainChanged mainChanged){
        this.mainChanged=mainChanged;
    }

    public interface TitleChanged{
        void changed(String name);
    }

    public void setTitleChanged(TitleChanged titleChanged){
        this.titleChanged=titleChanged;
    }

    public void initBasedate() {
        namelist.clear();
        coverlist.clear();
        ID.clear();
        db = billDateHelper.getWritableDatabase();
        Cursor name = db.rawQuery("select name from billdirc ", null);
        Cursor cover = db.rawQuery("select coverpicture from billdirc ", null);
        Cursor id=db.rawQuery("select _id from billdirc",null);
        for (; name.moveToNext(); name.isAfterLast())
            namelist.add(name.getString(name.getColumnIndex("name")));
        for (; cover.moveToNext(); cover.isAfterLast())
            coverlist.add(cover.getInt(cover.getColumnIndex("coverpicture")));
        for (;id.moveToNext();id.isAfterLast())
            ID.add(id.getInt(id.getColumnIndex("_id")));
        name.close();
        cover.close();
        id.close();
    }

    public void modBasedate(String name, int cover, int position) {
        db = billDateHelper.getWritableDatabase();
        db.execSQL("update billdirc set name ='" + name + "', coverpicture = " +
                  cover + " where _id = " + position );
    }

    public void delBasedate(int position) {
        db = billDateHelper.getWritableDatabase();
        db.execSQL("delete from billdirc where _id = " + position);
    }
}


