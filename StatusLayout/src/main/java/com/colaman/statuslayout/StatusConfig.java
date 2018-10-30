package com.colaman.statuslayout;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

/**
 * Create by kyle on 2018/10/17
 * Function : 状态属性
 */
public class StatusConfig {
    private String mStatus;
    @LayoutRes
    private int mLayoutRes;
    @IdRes
    private int mClickRes;

    public StatusConfig(String status, int layoutRes, int clickRes) {
        mStatus = status;
        mLayoutRes = layoutRes;
        mClickRes = clickRes;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public int getLayoutRes() {
        return mLayoutRes;
    }

    public void setLayoutRes(int layoutRes) {
        mLayoutRes = layoutRes;
    }

    public int getClickRes() {
        return mClickRes;
    }

    public void setClickRes(int clickRes) {
        mClickRes = clickRes;
    }
}
