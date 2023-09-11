package com.codemybrainsout.imageviewer.utility

import android.content.Context
import android.graphics.Bitmap
import android.icu.util.RangeValueIterator
import android.renderscript.Allocation
import android.renderscript.Element.U8_4
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

/**
 * Created by ahulr on 10-06-2017.
 */
object ImageUtils {

    fun blur(context: Context?, image: Bitmap?): Bitmap? {
        val bitmap = image?.copy(image.config, true)
        val rs = RenderScript.create(context)
        val input = Allocation.createFromBitmap(
            rs,
            bitmap,
            Allocation.MipmapControl.MIPMAP_FULL,
            Allocation.USAGE_SCRIPT
        )
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, U8_4(rs))
        script.setRadius(10f)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(bitmap)
        return bitmap
    }
}