package com.example.twitterlite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterlite.domain.model.User
import com.example.twitterlite.domain.repository.LoginRepository
import com.example.twitterlite.presentation.ui.LoginPageEvent
import com.example.twitterlite.presentation.ui.LoginState
import com.example.twitterlite.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    private var _loginStateFlow: MutableStateFlow<LoginState> = MutableStateFlow(
        LoginState()
    )
    val loginStateFlow = _loginStateFlow.asStateFlow()
    fun tryLogin(user: User) {
        viewModelScope.launch {
            val res = repository.isLoginSuccess(user)
            when (res) {
                is Resource.Success -> {
                    _loginStateFlow.value = _loginStateFlow.value.copy(
                        isLoggedIn = true,
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    _loginStateFlow.value = _loginStateFlow.value.copy(
                        isLoggedIn = false,
                        isLoading = false,
                        error = res.message ?: "Something went wrong"
                    )
                }

                is Resource.ErrorResponse -> {
                    _loginStateFlow.value = _loginStateFlow.value.copy(
                        isLoggedIn = false,
                        isLoading = false,
                        error = res.errorResponse?.message ?: "Something went wrong"
                    )
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    fun onEvent(event: LoginPageEvent) {
        when (event) {
            is LoginPageEvent.AttemptLogin -> {
                _loginStateFlow.value = _loginStateFlow.value.copy(
                    isLoading = true
                )
                tryLogin(event.user)
            }

            is LoginPageEvent.NavigateToSignUp -> {

            }
        }
    }
}