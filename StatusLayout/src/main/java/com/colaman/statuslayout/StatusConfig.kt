package com.colaman.statuslayout

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.View

/**
 * Create by kyle on 2018/10/17
 * Function : 状态属性
 */
data class StatusConfig(var status: String?,
                   @field:LayoutRes
                   var layoutRes: Int = -1,
                   var view: View? = null,
                   @field:IdRes
                   var clickRes: Int = -1)
