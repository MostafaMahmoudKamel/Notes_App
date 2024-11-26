package com.example.firebase_learn.presentition.navigation

import LoginScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.firebase_learn.presentition.add.AddScreen
import com.example.firebase_learn.presentition.home.HomeScreen
import com.example.firebase_learn.presentition.register.RegisterScreen
import com.example.firebase_learn.presentition.splash.SplashScreen
import com.example.firebase_learn.presentition.update.UpdateScreen

@Composable
fun AppNavHost() {
    Column {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Splash) {

            composable<Splash> {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(Home){
                            popUpTo(Splash){ //remove Splash
                                inclusive=true
                            }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Login){
                            popUpTo(Splash){
                                inclusive=true
                            }
                        }

                    })
            }
            composable<Register> {
                RegisterScreen(onNavigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(Register){//remove Register
                            inclusive=true
                        }
                    }
                }
                )
            }
            composable<Login> {
                LoginScreen(
                    oncNavigateToHome = {
                        navController.navigate(Home) {
                            popUpTo(Login) { inclusive = true }//remove login
                        }

                    },
                    onNavigateToRegister = { navController.navigate(Register){
                        popUpTo(Login){ inclusive=true }
                    } }
                )
            }
            composable<Home> {
                HomeScreen(
                    onNavigateToAdd = {
                        navController.navigate(Add){
                            popUpTo(Add){inclusive=true}
                        }

                    }, onNavigateToLogin = {
                        navController.navigate(Login) {
                            popUpTo(Home) { inclusive = true }
                        }
                    },
                    onNavigateToUpdate = { noteId ->
                        navController.navigate(Update(noteId)) {//Pass data
//                            popUpTo(Update){inclusive=true}
                        }
                    }
                )
            }
            composable<Add> {
                AddScreen(onNavigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(Home){inclusive=true}
                    }
                })
            }
            composable<Update> { backUpData ->
                val res = backUpData.toRoute<Update>() //res of passed noteIdA

                UpdateScreen(noteId = res.noteId,
                    onNavigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(Home){inclusive=true}
                    }
                })
            }
        }
    }

}