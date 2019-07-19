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
        findViewById<View>(R.id.btn_error).setOnClickListener { mStatusLayout!!.switchLayout(ERROR) }
        findViewById<View>(R.id.btn_empty).setOnClickListener { mStatusLayout!!.switchLayout(EMPTY) }
        findViewById<View>(R.id.btn_loading).setOnClickListener { mStatusLayout!!.switchLayout(LOADING) }
    }

    private fun initStatusLayout() {
        val errorView = LayoutInflater.from(this).inflate(R.layout.include_error, mStatusLayout, false)
        errorView.findViewById<View>(R.id.btn_retry).setOnClickListener { Log.d("statuslayout", "error click") }
        mStatusLayout = findViewById(R.id.status_layout)
        mStatusLayout!!
                .add(StatusConfig(LOADING, view = LayoutInflater.from(this).inflate(R.layout.include_loading, null)))
                .add(StatusConfig(ERROR, view = errorView))
                .setInAnimation(R.anim.anim_in_alpha)
                .setOutAnimation(R.anim.anim_out_alpha)
                .setDefaultAnimation()
                .setLayoutClickListener(object : StatusLayout.OnLayoutClickListener {
                    override fun OnLayoutClick(view: View, status: String?) {
                        Log.d("statuslayout", status!! + " =  click")
                    }
                })
    }

    companion object {
        val LOADING = "loading"
        val ERROR = "error"
        val EMPTY = "empty"
        val NORMAL = "normal"
    }
}
