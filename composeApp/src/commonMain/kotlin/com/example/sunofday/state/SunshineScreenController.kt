package com.example.sunofday.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SunshineScreenController {
    var state: SunshineScreenState by mutableStateOf(SunshineScreenState.Initial)
        private set

    fun onButtonClick(scope: CoroutineScope) {
        if (state is SunshineScreenState.Loading) return
        state = SunshineScreenState.Loading
        scope.launch {
            delay(2000L)
            state = SunshineScreenState.Result
        }
    }
}
