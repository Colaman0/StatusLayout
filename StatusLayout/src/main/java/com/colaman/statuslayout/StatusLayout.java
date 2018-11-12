package com.colaman.statuslayout;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.example.statuslayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by kyle on 2018/9/30
 * Function : 管理不同状态下布局显示
 */
public class StatusLayout extends ViewAnimator {
    // 全局状态布局的属性值
    private static List<StatusConfig> mStatusConfigs = new ArrayList<>();
    // 存放不同状态的布局对应的数据
    private SparseArray<String> mStatusArrays = new SparseArray<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    // 布局点击listener
    private OnLayoutClickListener mOnLayoutClickListener;
    // 当前显示view在viewgroup中的index
    private int mCurrentPosition;
    // 是否使用全局设置的属性
    private boolean mUseGlobal = true;
    // 是否已经加载过全局设置属性
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
     * 用于activity/fragment等view的初始化方式使用，在布局文件中可以不用手动把根部局替换成statuslayout,
     * 而是调用init方法把资源res传进来，返回一个statuslayout,直接把返回的statuslayout作为activity setcontentview()方法的参数
     *
     * @param context   上下文
     * @param layoutRes 布局资源文件
     * @return
     */
    public static StatusLayout init(Context context, @LayoutRes int layoutRes) {
        View rootView = LayoutInflater.from(context).inflate(layoutRes, null);
        return init(rootView);
    }

    /**
     * 用于activity/fragment等view的初始化方式使用，在布局文件中可以不用手动把根部局替换成statuslayout,
     * 而是调用init方法把资源res传进来，返回一个statuslayout,直接把返回的statuslayout作为activity setcontentview()方法的参数
     *
     * @param view 根view
     * @return
     */
    public static StatusLayout init(View view) {
        if (view == null) {
            throw new NullPointerException("view can not be null");
        }
        StatusLayout statusLayout = new StatusLayout(view.getContext());
        statusLayout.addView(view);
        return statusLayout;
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
     * @param statusConfigs 状态属性值
     */
    public static void setGlobalData(List<StatusConfig> statusConfigs) {
        mStatusConfigs.addAll(statusConfigs);
    }

    /**
     * 添加一种布局
     *
     * @param config StatusConfig
     * @return
     */
    public StatusLayout add(StatusConfig config) {
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
    public StatusLayout add(String status, @LayoutRes int layoutRes) {
        add(status, layoutRes, 0);
        return this;
    }

    /**
     * 添加一种布局,点击clickRes的view才回调，有的布局类似错误重试是点击一个按钮才触发，就多传一个按钮的id。
     *
     * @param status    布局状态
     * @param layoutRes 布局资源文件
     * @param clickRes  需要监听的某个view的id
     * @return
     */
    public StatusLayout add(String status, @LayoutRes int layoutRes, @IdRes int clickRes) {
        View view = mLayoutInflater.inflate(layoutRes, this, false);
        if (view == null) {
            throw new NullPointerException("layoutRes can't be converted to view ，please check the layoutRes");
        }
        add(status, view, clickRes);
        return this;
    }

    /**
     * 添加一种布局
     *
     * @param status 布局对应的状态
     * @param view   布局view
     * @return
     */
    public StatusLayout add(String status, View view) {
        add(status, view, 0);
        return this;
    }

    /**
     * 添加一种布局
     *
     * @param status   布局对应的状态
     * @param view     布局view
     * @param clickRes 需要监听的某个view的id
     * @return
     */
    public StatusLayout add(String status, View view, @IdRes int clickRes) {
        // 传入了clickRes的时候再监听，否则交给view自身去处理逻辑
        if (clickRes != 0) {
            View clickView = view.findViewById(clickRes);
            if (clickView == null) {
                clickView = view;
            }
            clickView.setOnClickListener(getClickListener(view, status));
        }
        int index = getViewIndexByStatus(status);
        if (index >= 0) {
            removeViewAt(index);
            addView(view, index);
        } else {
            int childCount = getChildCount();
            addView(view);
            mStatusArrays.put(childCount, status);
        }
        return this;
    }

    /**
     * 切换布局，根据status的值来切换相对应类型的布局
     *
     * @param status 布局所代表的状态
     */
    public void switchLayout(String status) {
        if (mUseGlobal && !mInited) {
            initDefault();
            mInited = true;
        }
        int index = getViewIndexByStatus(status);
        if (index >= 0 && mCurrentPosition != index) {
            setDisplayedChild(index);
            mCurrentPosition = index;
        }
    }

    /**
     * 切换到默认的内容，即布局资源文件中添加的内容
     */
    public void showDefaultContent() {
        if (mCurrentPosition != 0 && mStatusArrays.get(0) == null) {
            Toast.makeText(mContext, "没有content", Toast.LENGTH_SHORT);
            setDisplayedChild(0);
            mCurrentPosition = 0;
        }
    }

    /**
     * 设置是否使用全局设置的状态布局
     *
     * @param apply 是否使用，默认为使用
     * @return
     */
    public StatusLayout applyGlobal(boolean apply) {
        mUseGlobal = apply;
        return this;
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
     * 通过status来找出对应status的key是多少，这个key存放的就该status的view在viewgroup中的index
     *
     * @param status 状态
     * @return
     */
    private int getViewIndexByStatus(String status) {
        int index = -1;
        int valueIndex = mStatusArrays.indexOfValue(status);
        if (valueIndex >= 0) {
            index = mStatusArrays.keyAt(valueIndex);
        }
        return index;
    }

    /**
     * 获取当前显示了第几个布局
     *
     * @return
     */
    public int getCurrentPosition() {
        return mCurrentPosition;
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
