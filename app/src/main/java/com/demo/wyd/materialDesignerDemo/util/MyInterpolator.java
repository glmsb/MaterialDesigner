package com.demo.wyd.materialDesignerDemo.util;

import android.animation.TimeInterpolator;

/**
 * Description :自定义插值器
 * Created by wyd on 2016/12/6.
 */

public class MyInterpolator implements TimeInterpolator {

    /**
     * input参数与任何我们设定的值没关系，只与时间有关，随着时间的增长，动画的进度也自然的增加.
     *
     * @param input 当前动画的进度
     * @return 动画的当前数值进度
     */
    @Override
    public float getInterpolation(float input) {
        //倒序动画
        return 1 - input;
    }
}
