package com.example.firebase_learn.domain.usecase.userUseCase

import com.example.firebase_learn.utils.UiResource
import com.example.firebase_learn.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(private val userRepository: UserRepository){
    suspend operator fun invoke(): Flow<UiResource<Boolean>> {
        return userRepository.logout()
    }
}