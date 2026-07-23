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
import com.inappstory.sdk.compose.controllers.BannerCarouselController
import com.inappstory.sdk.core.banners.ICustomBannerCarouselAppearance

@Composable
fun BannerCarousel(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    bannerCarouselController: BannerCarouselController,
    placeId: String,
    uniqueId: String? = null,
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
    if (placeId.isEmpty()) return
    val uId = uniqueId ?: (placeId + "_carousel_id")
    val bannerPlaceWidget: BannerCarousel = remember {
        BannerCarousel(context).apply {
            setAppearanceManager(
                AppearanceManager().csBannerCarouselInterface(appearance)
            )
            bannerCarouselController.loadList = {
                this.loadBanners()
            }
            bannerCarouselController.reloadList = {
                this.loadBanners(true)
            }
            bannerCarouselController.next = {
                this.showNext()
            }
            bannerCarouselController.prev = {
                this.showPrevious()
            }
            bannerCarouselController.byIndex = {
                this.showByIndex(it)
            }
            bannerCarouselController.autoscrollOn = {
                this.resumeAutoscroll()
            }
            bannerCarouselController.autoscrollOff = {
                this.pauseAutoscroll()
            }
            loadCallback(object : BannerPlaceLoadCallback() {
                override fun bannerPlaceLoaded(
                    size: Int,
                    bannerData: List<BannerData?>?,
                    widgetHeight: Int
                ) {
                    bannerCarouselController.bannerPlaceLoaded(
                        size,
                        bannerData,
                        widgetHeight
                    )
                }

                override fun loadError() {
                    bannerCarouselController.loadError()
                }

                override fun bannerLoaded(bannerId: Int, isCurrent: Boolean) {
                    bannerCarouselController.bannerLoaded(
                        bannerId,
                        isCurrent
                    )
                }

                override fun bannerLoadError(bannerId: Int, isCurrent: Boolean) {
                    bannerCarouselController.bannerLoadError(
                        bannerId,
                        isCurrent
                    )
                }
            })
            navigationCallback(object : BannerCarouselNavigationCallback {
                override fun onPageScrolled(
                    position: Int,
                    total: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    pageScrolled(
                        position,
                        total,
                        positionOffset,
                        positionOffsetPixels
                    )
                }

                override fun onPageSelected(position: Int, total: Int) {
                    pageSelected(
                        position,
                        total
                    )
                }
            })

        }
    }
    bannerPlaceWidget.uniqueId(uId)
    bannerPlaceWidget.setPlaceId(placeId)
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            bannerPlaceWidget
        }
    )
    LaunchedEffect(true) {
        bannerCarouselController.loadList()
    }
}