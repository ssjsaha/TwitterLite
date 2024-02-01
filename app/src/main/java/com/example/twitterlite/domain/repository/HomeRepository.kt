package com.example.twitterlite.domain.repository

import com.example.twitterlite.domain.model.Post
import com.example.twitterlite.utils.Resource

interface HomeRepository {
    suspend fun uploadPost(post: Post): Resource<Boolean>
    suspend fun getAllPosts(): Resource<List<Post>>

    suspend fun logout(): Boolean
}