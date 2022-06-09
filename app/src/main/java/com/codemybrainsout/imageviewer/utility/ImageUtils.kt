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
    fun blur(context: Context?, image: Bitmap?, mRadius: Int): Bitmap? {
        if (null == image) return null
        val outputBitmap: Bitmap = Bitmap.createBitmap(image)
        val renderScript: RenderScript = RenderScript.create(context)
        val tmpIn: Allocation = Allocation.createFromBitmap(renderScript, image)
        val tmpOut: Allocation = Allocation.createFromBitmap(renderScript, outputBitmap)

        //Intrinsic Gausian blur filter
        val theIntrinsic: ScriptIntrinsicBlur =
            ScriptIntrinsicBlur.create(renderScript, RangeValueIterator.Element.U8_4(renderScript))
        theIntrinsic.setRadius(mRadius)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }
}