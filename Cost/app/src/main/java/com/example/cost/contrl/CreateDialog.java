package com.example.cost.contrl;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.cost.R;
import com.example.cost.Util;
import com.example.cost.adapter.GridviewAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义对话框，添加账本时弹出
 */
//实现账本添加的弹出对话框
public class CreateDialog extends Dialog{

    private EditText editText;//账本名
    private Context context;
    private GridviewAdapter gridviewAdapter;
    private listenerInterface listenerInterface;
    private int cover=-1;
    public CreateDialog(Context context) {
        super(context);
        this.context=context;
    }
    public CreateDialog(Context context,int cover) {
        super(context);
        this.context=context;
        this.cover=cover;
    }
    public interface listenerInterface{
        void onconfrim();
        void oncancel();
    }
    public void setListenerInterface(listenerInterface listenerInterface){
        this.listenerInterface=listenerInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init(){
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_create,null);
        setContentView(view);
        editText= (EditText) view.findViewById(R.id.dia_et);
        GridView gridView = (GridView) view.findViewById(R.id.dia_gv);
        Button confirm = (Button) view.findViewById(R.id.dia_positive_btn);
        Button cancel = (Button) view.findViewById(R.id.dia_negative_btn);

        editText.setSelection(editText.getText().length());
        if(cover==-1)
            gridviewAdapter = new GridviewAdapter(context);
        else
            gridviewAdapter=new GridviewAdapter(context,cover);
        gridView.setAdapter(gridviewAdapter);

        confirm.setOnClickListener(new listener());
        cancel.setOnClickListener(new listener());

        Window dialog=getWindow();
        WindowManager.LayoutParams layoutParams=dialog.getAttributes();
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        layoutParams.width=(int)(displayMetrics.widthPixels*0.8);
        layoutParams.height= Util.dpToPx(240);
        dialog.setAttributes(layoutParams);


    }
    //获取name及cover
    public Map<String,Object> getDate(){
        String name;
        if(editText.getText().length()==0)
            name="新建账本";
        else
            name = editText.getText().toString();
        int covernumber= gridviewAdapter.getCoverposition();
        if(covernumber==-1)
            covernumber=0;
        Map<String, Object> map=new HashMap<>();
        map.put("name",name);
        map.put("cover",covernumber);
        return map;
    }

    public class listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch (id){
                    case R.id.dia_negative_btn: {
                        listenerInterface.oncancel();
                        break;
                    }
                    case R.id.dia_positive_btn:
                        listenerInterface.onconfrim();
                        break;
            }

        }
    }

}
