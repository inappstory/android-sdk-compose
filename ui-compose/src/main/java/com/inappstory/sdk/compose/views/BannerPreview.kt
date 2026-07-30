package com.inappstory.sdk.compose.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.inappstory.sdk.AppearanceManager
import com.inappstory.sdk.banners.BannerCarouselNavigationCallback
import com.inappstory.sdk.banners.BannerData
import com.inappstory.sdk.banners.BannerPlaceLoadCallback
import com.inappstory.sdk.banners.ui.carousel.BannerCarousel
import com.inappstory.sdk.banners.ui.carousel.BannerPreview
import com.inappstory.sdk.compose.controllers.BannerCarouselController
import com.inappstory.sdk.compose.controllers.BannerPreviewController
import com.inappstory.sdk.core.banners.ICustomBannerCarouselAppearance

@Composable
fun BannerPreview(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    bannerPreviewController: BannerPreviewController,
    bannerId: String = "",
    uniqueId: String = "${bannerId}_preview_place",
    appearance: ICustomBannerCarouselAppearance = CustomBannerCarouselAppearance(),
    pageScrolled: (
        current: Int,
        total: Int,
        offsetFraction: Float,
        offsetInPx: Int
    ) -> Unit = { _, _, _, _ -> },
    pageSelected: (
        current: Int,
        total: Int
    ) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    if (bannerId.isEmpty()) return
    val bannerPreviewWidget: BannerPreview = remember {
        BannerPreview(context).apply {
            setAppearanceManager(
                AppearanceManager().csBannerCarouselInterface(appearance)
            )
            bannerPreviewController.loadPreview = {
                this.loadBanners()
            }
            bannerPreviewController.reloadPreview = {
                this.loadBanners(true)
            }
            loadCallback(object : BannerPlaceLoadCallback() {
                override fun bannerPlaceLoaded(
                    size: Int,
                    bannerData: List<BannerData?>?,
                    widgetHeight: Int
                ) {
                    bannerPreviewController.bannerPlaceLoaded(
                        size,
                        bannerData,
                        widgetHeight
                    )
                }

                override fun loadError() {
                    bannerPreviewController.loadError()
                }

                override fun bannerLoaded(bannerId: Int, isCurrent: Boolean) {
                    bannerPreviewController.bannerLoaded(
                        bannerId,
                        isCurrent
                    )
                }

                override fun bannerLoadError(bannerId: Int, isCurrent: Boolean) {
                    bannerPreviewController.bannerLoadError(
                        bannerId,
                        isCurrent
                    )
                }
            })
        }
    }
    bannerPreviewWidget.uniqueId(uniqueId)
    bannerPreviewWidget.setPlaceId(bannerId)
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            bannerPreviewWidget
        }
    )
    LaunchedEffect(true) {
        bannerPreviewWidget.loadBanners()
    }
}