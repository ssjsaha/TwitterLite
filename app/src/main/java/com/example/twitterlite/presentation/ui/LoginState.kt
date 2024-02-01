package com.example.twitterlite.presentation.ui

data class LoginState(
    var isLoading: Boolean = false,
    var isLoggedIn: Boolean = false,
    var error: String = "",
    var redirectToHome: Boolean = false
)