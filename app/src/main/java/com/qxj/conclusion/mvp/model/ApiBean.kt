package com.qxj.conclusion.mvp.model

/**
 * 用户信息
 */
class UserBean constructor (var name: String, var age: Int, var sex: String)

/**
 * 位置信息
 */
class LocationBean constructor(var userId: String, var mapId: String, var lat: String, var lng: String, var locationDate: String)