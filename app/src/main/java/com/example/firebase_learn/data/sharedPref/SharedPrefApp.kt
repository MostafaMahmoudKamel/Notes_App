package com.example.firebase_learn.data.sharedPref

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefApp @Inject constructor(private var sharedPreferences: SharedPreferences) {


    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun clearSharedPref() {
        sharedPreferences.edit().clear().apply()
    }
}