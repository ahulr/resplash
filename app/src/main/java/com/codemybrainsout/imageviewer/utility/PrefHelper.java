package com.codemybrainsout.imageviewer.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefHelper {


    public PrefHelper() {
        super();
    }

    public static final String PREF = "Resplash";


    public static void storeList(Context context, String key, List list) {

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(list);
        editor.putString(key, jsonFavorites);
        editor.commit();
    }

    public static ArrayList loadList(Context context, String key) {

        SharedPreferences settings;
        List favorites;
        settings = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if (settings.contains(key)) {
            String jsonFavorites = settings.getString(key, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites, String[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        } else
            return new ArrayList();
        return (ArrayList) favorites;
    }


    public static void deleteList(Context context) {

        SharedPreferences myPrefs = context.getSharedPreferences(PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.commit();
    }
}