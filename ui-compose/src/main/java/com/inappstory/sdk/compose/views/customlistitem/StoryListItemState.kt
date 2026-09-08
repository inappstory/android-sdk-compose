package com.inappstory.sdk.compose.views.customlistitem

import android.graphics.Color

data class StoryListItemState(
    val isRemovable: Boolean = false,
    val title: String = "",
    val backgroundColor: Int = Color.BLACK,
    val titleColor: Int = Color.BLACK,
    val imageLocalPath: String? = null,
    val videoLocalPath: String? = null,
    val opened: Boolean = false,
    val hasAudio: Boolean = false,
    val storyId: Int = -1
)