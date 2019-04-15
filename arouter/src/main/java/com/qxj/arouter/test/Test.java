package com.qxj.arouter.test;

public class Test {
    public static void main(String args[]) {
        Student student = new Student();
        student.setName("赵阳");
        student.setGrade(100);

        System.out.println("姓名：" + student.getName() + ", 成绩：" + student.getGrade());

        Student student1 = new Student("阳阳", 100);

        System.out.println("姓名：" + student1.getName() + ", 成绩：" + student1.getGrade());

        Retangle retangle = new Retangle(10, 5);
        double area = retangle.getArea();
        System.out.println("矩形面积：" + area);

        System.out.println("斐波那契数列的第n个数为：" + getN(6));
    }

    public static int getN(int n) {
        if (n <= 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }
        int n1 = getN(n - 1);
        int n2 = getN(n - 2);
        int x = n1 + n2;
        return x;
    }
}
