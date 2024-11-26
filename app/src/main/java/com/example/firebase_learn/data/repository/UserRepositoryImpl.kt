package com.example.firebase_learn.data.repository

import com.example.firebase_learn.data.model.UiResource
import com.example.firebase_learn.data.model.User
import com.example.firebase_learn.data.sharedPref.SharedPrefApp
import com.example.firebase_learn.domain.repository.UserRepository
import com.example.firebase_learn.utils.Collections
import com.example.firebase_learn.utils.SharedPrefKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sharedPrefApp: SharedPrefApp
) : UserRepository {
    override suspend fun signIn(email: String, password: String): Flow<UiResource<Boolean>> {
        return flow {
            emit(UiResource.Loading)
            delay(1000)

            try {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .await()//استنا لحد ما تخلص يباشا

//                val authRes = firebaseAuth.signInWithEmailAndPassword(email, password).await()//استنا لحد ما تخلص يباشا
//                val uid = authRes.user?.uid ?: throw Exception("User id not found")//userId

                //firestore
//                firestore.collection("Users").document(uid).set(user)

                //shared preferences
                sharedPrefApp.saveBoolean(SharedPrefKeys.isLogin, true)

                emit(UiResource.Success(true))
            } catch (e: Exception) {
                emit(UiResource.Failure(e))
            }
        }

    }

    override suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String
    ): Flow<UiResource<Boolean>> {
        return flow {
            emit(UiResource.Loading)
            delay(1000)
            try {
                //create account on firebase
                val authRes = firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .await()//require await
                val uid = authRes.user?.uid ?: throw Exception("User id not found")//userId
                val user = User(name = name, email = email, phone = phone)

                //add userInfo in firebaseFireStore
                firestore.collection(Collections.users).document(uid).set(user)

                emit(UiResource.Success(true))
            } catch (e: Exception) {
                emit(UiResource.Failure(e))
            }
        }

    }

    override suspend fun logout(): Flow<UiResource<Boolean>> {
        return flow {
            emit(UiResource.Loading)

            delay(1000)
            try {
                //logout from firebase
                firebaseAuth.signOut()
                emit(UiResource.Success(true))

                //clear shared prefereces
                sharedPrefApp.clearSharedPref()
            } catch (e: Exception) {
                emit(UiResource.Failure(e))
            }
        }
    }

    //get information about the user
    override suspend fun getUserData(): Flow<UiResource<User>> {
        val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val document = firestore.collection(Collections.users).document(uid).get().await()
        val user = document.toObject(User::class.java)
        println("user is $user") //
        return flow {
            emit(UiResource.Success(user ?: User("nullUser")))
        }


    }

}