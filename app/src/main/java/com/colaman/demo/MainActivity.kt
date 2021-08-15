package com.colaman.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.colaman.statuslayout.Status

import com.colaman.statuslayout.StatusConfig
import com.colaman.statuslayout.StatusLayout
import com.example.gg.statuslayout.R

class MainActivity : AppCompatActivity() {
    private var mStatusLayout: StatusLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(StatusLayout.wrapView(this, R.layout.activity_main).apply {
            mStatusLayout = this
        })
        initStatusLayout()
        findViewById<View>(R.id.btn_content).setOnClickListener { mStatusLayout!!.showDefaultContent() }
        findViewById<View>(R.id.btn_error).setOnClickListener { mStatusLayout!!.switchLayout(Status.Error) }
        findViewById<View>(R.id.btn_empty).setOnClickListener { mStatusLayout!!.switchLayout(Status.Empty) }
        findViewById<View>(R.id.btn_loading).setOnClickListener {
            mStatusLayout!!.switchLayout(
                Status.Loading
            )
        }
    }

    private fun initStatusLayout() {
        // 这里错误类型布局直接inflate成一个view，展示另一种添加布局的方法
        val errorView =
            LayoutInflater.from(this).inflate(R.layout.include_error, mStatusLayout, false)
        errorView.findViewById<View>(R.id.btn_retry).setOnClickListener {
            Toast.makeText(this@MainActivity, " error layout  is Click  ", Toast.LENGTH_SHORT)
                .show()
            mStatusLayout?.showDefaultContent()
        }

        // 下面的代码展示了两种add方式
        mStatusLayout!!
            .addStatus(
                Status.Loading,
                StatusConfig(
                    contentView = LayoutInflater.from(this).inflate(R.layout.include_loading, null),
                    autoClick = false
                )
            )
            .addStatus(Status.Error, StatusConfig(contentView = errorView, autoClick = true))
            .setLayoutActionListener(object : StatusLayout.LayoutActionListener {
                override fun onLayoutAction(status: Status, view: View) {
                    Toast.makeText(
                        this@MainActivity,
                        " status = $status  is Click  ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
