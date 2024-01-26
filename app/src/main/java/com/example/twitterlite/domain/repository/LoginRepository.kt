package com.example.twitterlite.domain.repository

import com.example.twitterlite.domain.model.User
import com.example.twitterlite.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun isLoginSuccess(user:User): Resource<Boolean>
}