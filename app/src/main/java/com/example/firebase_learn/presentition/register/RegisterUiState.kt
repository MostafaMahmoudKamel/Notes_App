package com.example.firebase_learn.presentition.register

import com.example.firebase_learn.data.model.User

data class RegisterUiState(
    var isLoading: Boolean = false,

    var name:String="",
    var nameError:Boolean=false,

    var email: String = "",
    var emailError:Boolean=false,

    var password:String="",
    var passwordError:Boolean=false,
    var isPasswordVisible:Boolean=false,//icon hidden password


    var confirmPassword: String = "",
    var confirmPasswordError:Boolean=false,
    var isConfirmPasswordVisible: Boolean=false,

    var errorMessage: String = "",//firebase Error
    var userInfo: User = User(name = "")
)

