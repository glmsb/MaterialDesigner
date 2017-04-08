package com.demo.wyd.materialDesignerDemo.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.demo.wyd.materialDesignerDemo.R;
import com.demo.wyd.materialDesignerDemo.adapter.RecyclerAdapter;
import com.demo.wyd.materialDesignerDemo.view.DividerItemDecoration;
import com.demo.wyd.materialDesignerDemo.view.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义View（自定义View的实现方式大概可以分为三种，自绘控件、组合控件、以及继承控件）
 * BottomSheetBehavior（design:23.2.0库里面的）
 * android-support-percent库（百分比布局库）
 * 沉浸式模式
 */
public class CustomViewActivity extends AppCompatActivity {
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.frag_4);
        initData();
        initView();
    }

    private void initData() {
        int dataLength = 40;
        data = new ArrayList<>(dataLength);
        for (int i = 0; i < dataLength; i++) {
            data.add("第" + i + "项");
        }
    }

    private void initView() {
        Context mContext = this;
        SwipeRecyclerView recyclerView = (SwipeRecyclerView) findViewById(R.id.swipe_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置增加或删除条目的动画
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider_style));
        RecyclerAdapter adapter = new RecyclerAdapter(mContext, data);
        //如果item为固定高度时，使用这句可以提高效率
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        final BottomSheetDialog sheetDialog = new BottomSheetDialog(mContext);
        View view = getLayoutInflater().inflate(R.layout.delete_button, null);
        sheetDialog.setContentView(view);

        //返回这个View引用的BottomSheetBehavior,作为CoordinatorLayout子View(recyclerView)的LayoutParams
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(recyclerView);
        findViewById(R.id.btn_scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
               /* if (sheetDialog.isShowing()) {
                    sheetDialog.dismiss();
                } else {
                    sheetDialog.show();
                }*/
            }
        });


        /* bottomSheet 的状态监听与回调 */
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.e("onStateChanged", newState + "");
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.e("onSlide", slideOffset + "");
            }
        });
    }

    /**
     * 在所有View绘制完毕的时候才会回调的
     *
     * @param hasFocus 是否获得焦点
     */
    //真正的沉浸式模式
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();  //获取当前界面的DecorView
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //必须和下面UI FLag的配合同时使用
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  //让应用的主体内容占用系统导航栏的空间
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  //会让应用的主体内容占用系统状态栏的空间
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION    //隐藏导航栏
                            | View.SYSTEM_UI_FLAG_FULLSCREEN    //全屏
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
