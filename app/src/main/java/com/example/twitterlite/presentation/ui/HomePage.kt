@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.twitterlite.presentation.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.twitterlite.BuildConfig
import com.example.twitterlite.presentation.ui.model.PostUI
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.StateFlow
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    state: StateFlow<HomeState>,
    onEvent: (HomePageEvent) -> Unit,
    navController: NavController
) {
    val homeState = state.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = homeState.value.refreshing)
    if (homeState.value.logout) {
        LaunchedEffect(key1 = true) {
            navController.navigate("auth") {
                popUpTo("home") {
                    inclusive = true
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Twitter Lite") },
                actions = {
                    Text(text = "Logout", Modifier.clickable {
                        onEvent.invoke(HomePageEvent.Logout)
                    })
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("post")
            }) {
                Icon(
                    Icons.Default.Add,
                    "contentDescription",
                )
            }
        }, modifier = Modifier.padding(16.dp)
    ) {
        if (homeState.value.loading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp),
                    color = Color.Green
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(it)
            ) {
                SwipeRefresh(state = swipeRefreshState, onRefresh = { onEvent.invoke(HomePageEvent.GetAllPost) }) {
                    val posts = homeState.value.posts
                    if (posts.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            items(posts.size) { it ->
                                SinglePost(PostUI(posts[it].text, posts[it].image, posts[it].userName))
                            }

                        }
                    }
                }
            }
        }
    }
}