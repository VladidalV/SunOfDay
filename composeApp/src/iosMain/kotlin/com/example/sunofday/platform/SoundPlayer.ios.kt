@file:OptIn(ExperimentalForeignApi::class)

package com.example.sunofday.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.AVFoundation.AVAudioPlayer
import platform.Foundation.NSBundle
import platform.Foundation.NSURL

private const val RESOURCE_DIR =
    "compose-resources/sunofday.composeapp.generated.resources/files"

@Composable
actual fun rememberMagicSoundPlayer(): () -> Unit {
    val player = remember {
        val path = NSBundle.mainBundle.pathForResource(
            name = "magic",
            ofType = "m4a",
            inDirectory = RESOURCE_DIR
        ) ?: return@remember null
        val url = NSURL.fileURLWithPath(path)
        memScoped {
            val errorPtr = alloc<ObjCObjectVar<platform.Foundation.NSError?>>()
            AVAudioPlayer(contentsOfURL = url, error = errorPtr.ptr)
        }.also { it.prepareToPlay() }
    }
    return { player?.play() }
}
