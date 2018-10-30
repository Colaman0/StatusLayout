package com.colaman.statuslayout;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewAnimator;

import com.example.statuslayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by kyle on 2018/9/30
 * Function : 管理不同状态下布局显示
 */
public class StatusLayout extends ViewAnimator {
    public static final String LOADING = "loading";
    public static final String EMPTY = "empty";
    public static final String ERROR = "error";

    private static List<StatusConfig> mStatusConfigs = new ArrayList<>();
    private static List<String> mStatusList = new ArrayList<>();
    private static List<View> mViewList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnLayoutClickListener mOnLayoutClickListener;
    private int mCurrentPosition ;
    private boolean mUseDefault = true;
    private boolean mInited = false;

    public StatusLayout(Context context) {
        this(context, null);
    }

    public StatusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    /**
     * 加载默认属性
     */
    private void initDefault() {
        for (StatusConfig config : mStatusConfigs) {
            add(config.getStatus(), config.getLayoutRes(), config.getClickRes());
        }
    }

    /**
     * 设置全局数据
     *
     * @param statusConfigs
     */
    public static void setGlobalData(List<StatusConfig> statusConfigs) {
        mStatusConfigs.addAll(statusConfigs);
    }

    /**
     * 添加一种布局
     *
     * @param config StatusConfig
     * @return
     * @throws RuntimeException
     */
    public StatusLayout add(StatusConfig config) throws RuntimeException {
        if (config == null) {
            throw new NullPointerException("config is null");
        }
        add(config.getStatus(), config.getLayoutRes(), config.getClickRes());
        return this;
    }

    /**
     * 添加一种布局,默认点击整个view回调LayoutClickListener
     *
     * @param layoutRes 布局资源文件
     * @param status    布局所代表的状态
     * @return
     */
    public StatusLayout add(String status, @LayoutRes int layoutRes) throws RuntimeException {
        add(status, layoutRes, 0);
        return this;
    }

    /**
     * 添加一种布局,点击clickRes的view才回调，有的布局类似错误重试是点击一个按钮才触发，就多传一个按钮的id。
     *
     * @param status    布局资源文件
     * @param layoutRes 布局资源文件
     * @param clickRes  需要监听的某个view的id
     * @return
     */
    public StatusLayout add(String status, @LayoutRes int layoutRes, int clickRes) throws RuntimeException {
        int index = mStatusList.indexOf(status);
        if (index >= 0) {
            removeViewAt(index);
        }
        View view = mLayoutInflater.inflate(layoutRes, this, false);
        if (view == null) {
            throw new NullPointerException("layoutRes can't be converted to view ，please check the layoutRes");
        }
        View clickView = view.findViewById(clickRes);
        if (clickView == null) {
            clickView = view;
        }
        clickView.setOnClickListener(getClickListener(view, status));
        addView(view);
        mStatusList.add(status);
        mViewList.add(view);
        return this;
    }

    /**
     * 切换布局，根据status的值来切换相对应类型的布局
     *
     * @param status 布局所代表的状态
     */
    public void switchLayout(String status) {
        if (mUseDefault && !mInited) {
            initDefault();
            mInited = true;
        }
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
        setInAnimation(R.anim.anim_in_alpha);
        setOutAnimation(R.anim.anim_out_alpha);
        return this;
    }

    /**
     * 获取当前显示了第几个布局
     *
     * @return
     */
    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public static void initEmpty(@LayoutRes int layoutRes) {
        mStatusList.add(EMPTY);
    }

    public static void initError(@LayoutRes int layoutRes) {
        mStatusList.add(ERROR);
    }

    public static void initLoading(@LayoutRes int layoutRes) {
        mStatusList.add(LOADING);
    }


    /**
     * get一个点击监听
     *
     * @param status 传入监听布局的status
     * @return
     */
    public OnClickListener getClickListener(final View view, final String status) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLayoutClickListener != null) {
                    mOnLayoutClickListener.OnLayoutClick(view, status);
                }
            }
        };
    }

    /**
     * 设置布局点击的监听
     *
     * @param onLayoutClickListener
     */
    public void setLayoutClickListener(OnLayoutClickListener onLayoutClickListener) {
        mOnLayoutClickListener = onLayoutClickListener;
    }

    /**
     * 布局点击事件的接口
     */
    public interface OnLayoutClickListener {
        /**
         * 布局点击之后的回调方法
         *
         * @param view   add的时候传入的资源文件转换的view，要在回调中做操作可以通过操作view来实现
         * @param status 当前点击的布局代表的status
         */
        void OnLayoutClick(View view, String status);
    }
}
