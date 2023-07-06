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

    private lateinit var invertedSunView: View
    private lateinit var seaView: View

    private var isFirstClick: Boolean = true //是否第一次点击
    private var isSunDown: Boolean = true //是否落日

    private lateinit var sunsetSkyAnimator: ObjectAnimator
    private lateinit var nightSkyAnimator: ObjectAnimator

    private lateinit var bigXAnimator: ObjectAnimator
    private lateinit var bigYAnimator: ObjectAnimator

    private lateinit var smallXAnimator: ObjectAnimator
    private lateinit var smallYAnimator: ObjectAnimator

    private lateinit var clockwiseAnimator: ObjectAnimator
    private lateinit var antiClockwiseAnimator: ObjectAnimator


    //倒影
    private lateinit var sunsetSeaAnimator: ObjectAnimator
    private lateinit var nightSeaAnimator: ObjectAnimator

    private lateinit var bigInvertedXAnimator: ObjectAnimator
    private lateinit var bigInvertedYAnimator: ObjectAnimator

    private lateinit var smallInvertedXAnimator: ObjectAnimator
    private lateinit var smallInvertedYAnimator: ObjectAnimator

    private lateinit var clockwiseInvertedAnimator: ObjectAnimator
    private lateinit var antiClockwiseInvertedAnimator: ObjectAnimator

    private lateinit var alphaAnimator: ObjectAnimator
    private lateinit var unAlphaAnimator: ObjectAnimator

    private lateinit var animatorSetInvertedDown: AnimatorSet
    private lateinit var animatorSetInvertedUp: AnimatorSet


    private lateinit var animatorSetDown: AnimatorSet
    private lateinit var animatorSetUp: AnimatorSet

    private lateinit var animatorSetDownTotal: AnimatorSet
    private lateinit var animatorSetUpTotal: AnimatorSet

    private val blueSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.blue_sky) }
    private val sunsetSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.sunset_sky) }
    private val nightSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.night_sky) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.sun)
        skyView = findViewById(R.id.sky)

        invertedSunView = findViewById(R.id.inverted_sun)
        seaView = findViewById(R.id.sea)


        initSkyAnimation()

        sceneView.setOnClickListener {
            if (isFirstClick) {
                initAnimationDownUp()
            }
            isFirstClick = false

            if (!animatorSetDown.isRunning && !animatorSetUp.isRunning) {
                if (isSunDown) {
                    animatorSetDownTotal.start()
                } else {
                    animatorSetUpTotal.start()
                }
                isSunDown = !isSunDown
            }

        }
    }

    /*
    *
    * */
    private fun initAnimationDownUp() {
        /*太阳*/
        //变大
        bigXAnimator = ObjectAnimator.ofFloat(sunView, "scaleX", 1.0f, 2.0f).setDuration(3000);
        bigYAnimator = ObjectAnimator.ofFloat(sunView, "scaleY", 1.0f, 2.0f).setDuration(3000);
        //变小
        smallXAnimator = ObjectAnimator.ofFloat(sunView, "scaleX", 2.0f, 1.0f).setDuration(3000);
        smallYAnimator = ObjectAnimator.ofFloat(sunView, "scaleY", 2.0f, 1.0f).setDuration(3000);

        //旋转特效
        //顺时针
        clockwiseAnimator =
            ObjectAnimator.ofFloat(sunView, "rotation", 0f, 720f).setDuration(3000);
        //逆时针
        antiClockwiseAnimator =
            ObjectAnimator.ofFloat(sunView, "rotation", 360f, 0f).setDuration(3000);

        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat().plus(75)//有放大效果，落日到边界后没有完全淹没，+75解决，怎么获取太阳增加的半径？？

        //太阳日落
        val heightAnimatorDown =
            ObjectAnimator.ofFloat(sunView, "y", sunYStart, sunYEnd).setDuration(3000)
        heightAnimatorDown.interpolator = AccelerateInterpolator()  //添加加速特效


        //太阳日出
        val heightAnimatorUp =
            ObjectAnimator.ofFloat(sunView, "y", sunYEnd, sunYStart).setDuration(3000)
        heightAnimatorUp.interpolator = AccelerateInterpolator()  //添加加速特效


        /*倒影*/
        //--------------------------------倒影--------------------------------//
        //透明度
        alphaAnimator =
            ObjectAnimator.ofFloat(invertedSunView, "alpha", 0.7f, 0.0f).setDuration(3000)
        unAlphaAnimator =
            ObjectAnimator.ofFloat(invertedSunView, "alpha", 0.0f, 0.7f).setDuration(3000)
        //大小
        bigInvertedXAnimator =
            ObjectAnimator.ofFloat(invertedSunView, "scaleX", 1.0f, 2.0f).setDuration(3000)
        bigInvertedYAnimator =
            ObjectAnimator.ofFloat(invertedSunView, "scaleY", 1.0f, 2.0f).setDuration(3000)
        smallInvertedXAnimator =
            ObjectAnimator.ofFloat(invertedSunView, "scaleX", 2.0f, 1.0f).setDuration(3000)
        smallInvertedYAnimator =
            ObjectAnimator.ofFloat(invertedSunView, "scaleY", 2.0f, 1.0f).setDuration(3000)
        //旋转特效
        clockwiseInvertedAnimator =
            ObjectAnimator.ofFloat(invertedSunView, "rotation", 0f, 720f).setDuration(3000)
        antiClockwiseInvertedAnimator =
            ObjectAnimator.ofFloat(invertedSunView, "rotation", 360f, 0f).setDuration(3000)

        //位移
        val sunInvertedYStart = invertedSunView.top.toFloat()
        val sunInvertedYEnd = 0.0f//seaView.height.toFloat()

        val invertedAnimatorUp =
            ObjectAnimator.ofFloat(invertedSunView, "y", sunInvertedYStart, sunInvertedYEnd)
                .setDuration(3000)
        invertedAnimatorUp.interpolator = AccelerateInterpolator()

        val invertedAnimatorDown =
            ObjectAnimator.ofFloat(invertedSunView, "y", sunInvertedYEnd, sunInvertedYStart)
                .setDuration(3000)
        invertedAnimatorDown.interpolator = AccelerateInterpolator()


        //太阳下降动画集
        animatorSetDown = AnimatorSet()
        animatorSetDown.play(heightAnimatorDown)
            .with(sunsetSkyAnimator)
            .with(bigXAnimator).with(bigYAnimator)
            .with(clockwiseAnimator)
            .with(alphaAnimator)
            .before(nightSkyAnimator)

        //太阳上升动画集
        animatorSetUp = AnimatorSet()
        animatorSetUp.play(heightAnimatorUp).with(sunsetSkyAnimator)
            .with(smallXAnimator).with(smallYAnimator)
            .with(antiClockwiseAnimator)

        //倒影上升动画集、
        animatorSetInvertedUp = AnimatorSet()
        animatorSetInvertedUp
            .play(clockwiseInvertedAnimator)
            .with(bigInvertedXAnimator).with(bigInvertedYAnimator)
            .with(alphaAnimator)
            .with(invertedAnimatorUp)

        //倒影下降动画集
        animatorSetInvertedDown = AnimatorSet()
        animatorSetInvertedDown
            .play(clockwiseInvertedAnimator)
            .with(smallInvertedXAnimator).with(smallInvertedYAnimator)
            .with(invertedAnimatorDown)
            .with(unAlphaAnimator)

        //总动画集
        animatorSetDownTotal = AnimatorSet()
        animatorSetDownTotal.play(animatorSetDown).with(animatorSetInvertedUp)

        animatorSetUpTotal = AnimatorSet()
        animatorSetUpTotal.play(animatorSetUp).with(animatorSetInvertedDown)
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