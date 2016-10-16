package com.wangfuhe.wfh.myproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MainActivity extends Activity {

    private WuziqiPanlAI wuziqiPanlAI;
    private WuziqiPanl wuziqiPanl;
    private RelativeLayout wzqChoice;
    private Button mAI, mNormal;
    private boolean isAI;
    private float startx = 0, endx = 0;
    private ScaleAnimation sato0 = new ScaleAnimation(1, 0, 1, 1,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT,
            0.5f);
    private ScaleAnimation sato1 = new ScaleAnimation(0, 1, 1, 1,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT,
            0.5f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initview();
    }

    private void initview() {
        wuziqiPanl = (WuziqiPanl) findViewById(R.id.wuziqi_panl);
        wuziqiPanlAI = (WuziqiPanlAI) findViewById(R.id.wuziqi_panlAI);
        wzqChoice = (RelativeLayout) findViewById(R.id.wzq_choice_rl);
        mAI = (Button) findViewById(R.id.AI_btn);
        mNormal = (Button) findViewById(R.id.nor_btn);
        mAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wzqChoice.setVisibility(View.GONE);
                wuziqiPanlAI.setVisibility(View.VISIBLE);
                wuziqiPanl.setVisibility(View.GONE);
                isAI = true;
            }
        });
        mNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wzqChoice.setVisibility(View.GONE);
                wuziqiPanlAI.setVisibility(View.GONE);
                wuziqiPanl.setVisibility(View.VISIBLE);
                isAI = false;
            }
        });
        sato0.setDuration(500);
        sato1.setDuration(500);
        sato0.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (wzqChoice.getVisibility() == View.VISIBLE) {
                    wzqChoice.setAnimation(null);
                    if (isAI) {
                        showAI();
                        wuziqiPanlAI.startAnimation(sato1);
                    } else {
                        showNor();
                        wuziqiPanl.startAnimation(sato1);
                    }
                } else {
                    if (isAI) {
                        wuziqiPanlAI.setAnimation(null);
                    }else {
                        wuziqiPanl.setAnimation(null);
                    }
                    showChoice();
                    wzqChoice.startAnimation(sato1);
                }
            }
        });
    }

    private void showChoice() {
        wzqChoice.setVisibility(View.VISIBLE);
        wuziqiPanl.setVisibility(View.GONE);
        wuziqiPanlAI.setVisibility(View.GONE);
    }

    private void showNor() {
        wzqChoice.setVisibility(View.GONE);
        wuziqiPanl.setVisibility(View.VISIBLE);
        wuziqiPanlAI.setVisibility(View.GONE);
    }

    private void showAI() {
        wzqChoice.setVisibility(View.GONE);
        wuziqiPanl.setVisibility(View.GONE);
        wuziqiPanlAI.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                endx = event.getX();
                return true;
            case MotionEvent.ACTION_UP:
                if (endx - startx > 20) {
                    wzqChoice.startAnimation(sato0);
                } else if (startx - endx > 20) {
                    if (isAI) {
                        wuziqiPanlAI.startAnimation(sato0);
                    } else {
                        wuziqiPanl.startAnimation(sato0);

                    }
                }
        }
        return false;
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (wuziqiPanlAI.getVisibility() == View.VISIBLE) {
                wuziqiPanlAI.restart();
            } else if (wuziqiPanl.getVisibility() == View.VISIBLE)
                wuziqiPanl.restart();
            return true;
        } else if (id == R.id.action_regraet) {
            if (wuziqiPanlAI.getVisibility() == View.VISIBLE) {
                wuziqiPanlAI.regraet();
            } else if (wuziqiPanl.getVisibility() == View.VISIBLE)
                wuziqiPanl.regraet();
        }
        return super.onOptionsItemSelected(item);
    }

}
