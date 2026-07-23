package com.inappstory.sdk.compose.screens

import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.inappstory.sdk.compose.controllers.InAppMessageScreenController

@Composable
fun InAppMessageScreen(screenController: InAppMessageScreenController) {
    val context = LocalContext.current
    val toastLayout = remember {
        val layout = FrameLayout(context)
        layout
    }
    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { ctx ->
            toastLayout
        }
    )
    DisposableEffect(true) {
        screenController.getLayout = { toastLayout }
        onDispose {
            screenController.getLayout = null
        }
    }
}