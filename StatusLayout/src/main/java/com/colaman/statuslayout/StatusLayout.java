package com.colaman.statuslayout;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ViewAnimator;

import com.example.statuslayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by kyle on 2018/9/30
 * Function : 管理不同状态下布局显示
 */
public class StatusLayout extends ViewAnimator {
    private List<String> mStatusList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mCurrentPosition = 0;

    public StatusLayout(Context context) {
        this(context, null);
    }

    public StatusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 添加一种布局
     *
     * @param layoutRes 布局资源文件
     * @param status    布局所代表的状态
     * @return
     */
    public StatusLayout add(String status, @LayoutRes int layoutRes) {
        addView(mLayoutInflater.inflate(layoutRes, this, false));
        mStatusList.add(status);
        return this;
    }

    /**
     * 切换布局，根据status的值来切换相对应类型的布局
     *
     * @param status 布局所代表的状态
     */
    public void switchLayout(String status) {
        int index = mStatusList.indexOf(status);
        if (index < 0 || index + 1 == mCurrentPosition) {
            return;
        }
        setDisplayedChild(index + 1);
        mCurrentPosition = index + 1;
    }

    /**
     * 切换到默认的内容，即布局资源文件中添加的内容
     */
    public void showDefaultContent() {
        if (mCurrentPosition == 0) {
            return;
        }
        setDisplayedChild(0);
        mCurrentPosition = 0;
    }

    /**
     * 显示布局时的动画效果
     *
     * @param animationRes 动画资源文件
     * @return
     */
    public StatusLayout setInAnimation(int animationRes) {
        setInAnimation(mContext, animationRes);
        return this;
    }

    /**
     * 隐藏布局时的动画效果
     *
     * @param animationRes 动画资源文件
     * @return
     */
    public StatusLayout setOutAnimation(int animationRes) {
        setOutAnimation(mContext, animationRes);
        return this;
    }

    /**
     * 设置默认的的动画效果
     *
     * @return
     */
    public StatusLayout setDefaultAnimation() {
        setInAnimation(R.anim.anim_in);
        setOutAnimation(R.anim.anim_out);
        return this;
    }

    /**
     * 获取当前显示了第几个布局，按add顺序来排序
     * @return
     */
    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
