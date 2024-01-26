package com.example.twitterlite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterlite.domain.model.User
import com.example.twitterlite.domain.repository.LoginRepository
import com.example.twitterlite.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {


     fun tryLogin() {
        val user = User("rum@gmail.com", "1234756")
        viewModelScope.launch {
            val a = repository.isLoginSuccess(user)
            when(a){
                is Resource.Success ->{
                    val ab= a
                }
                is Resource.Error -> {

                }
                is Resource.ErrorResponse -> {

                }
                is Resource.Loading -> {

                }
            }
        }
    }
}