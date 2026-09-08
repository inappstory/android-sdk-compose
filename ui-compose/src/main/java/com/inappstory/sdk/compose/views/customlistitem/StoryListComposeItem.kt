package com.inappstory.sdk.compose.views.customlistitem

import android.content.Context
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import java.util.UUID

class StoryListComposeItem(context: Context) : FrameLayout(context) {
    val uniqueId = UUID.randomUUID().toString()
    fun initializeComposable(content: @Composable () -> Unit) {
        isClickable = false
        addView(ComposeView(context).apply {
            isClickable = false
            setContent(content)
        })
    }
}