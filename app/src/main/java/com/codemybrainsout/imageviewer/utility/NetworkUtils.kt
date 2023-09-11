package com.codemybrainsout.imageviewer.utility

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by ahulr on 10-06-2017.
 */
object NetworkUtils {
    fun hasNetwork(context: Context): Boolean {
        var isConnected = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                isConnected = true
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                isConnected = true
            }
        }
        return isConnected
    }
}