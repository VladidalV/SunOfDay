package com.example.sunofday.state

sealed class SunshineScreenState {
    object Initial : SunshineScreenState()
    object Loading : SunshineScreenState()
    object Result : SunshineScreenState()
}
