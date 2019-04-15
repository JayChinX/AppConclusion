package com.qxj.arouter.test;

public class Retangle {
    double length;
    double wide;

    public Retangle (double length, double wide) {
        this.length = length;
        this.wide = wide;
    }

    public double getArea() {
        return length * wide;
    }
}
