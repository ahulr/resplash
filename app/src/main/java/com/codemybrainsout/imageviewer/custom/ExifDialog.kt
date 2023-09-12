package com.codemybrainsout.imageviewer.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import com.codemybrainsout.imageviewer.R
import com.codemybrainsout.imageviewer.databinding.LayoutExifBinding
import com.codemybrainsout.imageviewer.model.Exif
import com.codemybrainsout.imageviewer.viewmodel.ExifViewModel

/**
 * Created by ahulr on 11-06-2017.
 */
class ExifDialog(context: Context, private val exif: Exif) : AppCompatDialog(context, R.style.DialogTheme) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Data binding to set exif data directly to the UI
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val exifBinding: LayoutExifBinding = DataBindingUtil.inflate(inflater, R.layout.layout_exif, null, false)
        setContentView(exifBinding.root)
        val exifViewModel = ExifViewModel(exif)
        exifBinding.exif = exifViewModel
        exifBinding.layoutExifLL.setOnClickListener { dismiss() }
    }

    init {
        setCancelable(true)
    }
}