package com.demo.wyd.materialDesignerDemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.demo.wyd.materialDesignerDemo.R;
import com.demo.wyd.materialDesignerDemo.adapter.RecyclerAdapter;
import com.demo.wyd.materialDesignerDemo.view.DividerItemDecoration;
import com.demo.wyd.materialDesignerDemo.view.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :android-support-percent库（百分比布局库）
 * 自定义View（自定义View的实现方式大概可以分为三种，自绘控件、组合控件、以及继承控件）
 * Created by wyd on 2016/7/20.
 */
public class Fragment4 extends Fragment {
    private View rootView;
    private List<String> data;
    private int lastX, lastY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_4, container, false);
            initData();
            initView();
        } else {
            ViewGroup p = (ViewGroup) rootView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }
        return rootView;
    }

    private void initData() {
        int dataLength = 40;
        data = new ArrayList<>(dataLength);
        for (int i = 0; i < dataLength; i++) {
            data.add("第" + i + "项");
        }
    }

    private void initView() {
        dealScrollView();

        Context mContext = getContext();
        SwipeRecyclerView recyclerView = (SwipeRecyclerView) rootView.findViewById(R.id.swipe_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置增加或删除条目的动画
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL, 6, R.color.c_emphasize_blue));
        RecyclerAdapter adapter = new RecyclerAdapter(mContext, data);
        //如果item为固定高度时，使用这句可以提高效率
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 滑动View的几种实现方式
     */
    private void dealScrollView() {
//        Button btnScrollView = (Button) rootView.findViewById(R.id.btn_scroll);
        View btnScrollView = rootView.findViewById(R.id.btn_count_view);
        PercentRelativeLayout percentLayout = (PercentRelativeLayout) rootView.findViewById(R.id.pl_percent_layout);
        percentLayout.scrollTo(0, -(300));
        /* btnScrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getActivity().findViewById(R.id.cl_root), "clickAble", Snackbar.LENGTH_SHORT).show();
            }
        });*/
        btnScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //获取触摸事件距离控件左边和顶部的距离
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    //只有前一个action返回true，才会触发后一个action
                    case MotionEvent.ACTION_DOWN:
                        Log.i("event", "ACTION_DOWN");
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("event", "ACTION_MOVE");
                        // 计算移动的距离
                        int offSetX = x - lastX;
                        int offSetY = y - lastY;

                        //获取View自身左边到其父布局左边的距离 --> v.getLeft()
                        //方式一--> 调用View的layout方法来重新放置它的位置
//                        v.layout(v.getLeft() + offSetX, v.getTop() + offSetY, v.getRight() + offSetX, v.getBottom() + offSetY);

                        //方式二--> 调用View的layout方法来重新放置它的位置
//                        v.offsetLeftAndRight(offSetX);
//                        v.offsetTopAndBottom(offSetY);

                        //方式三--> 改变view所在的父布局的布局参数
//                        RelativeLayout.LayoutParams layoutParams = ((RelativeLayout.LayoutParams) v.getLayoutParams());
//                        layoutParams.leftMargin = v.getLeft() + offSetX;
//                        layoutParams.topMargin = v.getTop() + offSetY;
//                        v.setLayoutParams(layoutParams);

                        //方式四--> 移动view所在的父布局中所有的子view
                        //将偏移量设置为负值
                        ((View) v.getParent()).scrollBy(-offSetX, -offSetY);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.i("event", "ACTION_CANCEL");
                    case MotionEvent.ACTION_UP:
                        Log.i("event", "ACTION_UP");
                        ((ViewGroup) v.getParent()).scrollTo(0, 0);
                        break;
                }
                //true 表示事件已经被消费，不会再执行onTouchEvent()方法，而且onClick()方法也不会得到执行，因为它是在onTouchEvent()方法中调用的
                return false;
            }
        });
    }
}
