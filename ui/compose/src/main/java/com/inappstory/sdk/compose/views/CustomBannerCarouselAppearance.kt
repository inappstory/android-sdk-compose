package com.inappstory.sdk.compose.views

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.inappstory.sdk.banners.ui.carousel.DefaultBannerCarouselAppearance

class CustomBannerCarouselAppearance(val placeholder: (@Composable () -> Unit)? = null) :
    DefaultBannerCarouselAppearance() {
    override fun nextBannerOffset(): Int {
        return 20
    }

    override fun prevBannerOffset(): Int {
        return 20
    }

    override fun bannersOnScreen(): Int {
        return 1
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

    override fun loop(): Boolean {
        return true
    }
}