package com.inappstory.sdk.compose.controllers

import com.inappstory.sdk.banners.BannerData

class BannerVerticalListController {
    var bannerPlaceLoaded: (
        size: Int,
        bannerData: List<BannerData?>?,
        widgetHeight: Int
    ) -> Unit = { _, _, _ -> }

    var loadError: () -> Unit = { }

    var bannerLoaded: (bannerId: Int, isCurrent: Boolean) -> Unit = { _, _ -> }

    var bannerLoadError: (bannerId: Int, isCurrent: Boolean) -> Unit = { _, _ -> }

    internal var loadList: (() -> Unit) = {

    }

    internal var reloadList: (() -> Unit) = {

    }

    fun load() {
        loadList()
    }

    fun refresh() {
        reloadList()
    }

}