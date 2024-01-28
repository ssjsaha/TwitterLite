package com.example.twitterlite.presentation.ui

import java.io.File

sealed class HomePageEvent {
    data class UploadFile(val file: File?): HomePageEvent()
}