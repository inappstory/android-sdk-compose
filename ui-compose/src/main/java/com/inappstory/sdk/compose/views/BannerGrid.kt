package com.inappstory.sdk.compose.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.inappstory.sdk.AppearanceManager
import com.inappstory.sdk.banners.BannerData
import com.inappstory.sdk.banners.BannerPlaceLoadCallback
import com.inappstory.sdk.banners.ui.grid.BannerGrid
import com.inappstory.sdk.banners.ui.list.BannerList
import com.inappstory.sdk.compose.controllers.BannerVerticalGridController
import com.inappstory.sdk.compose.controllers.BannerVerticalListController
import com.inappstory.sdk.core.banners.ICustomBannerGridAppearance
import com.inappstory.sdk.core.banners.ICustomBannerListAppearance

@Composable
fun BannerGrid(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    bannerVerticalGridController: BannerVerticalGridController,
    placeId: String,
    uniqueId: String? = null,
    bannerVerticalGridInterface: ICustomBannerGridAppearance = CustomBannerGridAppearance()
) {
    val context = LocalContext.current
    if (placeId.isEmpty()) return
    val uId = uniqueId ?: (placeId + "_list_id")
    val bannerPlaceWidget: BannerGrid = remember {
        BannerGrid(context).apply {
            setAppearanceManager(
                AppearanceManager().csBannerGridInterface(
                    bannerVerticalGridInterface
                )
            )
            bannerVerticalGridController.pauseBanners = {
                this.pauseBanners()
            }
            bannerVerticalGridController.resumeBanners = {
                this.resumeBanners()
            }
            bannerVerticalGridController.loadGrid = {
                this.loadBanners()
            }
            bannerVerticalGridController.reloadGrid = {
                this.reloadBanners()
            }
            loadCallback(object : BannerPlaceLoadCallback() {
                override fun bannerPlaceLoaded(
                    size: Int,
                    bannerData: List<BannerData?>?,
                    widgetHeight: Int
                ) {
                    bannerVerticalGridController.bannerPlaceLoaded(
                        size,
                        bannerData,
                        widgetHeight
                    )
                }

                override fun loadError() {
                    bannerVerticalGridController.loadError()
                }

                override fun bannerLoaded(bannerId: Int, isCurrent: Boolean) {
                    bannerVerticalGridController.bannerLoaded(
                        bannerId,
                        isCurrent
                    )
                }

                override fun bannerLoadError(bannerId: Int, isCurrent: Boolean) {
                    bannerVerticalGridController.bannerLoadError(
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
        bannerVerticalGridController.loadGrid()
    }
}