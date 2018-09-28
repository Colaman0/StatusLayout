package com.colaman.statuslayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by kyle on 2018/9/28
 * Function : 对不同状态的布局进行管理
 */
public class StatusLayout extends FrameLayout {
    public static final String STATUS_NORMAL = "status_normal";
    private Map<String, View> mLayoutMap = new HashMap<>();
    // 最后一个显示的布局
    private View mLastShowView;
    private Context mContext;
    private long mAnimationDuration = 300;
    private boolean mHaveAnimation = true;
    // 当有动画效果的时候需要标记一下是否正在动画效果中，避免快速切换导致view不显示的bug
    private boolean mIsChanging = false;
    private String mShowType = STATUS_NORMAL;

    public StatusLayout(@NonNull Context context) {
        this(context, null);
    }

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * 加载默认的内容布局
     *
     * @param context
     * @param layoutRes
     * @return
     */
    public StatusLayout defaultInit(Context context, @LayoutRes int layoutRes) {
        View contentView = LayoutInflater.from(context).inflate(layoutRes, this, false);
        mLayoutMap.put(STATUS_NORMAL, contentView);
        mLastShowView = contentView;
        addView(contentView);
        return this;
    }

    /**
     * 设置切换布局的时候是否使用动画
     *
     * @param need
     * @return
     */
    public StatusLayout needAnimation(boolean need) {
        mHaveAnimation = need;
        return this;
    }

    /**
     * 添加一个新的布局
     *
     * @param layoutType     布局的类型，一般定义成常量
     * @param layoutRes      布局layout资源
     * @param isDelayInflate 是否延迟加载，一般像error/empty/networkError这种比较少可能会加载到的布局文件可以设成true
     * @return
     */
    public StatusLayout add(String layoutType, @LayoutRes int layoutRes, boolean isDelayInflate) {
        if (isDelayInflate) {
            ViewStub viewStub = new ViewStub(mContext, layoutRes);
            add(layoutType, viewStub);
        } else {
            add(layoutType, LayoutInflater.from(mContext).inflate(layoutRes, this, false));
        }
        return this;
    }

    /**
     * 添加一个新的布局
     *
     * @param layoutType 布局的类型，一般定义成常量
     * @param layoutView 布局view
     * @return
     */
    public StatusLayout add(String layoutType, View layoutView) {
        layoutView.setVisibility(View.GONE);
        mLayoutMap.put(layoutType, layoutView);
        if (layoutView.getParent() != null) {
            ((ViewGroup) layoutView.getParent()).removeView(layoutView);
        }
        addView(layoutView);
        return this;
    }

    /**
     * 切换布局
     *
     * @param layoutType 需要显示的布局类型，根据add的时候传入的类型来切换
     */
    public void switchLayout(String layoutType) {
        View view = mLayoutMap.get(layoutType);
        if (view != null && mShowType != layoutType && !mIsChanging) {
            mIsChanging = true;
            mShowType = layoutType;
            if (view instanceof ViewStub) {
                view = ((ViewStub) view).inflate();
                mLayoutMap.put(layoutType, view);
            }
            // 隐藏上一个显示的view，并且把mLastShowView改成当前显示的view
            if (mLastShowView != null) {
                goneView(mLastShowView);
                mLastShowView = view;
            }
            // 把需要显示的view显示出来
            visibleView(view);
        }
    }

    /**
     * 显示view
     *
     * @param view
     */
    private void visibleView(final View view) {
        if (mHaveAnimation) {
            view.setAlpha(0f);
            view.setVisibility(View.VISIBLE);
            view.animate()
                    .alpha(1f)
                    .setDuration(mAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mIsChanging = false;
                        }
                    });
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏view
     *
     * @param view
     */
    private void goneView(final View view) {
        if (mHaveAnimation) {
            view.animate().alpha(0f)
                    .setDuration(mAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(GONE);
                            mIsChanging = false;
                        }
                    });
        } else {
            view.setVisibility(GONE);
        }
    }

    /**
     * 设置动画时间持续时长
     * @param animationDuration
     */
    public void setAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }
}
