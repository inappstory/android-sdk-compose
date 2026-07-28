package com.inappstory.sdk.compose.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.compose.ui.zIndex
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.inappstory.sdk.compose.R
import com.inappstory.sdk.compose.controllers.FragmentsNavController
import com.inappstory.sdk.compose.controllers.NavContentType
import com.inappstory.sdk.compose.databinding.IasDefaultGameFragmentBinding
import com.inappstory.sdk.compose.databinding.IasDefaultStoryFragmentBinding
import com.inappstory.sdk.stories.utils.IASBackPressHandler

@Composable
fun StoryAndGameFragmentScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    openStoryAsFragment: Boolean = true,
    openGameAsFragment: Boolean = true,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    if (context !is FragmentActivity) return
    val fragmentsNavController = remember {
        FragmentsNavController()
    }
    val topContent by fragmentsNavController.topContentType.collectAsState()
    var sElevation = 0f
    var gElevation = 0f
    if (topContent == NavContentType.STORY) sElevation = 10f
    else if (topContent == NavContentType.GAME) gElevation = 10f
    if (topContent != NavContentType.NONE) {
        Spacer(
            modifier =
                modifier
                    .noRippleClickable {}
        )
    }
    if (openStoryAsFragment)
        AndroidViewBinding(
            modifier = modifier
                .zIndex(sElevation),
            factory = IasDefaultStoryFragmentBinding::inflate
        )
    if (openGameAsFragment)
        AndroidViewBinding(
            modifier = modifier
                .zIndex(gElevation),
            factory = IasDefaultGameFragmentBinding::inflate
        )
    DisposableEffect(true) {
        if (openStoryAsFragment)
            fragmentsNavController.setStoriesReaderPresentation()
        if (openGameAsFragment)
            fragmentsNavController.setGameReaderPresentation()
        onDispose {
            if (openStoryAsFragment)
                fragmentsNavController.clearStoriesReaderPresentation()
            if (openGameAsFragment)
                fragmentsNavController.clearGameReaderPresentation()
        }
    }
    BackHandler(enabled = topContent == NavContentType.STORY) {
        val fragmentManager = context.supportFragmentManager
        val fragment: Fragment? =
            fragmentManager.findFragmentById(R.id.ias_default_story_fragment_container)
        if (fragment !is IASBackPressHandler || !(fragment as IASBackPressHandler).onBackPressed()) {
            onBackPressed.invoke()
        }
    }
    BackHandler(enabled = topContent == NavContentType.GAME) {
        val fragmentManager = context.supportFragmentManager
        val fragment: Fragment? =
            fragmentManager.findFragmentById(R.id.ias_default_game_fragment_container)
        if (fragment !is IASBackPressHandler || !(fragment as IASBackPressHandler).onBackPressed()) {
            onBackPressed.invoke()
        }
    }

}


inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}