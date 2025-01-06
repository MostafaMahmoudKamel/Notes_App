package com.example.firebase_learn.domain.repository

interface DataStoreRepository {
    suspend fun updateBoolean(value: Boolean)
    suspend fun getBoolean(): Boolean
}