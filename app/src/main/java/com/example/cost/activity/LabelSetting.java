package com.example.cost.activity;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.cost.R;
import com.example.cost.adapter.ColorChooseAdapter;
import com.example.cost.adapter.LabelItemAdapter;
import com.example.cost.contrl.RecyclerItemDivider;
import com.example.cost.datebase.BillDateHelper;

/**
 * 标签设置颜色选择
 *
 */
public class LabelSetting extends BaseActivity{

    private Toolbar toolbar;
    private ImageButton addBtn;
    private RecyclerView recyclerView;
    private BillDateHelper billDateHelper;
    private LabelItemAdapter adapter;
    private int Color=-1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_setting);
        billDateHelper=new BillDateHelper(this,"allbill.db",1);
        init();
        setRecyclerView();
        setToolbar();
        setListener();
    }

    public void init(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        addBtn= (ImageButton) findViewById(R.id.activity_label_addbtn);
        recyclerView= (RecyclerView) findViewById(R.id.activity_label_recycle);
    }

    public void setListener(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupColorChoose();
            }
        });
    }

    public void setupColorChoose(){
        AlertDialog.Builder builder=new AlertDialog.Builder(LabelSetting.this);
        View view= LayoutInflater.
                from(this).inflate(R.layout.view_color_choose,null);
        GridView gridView= (GridView) view.findViewById(R.id.label_color_gridView);
        Button button= (Button) view.findViewById(R.id.label_color_confirm);
        final ColorChooseAdapter adapter=new ColorChooseAdapter(LabelSetting.this,billDateHelper.getAllColors());
        adapter.setListener(new ColorChooseAdapter.ColorListener() {
            @Override
            public void getColor(int color) {
                Color=color;
            }
        });
        gridView.setAdapter(adapter);
        gridView.setVerticalScrollBarEnabled(false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Color!=-1) {
                    billDateHelper.addlabel("新建标签", Color);
                    int id = billDateHelper.getColorID(Color);
                    billDateHelper.deleteColor(id);
                    updateAdapter();
                    dialog.dismiss();
                }
                else
                    Snackbar.make(findViewById(android.R.id.content),
                            "选一个嘛~", Snackbar.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }

    public void setRecyclerView(){
        adapter=new LabelItemAdapter(this,billDateHelper.getLabelColor());
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new RecyclerItemDivider(this));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void updateAdapter(){
        adapter.initData();
        adapter.notifyDataSetChanged();
    }

    public void setToolbar(){
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("标签设置");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
