//package com.example.firebase_learn.di
//
//import android.content.Context
//import android.content.SharedPreferences
//import com.example.firebase_learn.data.sharedPref.PreferenceRepositoryImpl
//import com.example.firebase_learn.domain.repository.PreferenceRepository
//import com.example.firebase_learn.utils.Constants
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module //the first running of application any thing with annotation @Module
//@InstallIn(SingletonComponent::class)
//object PreferenceModule {
//
//    @Singleton
//    @Provides //this module will replace SharedPreferences with the returned value
//    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
//        return context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
//    }
//
//
//    @Singleton
//    @Provides//when i make inject such @Inject preferenceRepository it replace it with return
//    fun provideSharedPreferenceRepository(sharedPreferences: SharedPreferences):PreferenceRepository{
//        return PreferenceRepositoryImpl(sharedPreferences)
//    }
//
//
//}