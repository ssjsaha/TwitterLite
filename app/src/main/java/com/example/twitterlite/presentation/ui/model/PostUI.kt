package com.example.twitterlite.presentation.ui.model

import android.graphics.Bitmap

data class PostUI(
    val text: String,
    val image: Bitmap?,
    val userName: String
)