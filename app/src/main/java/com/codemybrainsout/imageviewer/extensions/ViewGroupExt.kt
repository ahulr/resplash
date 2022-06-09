package com.codemybrainsout.imageviewer.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codemybrainsout.imageviewer.R

fun ViewGroup.addProgressLayout(context: Context) {
    val vi: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val v: View = vi.inflate(R.layout.view_progress, null)
    addView(
        v,
        childCount,
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    )
}

fun ViewGroup.removeProgressLayout() {
    if (findViewById<View>(R.id.view_progress) != null) {
        removeView(findViewById(R.id.view_progress))
    }
}