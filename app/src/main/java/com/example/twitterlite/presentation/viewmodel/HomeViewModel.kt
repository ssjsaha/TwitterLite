package com.example.twitterlite.presentation.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterlite.domain.model.Post
import com.example.twitterlite.domain.repository.HomeRepository
import com.example.twitterlite.presentation.ui.HomePageEvent
import com.example.twitterlite.presentation.ui.HomeState
import com.example.twitterlite.presentation.ui.model.PostUI
import com.example.twitterlite.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: HomeRepository) : ViewModel() {
    private var _homeStateFlow: MutableStateFlow<HomeState> = MutableStateFlow(
        HomeState()
    )
    val homeStateFlow = _homeStateFlow.asStateFlow()

    fun onEvent(event: HomePageEvent) {
        when (event) {
            is HomePageEvent.UploadPost -> {
                uploadFile(event.file, event.text, event.userName)
            }
        }
    }

    init {
        getAllPosts()
    }

    fun uploadFile(file: File?, text: String, userName: String) {
        file?.let {
            val imageStr = encodeImage(it)
            viewModelScope.launch {
                repo.uploadPost(Post(text, imageStr, userName))
            }
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            val res = repo.getAllPosts()
            when (res) {
                is Resource.Success -> {
                    res.data?.let {
                        _homeStateFlow.value = _homeStateFlow.value.copy(
                            posts = mapPostUI(it)
                        )
                    }
                }

                is Resource.Error -> {

                }

                is Resource.ErrorResponse -> {

                }
            }
        }
    }

    private fun mapPostUI(list: List<Post>): List<PostUI> {
        val listPostUI = list.map {
            PostUI(it.text, decodeImage(it.image), it.userName)
        }
        return listPostUI
    }

    private fun encodeImage(file: File): String? {
        val a = file.path
        val bm = BitmapFactory.decodeFile(file.path)
        val byteInputStr = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 50, byteInputStr)
        val b = byteInputStr.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun decodeImage(base64: String?): Bitmap? {
        val decodedString: ByteArray = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}