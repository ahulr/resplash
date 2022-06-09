package com.codemybrainsout.imageviewer.utility

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by ahulr on 10-06-2017.
 */
object NetworkUtils {
    fun hasNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}