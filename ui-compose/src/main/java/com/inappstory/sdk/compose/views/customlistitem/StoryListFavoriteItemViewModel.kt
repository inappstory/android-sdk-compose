package com.inappstory.sdk.compose.views.customlistitem

import android.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StoryListFavoriteItemViewModel : ViewModel() {
    private var _storyListFavoriteItemState = MutableStateFlow(StoryListFavoriteItemState())
    val storyFavoriteListItemState = _storyListFavoriteItemState.asStateFlow()

    fun setBackgrounds(backgrounds: List<Int>, count: Int) {
        val resBackgrounds = arrayListOf<Int>()
        val resImages = arrayListOf<String?>()
        for (i in 0 until count) {
            resBackgrounds.add(backgrounds.getOrNull(i) ?: Color.BLACK)
            resImages.add(null)
        }
        _storyListFavoriteItemState.update {
            it.copy(count = count, images = resImages, backgroundColors = resBackgrounds)
        }
    }

    fun setImages(images: List<String?>, backgrounds: List<Int>, count: Int) {
        val resImages = arrayListOf<String?>()
        val resBackgrounds = arrayListOf<Int>()
        for (i in 0 until count) {
            resImages.add(images.getOrNull(i))
            resBackgrounds.add(backgrounds.getOrNull(i) ?: Color.BLACK)
        }
        _storyListFavoriteItemState.update {
            it.copy(count = count, images = resImages, backgroundColors = resBackgrounds)
        }
    }
}