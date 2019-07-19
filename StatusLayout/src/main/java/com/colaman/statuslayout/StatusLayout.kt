package com.colaman.statuslayout

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.ViewAnimator
import com.colaman.statuslayout.StatusLayout.Companion.mStatusConfigs

import com.example.statuslayout.R

import java.util.ArrayList

/**
 * Create by kyle on 2018/9/30
 * Function : 管理不同状态下布局显示
 *             有STATUS_NORMAL/STATUS_LOADING等默认的几个key可以使用，正常状态的布局建议用STATUS_NORMAL作为key添加
 *             如果用自定义的key，那么需要最先添加到statuslayout中，再添加如loading，error等布局
 */
class StatusLayout constructor(protected var mContext: Context, attrs: AttributeSet? = null) : ViewAnimator(mContext, attrs) {
    /**
     * 默认的一些key，可用可不用
     */
    companion object {
        const val STATUS_NORMAL = "normal_content"
        const val STATUS_LOADING = "loading_content"
        const val STATUS_ERROR = "error_content"
        const val STATUS_EMPTY = "empty_content"

        // 全局状态布局的属性值
        var mStatusConfigs: MutableList<StatusConfig> = ArrayList()


        /**
         * 用于activity/fragment等view的初始化方式使用，在布局文件中可以不用手动把根部局替换成statuslayout,
         * 而是调用init方法把资源res传进来，返回一个statuslayout,直接把返回的statuslayout作为activity setcontentview()方法的参数
         *
         * @param context   上下文
         * @param layoutRes 布局资源文件
         * @return
         */
        fun init(context: Context, @LayoutRes layoutRes: Int): StatusLayout {
            val rootView = LayoutInflater.from(context).inflate(layoutRes, null)
            return init(rootView)
        }

        /**
         * 用于activity/fragment等view的初始化方式使用，在布局文件中可以不用手动把根部局替换成statuslayout,
         * 而是调用init方法把资源res传进来，返回一个statuslayout,直接把返回的statuslayout作为activity setcontentview()方法的参数
         *
         * @param view 根view
         * @return
         */
        fun init(view: View?): StatusLayout {
            if (view == null) {
                throw NullPointerException("view can not be null")
            }
            val statusLayout = StatusLayout(view.context)
            mStatusConfigs.add(StatusConfig(STATUS_NORMAL, view = view))
            statusLayout.addView(view)
            return statusLayout
        }

        /**
         * 设置全局数据
         *
         * @param statusConfigs 状态属性值
         */
        fun setGlobalData(vararg statusConfigs: StatusConfig) {
            mStatusConfigs.addAll(statusConfigs)
        }
    }

    protected val mLayoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    // 布局点击listener
    protected var mOnLayoutClickListener: OnLayoutClickListener? = null

    // 当前显示view在viewgroup中的index
    var currentPosition: Int = 0

    // 是否使用全局设置的属性
    protected var mUseGlobal = true

    // 是否已经加载过全局设置属性
    protected var mInited = false


    /**
     * 加载默认属性
     */
    protected fun initDefault() {
        for (config in mStatusConfigs) {
            add(config)
        }
    }

    /**
     * 添加一种布局
     *
     * @param config StatusConfig
     * @return
     */
    fun add(config: StatusConfig?): StatusLayout {
        if (config == null) {
            throw NullPointerException("config is null")
        }
        var (status, layoutRes, view, clickRes) = config
        if (view == null) {
            view = mLayoutInflater.inflate(layoutRes, this, false)
        }
        // 传入了clickRes的时候再监听，否则交给view自身去处理逻辑
        if (clickRes != 0) {
            var clickView: View? = view?.findViewById(clickRes)
            if (clickView == null) {
                clickView = view
            }
            clickView?.setOnClickListener(getClickListener(view!!, status))
        }
        val index = getViewIndexByStatus(status)
        // 把status作为tag设置到view上
        view?.tag = status
        // 如果同个status已经被添加过，那先移除原本的
        if (index >= 0) {
            removeViewAt(index)
            addView(view, index)
        } else {
            addView(view)
        }
        return this
    }

