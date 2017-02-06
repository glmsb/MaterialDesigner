package com.demo.wyd.materialDesignerDemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

import com.demo.wyd.materialDesignerDemo.R;
import com.demo.wyd.materialDesignerDemo.adapter.RecyclerAdapter;
import com.demo.wyd.materialDesignerDemo.util.FabBehavior;
import com.demo.wyd.materialDesignerDemo.view.DividerItemDecoration;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Description :{@link NestedScrollView }
 * {@link RecyclerView}
 * Created by wyd on 2016/7/20.
 */
public class Fragment3 extends Fragment {
    private View rootView;
    private List<String> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_3, container, false);
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
        Map<String, String> dateMap = new HashMap<>();

        int dataLength = 50;
        int randomNum;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒", Locale.getDefault());
        data = new ArrayList<>(dataLength);
        for (int i = 0; i < dataLength; i++) {
            //Math.random()方法是一个可以产生[0.0,1.0]区间内的一个双精度浮点数
            //产生一个10-1000之间的随机数
            randomNum = (int) (10 + Math.random() * 1000);
            String date = dateFormat.format(Calendar.getInstance().getTime());
            data.add(randomNum + "--->" + date);

            dateMap.put("random_date", date);
            //id  为事件ID,map  为当前事件的属性和取值,du  为当前事件的数值为当前事件的数值
            MobclickAgent.onEventValue(getContext(), "randomNum", dateMap, randomNum);
        }
    }

    private void initView() {
        final Context mContext = getContext();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(8, LinearLayoutManager.HORIZONTAL);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置增加或删除条目的动画
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL, 6, R.color.c_emphasize_blue));
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL, R.drawable.recycler_divider_style));
        final RecyclerAdapter adapter = new RecyclerAdapter(mContext, data);
        //如果item为固定高度时，使用这句可以提高效率
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                data.remove(position);
                adapter.notifyItemRemoved(position);
                //必须以snackBar弹出的父CoordinatorLayout作为相对的view
                CoordinatorLayout parentCLRoot = (CoordinatorLayout) getActivity().findViewById(R.id.cl_root);
                Snackbar.make(parentCLRoot, position + " 被删除了", Snackbar.LENGTH_LONG).show();
            }
        });

//        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
//        View view = LayoutInflater.from(mContext).inflate(R.layout.delete_button, null);
//        bottomSheetDialog.setContentView(view);
//        bottomSheetDialog.show();
        FabBehavior fabBehavior = FabBehavior.from(((FloatingActionButton) getActivity().findViewById(R.id.fab_button)));
        fabBehavior.setOnScrollStateChangeListener(new FabBehavior.OnScrollStateChangeListener() {
            @Override
            public void stateChange(boolean isScrollUp) {
               /* if (isScrollUp) {
                    bottomSheetDialog.show();
                } else {
                    bottomSheetDialog.dismiss();
                }*/
            }
        });

        buildLayoutAnimation(recyclerView);
    }

    // 实现布局动画
    private void buildLayoutAnimation(ViewGroup recyclerView) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -0.4f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0);
        translateAnimation.setDuration(500);//这里必须设置时间，否则下面的动画时间没有参照，就不会执行动画
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(translateAnimation, 0.3f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.startLayoutAnimation();
    }
}
