package com.example.twitterlite.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.unit.dp
import com.example.twitterlite.presentation.ui.model.PostUI

@Composable
fun SinglePost(post: PostUI) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.AccountCircle,
                "contentDescription",
                modifier = Modifier.size(64.dp)
            )
            Text(text = post.userName)
            Spacer(modifier = Modifier
                .height(32.dp)
                .width(16.dp))
        }
        Text(text = post.text)
        val bitmap = post.image
        bitmap?.let {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                bitmap = it.asImageBitmap(),
                contentDescription = "",
            )
        }
    }
}