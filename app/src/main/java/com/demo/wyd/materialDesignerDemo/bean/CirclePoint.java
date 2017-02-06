package com.demo.wyd.materialDesignerDemo.bean;

/**
 * 小圆点类
 */
public class CirclePoint {
    private float radius;
    private int color;

    public CirclePoint(float radius, int color) {
        this.radius = radius;
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}