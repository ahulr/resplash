package com.codemybrainsout.imageviewer.utility

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.*

object PrefHelper {

    const val PREF = "Resplash"

    @JvmStatic
    fun storeList(context: Context, key: String, list: List<String>) {
        val editor: SharedPreferences.Editor
        val settings: SharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        editor = settings.edit()
        val gson = Gson()
        val jsonFavorites = gson.toJson(list)
        editor.putString(key, jsonFavorites)
        editor.apply()
    }

    @JvmStatic
    fun loadList(context: Context, key: String): ArrayList<String> {
        var favorites: List<String>?
        val settings: SharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        if (settings.contains(key)) {
            val jsonFavorites = settings.getString(key, null)
            val gson = Gson()
            val favoriteItems = gson.fromJson(jsonFavorites, Array<String>::class.java)
            favorites = listOf(*favoriteItems)
            favorites = ArrayList(favorites)
        } else return ArrayList<String>()
        return favorites
    }

    fun deleteList(context: Context) {
        val myPrefs = context.getSharedPreferences(
            PREF,
            Context.MODE_PRIVATE
        )
        val editor = myPrefs.edit()
        editor.clear()
        editor.apply()
    }
}