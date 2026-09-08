package com.inappstory.sdk.compose.views.customlistitem

import android.graphics.Color

data class StoryListFavoriteItemState(
    val title: String = "",
    val count: Int = 0,
    val backgroundColors: List<Int> = emptyList(),
    val titleColor: Int = Color.BLACK,
    val images: List<String?> = emptyList()
)