package com.example.twitterlite.presentation.ui

import AddPostPage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.twitterlite.presentation.viewmodel.HomeViewModel
import com.example.twitterlite.presentation.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            ) {
                composable(route = "main") {
                    val viewModel =
                        it.sharedViewModel<HomeViewModel>(navController = navController)
                    HomePage(viewModel.homeStateFlow, viewModel::onEvent, navController)
                }
                composable(route = "post") {
                    val viewModel =
                        it.sharedViewModel<HomeViewModel>(navController = navController)
                    AddPostPage(navController)
                }
            }
        }
}