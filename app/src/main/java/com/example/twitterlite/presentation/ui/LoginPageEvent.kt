package com.example.twitterlite.presentation.ui

import com.example.twitterlite.domain.model.User


sealed class LoginPageEvent {
    data class AttemptLogin(val user: User) : LoginPageEvent()
    data class AttemptSignup(val user: User) : LoginPageEvent()
}