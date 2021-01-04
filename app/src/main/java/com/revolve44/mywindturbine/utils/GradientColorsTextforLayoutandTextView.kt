package com.revolve44.mywindturbine.utils

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color



// for blinked of Text Color
@SuppressLint("ObjectAnimatorBinding")
fun blinkATextView(uiElement: Any, color1: Int, color2: Int, color3: Int, duration: Int){
    val skyAnim: ValueAnimator = ObjectAnimator.ofInt(uiElement, "textColor",
        (color1),
        (color2),
        (color3))

    skyAnim.duration = duration.toLong()
    skyAnim.setEvaluator(ArgbEvaluator())
    skyAnim.start()

}

/** smooth gradient animation for background
 * [1,2 - nedded in all cases, 3dr color is option (may equal him to zero)]  */

@SuppressLint("ObjectAnimatorBinding")
fun gradientAnimation(uiElement: Any, color1: Int, color2: Int, color3 : Int, duration: Int){

    val skyAnim2: ValueAnimator = ObjectAnimator.ofInt(uiElement, "backgroundColor",
        color1,
        color2)

    skyAnim2.duration = duration.toLong()
    skyAnim2.setEvaluator(ArgbEvaluator())
    skyAnim2.start()
}

fun randomName() = listOf(
    "not tired?:)", "are you kidding?", "OK, I get it.", "all will be alright", "now i am tired"
).random()

fun listOfColor(num: Int) = listOf(
    Color.WHITE, Color.BLUE, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.RED
).get(num)