package com.example.firebase_learn.domain.usecase.userUseCase

import com.example.firebase_learn.data.model.UiResource
import com.example.firebase_learn.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(name:String,email: String,phone:String, password: String): Flow<UiResource<Boolean>> {
        return userRepository.register(name = name,email=email,phone=phone , password = password)
    }
}