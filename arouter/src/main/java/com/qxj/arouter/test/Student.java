package com.qxj.arouter.test;

public class Student {
    String name;
    double grade;

    public Student() {

    }

    public Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getGrade() {
        return this.grade;
    }
}
