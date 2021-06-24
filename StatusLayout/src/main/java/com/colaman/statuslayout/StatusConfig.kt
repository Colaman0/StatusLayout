package com.colaman.statuslayout

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.View

/**
 * Create by kyle on 2018/10/17
 * Function : 状态属性
 */
data class StatusConfig(
        @field:LayoutRes
        var contentLayoutRes: Int = 0,
        var contentView: View? = null,
        val clickRes: MutableList<Int> = mutableListOf(),
        var autoClick: Boolean = true
)