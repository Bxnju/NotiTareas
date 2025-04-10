package com.benchopo.notitareas.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@Composable
fun rememberSnackbarHostState(): SnackbarHostState {
    return remember { SnackbarHostState() }
}

@Composable
fun SnackbarComponent(snackbarHostState: SnackbarHostState) {
    val snackbarData = snackbarHostState.currentSnackbarData
    var isVisible by remember { mutableStateOf(false) }
    var dismissed by remember { mutableStateOf(false) }

    LaunchedEffect(snackbarData) {
        if (snackbarData != null) {
            isVisible = true
            delay(3000)
            if (!dismissed) {
                isVisible = false
                snackbarData.dismiss()
            }
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp)
            .zIndex(1f)
    ) {
        snackbarData?.let {
            Surface(
                shape = RoundedCornerShape(30.dp),
                tonalElevation = 10.dp,
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (kotlin.math.abs(dragAmount) > 50) {
                                dismissed = true
                                isVisible = false
                                snackbarData.dismiss()
                            }
                        }
                    }
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = it.visuals.message,
                    modifier = Modifier
                        .padding(16.dp),
                    color = Color.White
                )
            }
        }
    }
}
