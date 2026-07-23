package com.inappstory.sdk.compose.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.FragmentActivity
import com.inappstory.sdk.compose.R
import com.inappstory.sdk.compose.controllers.FragmentSettings
import com.inappstory.sdk.compose.controllers.InAppMessageFragmentScreenController
import com.inappstory.sdk.compose.databinding.IasDefaultIamFragmentBinding

@Composable
fun InAppMessageFragmentScreen(screenController: InAppMessageFragmentScreenController) {
    val context = LocalContext.current
    if (context !is FragmentActivity) return
    AndroidViewBinding(
        modifier = Modifier.fillMaxSize(),
        factory = IasDefaultIamFragmentBinding::inflate
    )
    DisposableEffect(true) {
        screenController.getSettings = {
            FragmentSettings(
                fragmentId = R.id.ias_default_iam_fragment_container,
                fragmentManager = context.supportFragmentManager
            )
        }
        onDispose {
            screenController.getSettings = null
        }
    }
}