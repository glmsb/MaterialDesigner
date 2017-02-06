package com.demo.wyd.materialDesignerDemo.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;

import com.demo.wyd.materialDesignerDemo.bean.CirclePoint;
import com.demo.wyd.materialDesignerDemo.util.CharEvaluator;

/**
 * Description :自绘控件（onMeasure-->onLayout-->onDraw）
 * 属性动画
 * Created by wyd on 2016/9/29.
 */

public class CounterView extends View implements View.OnClickListener {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//使位图抗锯齿的标志
    private char count;
    private Rect mBounds;
    private CirclePoint circlePoint;

    public CounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBounds = new Rect();
        setOnClickListener(this);//注册控件的点击事件
    }

    //测量视图的大小，widthMeasureSpec和heightMeasureSpec，这两个值分别用于确定视图的宽度和高度的规格和大小。
    //这两个值都是由父视图经过计算后传递给子视图的，说明父视图会在一定程度上决定子视图的大小。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("CounterView", "onMeasure()");
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                viewHeight = 200;
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        //重新设置宽和高
        setMeasuredDimension(viewWidth, viewHeight);
        Log.e("CounterView", "宽度：" + viewWidth + "  高度：" + viewHeight + " 规格：" + (heightMode == MeasureSpec.AT_MOST ? "AT_MOST" : "EXACTLY"));
    }

    //为了确定视图在布局中所在的位置，而这个操作应该是由布局来完成的，即父视图决定子视图的显示位置.
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e("CounterView", "onLayout()");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("CounterView", "onDraw()");
        if (getBackground() == null) {
            //绘制矩形（背景图）
            mPaint.setColor(Color.BLUE);
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        }

        if (circlePoint != null) {
            mPaint.setColor(circlePoint.getColor());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2 + getLeft(), getHeight() / 2 + getTop(), circlePoint.getRadius(), mPaint);
//            mPaint.reset();
        }

        mPaint.setColor(Color.YELLOW);
        mPaint.setTextSize(130);
        String text = String.valueOf(count);
        //获取文字的宽度和高度
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        float textWidth = mBounds.width();
        float textHeight = mBounds.height();
        //控件的中心坐标在x轴方向往左偏移文字宽度的一半，在Y轴方向往下偏移文字高度的一半
        canvas.drawText(text, getMeasuredWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 2, mPaint);
//        mPaint.reset();
    }

    @Override
    public void onClick(View v) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new CharEvaluator(), 'A', 'Z');
        valueAnimator.setDuration(5000);
        valueAnimator.setStartDelay(1000);
        valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                count = (char) animation.getAnimatedValue();
                //requestLayout();  //重新走一遍完整的绘制流程
                invalidate();  //强制要求视图重绘，即调用onDraw（）
            }
        });
        valueAnimator.start();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(CounterView.this, "CircleRadius", getWidth() / 3);
        objectAnimator.setDuration(6000);
        objectAnimator.setInterpolator(new BounceInterpolator());
        objectAnimator.start();

       /* CirclePoint circlePoint1 = new CirclePoint(getWidth() / 7, Color.BLUE);
        CirclePoint circlePoint2 = new CirclePoint(getWidth() / 3, Color.BLACK);
        CirclePoint circlePoint3 = new CirclePoint(getWidth() / 5, Color.RED);
        ValueAnimator valueAnimator1 = ValueAnimator.ofObject(new CirclePointEvaluator(), circlePoint1, circlePoint2, circlePoint3);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circlePoint = (CirclePoint) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator1.setDuration(6000);
        valueAnimator1.setInterpolator(new BounceInterpolator());
        valueAnimator1.start();*/
    }

    private void setCircleRadius(float radius) {
        if (circlePoint == null) {
            circlePoint = new CirclePoint(0, Color.GREEN);
        }
        circlePoint.setRadius(radius);
        invalidate();
    }

    public float getCircleRadius() {
        if (circlePoint == null)
            return 50.0f;
        return circlePoint.getRadius();
    }
}
