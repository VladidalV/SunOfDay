package com.example.sunofday.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.sunofday.platform.rememberHasCameraPermission
import com.example.sunofday.platform.rememberMagicSoundPlayer
import com.example.sunofday.state.SunshineScreenController
import com.example.sunofday.state.SunshineScreenState
import com.example.sunofday.ui.components.CuteDecorations
import com.example.sunofday.ui.components.GifImage
import com.example.sunofday.ui.components.SunshineButton
import com.example.sunofday.ui.components.SunshineCircle
import kotlinx.coroutines.delay

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
    val hasCameraPermission = rememberHasCameraPermission()

    var showPermissionWarning by remember { mutableStateOf(false) }

    LaunchedEffect(showPermissionWarning) {
        if (showPermissionWarning) {
            delay(2500)
            showPermissionWarning = false
        }
    }

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
                label = "titleAnim",
                modifier = Modifier.height(80.dp)
            ) { title ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextBrown,
                        textAlign = TextAlign.Center,
                        lineHeight = 34.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

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

            Spacer(modifier = Modifier.height(52.dp))

            SunshineButton(
                state = state,
                onClick = {
                    if (state is SunshineScreenState.Initial && !hasCameraPermission) {
                        showPermissionWarning = true
                    } else {
                        if (state is SunshineScreenState.Initial) playMagicSound()
                        controller.onButtonClick(scope)
                    }
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

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 32.dp, vertical = 16.dp),
            visible = showPermissionWarning,
            enter = fadeIn(tween(250)) + slideInVertically { it / 2 },
            exit = fadeOut(tween(250)) + slideOutVertically { it / 2 },
        ) {
            Box(
                modifier = Modifier
                    .background(TextBrown.copy(alpha = 0.92f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Разреши доступ к камере\nв настройках телефона",
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }
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
