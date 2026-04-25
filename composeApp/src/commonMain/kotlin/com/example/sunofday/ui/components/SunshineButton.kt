package com.example.sunofday.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunofday.state.SunshineScreenState

private val ButtonOrange = Color(0xFFFFB347)
private val ButtonDisabled = Color(0xFFFFD699)

@Composable
fun SunshineButton(
    state: SunshineScreenState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEnabled = state !is SunshineScreenState.Loading
    val label = when (state) {
        is SunshineScreenState.Initial -> "Узнать"
        is SunshineScreenState.Loading -> "Секундочку…"
        is SunshineScreenState.Result  -> "Ещё раз"
    }

    Button(
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonOrange,
            disabledContainerColor = ButtonDisabled,
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        modifier = modifier
            .widthIn(min = 180.dp)
            .height(56.dp)
    ) {
        AnimatedContent(targetState = label, label = "buttonLabel") { text ->
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
