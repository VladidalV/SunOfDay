package com.example.sunofday.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunofday.platform.RequestCameraPermission
import com.example.sunofday.platform.rememberMagicSoundPlayer
import com.example.sunofday.state.SunshineScreenController
import com.example.sunofday.state.SunshineScreenState
import com.example.sunofday.ui.components.CuteDecorations
import com.example.sunofday.ui.components.GifImage
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
    val playMagicSound = rememberMagicSoundPlayer()

    val screenWidthDp = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }

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
                onClick = {
                    if (state is SunshineScreenState.Initial) playMagicSound()
                    controller.onButtonClick(scope)
                }
            )
        }

        CustomAnimatedVisibility(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(15.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFFFD700), CircleShape),
            state = state,
            path = "files/pes.gif",
            gifSize = 150,
        )

        CustomAnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 15.dp, vertical = 10.dp),
            state = state,
            path = "files/pushka.gif",
            gifSize = 250,
        )

        CustomAnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Center),
            state = state,
            path = "files/confetti.gif",
            gifSize = screenWidthDp.value.toInt(),
        )
    }
}

@Composable
private fun CustomAnimatedVisibility(
    modifier: Modifier = Modifier,
    state: SunshineScreenState,
    path: String,
    gifSize: Int,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = state is SunshineScreenState.Result,
        enter = fadeIn(tween(400)),
        exit = fadeOut(tween(300)),
    ) {
        GifImage(
            modifier = Modifier.size(gifSize.dp),
            path = path,
        )
    }
}
