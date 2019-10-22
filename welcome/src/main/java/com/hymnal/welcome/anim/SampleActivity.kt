package com.hymnal.welcome.anim

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.hymnal.welcome.R
import com.hymnal.welcome.base.BaseActivity
import kotlinx.android.synthetic.main.sample_activity.*

@Route(path = "/sample/anim/SampleActivity")
class SampleActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.sample_activity


    private val objectAnim by lazy {
        animSet {
            objectAnim {
                target = tv
                translationX = floatArrayOf(0f, 200f)
                scaleX = floatArrayOf(1f, 1.5f)
                alpha = floatArrayOf(1f, 0.5f)
            }
            duration = 300L
            interpolator = AccelerateInterpolator()
        }
    }

    private val togetherAnim by lazy {
        animSet {
            anim {
                values = floatArrayOf(1.0f, 1.4f)
                action = { value -> tv.scaleX = (value as Float) }
            } with anim {
                values = floatArrayOf(0f, -200f)
                action = { value -> tv.translationY = (value as Float) }
            } with anim {
                values = floatArrayOf(1.0f, 0.5f)
                action = { value -> tv.alpha = (value as Float) }
            }
            duration = 200L
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    private val sequenceAnim by lazy {
        animSet {
            anim {
                values = floatArrayOf(1.0f, 1.5f)
                action = { value -> tv.scaleX = (value as Float) }
                onStart = { Toast.makeText(this@SampleActivity, "end", Toast.LENGTH_SHORT).show() }
            } before anim {
                values = floatArrayOf(1.5f, 1.0f)
                action = { value -> tv.scaleX = (value as Float) }
            } before anim {
                values = floatArrayOf(1.0f, 1.5f)
                action = { value -> tv.scaleX = (value as Float) }
            }
            duration = 400L
            interpolator = AccelerateInterpolator()
        }
    }

    private val complexAnim by lazy {
        animSet {
            anim {
                values = floatArrayOf(0f, -100f)
                delay = 150L
                action = { value -> tv.translationX = (value as Float) }
            } before anim {
                values = floatArrayOf(1.0f, 2f)
                action = { value -> tv.scaleY = (value as Float) }
            } with objectAnim {
                target = tv
                scaleX = floatArrayOf(1f, 1.5f)
                rotationX = floatArrayOf(0f, 360f)
                alpha = floatArrayOf(1f, 0.5f, 1f)
            }
            duration = 500L
            delay = 100L
            interpolator = LinearInterpolator()
            onStart = { Toast.makeText(this@SampleActivity, "start", Toast.LENGTH_SHORT).show() }
        }
    }

    private val repeatAnim by lazy {
        animSet {
            objectAnim {
                target = tv
                translationX = floatArrayOf(-100f, 100f)
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = LinearInterpolator()
            }
        }
    }

    override fun subscribeUi() {


        /**
         * case:ObjectAnim and reverse it
         */
        btnObject.setOnClickListener { objectAnim.start() }
        btnObjectReverse.setOnClickListener { objectAnim.reverse() }

        /**
         * case:play animations together by DSL and reverse it
         */
        btnFree.setOnClickListener { togetherAnim.start() }
        btnFreeReverse.setOnClickListener { togetherAnim.reverse() }

        /**
         * case:play animations Sequence by DSL and reverse it
         */
        btnSequence.setOnClickListener { sequenceAnim.start() }
        btnSequenceReverse.setOnClickListener { sequenceAnim.reverse() }

        /**
         * case:complex animations
         */
        (complexAnim.getAnim(2) as ObjectAnim).scaleX =
            floatArrayOf(1.0f, 3.0f)//change property values after animation built
        btnComplex.setOnClickListener { complexAnim.start() }
        btnComplexReverse.setOnClickListener { complexAnim.reverse() }

        /**
         * case:repeat animation
         */
        btnRepeat.setOnClickListener {
            repeatAnim.start()
        }

        /**
         * case:to the beginning of animation
         */
        btnToBeginning.setOnClickListener {
            objectAnim.toBeginning()
        }
    }
}
