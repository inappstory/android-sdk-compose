package com.inappstory.sdk.compose.iasapi

import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.UseManagerInstanceCallback
import com.inappstory.sdk.banners.BannerData
import com.inappstory.sdk.banners.BannerPlaceLoadSettings
import com.inappstory.sdk.banners.BannerPlacePreloadCallback
import com.inappstory.sdk.banners.BannerWidgetCallback
import com.inappstory.sdk.banners.ShowBannerCallback
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.core.api.IASCallbackType
import com.inappstory.sdk.inappmessage.InAppMessageData
import com.inappstory.sdk.inappmessage.InAppMessageWidgetCallback

class IASBanners {

    private fun useCore(callback: UseIASCoreCallback) {
        InAppStoryManager.useCore(callback)
    }

    fun onShowBanner(showBanner: (bannerData: BannerData?) -> Unit) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.SHOW_BANNER,
                    object : ShowBannerCallback {
                        override fun showBanner(bannerData: BannerData?) {
                            showBanner.invoke(bannerData)
                        }
                    }
                );
            }
        })
    }

    fun onBannerWidget(
        widgetEvent: (
            bannerData: BannerData?,
            widgetEventName: String?,
            widgetData: Map<String?, String?>?
        ) -> Unit
    ) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.BANNER_WIDGET,
                    object : BannerWidgetCallback {
                        override fun bannerWidget(
                            bannerData: BannerData?,
                            widgetEventName: String?,
                            widgetData: Map<String?, String?>?
                        ) {
                            widgetEvent.invoke(
                                bannerData,
                                widgetEventName,
                                widgetData
                            )
                        }
                    }
                );
            }
        })
    }

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