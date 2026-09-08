package com.inappstory.sdk.compose.views.customlistitem

import android.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StoryListItemViewModel(uniqueId: String) : ViewModel() {
    private var _storyListItemState = MutableStateFlow(StoryListItemState())
    val storyListItemState = _storyListItemState.asStateFlow()

    fun setTitle(title: String?, titleColor: Int?) {
        _storyListItemState.update {
            it.copy(title = title ?: "", titleColor = titleColor ?: Color.BLACK)
        }
    }


    fun setVideoLocalPath(videoLocalPath: String?) {
        _storyListItemState.update {
            it.copy(videoLocalPath = videoLocalPath)
        }
    }

    fun clear() {
        _storyListItemState.update {
            StoryListItemState()
        }
    }

    fun setImageLocalPath(imageLocalPath: String?, backgroundColor: Int?) {
        _storyListItemState.update {
            it.copy(
                imageLocalPath = imageLocalPath,
                backgroundColor = backgroundColor ?: Color.BLACK
            )
        }
    }

    fun setStoryId(storyId: Int?) {
        _storyListItemState.update {
            it.copy(storyId = storyId ?: -1)
        }
    }

    fun setOpened(opened: Boolean) {
        _storyListItemState.update {
            it.copy(opened = opened)
        }
    }

    fun setHasAudio(hasAudio: Boolean) {
        _storyListItemState.update {
            it.copy(hasAudio = hasAudio)
        }
    }
}