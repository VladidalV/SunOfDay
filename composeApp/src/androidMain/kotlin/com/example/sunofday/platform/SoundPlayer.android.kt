package com.example.sunofday.platform

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private const val ASSET_PATH =
    "composeResources/sunofday.composeapp.generated.resources/files/magic.m4a"

@Composable
actual fun rememberMagicSoundPlayer(): () -> Unit {
    val context = LocalContext.current
    val player = remember {
        runCatching {
            MediaPlayer().apply {
                val afd = context.assets.openFd(ASSET_PATH)
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepare()
            }
        }.getOrNull()
    }
    DisposableEffect(Unit) {
        onDispose { player?.release() }
    }
    return {
        player?.let {
            if (it.isPlaying) it.seekTo(0) else it.start()
        }
    }
}
