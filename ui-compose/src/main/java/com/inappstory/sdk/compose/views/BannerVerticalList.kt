package com.inappstory.sdk.compose.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.inappstory.sdk.AppearanceManager
import com.inappstory.sdk.banners.BannerData
import com.inappstory.sdk.banners.BannerPlaceLoadCallback
import com.inappstory.sdk.banners.ui.list.BannerList
import com.inappstory.sdk.compose.controllers.BannerVerticalListController
import com.inappstory.sdk.core.banners.ICustomBannerListAppearance

@Composable
fun BannerVerticalList(
    modifier: Modifier = Modifier
        .fillMaxSize(),
    bannerVerticalListController: BannerVerticalListController,
    placeId: String,
    uniqueId: String? = null,
    bannerVerticalListInterface: ICustomBannerListAppearance = CustomBannerListAppearance()
) {
    val context = LocalContext.current
    if (placeId.isEmpty()) return
    val uId = uniqueId ?: (placeId + "_list_id")
    val bannerPlaceWidget: BannerList = remember {
        BannerList(context).apply {
            setAppearanceManager(
                AppearanceManager().csBannerListInterface(
                    bannerVerticalListInterface
                )
            )
            bannerVerticalListController.loadList = {
                this.loadBanners()
            }
            bannerVerticalListController.reloadList = {
                this.loadBanners(true)
            }
            loadCallback(object : BannerPlaceLoadCallback() {
                override fun bannerPlaceLoaded(
                    size: Int,
                    bannerData: List<BannerData?>?,
                    widgetHeight: Int
                ) {
                    bannerVerticalListController.bannerPlaceLoaded(
                        size,
                        bannerData,
                        widgetHeight
                    )
                }

                override fun loadError() {
                    bannerVerticalListController.loadError()
                }

                override fun bannerLoaded(bannerId: Int, isCurrent: Boolean) {
                    bannerVerticalListController.bannerLoaded(
                        bannerId,
                        isCurrent
                    )
                }

                override fun bannerLoadError(bannerId: Int, isCurrent: Boolean) {
                    bannerVerticalListController.bannerLoadError(
                        bannerId,
                        isCurrent
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
        bannerVerticalListController.loadList()
    }
}