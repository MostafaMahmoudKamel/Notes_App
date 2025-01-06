package com.example.firebase_learn.data.repository

import com.example.firebase_learn.data.model.User
import com.example.firebase_learn.domain.repository.UserRepository
import com.example.firebase_learn.utils.Collections
import com.example.firebase_learn.utils.UiResource
import com.example.firebase_learn.utils.wrapWithFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
//    private val sharedPrefApp: SharedPrefApp //
//    private val saveBooleanPreferenceUseCase: SaveBooleanPreferenceUseCase,
//    private val getBooleanPreference: GetBooleanPreference,
//    private val clearPreferenceUseCase: ClearPreferenceUseCase
) : UserRepository {
    override suspend fun signIn(email: String, password: String): Flow<UiResource<Boolean>> =
        wrapWithFlow {

            firebaseAuth.signInWithEmailAndPassword(email, password).await()

            //shared preferences
//            saveBooleanPreferenceUseCase(SharedPrefKeys.isLogin, true)
            true
        }

    override suspend fun register(
        name: String,
        email: String,
        validPassword:String,
        password: String
    ): Flow<UiResource<Boolean>> = wrapWithFlow {

        //add userInfo in firebaseFireStore
        if(validPassword==password) {
            val authRes = firebaseAuth.createUserWithEmailAndPassword(email, password).await()// await
            val uid = authRes.user?.uid ?: throw Exception("User id not found")//userId
            val user = User(name = name, email = email)
            firestore.collection(Collections.users).document(uid).set(user)
            true
        }else{
            throw(Exception("password and valid Password aren't Same"))
        }

    }


    override suspend fun logout(): Flow<UiResource<Boolean>> = wrapWithFlow {
        firebaseAuth.signOut()//logout
//        sharedPrefApp.clearSharedPref() //clear sharedPerf
//        clearPreferenceUseCase()

        true

    }


    //get information about the user name,email,uid
    override suspend fun getUserData(): Flow<UiResource<User>> = wrapWithFlow {
        val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val document = firestore.collection(Collections.users).document(uid).get().await()
        val user = document.toObject(User::class.java)
        println("user is $user") //
        user ?: User(name = "NullUser")

    }



}