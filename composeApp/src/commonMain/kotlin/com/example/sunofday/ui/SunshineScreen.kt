package com.example.sunofday.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunofday.platform.RequestCameraPermission
import com.example.sunofday.state.SunshineScreenController
import com.example.sunofday.state.SunshineScreenState
import com.example.sunofday.ui.components.CuteDecorations
import com.example.sunofday.ui.components.PesGifImage
import com.example.sunofday.ui.components.SunshineButton
import com.example.sunofday.ui.components.SunshineCircle

private val BgTop     = Color(0xFFFFF8F0)
private val BgBottom  = Color(0xFFFFEDD8)
private val TextBrown = Color(0xFF5D4037)

private val CIRCLE_SIZE      = 260.dp
private val DECORATIONS_SIZE = 330.dp

@Composable
fun SunshineScreen() {
    RequestCameraPermission()

    val controller = remember { SunshineScreenController() }
    val scope = rememberCoroutineScope()
    val state = controller.state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgTop, BgBottom)))
            .windowInsetsPadding(WindowInsets.safeContent),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = when (state) {
                    is SunshineScreenState.Result -> "Это ты"
                    else -> "Узнай, кто сегодня\nсолнышко дня"
                },
                transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(300)) },
                label = "titleAnim"
            ) { title ->
                Text(
                    text = title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBrown,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Box(contentAlignment = Alignment.Center) {
                CuteDecorations(
                    state = state,
                    modifier = Modifier.size(DECORATIONS_SIZE)
                )
                SunshineCircle(
                    state = state,
                    size = CIRCLE_SIZE
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            SunshineButton(
                state = state,
                onClick = { controller.onButtonClick(scope) }
            )
        }

        AnimatedVisibility(
            visible = state is SunshineScreenState.Result,
            enter = fadeIn(tween(400)),
            exit = fadeOut(tween(300)),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(30.dp)
        ) {
            PesGifImage(modifier = Modifier.size(120.dp))
        }
    }
}
