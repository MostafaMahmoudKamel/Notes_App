package com.example.firebase_learn.domain.usecase.userUseCase

import com.example.firebase_learn.utils.UiResource
import com.example.firebase_learn.data.model.User
import com.example.firebase_learn.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFireStoreDataUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<UiResource<User>> {
        return userRepository.getUserData()
    }
}