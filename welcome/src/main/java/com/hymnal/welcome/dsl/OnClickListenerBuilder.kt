package com.hymnal.welcome.dsl

import android.view.View

class OnClickListenerBuilder {

    var onClickAction: ((View) -> Unit)? = null

    fun onClick(action: (View) -> Unit) {
        this.onClickAction = action
    }
}

fun View.setOnDataClickListener(action: OnClickListenerBuilder.() -> Unit) {
    setOnClickListener(OnClickListenerBuilder().apply (action).let { builder ->
        View.OnClickListener {
            builder.onClickAction?.invoke(it)
        }
    })
}

