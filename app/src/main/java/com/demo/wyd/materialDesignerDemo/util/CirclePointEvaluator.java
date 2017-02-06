package com.demo.wyd.materialDesignerDemo.util;

import android.animation.ArgbEvaluator;
import android.animation.TypeEvaluator;

import com.demo.wyd.materialDesignerDemo.bean.CirclePoint;

/**
 * Description :
 * Created by wyd on 2016/12/7.
 */

public class CirclePointEvaluator implements TypeEvaluator<CirclePoint> {
    @Override
    public CirclePoint evaluate(float fraction, CirclePoint startValue, CirclePoint endValue) {
        float startRadius = startValue.getRadius();
        float endRadius = endValue.getRadius();
        int startColor = startValue.getColor();
        int endColor = endValue.getColor();

        float curRadius = (endRadius - startRadius) * fraction + startRadius;
        int curColor = (int) new ArgbEvaluator().evaluate(fraction, startColor, endColor);
        return new CirclePoint(curRadius, curColor);
    }
}

