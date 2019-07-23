package com.colaman.demo

import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.colaman.statuslayout.StatusConfig
import com.colaman.statuslayout.StatusLayout
import com.example.gg.statuslayout.R

class MainActivity : AppCompatActivity() {
    private var mStatusLayout: StatusLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initStatusLayout()
        findViewById<View>(R.id.btn_content).setOnClickListener { mStatusLayout!!.showDefaultContent() }
        findViewById<View>(R.id.btn_error).setOnClickListener { mStatusLayout!!.switchLayout(StatusLayout.STATUS_ERROR) }
        findViewById<View>(R.id.btn_empty).setOnClickListener { mStatusLayout!!.switchLayout(StatusLayout.STATUS_EMPTY) }
        findViewById<View>(R.id.btn_loading).setOnClickListener { mStatusLayout!!.switchLayout(StatusLayout.STATUS_LOADING) }
    }

    private fun initStatusLayout() {
        mStatusLayout = findViewById(R.id.status_layout)

        // 这里错误类型布局直接inflate成一个view，展示另一种添加布局的方法
        val errorView = LayoutInflater.from(this).inflate(R.layout.include_error, mStatusLayout, false)
        errorView.findViewById<View>(R.id.btn_retry).setOnClickListener {
            Toast.makeText(this@MainActivity, " error layout  is Click  ", Toast.LENGTH_SHORT).show()
            mStatusLayout?.showDefaultContent()
        }

        // 下面的代码展示了两种add方式
        mStatusLayout!!
                .add(StatusConfig(StatusLayout.STATUS_LOADING, view = LayoutInflater.from(this).inflate(R.layout.include_loading, null)))
                .add(StatusConfig(StatusLayout.STATUS_ERROR, view = errorView, autoClcik = true))
                .setLayoutClickListener(object : StatusLayout.OnLayoutClickListener {
                    override fun OnLayoutClick(view: View, status: String?) {
                        Toast.makeText(this@MainActivity, " status = $status  is Click  ", Toast.LENGTH_SHORT).show()
                    }
                })
    }
}
