package com.demo.wyd.materialDesignerDemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.demo.wyd.materialDesignerDemo.R;
import com.demo.wyd.materialDesignerDemo.adapter.CardViewAdapter;
import com.demo.wyd.materialDesignerDemo.adapter.RecyclerAdapter;
import com.demo.wyd.materialDesignerDemo.view.DividerGridItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Description :{@link CollapsingToolbarLayout }
 * item布局中使用CardView
 * Palette 获取图片颜色值
 * ToolBar ，沉浸式状态栏，（状态栏透明+fitSystemWindow）
 * Created by wyd on 2016/7/20.
 */
public class CtlActivity extends AppCompatActivity {
    private List<String> data;
    private CardViewAdapter adapter;
    private int color = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//继承AppCompatActivity使用
        setContentView(R.layout.aty_ctl);
        initData();
        initView();
    }

    private void initData() {
        int dataLength = 155;
        int randomNum;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒", Locale.getDefault());
        data = new ArrayList<>(dataLength);
        for (int i = 0; i < dataLength; i++) {
            //Math.random()方法是一个可以产生[0.0,1.0]区间内的一个双精度浮点数
            //产生一个10-1000之间的随机数
            randomNum = (int) (10 + Math.random() * 1000);
            String date = dateFormat.format(Calendar.getInstance().getTime());
            data.add(randomNum + "--->" + date);
        }
        adapter = new CardViewAdapter(this, data);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_1);
        setSupportActionBar(toolbar);
        // 给左上角图标的左边加上一个返回的图标
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        CollapsingToolbarLayout ctlRoot = (CollapsingToolbarLayout) findViewById(R.id.ctl_root);
        //在滑动的过程中，会自动变化到该颜色。
        ctlRoot.setContentScrimColor(usePaletteGetRgb());
        //状态栏最终自动变化到该颜色
        ctlRoot.setStatusBarScrimColor(usePaletteGetRgb());

        //竖直方向延伸，共6列
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置增加或删除条目的动画
        //如果item为固定高度时，使用这句可以提高效率
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                data.remove(position);
                adapter.notifyItemRemoved(position);
                Snackbar.make(view, position + " 被删除了", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 通过{@link Palette}来获取bitmap的颜色值
     *
     * @return 颜色值
     */
    private int usePaletteGetRgb() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.imv_scenery);
        Palette.from(bmp).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    color = swatch.getRgb();
                }
            }
        });
        return color;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// 监听菜单项的点击事件
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
