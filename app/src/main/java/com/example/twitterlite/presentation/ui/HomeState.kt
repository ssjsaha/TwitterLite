package com.example.twitterlite.presentation.ui

import com.example.twitterlite.presentation.ui.model.PostUI

data class HomeState(
    var posts: List<PostUI> = listOf(),
    var error: String? = null,
    var loading: Boolean = false,
    var logout: Boolean = false,
    var refreshing: Boolean = false
)