package com.demo.wyd.materialDesignerDemo.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

/**
 * Description :内部的原理就是滑动某个可以滑动的View，通知给coordinatorLayout再反馈给FAB
 * Created by wyd on 2016/11/11.
 */
public class FabBehavior extends FloatingActionButton.Behavior {

    private static final int TIME = 3000;
    private static final int MSG_WHAT = 1;

    //加速器实现弹射效果
    private Interpolator foListener = new FastOutLinearInInterpolator();
    //是否正在滑进来
    private boolean isAnimatingIn = false;
    //是否正在滑出去
    private boolean isAnimatingOut = false;
    private FloatingActionButton fab;
    private OnScrollStateChangeListener onScrollStateChangeListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT:
                    animateOut(fab);
                    removeMessages(MSG_WHAT);
                    break;
            }
        }
    };

    //一定要重写这个构造函数。因为CoordinatorLayout源码中parseBehavior()函数中直接反射调用这个构造函数。
    public FabBehavior(Context context, AttributeSet attrs) {
        super();
    }

    public void setOnScrollStateChangeListener(OnScrollStateChangeListener onScrollStateChangeListener) {
        this.onScrollStateChangeListener = onScrollStateChangeListener;
    }

    public static FabBehavior from(FloatingActionButton fab) {
        ViewGroup.LayoutParams layoutParams = fab.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("这个View不是CoordinatorLayout的子View");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (!(behavior instanceof FabBehavior)) {
            throw new IllegalArgumentException("这个View的Behavior不是ScaleDownShowBehavior");
        }
        return ((FabBehavior) behavior);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        if (fab == null) {
            fab = child;
        }
        //nestedScrollAxes滑动关联的方向
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //判断滑动的方向 dyConsumed 某个方向的增量
        if (dyConsumed > 0 && !isAnimatingOut && child.getVisibility() == View.VISIBLE) {
            animateOut(child);

            if (onScrollStateChangeListener != null) {
                onScrollStateChangeListener.stateChange(false);
            }
        } else if (dyConsumed < 0 && !isAnimatingIn) {
            animateIn(child);
            handler.removeMessages(MSG_WHAT);

            if (onScrollStateChangeListener != null) {
                onScrollStateChangeListener.stateChange(true);
            }
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        handler.sendEmptyMessageDelayed(MSG_WHAT, TIME);
    }


    //滑进来
    private void animateIn(FloatingActionButton child) {
        if (child.getVisibility() == View.VISIBLE) {
            return;
        }
        Log.e("animateIn()", "visible");
        //属性动画
        ViewCompat.animate(child)
                .translationY(0)
                .setInterpolator(foListener)
                .setDuration(500)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        view.setVisibility(View.VISIBLE);
                        isAnimatingIn = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        isAnimatingIn = false;
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        isAnimatingIn = false;
                    }
                }).start();
    }

    //滑出去
    private void animateOut(FloatingActionButton child) {
        if (child.getVisibility() != View.VISIBLE) {
            return;
        }
        Log.e("animateOut()", "inVisible");
        Log.e("translationY_out", child.getHeight() + getMarginBottom(child) + "");
        ViewCompat.animate(child)
                .translationY(child.getHeight() + getMarginBottom(child))
                .setInterpolator(foListener)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        isAnimatingOut = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        isAnimatingOut = false;
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        isAnimatingOut = false;
                    }
                }).start();
    }

    private int getMarginBottom(View v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        return ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
    }

    public interface OnScrollStateChangeListener {
        /**
         * @param isScrollUp 是否正在往上滚动
         */
        void stateChange(boolean isScrollUp);
    }
}
