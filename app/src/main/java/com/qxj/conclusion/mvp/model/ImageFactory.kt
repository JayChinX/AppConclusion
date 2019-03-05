package com.qxj.conclusion.mvp.model

import android.content.Context
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.setImage(context: Context, url: String, @DrawableRes resourceId: Int) {
    Glide.with(context)
            .load(url)
            .placeholder(resourceId)//占位图
            .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用缓存
            .error(resourceId)//异常占位图
            .into(this)
}