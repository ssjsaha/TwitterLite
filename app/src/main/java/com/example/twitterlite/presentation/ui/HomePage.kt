@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.twitterlite.presentation.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.twitterlite.BuildConfig
import com.example.twitterlite.presentation.ui.model.PostUI
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
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Twitter Lite") },
                actions = {
                    Text(text = "Logout", Modifier.clickable {
                        navController.navigate("auth") {
                            popUpTo("home") {
                                inclusive = true
                            }
                        }
                    })
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("post")
                /*       val permissionCheckResult =
                           ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                       if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                           cameraLauncher.launch(uri)
                       } else {
                           // Request a permission
                           permissionLauncher.launch(Manifest.permission.CAMERA)
                       }*/
            }) {
                Icon(
                    Icons.Default.Add,
                    "contentDescription",
                )
            }
        }, modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
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