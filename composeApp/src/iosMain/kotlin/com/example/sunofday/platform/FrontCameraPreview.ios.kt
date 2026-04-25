@file:OptIn(ExperimentalForeignApi::class)

package com.example.sunofday.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readValue
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionFront
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.defaultDeviceWithDeviceType
import platform.AVFoundation.requestAccessForMediaType
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSError
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIColor
import platform.UIKit.UIView

@Composable
actual fun FrontCameraPreview(modifier: Modifier) {
    var permissionGranted by remember {
        mutableStateOf(
            AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
                == AVAuthorizationStatusAuthorized
        )
    }

    DisposableEffect(Unit) {
        val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        if (status == AVAuthorizationStatusNotDetermined) {
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                NSOperationQueue.mainQueue().addOperationWithBlock {
                    permissionGranted = granted
                }
            }
        }
        onDispose { }
    }

    if (permissionGranted) {
        val cameraView = remember { CameraPreviewUIView() }
        @Suppress("DEPRECATION")
        UIKitView(
            factory = { cameraView },
            onRelease = { it.stopSession() },
            modifier = modifier
        )
    } else {
        IosCameraPermissionPlaceholder(modifier = modifier)
    }
}

private class CameraPreviewUIView : UIView(frame = CGRectZero.readValue()) {
    private val captureSession = AVCaptureSession()
    private var previewLayer: AVCaptureVideoPreviewLayer? = null

    init {
        backgroundColor = UIColor.blackColor
        setupSession()
    }

    private fun setupSession() {
        val device = AVCaptureDevice.defaultDeviceWithDeviceType(
            AVCaptureDeviceTypeBuiltInWideAngleCamera,
            AVMediaTypeVideo,
            AVCaptureDevicePositionFront
        ) ?: return

        val input = memScoped {
            val errorRef = alloc<ObjCObjectVar<NSError?>>()
            AVCaptureDeviceInput.deviceInputWithDevice(device, errorRef.ptr)
        } ?: return

        if (captureSession.canAddInput(input)) captureSession.addInput(input)

        val layer = AVCaptureVideoPreviewLayer(session = captureSession)
        layer.videoGravity = AVLayerVideoGravityResizeAspectFill
        this.layer.addSublayer(layer)
        previewLayer = layer

        NSOperationQueue.new()?.addOperationWithBlock {
            this.captureSession.startRunning()
        }
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        previewLayer?.frame = this.bounds
    }

    fun stopSession() {
        NSOperationQueue.new()?.addOperationWithBlock {
            this.captureSession.stopRunning()
        }
    }
}

@Composable
private fun IosCameraPermissionPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Color(0xFFFFF3E0)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "☀️\nРазреши камеру,\nчтобы увидеть\nсолнышко",
            fontSize = 14.sp,
            color = Color(0xFF5D4037),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}
