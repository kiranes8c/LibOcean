package com.example.libocean.services

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.RelativeLayout

class BgAnim(private val view: RelativeLayout, private var targetBackGround: Int) : Animation() {
    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        super.applyTransformation(interpolatedTime, t)
        view.setBackgroundColor(targetBackGround)
    }

    fun setColor(color: Int) {
        targetBackGround = color
    }
}
