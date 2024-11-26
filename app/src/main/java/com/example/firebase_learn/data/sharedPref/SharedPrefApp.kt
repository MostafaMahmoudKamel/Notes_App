package com.example.firebase_learn.data.sharedPref

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefApp @Inject constructor(private var sharedPreferences: SharedPreferences) {
    //    var sh: SharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
//    fun saveBoolean() {
//        sharedPreferences.edit().putBoolean("isLogin", false).apply()
//    }
//
//    fun getBoolean(): Boolean {
//        return sharedPreferences.getBoolean("isLogin", false)
//    }

    fun saveBoolean(key:String,value:Boolean){
        sharedPreferences.edit().putBoolean(key,value).apply()
    }

    fun getBoolean(key: String,defaultValue: Boolean=false):Boolean{
        return sharedPreferences.getBoolean(key,defaultValue)
    }

    fun clearSharedPref(){
        sharedPreferences.edit().clear().apply()
    }
}