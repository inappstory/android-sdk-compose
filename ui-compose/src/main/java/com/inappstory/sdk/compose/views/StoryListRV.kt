package com.inappstory.sdk.compose.views

import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.RecyclerView
import com.inappstory.sdk.AppearanceManager
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.compose.controllers.StoryListController
import com.inappstory.sdk.stories.outercallbacks.common.reader.StoryData
import com.inappstory.sdk.stories.outercallbacks.storieslist.ListCallback
import com.inappstory.sdk.stories.outercallbacks.storieslist.ListScrollCallback
import com.inappstory.sdk.stories.ui.list.ShownStoriesListItem
import com.inappstory.sdk.stories.ui.list.StoriesList
import com.inappstory.sdk.stories.ui.list.StoryTouchListener

@Composable
fun StoryListRV(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    storyListController: StoryListController,
    layoutManager: RecyclerView.LayoutManager? = null,
    cacheId: String? = null,
    feed: String = "default",
    appearanceManager: AppearanceManager = AppearanceManager(),
    listItemTouchDown: (view: View?, position: Int) -> Unit = { _, _ ->
    },
    listItemTouchUp: (view: View?, position: Int) -> Unit = { _, _ ->
    },
    favoriteCellClick: () -> Unit,
    listItemClick: (
        storyData: StoryData?,
        index: Int
    ) -> Unit = { _, _ -> },
    listScrollStart: () -> Unit = {},
    listScrollEnd: () -> Unit = {},
    visibleAreaUpdated: (shownStoriesListItemData: List<ShownStoriesListItem?>?) -> Unit = {}
) {
    val context = LocalContext.current
    val uniqueId = cacheId ?: feed
    val storiesList = remember {
        StoriesList(context).apply {
            this.setCacheId(uniqueId)
            this.feed = feed
            this.setOnFavoriteItemClick {
                favoriteCellClick.invoke()
            }
            storyListController.loadList = {
                this.loadStories()
            }
            storyListController.updateVisibleArea = { triggerScrollCallback ->
                this.updateVisibleArea(triggerScrollCallback)
            }
            storyListController.reloadList = {
                InAppStoryManager.getInstance()?.clearCachedListById(uniqueId)
                this.refresh()
            }
            this.setScrollCallback(object : ListScrollCallback {
                override fun scrollStart() {
                    listScrollStart()
                }

                override fun onVisibleAreaUpdated(shownStoriesListItemData: List<ShownStoriesListItem?>?) {
                    visibleAreaUpdated(shownStoriesListItemData)
                }

                override fun scrollEnd() {
                    listScrollEnd()
                }
            })
            this.setStoryTouchListener(object : StoryTouchListener {
                override fun touchDown(view: View?, position: Int) {
                    listItemTouchDown(view, position)
                }

                override fun touchUp(view: View?, position: Int) {
                    listItemTouchUp(view, position)
                }
            })
            this.setCallback(object : ListCallback {
                override fun storiesLoaded(
                    size: Int,
                    feed: String?,
                    storyData: List<StoryData?>?
                ) {
                    storyListController.storiesLoaded(
                        feed,
                        size,
                        storyData
                    )
                }

                override fun storiesUpdated(
                    size: Int,
                    feed: String?,
                    storyData: List<StoryData?>?
                ) {
                    storyListController.storiesUpdated(
                        feed,
                        size,
                        storyData
                    )
                }

                override fun loadError(feed: String?) {
                    storyListController.loadError(
                        feed,
                    )
                }

                override fun itemClick(
                    storyData: StoryData?,
                    index: Int
                ) {
                    listItemClick(
                        storyData,
                        index
                    )
                }
            })
            this.setAppearanceManager(appearanceManager)
        }
    }
    layoutManager?.let {
        storiesList.layoutManager = it
    }
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            storiesList
        }
    )
    LaunchedEffect(true) {
        storyListController.loadList()
    }
}

@Composable
fun FavoriteStoryListRV(
    modifier: Modifier = Modifier
        .fillMaxSize(),
    storyListController: StoryListController,
    layoutManager: RecyclerView.LayoutManager? = null,
    appearanceManager: AppearanceManager = AppearanceManager(),
    listItemTouchDown: (view: View?, position: Int) -> Unit = { _, _ ->
    },
    listItemTouchUp: (view: View?, position: Int) -> Unit = { _, _ ->
    },
    listItemClick: (
        storyData: StoryData?,
        index: Int
    ) -> Unit = { _, _ -> },
    listScrollStart: () -> Unit = {},
    listScrollEnd: () -> Unit = {},
    visibleAreaUpdated: (shownStoriesListItemData: List<ShownStoriesListItem?>?) -> Unit = {}
) {
    val context = LocalContext.current
    val storiesList = remember {
        StoriesList(context, true).apply {
            storyListController.loadList = { this.loadStories() }
            storyListController.reloadList = { this.refresh() }
            storyListController.updateVisibleArea = { triggerScrollCallback ->
                this.updateVisibleArea(triggerScrollCallback)
            }
            this.setScrollCallback(object : ListScrollCallback {
                override fun scrollStart() {
                    listScrollStart()
                }

                override fun onVisibleAreaUpdated(shownStoriesListItemData: List<ShownStoriesListItem?>?) {
                    visibleAreaUpdated(shownStoriesListItemData)
                }

                override fun scrollEnd() {
                    listScrollEnd()
                }
            })
            this.setStoryTouchListener(object : StoryTouchListener {
                override fun touchDown(view: View?, position: Int) {
                    listItemTouchDown(view, position)
                }

                override fun touchUp(view: View?, position: Int) {
                    listItemTouchUp(view, position)
                }
            })
            this.setCallback(object : ListCallback {
                override fun storiesLoaded(
                    size: Int,
                    feed: String?,
                    storyData: List<StoryData?>?
                ) {
                    storyListController.storiesLoaded(
                        feed,
                        size,
                        storyData
                    )
                }

                override fun storiesUpdated(
                    size: Int,
                    feed: String?,
                    storyData: List<StoryData?>?
                ) {
                    storyListController.storiesUpdated(
                        feed,
                        size,
                        storyData
                    )
                }

                override fun loadError(feed: String?) {
                    storyListController.loadError(
                        feed,
                    )
                }

                override fun itemClick(
                    storyData: StoryData?,
                    index: Int
                ) {
                    listItemClick(
                        storyData,
                        index
                    )
                }
            })
        }
    }
    layoutManager?.let {
        storiesList.layoutManager = it
    }
    storiesList.setAppearanceManager(appearanceManager)
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            storiesList
        }
    )
    LaunchedEffect(true) {
        storyListController.loadList()
    }
}