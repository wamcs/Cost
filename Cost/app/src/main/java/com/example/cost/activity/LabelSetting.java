package com.example.cost.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.example.cost.datebase.BillDateHelper;

public class LabelSetting extends BaseActivity{

    private Toolbar toolbar;
    private ImageButton addBtn;
    private RecyclerView recyclerView;
    private BillDateHelper billDateHelper;
    private int color;

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
                from(this).inflate(R.layout.view_label_item_show,null);
        GridView gridView= (GridView) view.findViewById(R.id.label_color_gridView);
        Button button= (Button) view.findViewById(R.id.label_color_confirm);
        ColorChooseAdapter adapter=new ColorChooseAdapter(this,billDateHelper.getLabelColor());
        adapter.setListener(new ColorChooseAdapter.ColorListener() {
            @Override
            public void getColor(int c) {
                color=c;
            }
        });
        gridView.setAdapter(adapter);
        builder.setView(view);
        final Dialog dialog=builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billDateHelper.addlabel("新建标签",color);
                setRecyclerView();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void setRecyclerView(){
        LabelItemAdapter adapter=new LabelItemAdapter(this,billDateHelper.getLabelColor());
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void setToolbar(){
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_36dp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
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
