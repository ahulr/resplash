package com.codemybrainsout.imageviewer.custom

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import android.graphics.RectF
import com.codemybrainsout.imageviewer.custom.CircleImageView
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.ColorFilter
import kotlin.jvm.JvmOverloads
import android.content.res.TypedArray
import com.codemybrainsout.imageviewer.R
import android.widget.ImageView.ScaleType
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.Shader
import com.codemybrainsout.imageviewer.model.Exif
import androidx.appcompat.app.AppCompatDialog
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import com.codemybrainsout.imageviewer.viewmodel.ExifViewModel
import android.view.View.MeasureSpec

/**
 * Created by ahulr on 12-06-2017.
 */
class SquareImageView(context: Context, attrs: AttributeSet?, defStyle: Int) : AppCompatImageView(context, attrs, defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            var height = width
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec))
            }
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}