package com.example.twitterlite.presentation.ui

import com.example.twitterlite.domain.model.User


sealed class LoginPageEvent {
    object NavigateToSignUp : LoginPageEvent()
    data class AttemptLogin(val user: User) : LoginPageEvent()
}