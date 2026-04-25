package com.example.sunofday.ui.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunofday.platform.FrontCameraPreview
import com.example.sunofday.state.SunshineScreenState

private val SunYellow    = Color(0xFFFFD700)
private val CircleBorder = Color(0xFFFFE082)
private val SunOrange    = Color(0xFFFFB347)

@Composable
fun SunshineCircle(
    state: SunshineScreenState,
    size: Dp = 260.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "circleAnim")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circleScale"
    )

    val circleScale = if (state is SunshineScreenState.Loading) scale else 1f

    Box(
        modifier = modifier
            .size(size)
            .scale(circleScale)
            .shadow(12.dp, CircleShape, ambientColor = SunOrange.copy(alpha = 0.3f))
            .clip(CircleShape)
            .border(3.dp, CircleBorder, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is SunshineScreenState.Initial,
            is SunshineScreenState.Loading -> SunPlaceholder(modifier = Modifier.fillMaxSize())
            is SunshineScreenState.Result  -> FrontCameraPreview(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun SunPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawSunRays(this)
        }
        Text(
            text = "☀️",
            fontSize = 72.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun drawSunRays(scope: DrawScope) {
    val cx = scope.size.width / 2f
    val cy = scope.size.height / 2f
    val rayLen = scope.size.minDimension * 0.12f
    val rayWidth = scope.size.minDimension * 0.04f
    val rayOrbit = scope.size.minDimension * 0.36f + rayLen * 0.3f

    for (i in 0 until 8) {
        scope.rotate(i * 45f, pivot = Offset(cx, cy)) {
            val path = Path().apply {
                moveTo(cx - rayWidth / 2f, cy - rayOrbit)
                lineTo(cx + rayWidth / 2f, cy - rayOrbit)
                lineTo(cx + rayWidth * 0.3f, cy - rayOrbit - rayLen)
                lineTo(cx - rayWidth * 0.3f, cy - rayOrbit - rayLen)
                close()
            }
            drawPath(path, SunYellow.copy(alpha = 0.5f))
        }
    }
}
