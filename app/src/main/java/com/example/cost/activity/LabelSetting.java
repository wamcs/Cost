package com.example.cost.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.cost.R;
import com.example.cost.adapter.ColorChooseAdapter;
import com.example.cost.adapter.LabelItemAdapter;
import com.example.cost.contrl.RecyclerItemDivider;
import com.example.cost.datebase.BillDataHelper;

/**
 * 标签设置颜色选择
 *
 */
public class LabelSetting extends BaseActivity{

    private Toolbar toolbar;
    private ImageButton addBtn;
    private RecyclerView recyclerView;
    private BillDataHelper billDataHelper;
    private LabelItemAdapter adapter;
    private int Color=-1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_setting);
        billDataHelper =new BillDataHelper(this,"allbill.db",1);
        init();
        setRecyclerView();
        setToolbar();
        setListener();
    }

    public void init(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        addBtn= (ImageButton) findViewById(R.id.activity_label_addbtn);
        recyclerView= (RecyclerView) findViewById(R.id.label_setting_activity_recyclerview);
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
                from(this).inflate(R.layout.layout_color_choose,null);
        GridView gridView= (GridView) view.findViewById(R.id.label_setting_activity_color_gridView);
        Button button= (Button) view.findViewById(R.id.label_setting_activity_color_confirm);
        final ColorChooseAdapter adapter=new ColorChooseAdapter(LabelSetting.this, billDataHelper.getAllColors());
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
                    billDataHelper.addlabel("新建标签", Color);
                    int id = billDataHelper.getColorID(Color);
                    billDataHelper.deleteColor(id);
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
        adapter=new LabelItemAdapter(this, billDataHelper.getLabelColor());
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
