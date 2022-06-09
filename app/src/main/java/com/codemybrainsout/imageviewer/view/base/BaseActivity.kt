package com.codemybrainsout.imageviewer.view.base

import androidx.appcompat.app.AppCompatActivity

/**
 * Created by ahulr on 06-06-2017.
 */
abstract class BaseActivity : AppCompatActivity() {

    private fun resolveError(str: String): String {
        return if (str.contains("HTTP 403 ")) {
            "API Limit Exhausted."
        } else if (str.contains("Unable to resolve host")) {
            "No Internet Connection."
        } else {
            str
        }
    }
}