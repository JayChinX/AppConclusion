package com.qxj.conclusion.mvvm.model;//package com.qxj.conclusion.mvvm.model;
//
//import android.databinding.BaseObservable;
//import android.databinding.Bindable;
//
//import com.qxj.conclusion.BR;
//
//public class User extends BaseObservable {
//    private String name;
//    private int age;
//    private int sex;
//    private String msg;
//    public User(String name, int age, int sex, String msg) {
//        this.name = name;
//        this.age = age;
//        this.sex = sex;
//        this.msg = msg;
//    }
//
//    @Bindable
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//        notifyPropertyChanged(BR.name);
//    }
//
//    @Bindable
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//        notifyPropertyChanged(BR.age);
//    }
//
//    @Bindable
//    public int getSex() {
//        return sex;
//    }
//
//    public void setSex(int sex) {
//        this.sex = sex;
//        notifyPropertyChanged(BR.sex);
//    }
//
//    @Bindable
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//        notifyPropertyChanged(BR.msg);
//    }
//}
