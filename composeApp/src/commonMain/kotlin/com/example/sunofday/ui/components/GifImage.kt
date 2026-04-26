package com.example.sunofday.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import sunofday.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    path: String,
) {
    AsyncImage(
        model = Res.getUri(path),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}
