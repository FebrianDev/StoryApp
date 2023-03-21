package com.febrian.storyapp.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(c: Context) {
    private var sharedPreference: SharedPreferences =
        c.getSharedPreferences(Constant.sharedPreferences, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        return sharedPreference.getString(key, "").toString()
    }

    fun clear(key: String) {
        val editor = sharedPreference.edit()
        editor.remove(key).apply()
    }
}