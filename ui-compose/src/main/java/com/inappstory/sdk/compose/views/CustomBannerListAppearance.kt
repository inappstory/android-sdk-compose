package com.inappstory.sdk.compose.views

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.inappstory.sdk.banners.ui.list.DefaultBannerListAppearance

class CustomBannerListAppearance(val placeholder: (@Composable () -> Unit)? = null) :
    DefaultBannerListAppearance() {
    override fun edgeBannersPadding(): Int {
        return 8
    }

    override fun bannersGap(): Int {
        return 8
    }

    override fun cornerRadius(): Int {
        return 16
    }

    override fun loadingPlaceholder(context: Context?): View? {
        if (context == null) return null
        if (placeholder == null) return null
        return ComposeView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setContent {
                MaterialTheme {
                    placeholder.invoke()
                }
            }
        }
    }

}