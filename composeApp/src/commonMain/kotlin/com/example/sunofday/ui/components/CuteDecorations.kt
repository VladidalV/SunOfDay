package com.example.sunofday.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.sunofday.state.SunshineScreenState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val SparkleGold   = Color(0xFFFFD700)
private val SparklePink   = Color(0xFFFF8FAB)
private val SparkleOrange = Color(0xFFFFB347)

@Composable
fun CuteDecorations(
    state: SunshineScreenState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "decorations")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val orbitRadius = size.minDimension / 2f - 12f

        when (state) {
            is SunshineScreenState.Initial -> {
                drawSparklesAt(cx, cy, orbitRadius, count = 8, rotationOffset = 0f, alpha = 0.35f, sparkleSize = 8f)
            }
            is SunshineScreenState.Loading -> {
                rotate(degrees = rotation, pivot = Offset(cx, cy)) {
                    drawSparklesAt(cx, cy, orbitRadius, count = 8, rotationOffset = 0f, alpha = 0.9f, sparkleSize = 12f, color = SparkleOrange)
                }
                rotate(degrees = -rotation * 0.6f, pivot = Offset(cx, cy)) {
                    drawSparklesAt(cx, cy, orbitRadius * 0.85f, count = 6, rotationOffset = 22.5f, alpha = 0.6f, sparkleSize = 7f, color = SparklePink)
                }
            }
            is SunshineScreenState.Result -> {
                drawSparklesAt(cx, cy, orbitRadius, count = 12, rotationOffset = rotation * 0.05f, alpha = alpha, sparkleSize = 14f, color = SparkleGold)
                drawSparklesAt(cx, cy, orbitRadius * 0.82f, count = 8, rotationOffset = rotation * 0.07f + 15f, alpha = alpha * 0.7f, sparkleSize = 7f, color = SparklePink)
            }
        }
    }
}

private fun DrawScope.drawSparklesAt(
    cx: Float,
    cy: Float,
    orbit: Float,
    count: Int,
    rotationOffset: Float,
    alpha: Float,
    sparkleSize: Float,
    color: Color = SparkleGold
) {
    val step = 360f / count
    for (i in 0 until count) {
        val angleDeg = i * step + rotationOffset
        val angleRad = angleDeg.toDouble() * PI / 180.0
        val x = cx + orbit * cos(angleRad).toFloat()
        val y = cy + orbit * sin(angleRad).toFloat()
        drawStar(x, y, sparkleSize, color.copy(alpha = alpha))
    }
}

private fun DrawScope.drawStar(cx: Float, cy: Float, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(cx, cy - size)
        lineTo(cx + size * 0.25f, cy - size * 0.25f)
        lineTo(cx + size, cy)
        lineTo(cx + size * 0.25f, cy + size * 0.25f)
        lineTo(cx, cy + size)
        lineTo(cx - size * 0.25f, cy + size * 0.25f)
        lineTo(cx - size, cy)
        lineTo(cx - size * 0.25f, cy - size * 0.25f)
        close()
    }
    drawPath(path, color)
}
