package com.inappstory.sdk.compose.controllers

import com.inappstory.sdk.stories.outercallbacks.common.reader.StoryData

class StoryListController {
    var storiesLoaded: (feed: String?, size: Int, storyData: List<StoryData?>?) -> Unit =
        { _, _, _ -> }

    var storiesUpdated: (feed: String?, size: Int, storyData: List<StoryData?>?) -> Unit =
        { _, _, _ -> }

    var loadError: (feed: String?) -> Unit = {}

    internal var loadList: (() -> Unit) = {

    }

    internal var reloadList: (() -> Unit) = {

    }

    fun load() {
        loadList.invoke()
    }

    fun refresh() {
        reloadList.invoke()
    }
}