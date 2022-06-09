package com.codemybrainsout.imageviewer.utility

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

/**
 * Created by ahulr on 11-06-2017.
 */
object Utils {
    private var screenHeight = 0
    private var screenWidth = 0
    @JvmStatic
    fun getScreenHeight(c: Context): Int {
        if (screenHeight == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenHeight = size.y
        }
        return screenHeight
    }

    fun getScreenWidth(c: Context): Int {
        if (screenWidth == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
        }
        return screenWidth
    }
}