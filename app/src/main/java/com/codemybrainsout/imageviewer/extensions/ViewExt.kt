package com.codemybrainsout.imageviewer.extensions

import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.codemybrainsout.imageviewer.R
import com.google.android.material.snackbar.Snackbar

fun View.showErrorSnackbar(@StringRes strId: Int) {
    val snackbar: Snackbar = Snackbar.make(this, strId, Snackbar.LENGTH_SHORT)
    snackbar.setBackgroundTint(
        ContextCompat.getColor(
            context,
            R.color.colorPrimaryDark
        )
    )
    snackbar.setTextColor(ContextCompat.getColor(context, R.color.textColorLight))
    snackbar.show()
}

fun View.showErrorSnackbar(text: String) {
    val snackbar: Snackbar = Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
    snackbar.setBackgroundTint(
        ContextCompat.getColor(
            context,
            R.color.colorPrimaryDark
        )
    )
    snackbar.setTextColor(ContextCompat.getColor(context, R.color.textColorLight))
    snackbar.show()
}
