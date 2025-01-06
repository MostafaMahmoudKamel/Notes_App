package com.example.firebase_learn.data.dataStore

import androidx.datastore.core.DataStore
import com.example.firebase_learn.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(private val dataStore: DataStore<DataStoreHelper>) :
    DataStoreRepository {

    override suspend fun updateBoolean(value: Boolean) {
        dataStore.updateData {
            it.copy(isLogin = value)
        }
    }

    override suspend fun getBoolean(): Boolean {
        return dataStore.data.first().isLogin
    }
}