package com.wangfuhe.wfh.myproject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;





public class MainActivity extends Activity {

    private WuziqiPanlAI wuziqiPanlAI;
    private WuziqiPanl wuziqiPanl;
    Button mAI,mNormal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        wuziqiPanl = (WuziqiPanl) findViewById(R.id.wuziqi_panl);
        wuziqiPanlAI = (WuziqiPanlAI) findViewById(R.id.wuziqi_panlAI);
        mAI= (Button) findViewById(R.id.AI_btn);
        mNormal= (Button) findViewById(R.id.nor_btn);
        mAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wuziqiPanlAI.setVisibility(View.VISIBLE);
                wuziqiPanl.setVisibility(View.GONE);
            }
        });
        mNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wuziqiPanlAI.setVisibility(View.GONE);
                wuziqiPanl.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            if(wuziqiPanlAI.getVisibility()==View.VISIBLE){
                wuziqiPanlAI.restart();
            }else if(wuziqiPanl.getVisibility()==View.VISIBLE)
                wuziqiPanl.restart();
            return true;
        }else if(id==R.id.action_regraet) {
            if(wuziqiPanlAI.getVisibility()==View.VISIBLE){
                wuziqiPanlAI.regraet();
            }else if(wuziqiPanl.getVisibility()==View.VISIBLE)
                wuziqiPanl.regraet();
        }
        return super.onOptionsItemSelected(item);
    }

}
