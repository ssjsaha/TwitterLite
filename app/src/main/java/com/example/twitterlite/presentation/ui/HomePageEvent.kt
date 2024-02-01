package com.example.twitterlite.presentation.ui

import java.io.File

sealed class HomePageEvent {
    data class UploadPost(val file: File?, val text: String,val userName:String="") : HomePageEvent()
    object Logout : HomePageEvent()
}