    /**
     * 切换布局，根据status的值来切换相对应类型的布局
     *
     * @param status 布局所代表的状态
     */
    fun switchLayout(status: String) {
        /**
         * 如果有全局设置，延迟到切换布局时再添加到statuslayout中
         */
        if (mUseGlobal && !mInited) {
            initDefault()
            mInited = true
        }
        val index = getViewIndexByStatus(status)
        if (index >= 0 && currentPosition != index) {
            displayedChild = index
            currentPosition = index
        }
    }

    /**
     * 找到defaultcontent的index
     */
    fun findDefaultContentIndex(): Int {
        /**
         * 先寻找有没有用STAUTS_NORMAL 作为key的布局，优先级比较高，
         * 如果没有的话则用户是用自定义的key，则默认用index=0的布局作为默认的布局，所以如果用自定义的key添加默认布局的时候需要最先添加
         */
        for (position in 0 until childCount) {
            if (getChildAt(position).tag == STATUS_NORMAL) {
                return position
            }
        }
        return 0
    }

    /**
     * 切换到默认的内容，即布局资源文件中添加的内容
     */
    fun showDefaultContent() {
        /**
         * 先寻找有没有用STAUTS_NORMAL 作为key的布局，优先级比较高，
         * 如果没有的话则用户是用自定义的key，则默认用index=0的布局作为默认的布局，所以如果用自定义的key添加默认布局的时候需要最先添加
         */
        val index = findDefaultContentIndex()
        displayedChild = index
        currentPosition = index
    }

    fun getDefaultContentView() = getChildAt(findDefaultContentIndex())

    /**
     * 设置是否使用全局设置的状态布局
     *
     * @param apply 是否使用，默认为使用
     * @return
     */
    fun applyGlobal(apply: Boolean): StatusLayout {
        mUseGlobal = apply
        return this
    }

    /**
     * 显示布局时的动画效果
     *
     * @param animationRes 动画资源文件
     * @return
     */
    fun setInAnimation(animationRes: Int): StatusLayout {
        setInAnimation(mContext, animationRes)
        return this
    }

    /**
     * 隐藏布局时的动画效果
     *
     * @param animationRes 动画资源文件
     * @return
     */
    fun setOutAnimation(animationRes: Int): StatusLayout {
        setOutAnimation(mContext, animationRes)
        return this
    }

    /**
     * 设置默认的的动画效果
     *
     * @return
     */
    fun setDefaultAnimation(): StatusLayout {
        setInAnimation(R.anim.anim_in_alpha)
        setOutAnimation(R.anim.anim_out_alpha)
        return this
    }

    /**
     * 通过status来找出对应status的key是多少，这个key存放的就该status的view在viewgroup中的index
     *
     * @param status 状态
     * @return
     */
    protected fun getViewIndexByStatus(status: String?): Int {
        for (position in 0 until childCount) {
            if (getChildAt(position).tag === status) {
                return position
            }
        }
        return -1
    }

    /**
     * get一个点击监听
     *
     * @param status 传入监听布局的status
     * @return
     */
    fun getClickListener(view: View, status: String?): View.OnClickListener {
        return OnClickListener {
            if (mOnLayoutClickListener != null) {
                mOnLayoutClickListener!!.OnLayoutClick(view, status)
            }
        }
    }

    /**
     * 设置布局点击的监听
     *
     * @param onLayoutClickListener
     */
    fun setLayoutClickListener(onLayoutClickListener: OnLayoutClickListener) {
        mOnLayoutClickListener = onLayoutClickListener
    }

    /**
     * 布局点击事件的接口
     */
    interface OnLayoutClickListener {
        /**
         * 布局点击之后的回调方法
         *
         * @param view   add的时候传入的资源文件转换的view，要在回调中做操作可以通过操作view来实现
         * @param status 当前点击的布局代表的status
         */
        fun OnLayoutClick(view: View, status: String?)
    }
}
