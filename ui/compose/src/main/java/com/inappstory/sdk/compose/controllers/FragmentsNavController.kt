package com.inappstory.sdk.compose.controllers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.compose.R
import com.inappstory.sdk.game.reader.GameMainFragment
import com.inappstory.sdk.stories.outercallbacks.common.objects.DefaultOpenGameReader
import com.inappstory.sdk.stories.outercallbacks.common.objects.DefaultOpenStoriesReader
import com.inappstory.sdk.stories.outercallbacks.common.objects.IOpenGameReaderAdapter
import com.inappstory.sdk.stories.outercallbacks.common.objects.IOpenStoriesReaderAdapter
import com.inappstory.sdk.stories.ui.reader.StoriesMainFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class FragmentsNavController {

    private var contentTypeStack = mutableListOf<NavContentType>()
    private var _topContentType = MutableStateFlow(NavContentType.NONE)

    val topContentType = _topContentType.asStateFlow()

    private fun topContentUpdate() {
        _topContentType.update {
            contentTypeStack.firstOrNull() ?: NavContentType.NONE
        }
    }

    fun clearStoriesReaderPresentation() {
        InAppStoryManager.getInstance()?.setOpenStoriesReader(DefaultOpenStoriesReader())
    }

    fun setStoriesReaderPresentation() {
        InAppStoryManager.getInstance()?.setOpenStoriesReader(object : IOpenStoriesReaderAdapter() {
            override fun onOpen(context: Context?, bundle: Bundle) {
                if (context !is FragmentActivity) return
                val fragmentManager = context.supportFragmentManager
                val t: FragmentTransaction = fragmentManager.beginTransaction()
                    .add(
                        R.id.ias_default_story_fragment_container,
                        StoriesMainFragment.newInstance(
                            bundle,
                            context
                        ),
                        "STORIES_MAIN_FRAGMENT"
                    )
                t.addToBackStack("STORIES_MAIN_FRAGMENT")
                t.commit()
                contentTypeStack.remove(NavContentType.STORY)
                contentTypeStack.add(0, NavContentType.STORY)
                topContentUpdate()
            }

            override fun onRestoreStatusBar(context: Context?) {
                contentTypeStack.remove(NavContentType.STORY)
                topContentUpdate()
            }
        })
    }

    fun clearGameReaderPresentation() {
        InAppStoryManager.getInstance()?.setOpenGameReader(DefaultOpenGameReader())
    }

    fun setGameReaderPresentation() {
        InAppStoryManager.getInstance()?.setOpenGameReader(object : IOpenGameReaderAdapter() {
            override fun onOpen(context: Context?, bundle: Bundle) {
                if (context !is FragmentActivity) return
                val fragmentManager = context.supportFragmentManager

                val t: FragmentTransaction = fragmentManager.beginTransaction()
                    .add(
                        R.id.ias_default_game_fragment_container,
                        GameMainFragment().apply {
                            arguments = bundle
                        },
                        "GAME_MAIN_FRAGMENT"
                    )
                t.addToBackStack("GAME_MAIN_FRAGMENT")
                t.commit()
                contentTypeStack.remove(NavContentType.GAME)
                contentTypeStack.add(0, NavContentType.GAME)
                topContentUpdate()
            }

            override fun onRestoreScreen(context: Context?) {
                contentTypeStack.remove(NavContentType.GAME)
                topContentUpdate()
            }
        })
    }
}