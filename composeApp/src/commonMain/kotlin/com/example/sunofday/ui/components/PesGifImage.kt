package com.example.sunofday.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import sunofday.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PesGifImage(modifier: Modifier = Modifier) {
    AsyncImage(
        model = Res.getUri("files/pes.gif"),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .border(2.dp, Color(0xFFFFD700), CircleShape)
    )
}
