package com.android.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationSet
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: View
    private lateinit var sunView: View
    private lateinit var skyView: View
    private var isFirstClick: Boolean = true //是否第一次点击
    private var isSunDown: Boolean = true //是否落日

    private lateinit var sunsetSkyAnimator: ObjectAnimator
    private lateinit var nightSkyAnimator: ObjectAnimator

    private lateinit var animatorSetDown: AnimatorSet
    private lateinit var animatorSetUp: AnimatorSet

    private val blueSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.blue_sky) }
    private val sunsetSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.sunset_sky) }
    private val nightSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.night_sky) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.sun)
        skyView = findViewById(R.id.sky)


        initSkyAnimation()

        sceneView.setOnClickListener {
            if (isFirstClick) {
                initAnimationDownUp()
            }
            isFirstClick = false

            if (!animatorSetDown.isRunning && !animatorSetUp.isRunning) {
                if (isSunDown) {
                    animatorSetDown.start()
                } else {
                    animatorSetUp.start()
                }
                isSunDown = !isSunDown
            }

        }
    }

    /*
    *日落
    *动画实现
    * */
    private fun initAnimationDownUp() {
        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()


        val heightAnimatorDown =
            ObjectAnimator.ofFloat(sunView, "y", sunYStart, sunYEnd).setDuration(3000)
        heightAnimatorDown.interpolator = AccelerateInterpolator()  //添加加速特效

        animatorSetDown = AnimatorSet()
        animatorSetDown.play(heightAnimatorDown).with(sunsetSkyAnimator).before(nightSkyAnimator)


        val heightAnimatorUp =
            ObjectAnimator.ofFloat(sunView, "y", sunYEnd, sunYStart).setDuration(3000)
        heightAnimatorUp.interpolator = AccelerateInterpolator()  //添加加速特效

        animatorSetUp = AnimatorSet()
        animatorSetUp.play(heightAnimatorUp).with(sunsetSkyAnimator)
    }

    /*
    * 日出实现
    * */

    private fun initSkyAnimation() {

        /*
        * 实现天空的色彩变换
        * TypeEvaluator能帮助ObjectAnimator对象精确地计算开始到结束间的递增值 子类: ArgbEvaluator
        * */
        sunsetSkyAnimator =
            ObjectAnimator.ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
                .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())//解决屏闪问题

        /*夜空颜色变换*/
        nightSkyAnimator =
            ObjectAnimator.ofInt(skyView, "backgroundColor", sunsetSkyColor, nightSkyColor)
                .setDuration(3000)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

    }
}