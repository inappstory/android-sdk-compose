package com.inappstory.sdk.compose.controllers

import com.inappstory.sdk.stories.outercallbacks.common.reader.StoryData

class StoryListController {
    internal var updateVisibleArea: (triggerScrollCallback: Boolean) -> Unit = { triggerScrollCallback ->

    }

    var storiesLoaded: (feed: String?, size: Int, storyData: List<StoryData?>?) -> Unit =
        { _, _, _ -> }

    var storiesUpdated: (feed: String?, size: Int, storyData: List<StoryData?>?) -> Unit =
        { _, _, _ -> }

    var loadError: (feed: String?) -> Unit = {}


    internal var loadList: (() -> Unit) = {

    }

    internal var reloadList: (() -> Unit) = {

    }

    fun updateVisibleArea(triggerScrollCallback: Boolean) {
        updateVisibleArea.invoke(triggerScrollCallback)
    }

    fun load() {
        loadList.invoke()
    }

    fun refresh() {
        reloadList.invoke()
    }
}