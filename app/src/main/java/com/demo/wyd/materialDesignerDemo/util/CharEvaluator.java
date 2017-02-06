package com.demo.wyd.materialDesignerDemo.util;

import android.animation.TypeEvaluator;

/**
 * Description :我们可以通过重写加速器改变数值进度来改变数值位置，也可以通过改变Evaluator中进度所对应的数值来改变数值位置。
 * Created by wyd on 2016/12/6.
 */

public class CharEvaluator implements TypeEvaluator<Character> {

    /**
     * @param fraction   加速器中的返回值，表示当前动画的数值进度，百分制的小数表示
     * @param startValue ofInt(int start,int end)中的start数值；
     * @param endValue   ofInt(int start,int end)中的end数值；
     * @return 从加速器返回的数字进度转成对应的数字值
     */
    @Override
    public Character evaluate(float fraction, Character startValue, Character endValue) {
        int start = startValue;
        int end = endValue;
        //实现倒序动画
        int current = (int) (end - (end - start) * fraction);
        return (char) current;
    }
}
