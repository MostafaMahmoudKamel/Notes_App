package com.example.firebase_learn.domain.usecase.userUseCase

import com.example.firebase_learn.domain.repository.UserRepository
import com.example.firebase_learn.utils.UiResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(name:String,email: String,validPassword: String, password: String): Flow<UiResource<Boolean>> {
        return userRepository.register(name = name,email=email,validPassword=validPassword ,password = password)
    }
}