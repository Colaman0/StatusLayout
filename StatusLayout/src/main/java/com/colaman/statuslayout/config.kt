package com.colaman.statuslayout

/**
 *
 * Author   : kyle
 *
 * Date     : 2021/6/24
 *
 * Function :
 *
 */

sealed class Status {
    object Empty : Status()
    object Normal : Status()
    object Loading : Status()
    object Error : Status()
}
