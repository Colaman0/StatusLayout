package com.colaman.statuslayout


import android.content.Context
import android.support.annotation.AnimRes
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ViewAnimator
import com.example.statuslayout.R

/**
 * Create by kyle on 2018/9/30
 * Function : 管理不同状态下布局显示
 *             有STATUS_NORMAL/STATUS_LOADING等默认的几个key可以使用，正常状态的布局建议用STATUS_NORMAL作为key添加
 *             如果用自定义的key，那么需要最先添加到statuslayout中，再添加如loading，error等布局
 */
open class StatusLayout constructor(private var mContext: Context, attrs: AttributeSet? = null) :
    ViewAnimator(mContext, attrs) {

    companion object {

        // 全局状态布局的属性值
        private val mGlobalStatusConfigs by lazy {
            HashMap<Status, StatusConfig>()
        }

        // 全局的布局显示的动画
        var globalInAnimation: Int = R.anim.anim_in_alpha
        var globalOutAnimation: Int = R.anim.anim_out_alpha

        /**
         * 取代view的在父布局中的位置，把view当成了[Status.Normal]布局
         * 1.可以直接用于Activity/Fragment等，直接wrap根布局，把StatusLayout作为根布局
         * 2.用于替换布局中某个View，方便局部替换
         *
         * @param view View 要包裹住的View
         * @return StatusLayout
         */
        fun wrapView(view: View): StatusLayout {
            val statusLayout = StatusLayout(view.context)
            (view.parent as? ViewGroup)?.apply {
                val index = indexOfChild(view)
                removeViewAt(index)
                addView(statusLayout, index, view.layoutParams)
            }
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            statusLayout.addStatus(Status.Normal, StatusConfig(contentView = view))
            statusLayout.switchLayout(Status.Normal)
            return statusLayout
        }

        fun wrapView(context: Context, @LayoutRes layoutRes: Int): StatusLayout {
            return wrapView(LayoutInflater.from(context).inflate(layoutRes, null))
        }


        /**
         * 设置全局数据
         *
         * @param statusConfigs 状态属性值
         */
        fun setGlobalData(statusList: MutableList<Pair<Status, StatusConfig>>) {
            statusList.forEach {
                mGlobalStatusConfigs[it.first] = it.second
            }
        }

        /**
         * 设置全局动画
         *
         * @param inAnimRes
         * @param outAnimRes
         */
        fun setGlobalAnim(@AnimRes inAnimRes: Int, @AnimRes outAnimRes: Int) {
            globalInAnimation = inAnimRes
            globalOutAnimation = outAnimRes
        }
    }

    // 状态布局集合
    private val statusConfigs by lazy {
        HashMap<Status, StatusConfig>()
    }

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(mContext)
    }

    // 布局点击listener
    private var layoutActionListener: LayoutActionListener? = null

    // 当前显示view在viewgroup中的index
    var currentPosition: Int = 0

    // 是否使用全局设置的属性
    private var useGlobal = true

    // 布局显示的动画
    var inAnimation: Int = globalInAnimation
    var outAnimation: Int = globalOutAnimation


    init {
        setLayoutAnimation(inAnimation, outAnimation)
    }

    /**
     * 添加一种布局配置
     *
     * @param config StatusConfig
     * @return
     */
    fun addStatus(status: Status, config: StatusConfig?): StatusLayout {
        config?.let {
            statusConfigs[status] = it
        }
        return this
    }

    /**
     * 切换布局，根据status的值来切换相对应类型的布局
     *
     * @param status 布局所代表的状态
     */
    fun switchLayout(status: Status) {
        val index = getIndexByStatusIfAbsent(status)
        if (index >= 0 && currentPosition != index) {
            displayedChild = index
            currentPosition = index
        }
    }

    /**
     * 根据Status返回对应Index，如果Status还没被添加过，就去查找配置map，把view添加进去再返回
     * @param status Status
     * @return Int Status对应的View的Index，如果没有被添加过并且配置Map里找不到则返回-1
     */
    private fun getIndexByStatusIfAbsent(status: Status): Int {
        var index = getViewIndexByStatus(status)
        // 在ViewGroup里找不到对应status的时候，在configmap里寻找对应的statusConfig，然后生成配置相关View
        if (index < 0) {
            val config = statusConfigs.get(status)
                ?: if (useGlobal) mGlobalStatusConfigs[status] else null
            index = config?.run {
                putViewByStatus(status, config)
            } ?: -1
        }
        return index
    }

    /**
     * 把配置中的View加入到布局中，并返回对应下标
     * @param status Status 配置对应的Status
     * @param config StatusConfig   Status配置
     * @return Int 对应下标
     */
    private fun putViewByStatus(
        status: Status,
        config: StatusConfig
    ): Int {
        var index = -1
        config.run { createView(status, config) }
            ?.also {
                addView(it, it.layoutParams ?: getDefaultLayoutParams())
                index = indexOfChild(it)
            }
        return index
    }

    private fun getDefaultLayoutParams() = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    /**
     * 根据对应的Status和配置生成View，并设置点击事件等
     * @param status Status
     * @param config StatusConfig
     * @return View?
     */
    private fun createView(status: Status, config: StatusConfig): View? {
        var (layoutRes, view, clickRes, layoutAutoClick) = config
        if (view == null) {
            view = layoutInflater.inflate(layoutRes, this, false)
        }

        /**
         * 设置一系列点击事件，设置的View都设置了tag为status
         */
        clickRes.forEach {
            view?.findViewById<View>(it)?.apply {
                setOnClickListener(getClickListener(this, status))
                // 这里给设置了点击事件的view也设置tag
                tag = status
            }
        }
        if (layoutAutoClick) {
            view?.setOnClickListener(getClickListener(view, status))
        }
        view?.tag = status
        return view
    }

    /**
     * 找到[Status.Normal]布局的下标
     * @return Int
     */
    private fun findNormalStatusIndex(): Int {
        for (position in 0 until childCount) {
            if (getChildAt(position).tag == Status.Normal) {
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
         * 先寻找有没有用[Status.Normal] 作为key的布局，优先级比较高，
         * 如果没有的话则用户是用自定义的key，则默认用index=0的布局作为默认的布局，所以如果用自定义的key添加默认布局的时候需要最先添加
         */
        val index = findNormalStatusIndex()
        if (index >= 0 && currentPosition != index) {
            displayedChild = index
            currentPosition = index
        }
    }


    /**
     * 设置是否使用全局设置的状态布局
     *
     * @param apply 是否使用，默认为使用
     * @return
     */
    fun applyGlobal(apply: Boolean): StatusLayout {
        useGlobal = apply
        return this
    }

    /**
     * 显示布局时的动画效果
     *
     * @param animationRes 动画资源文件
     * @return
     */
    fun setLayoutAnimation(@AnimRes inRes: Int, @AnimRes outRes: Int): StatusLayout {
        setInAnimation(mContext, inRes)
        setOutAnimation(mContext, outRes)
        return this
    }


    /**
     * 通过status来找出对应status的key是多少，这个key存放的就该status的view在viewgroup中的index
     *
     * @param status 状态
     * @return
     */
    private fun getViewIndexByStatus(status: Status): Int {
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
    private fun getClickListener(view: View, status: Status): OnClickListener {
        return OnClickListener {
            if (layoutActionListener != null) {
                layoutActionListener!!.onLayoutAction(status, view)
            }
        }
    }

    /**
     * 设置布局点击的监听
     *
     * @param layoutActionListener
     */
    fun setLayoutActionListener(layoutActionListener: LayoutActionListener) {
        this.layoutActionListener = layoutActionListener
    }


    /**
     * 布局点击事件的接口
     */
    interface LayoutActionListener {
        /**
         * 布局点击之后的回调方法
         *
         * @param view   add的时候传入的资源文件转换的view，要在回调中做操作可以通过操作view来实现
         * @param status 当前点击的布局代表的status
         */
        fun onLayoutAction(status: Status, view: View)
    }
}
