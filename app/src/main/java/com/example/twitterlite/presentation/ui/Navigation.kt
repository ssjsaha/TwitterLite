package com.example.twitterlite.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.twitterlite.presentation.viewmodel.HomeViewModel
import com.example.twitterlite.presentation.viewmodel.LoginViewModel
import com.example.twitterlite.utils.Constants.ROUTE_AUTH
import com.example.twitterlite.utils.Constants.ROUTE_HOME
import com.example.twitterlite.utils.Constants.ROUTE_LOGIN
import com.example.twitterlite.utils.Constants.ROUTE_MAIN
import com.example.twitterlite.utils.Constants.ROUTE_POST
import com.example.twitterlite.utils.Constants.ROUTE_SIGNUP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ROUTE_AUTH) {
        navigation(
            startDestination = ROUTE_LOGIN,
            route = ROUTE_AUTH
        ) {
            composable(route = ROUTE_LOGIN) {
                val viewModel =
                    it.sharedViewModel<LoginViewModel>(navController = navController)
                viewModel.isAlreadyLoggedIn()
                LoginPage(viewModel::onEvent, viewModel.loginStateFlow, navController)
            }
            composable(route = ROUTE_SIGNUP) {
                val viewModel =
                    it.sharedViewModel<LoginViewModel>(navController = navController)
                viewModel.isAlreadyLoggedIn()

                SignupPage(viewModel::onEvent, viewModel.loginStateFlow, navController)
            }
        }
        navigation(
            startDestination = ROUTE_MAIN,
            route = ROUTE_HOME
        ) {
            composable(route = ROUTE_MAIN) {
                val viewModel =
                    it.sharedViewModel<HomeViewModel>(navController = navController)
                HomePage(viewModel.homeStateFlow, viewModel::onEvent, navController)
            }
            composable(route = ROUTE_POST) {
                val viewModel =
                    it.sharedViewModel<HomeViewModel>(navController = navController)
                AddPostPage(viewModel::onEvent, navController)
            }
        }
    }
}