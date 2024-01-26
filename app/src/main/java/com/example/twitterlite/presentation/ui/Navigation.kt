package com.example.twitterlite.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.twitterlite.presentation.viewmodel.LoginViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navHost =
        NavHost(navController = navController, startDestination = "auth") {
            navigation(
                startDestination = "login",
                route = "auth"
            ) {
                composable(route = "login") {
                    val viewModel =
                        it.sharedViewModel<LoginViewModel>(navController = navController)
                    LoginPage(viewModel::onEvent, viewModel.loginStateFlow, navController)
                }
            }
            navigation(
                startDestination = "main",
                route = "home"
            ){
                composable(route = "main"){
                    HomePage()
                }
            }
        }
}