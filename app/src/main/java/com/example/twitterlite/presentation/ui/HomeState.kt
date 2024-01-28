package com.example.twitterlite.presentation.ui

import com.example.twitterlite.domain.model.Post
import com.example.twitterlite.presentation.ui.model.PostUI
import java.io.File

data class HomeState(
    var posts: List<PostUI> = listOf()
)