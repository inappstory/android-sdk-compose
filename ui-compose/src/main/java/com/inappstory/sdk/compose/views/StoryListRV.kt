package com.inappstory.sdk.compose.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.RecyclerView
import com.inappstory.sdk.AppearanceManager
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.compose.R
import com.inappstory.sdk.compose.controllers.StoryListController
import com.inappstory.sdk.compose.views.customlistitem.StoryListFavoriteItemState
import com.inappstory.sdk.compose.views.customlistitem.StoryListComposeItem
import com.inappstory.sdk.compose.views.customlistitem.StoryListItemState
import com.inappstory.sdk.compose.views.customlistitem.StoryListItemViewModelsHolder
import com.inappstory.sdk.stories.outercallbacks.common.reader.StoryData
import com.inappstory.sdk.stories.outercallbacks.storieslist.ListCallback
import com.inappstory.sdk.stories.outercallbacks.storieslist.ListScrollCallback
import com.inappstory.sdk.stories.ui.list.ShownStoriesListItem
import com.inappstory.sdk.stories.ui.list.StoriesList
import com.inappstory.sdk.stories.ui.list.StoryTouchListener
import com.inappstory.sdk.stories.ui.views.IGetFavoriteListItem
import com.inappstory.sdk.stories.ui.views.IStoriesListItem

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
    customItem: (@Composable (StoryListItemState) -> Unit)? = null,
    customFavItem: (@Composable (StoryListFavoriteItemState) -> Unit)? = null,
    listItemTouchDown: (view: View?, position: Int) -> Unit = { _, _ ->
    },
    listItemTouchUp: (view: View?, position: Int) -> Unit = { _, _ ->
    },
    favoriteCellClick: () -> Unit = {},
    listItemClick: (
        storyData: StoryData?,
        index: Int
    ) -> Unit = { _, _ -> },
    listScrollStart: () -> Unit = {},
    listScrollEnd: () -> Unit = {},
    visibleAreaUpdated: (shownStoriesListItemData: List<ShownStoriesListItem?>?) -> Unit = {}
) {
    val context = LocalContext.current
    val modifiedAppearanceManager =
        appearanceManager
            .updateWithCustomItem(
                context = context,
                isFavorite = false,
                customItem = customItem
            )
            .updateWithCustomItem(
                context = context,
                isFavorite = true,
                customFavItem = customFavItem
            )

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
            this.setAppearanceManager(modifiedAppearanceManager)
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
        .fillMaxWidth()
        .wrapContentHeight(),
    storyListController: StoryListController,
    layoutManager: RecyclerView.LayoutManager? = null,
    appearanceManager: AppearanceManager = AppearanceManager(),
    customItem: (@Composable (StoryListItemState) -> Unit)? = null,
    listItemTouchDown: (view: View?, position: Int) -> Unit = { _, _ ->
    },
    listItemTouchUp: (view: View?, position: Int) -> Unit = { _, _ ->
    },
    listItemClick: (
        storyData: StoryData?,
        index: Int
    ) -> Unit = { _, _ -> },
    customStoryListItem: (() -> Unit)? = null,
    listScrollStart: () -> Unit = {},
    listScrollEnd: () -> Unit = {},
    visibleAreaUpdated: (shownStoriesListItemData: List<ShownStoriesListItem?>?) -> Unit = {}
) {
    val context = LocalContext.current
    val modifiedAppearanceManager =
        appearanceManager
            .updateWithCustomItem(
                context = context,
                isFavorite = false,
                customItem = customItem
            )
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
    storiesList.setAppearanceManager(modifiedAppearanceManager)
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


private fun AppearanceManager.updateWithCustomItem(
    context: Context,
    isFavorite: Boolean,
    customItem: (@Composable (StoryListItemState) -> Unit)? = null,
    customFavItem: (@Composable (StoryListFavoriteItemState) -> Unit)? = null

): AppearanceManager {
    if (isFavorite) {
        if (customFavItem == null) return this
        this.csFavoriteListItemInterface(
            object : IGetFavoriteListItem {
                private fun getComposeView(view: View?): StoryListComposeItem? {
                    return (view as ViewGroup?)?.findViewById(
                        R.id.ias_story_list_compose_fav_item
                    )
                }


                override fun getFavoriteItem(): View {
                    return StoryListComposeItem(context).apply {
                        id = R.id.ias_story_list_compose_fav_item
                        initializeComposable {
                            val vm = StoryListItemViewModelsHolder.getFavorite()
                            val state by vm.storyFavoriteListItemState.collectAsState()
                            customFavItem(state)
                        }
                    }
                }

                override fun bindFavoriteItem(
                    favCell: View?,
                    backgroundColors: List<Int>?,
                    count: Int
                ) {
                    StoryListItemViewModelsHolder.getFavorite()
                        .setBackgrounds(
                            backgroundColors ?: emptyList(),
                            count
                        )
                }

                override fun setImages(
                    favCell: View?,
                    favoriteImages: List<String?>?,
                    backgroundColors: List<Int>?,
                    count: Int
                ) {
                    StoryListItemViewModelsHolder.getFavorite()
                        .setImages(
                            favoriteImages ?: emptyList(),
                            backgroundColors ?: emptyList(),
                            count
                        )
                }

            }
        )
    } else {
        if (customItem == null) return this
        this.csListItemInterface(
            object : IStoriesListItem {
                override fun getView(): View {
                    return StoryListComposeItem(context).apply {
                        id = R.id.ias_story_list_compose_item
                        initializeComposable {
                            val vm = StoryListItemViewModelsHolder.get(uniqueId = uniqueId)
                            val state by vm.storyListItemState.collectAsState()
                            customItem(state)
                        }
                    }
                }

                override fun getVideoView(): View {
                    return StoryListComposeItem(context).apply {
                        id = R.id.ias_story_list_compose_item
                        initializeComposable {
                            val vm = StoryListItemViewModelsHolder.get(uniqueId = uniqueId)
                            val state by vm.storyListItemState.collectAsState()
                            customItem(state)
                        }
                    }
                }

                override fun setId(itemView: View?, storyId: Int) {
                    getComposeView(itemView)?.let {
                        StoryListItemViewModelsHolder.get(it.uniqueId).setStoryId(storyId)
                    }
                }

                override fun setTitle(
                    itemView: View?,
                    title: String?,
                    titleColor: Int?
                ) {
                    getComposeView(itemView)?.let {
                        StoryListItemViewModelsHolder
                            .get(it.uniqueId)
                            .setTitle(
                                title = title,
                                titleColor = titleColor
                            )
                    }
                }

                override fun setImage(
                    itemView: View?,
                    imagePath: String?,
                    backgroundColor: Int
                ) {
                    getComposeView(itemView)?.let {
                        StoryListItemViewModelsHolder
                            .get(it.uniqueId)
                            .setImageLocalPath(
                                imageLocalPath = imagePath,
                                backgroundColor = backgroundColor
                            )
                    }
                }

                override fun setHasAudio(itemView: View?, hasAudio: Boolean) {
                    getComposeView(itemView)?.let {
                        StoryListItemViewModelsHolder
                            .get(it.uniqueId)
                            .setHasAudio(
                                hasAudio = hasAudio
                            )
                    }
                }

                override fun setVideo(itemView: View?, videoPath: String?) {
                    getComposeView(itemView)?.let {
                        StoryListItemViewModelsHolder
                            .get(it.uniqueId)
                            .setVideoLocalPath(
                                videoLocalPath = videoPath
                            )
                    }
                }

                override fun setOpened(itemView: View?, isOpened: Boolean) {
                    getComposeView(itemView)?.let {
                        StoryListItemViewModelsHolder
                            .get(it.uniqueId)
                            .setOpened(
                                opened = isOpened
                            )
                    }
                }


                private fun getComposeView(view: View?): StoryListComposeItem? {
                    return (view as ViewGroup?)?.findViewById(
                        R.id.ias_story_list_compose_item
                    )
                }

            }
        )
    }
    return this
}