package com.inappstory.sdk.compose.iasapi

import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.UseManagerInstanceCallback
import com.inappstory.sdk.banners.BannerData
import com.inappstory.sdk.banners.BannerPlaceLoadSettings
import com.inappstory.sdk.banners.BannerPlacePreloadCallback

class IASBanners {
    fun preloadBannerPlace(
        bannerPlace: String,
        error: () -> Unit,
        loadedCurrent: () -> Unit
    ) {
        InAppStoryManager.useInstance(object : UseManagerInstanceCallback() {
            override fun use(manager: InAppStoryManager) {
                manager.preloadBannerPlace(
                    BannerPlaceLoadSettings().placeId(bannerPlace),
                    object : BannerPlacePreloadCallback(bannerPlace) {
                        override fun bannerPlaceLoaded(
                            size: Int,
                            bannerData: MutableList<BannerData>?
                        ) {

                        }

                        override fun loadError() {
                            error()
                        }

                        override fun bannerContentLoaded(bannerId: Int, isFirst: Boolean) {
                            if (isFirst)
                                loadedCurrent.invoke()
                        }

                        override fun bannerContentLoadError(bannerId: Int, isFirst: Boolean) {
                            if (isFirst)
                                error()
                        }

                    })
            }
        })
    }
}