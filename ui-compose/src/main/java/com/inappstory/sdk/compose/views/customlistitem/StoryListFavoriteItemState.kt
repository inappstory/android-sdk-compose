package com.inappstory.sdk.compose.views.customlistitem

data class StoryListFavoriteItemState(
    val count: Int = 0,
    val backgroundColors: List<Int> = emptyList(),
    val images: List<String?> = emptyList()
)