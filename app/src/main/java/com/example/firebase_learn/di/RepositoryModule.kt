package com.example.firebase_learn.di

import com.example.firebase_learn.data.repository.NoteRepositoryImpl
import com.example.firebase_learn.data.repository.UserRepositoryImpl
import com.example.firebase_learn.domain.repository.NoteRepository
import com.example.firebase_learn.domain.repository.UserRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(firebaseAuth,firestore)
    }
//@Provides
//@Singleton
//fun provideUserRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore,saveBooleanPreference: SaveBooleanPreference,clearBooleanPreference: ClearBooleanPreference): UserRepository {
//    return UserRepositoryImpl(firebaseAuth,firestore,saveBooleanPreference,clearBooleanPreference)
//}

    @Provides
    @Singleton
    fun provideNoteRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore):NoteRepository{
        return NoteRepositoryImpl(firebaseAuth,firestore)
    }

//    @Binds
//    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}