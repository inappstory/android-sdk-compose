package com.inappstory.sdk.compose.controllers

import com.inappstory.sdk.banners.BannerData

class BannerCarouselController {
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

    internal var next: (() -> Unit) = {

    }

    internal var prev: (() -> Unit) = {

    }

    internal var autoscrollOn: (() -> Unit) = {

    }

    internal var autoscrollOff: (() -> Unit) = {

    }

    internal var byIndex: ((index: Int) -> Unit) = {}

    fun load() {
        loadList()
    }

    fun refresh() {
        reloadList()
    }

    fun showNext() {
        next()
    }

    fun showByIndex(index: Int) {
        byIndex(index)
    }

    fun showPrevious() {
        prev()
    }

    fun resumeAutoscroll() {
        autoscrollOn()
    }

    fun pauseAutoscroll() {
        autoscrollOff()
    }
}