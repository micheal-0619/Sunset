package com.android.sunset

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: View
    private lateinit var sunView: View
    private lateinit var skyView: View


    private val blueSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.blue_sky) }
    private val sunsetSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.sunset_sky) }
    private val nightSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.night_sky) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.sun)
        skyView = findViewById(R.id.sky)

        sceneView.setOnClickListener {
            startAnimation()
        }
    }

    /*
    *
    *动画实现
    * */
    private fun startAnimation() {
        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()


        val heightAnimator =
            ObjectAnimator.ofFloat(sunView, "y", sunYStart, sunYEnd).setDuration(3000)

        heightAnimator.interpolator = AccelerateInterpolator()  //添加加速特效

        /*
        * 实现天空的色彩变换
        * TypeEvaluator能帮助ObjectAnimator对象精确地计算开始到结束间的递增值 子类: ArgbEvaluator
        * */
        val sunsetSkyAnimator =
            ObjectAnimator.ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
                .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())//解决屏闪问题

        heightAnimator.start()
        sunsetSkyAnimator.start()
    }
}