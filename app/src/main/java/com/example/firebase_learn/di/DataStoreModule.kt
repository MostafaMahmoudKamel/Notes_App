package com.example.firebase_learn.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.firebase_learn.data.dataStore.DataStoreHelper
import com.example.firebase_learn.data.dataStore.DataStoreRepositoryImpl
import com.example.firebase_learn.data.dataStore.DataStoreSerialize
import com.example.firebase_learn.domain.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object DataStoreModule {

    private val Context.dataStore by dataStore(
        "Setting.json",
        DataStoreSerialize
    )

    @Provides
    @Singleton
    fun provideDataStoreSerializer(): DataStoreSerialize {
        return DataStoreSerialize
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<DataStoreHelper> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStore: DataStore<DataStoreHelper>): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore)
    }

}