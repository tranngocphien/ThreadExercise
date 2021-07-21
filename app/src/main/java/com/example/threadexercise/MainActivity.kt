package com.example.threadexercise

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.graphics.Color.argb as Color

class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
    var handlerThread: HandlerThread = HandlerThread("MyHandlerThread")
    lateinit var zero: Runnable
    lateinit var plus: Runnable
    lateinit var minus: Runnable
    private var previousX: Float = 0f

    var count: Int = 0
    lateinit var mHandler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handlerThread.start()
        mHandler = Handler(handlerThread.looper)

        initViews()
        initRunnable()
    }

    fun initViews() {
        btn_plus.setOnClickListener(this)
        btn_minus.setOnClickListener(this)
        btn_plus.setOnLongClickListener(this)
        btn_minus.setOnLongClickListener(this)
        count_area.setOnTouchListener(this)

    }

    fun initRunnable() {
        zero = Runnable {
            if (this.count > 0) {
                count--
                this.runOnUiThread {
                    count_text.text = count.toString()
                }
                mHandler.postDelayed(zero, 50)
            } else if (this.count < 0) {
                count++
                this.runOnUiThread {
                    count_text.text = count.toString()
                }
                mHandler.postDelayed(zero, 50)
            }
            else {
                mHandler.removeCallbacks(zero)
            }
        }

        plus = Runnable {
            mHandler.removeCallbacks(zero)
            if(btn_plus.isPressed){
                count ++
                mHandler.postDelayed(plus, 50)
                this.runOnUiThread {
                    count_text.text = count.toString()
                    randomColor()
                }
            }
            else {
                mHandler.removeCallbacks(plus)
                mHandler.postDelayed(zero, 1000)
            }
        }

        minus = Runnable {
            mHandler.removeCallbacks(zero)
            if(btn_minus.isPressed){
                count --
                mHandler.postDelayed(minus, 50)
                this.runOnUiThread {
                    count_text.text = count.toString()
                    randomColor()
                }
            }
            else {
                mHandler.removeCallbacks(minus)
                mHandler.postDelayed(zero, 1000)
            }
        }

    }

    override fun onClick(v: View?) {
        when(v){
            btn_minus -> {
                mHandler.removeCallbacks(zero)
                count --
                this.runOnUiThread{
                    count_text.text = count.toString()
                }
                mHandler.postDelayed(zero, 1000)
            }

            btn_plus -> {
                mHandler.removeCallbacks(zero)
                count ++
                this.runOnUiThread{
                    count_text.text = count.toString()
                }
                mHandler.postDelayed(zero, 1000)
            }
        }

    }

    override fun onLongClick(v: View?): Boolean {
        when(v){
            btn_plus -> {
                mHandler.postDelayed(plus, 50)
            }
            btn_minus -> {
                Log.d("A", "a")
                mHandler.postDelayed(minus, 50)
            }
        }
        return false
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        val x: Float = event?.y ?: 0f

        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    mHandler.removeCallbacks(zero)
                    count = (x - previousX).toInt()
                    this.runOnUiThread{
                        count_text.text = count.toString()
                        randomColor()
                    }
                    mHandler.postDelayed(zero, 1000)

                }
            }
        }
        previousX = x
        return true    }

    fun randomColor() {
       var random:Random = Random()
       var color = Color(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        count_text.setTextColor(color)
    }
}