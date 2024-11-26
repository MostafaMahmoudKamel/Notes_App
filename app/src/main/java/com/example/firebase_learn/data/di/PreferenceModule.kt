package com.example.firebase_learn.data.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module //the first running of application any thing with annotation @Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Singleton
    @Provides //this module will replace SharedPreferences with the returned value
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

}