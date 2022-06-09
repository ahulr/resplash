package com.codemybrainsout.imageviewer.transformation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.renderscript.RSRuntimeException
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.codemybrainsout.imageviewer.utility.ImageUtils
import java.security.MessageDigest

/**
 * Created by ahulr on 10-06-2017.
 */
class BlurTransformation @JvmOverloads constructor(
    context: Context,
    pool: BitmapPool = Glide.get(context).bitmapPool,
    radius: Int = MAX_RADIUS,
    sampling: Int = DEFAULT_DOWN_SAMPLING
) : Transformation<Bitmap?> {
    private val mContext: Context
    private val mBitmapPool: BitmapPool
    private val mRadius: Int
    private val mSampling: Int

    constructor(context: Context, radius: Int) : this(
        context,
        Glide.get(context).bitmapPool,
        radius,
        DEFAULT_DOWN_SAMPLING
    ) {
    }

    constructor(context: Context, radius: Int, sampling: Int) : this(
        context,
        Glide.get(context).bitmapPool,
        radius,
        sampling
    ) {
    }

    override fun transform(
        context: Context,
        resource: Resource<Bitmap?>,
        outWidth: Int,
        outHeight: Int
    ): Resource<Bitmap?> {
        val source = resource.get()
        val width = source.width
        val height = source.height
        val scaledWidth = width / mSampling
        val scaledHeight = height / mSampling
        var bitmap: Bitmap? = mBitmapPool[scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888]
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap!!)
        canvas.scale(1 / mSampling.toFloat(), 1 / mSampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(source, 0f, 0f, paint)
        try {
            bitmap = ImageUtils.blur(mContext, bitmap, mRadius)
        } catch (e: RSRuntimeException) {
            e.printStackTrace()
        }
        return BitmapResource.obtain(resource, mBitmapPool)
    }

    val id: String
        get() = "BlurTransformation(radius=$mRadius, sampling=$mSampling)"

    companion object {
        private const val MAX_RADIUS = 25
        private const val DEFAULT_DOWN_SAMPLING = 1
    }

    init {
        mContext = context.applicationContext
        mBitmapPool = pool
        mRadius = radius
        mSampling = sampling
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        TODO("Not yet implemented")
    }
}