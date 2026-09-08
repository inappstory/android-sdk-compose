package com.inappstory.sdk.compose.views.customlistitem

object StoryListItemViewModelsHolder {
    private val storyListItemViewModels = hashMapOf<String, StoryListItemViewModel>()
    private val storyFavoriteItemViewModel = StoryListFavoriteItemViewModel()

    fun get(uniqueId: String): StoryListItemViewModel {
        return storyListItemViewModels.getOrPut(uniqueId) {
            StoryListItemViewModel(uniqueId)
        }
    }

    fun clear() {
        storyListItemViewModels.values.forEach { it.clear() }
        storyListItemViewModels.clear()
    }

    fun getFavorite() = storyFavoriteItemViewModel
}