//package com.qxj.conclusion.mvvm.model
//
//import androidx.databinding.ObservableField
//import androidx.databinding.ObservableInt
//
//class User constructor(var name: ObservableField<String>, var age: ObservableInt, var sex: ObservableInt, var msg: ObservableField<String>) {
//    constructor(name: String = "qxj", age: Int = 0, sex: Int = 1, msg: String = "good")
//            : this(ObservableField(name), ObservableInt(age), ObservableInt(sex), ObservableField(msg))
//
//    fun updateUser(name: String, age: Int, sex: Int, msg: String) {
//        this.name.set(name)
//        this.age.set(age)
//        this.sex.set(sex)
//        this.msg.set(msg)
//    }
//}
//class UserBean constructor (var name: String, var age: Int, var sex: String)
