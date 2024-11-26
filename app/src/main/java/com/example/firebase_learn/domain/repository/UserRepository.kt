package com.example.firebase_learn.domain.repository

import com.example.firebase_learn.data.model.UiResource
import com.example.firebase_learn.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signIn(email: String, password: String): Flow<UiResource<Boolean>> //login
    suspend fun register(name:String,email: String,phone:String, password: String): Flow<UiResource<Boolean>> //Result
    suspend fun logout(): Flow<UiResource<Boolean>>
   suspend fun getUserData():Flow<UiResource<User>>
}