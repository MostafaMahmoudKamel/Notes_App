package com.example.firebase_learn.domain.usecase.dataStoreUsecase

import com.example.firebase_learn.domain.repository.DataStoreRepository
import javax.inject.Inject

class GetBooleanUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    suspend operator fun invoke(): Boolean {
        return dataStoreRepository.getBoolean()
    }
}