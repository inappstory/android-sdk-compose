package com.inappstory.sdk.compose.controllers

import com.inappstory.sdk.banners.BannerData

class BannerVerticalGridController {
    var bannerPlaceLoaded: (
        size: Int,
        bannerData: List<BannerData?>?,
        widgetHeight: Int
    ) -> Unit = { _, _, _ -> }

    var loadError: () -> Unit = { }

    var bannerLoaded: (bannerId: Int, isCurrent: Boolean) -> Unit = { _, _ -> }

    var bannerLoadError: (bannerId: Int, isCurrent: Boolean) -> Unit = { _, _ -> }

    internal var loadGrid: (() -> Unit) = {

    }

    internal var reloadGrid: (() -> Unit) = {

    }

    internal var pauseBanners: (() -> Unit) = {

    }

    internal var resumeBanners: (() -> Unit) = {

    }

    fun pause() {
        pauseBanners()
    }

    fun resume() {
        resumeBanners()
    }

    fun load() {
        loadGrid()
    }

    fun refresh() {
        reloadGrid()
    }

}