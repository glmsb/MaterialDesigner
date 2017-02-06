package com.demo.wyd.materialDesignerDemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.demo.wyd.materialDesignerDemo.R;

/**
 * Description :继承控件
 * Created by wyd on 2016/9/29.
 */

public class SwipeRecyclerView extends RecyclerView implements View.OnTouchListener {
    private boolean isShowDelete; //是否已经显示Delete按钮
    private int selectedItem;     //选中item的位置
    private ViewGroup itemLayout; //选中item的布局
    private View deleteButton;    //Delete按钮的布局
    private GestureDetector gestureDetector;  //各种手势处理的类

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new MySimpleOnGestureListener());
        setOnTouchListener(this); //注册控件的触摸事件(控件绑定)
    }

    //OnTouchListener中的回调方法
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e("onTouch", "isShowDelete:" + isShowDelete);
        if (isShowDelete) {
            itemLayout.removeView(deleteButton);
            isShowDelete = false;
            deleteButton = null;
            return false;  //事件未被消费，向下传递
        }
        //拦截事件继续往下传递,将捕捉到的MotionEvent交给GestureDetector
        return gestureDetector.onTouchEvent(event);
    }


    /**
     * Description :手势监听接口的适配器，只需重写需要实现的方法
     */
    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        /*****OnGestureListener的函数*****/

        /**
         * 用户按下屏幕就会触发,由1个MotionEvent ACTION_DOWN触发
         */
        //通过点击的坐标计算当前的position
        @Override
        public boolean onDown(MotionEvent e) {
            Log.e("OnGestureListener", "onDown()");
            if (!isShowDelete) {
                //使用布局管理器来获取第一个可见的item的位置
                int mFirstPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                Rect frame = new Rect();
                int count = getChildCount();
                for (int i = count - 1; i >= 0; i--) {
                    View child = getChildAt(i);
                    child.getHitRect(frame);
                    //判断点击坐标是否在某个item的布局内
                    if (frame.contains((int) e.getX(), (int) e.getY())) {
                        Log.e("SwipeRecyclerView", "onDown()--->" + selectedItem);
                        selectedItem = mFirstPosition + i;
                        itemLayout = (ViewGroup) child;
                        break;
                    }
                }
            }
            return false;
        }

        /**
         * 滑屏，用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
         *
         * @param e1        第1个ACTION_DOWN MotionEvent
         * @param e2        最后一个ACTION_MOVE MotionEvent
         * @param velocityX X轴上的移动速度，像素/秒
         * @param velocityY Y轴上的移动速度，像素/秒
         * @return 是否消费事件（是否已处理完毕该事件）
         * <p>
         * 滑屏：手指触动屏幕后，稍微滑动后立即松开
         * onDown-----》onScroll----》onScroll----》onScroll----》………----->onFling
         */
        //当手指快速滑动时，会调用onFling()方法
        @SuppressLint("InflateParams")
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("OnGestureListener", "onFling()");
            if (!isShowDelete && Math.abs(velocityX) > Math.abs(velocityY)) {
                deleteButton = LayoutInflater.from(getContext()).inflate(R.layout.delete_button, null);
                deleteButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, selectedItem + " 被删除", Snackbar.LENGTH_SHORT).show();
                        itemLayout.removeView(deleteButton);
                        deleteButton = null;
                        isShowDelete = false;
                    }
                });
                if (itemLayout != null) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                    //            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            params.addRule(RelativeLayout.CENTER_VERTICAL);
                    //被加载布局的真在属性在这里被指定，因为父布局决定子视图的显示
                    itemLayout.addView(deleteButton, params);
                    isShowDelete = true;
                }
            }
            return true;
        }

        /**
         * 次单独的轻击抬起操作,也就是轻击一下屏幕，立刻抬起来，才会有这个触发，
         * 当然,如果除了Down以外还有其它操作,那就不再算是Single操作了,所以也就不会触发这个事件.
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("OnGestureListener", "onSingleTapUp()");
            return false;
        }

        /**
         * 在屏幕上拖动事件。无论是用手拖动view，或者是以抛的动作滚动，都会多次触发,这个方法在ACTION_MOVE动作发生时就会触发
         * <p>
         * 拖动
         * onDown------》onScroll----》onScroll------》onFiling
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e("OnGestureListener", "onScroll():" + distanceX);
            return false;
        }

        /**
         * 如果是按下的时间超过瞬间，而且在按下的时候没有松开或者是拖动的，那么onShowPress就会执行,拖动了，就不执行onShowPress。
         */
        @Override
        public void onShowPress(MotionEvent e) {
            Log.e("OnGestureListener", "onShowPress()");
        }

        /**
         * 长按触摸屏，超过一定时长，就会触发这个事件
         * <p>
         * 触发顺序：onDown->onShowPress->onLongPress
         */
        @Override
        public void onLongPress(MotionEvent e) {
            Log.e("OnGestureListener", "onLongPress()");
        }


        /*****OnDoubleTapListener的函数*****/

        /**
         * 触发顺序是：OnDown->OnSingleTapUp->OnSingleTapConfirmed
         * <p>
         * onSingleTapUp，只要手抬起就会执行，而对于onSingleTapConfirmed来说，如果双击的话，则onSingleTapConfirmed不会执行。
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e("OnDoubleTapListener", "onSingleTapConfirmed()");
            return super.onSingleTapConfirmed(e);
        }

        /**
         * 双击事件,,在第二下点击时，先触发OnDoubleTap，然后再触发OnDown（第二次点击）
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e("OnDoubleTapListener", "onDoubleTap()");
            return super.onDoubleTap(e);
        }

        /**
         * 双击间隔中发生的动作。指触发onDoubleTap以后，在双击之间发生的其它动作，包含down、up和move事件；
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.e("OnDoubleTapListener", "onDoubleTapEvent()");
            return super.onDoubleTapEvent(e);
        }
    }
}
