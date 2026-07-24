package com.inappstory.sdk.compose.iasapi

import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.core.api.IASCallbackType
import com.inappstory.sdk.stories.outercallbacks.common.reader.ClickOnShareStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.CloseReader
import com.inappstory.sdk.stories.outercallbacks.common.reader.CloseStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.FavoriteStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.LikeDislikeStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.ShowSlideCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.ShowStoryAction
import com.inappstory.sdk.stories.outercallbacks.common.reader.ShowStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.SlideData
import com.inappstory.sdk.stories.outercallbacks.common.reader.StoryData
import com.inappstory.sdk.stories.outercallbacks.common.reader.StoryWidgetCallback

class IASStories {
    val single: IASSingle = IASSingle()
    val onboardings: IASOnboardings = IASOnboardings()

    private fun useCore(callback: UseIASCoreCallback) {
        InAppStoryManager.useCore(callback)
    }

    fun onClickOnShareStory(shareClick: (SlideData?) -> Unit = {}) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI()
                    .setCallback(
                        IASCallbackType.CLICK_SHARE,
                        object : ClickOnShareStoryCallback {
                            override fun shareClick(slideData: SlideData?) {
                                shareClick.invoke(slideData)
                            }

                        }
                    )
            }
        })
    }

    fun onStoryWidget(
        widgetEvent: (
            slideData: SlideData?,
            widgetEventName: String?,
            widgetData: Map<String?, String?>?
        ) -> Unit
    ) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.STORY_WIDGET,
                    object : StoryWidgetCallback {
                        override fun widgetEvent(
                            slideData: SlideData?,
                            widgetEventName: String?,
                            widgetData: Map<String?, String?>?
                        ) {
                            widgetEvent.invoke(
                                slideData,
                                widgetEventName,
                                widgetData
                            )
                        }
                    });
            }
        })
    }

    fun onCloseStory(
        closeStory: (
            slideData: SlideData?,
            action: CloseReader?
        ) -> Unit
    ) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI()
                    .setCallback(
                        IASCallbackType.CLOSE_STORY,
                        object : CloseStoryCallback {
                            override fun closeStory(
                                slideData: SlideData?,
                                action: CloseReader?
                            ) {
                                closeStory.invoke(slideData, action)
                            }
                        }
                    );
            }
        })
    }

    fun onFavoriteStory(
        favoriteStory: (
            slideData: SlideData?,
            value: Boolean
        ) -> Unit
    ) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.FAVORITE,
                    object : FavoriteStoryCallback {
                        override fun favoriteStory(
                            slideData: SlideData?,
                            value: Boolean
                        ) {
                            favoriteStory.invoke(slideData, value)
                        }
                    });
            }
        })
    }

    fun onLikeDislikeStory(
        likeStory: (
            slideData: SlideData?,
            value: Boolean
        ) -> Unit,
        dislikeStory: (
            slideData: SlideData?,
            value: Boolean
        ) -> Unit
    ) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI()
                    .setCallback(
                        IASCallbackType.LIKE_DISLIKE,
                        object : LikeDislikeStoryCallback {
                            override fun likeStory(
                                slideData: SlideData?,
                                value: Boolean
                            ) {
                                likeStory.invoke(slideData, value)
                            }

                            override fun dislikeStory(
                                slideData: SlideData?,
                                value: Boolean
                            ) {
                                dislikeStory.invoke(slideData, value)
                            }

                        }
                    );
            }
        })
    }

    fun onShowSlide(showSlide: (slideData: SlideData?) -> Unit) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.SHOW_SLIDE,
                    object : ShowSlideCallback {
                        override fun showSlide(slideData: SlideData?) {
                            showSlide.invoke(slideData)
                        }

                    }
                );
            }
        })
    }

    fun onShowStory(
        showStory: (
            storyData: StoryData?,
            action: ShowStoryAction?
        ) -> Unit
    ) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.SHOW_STORY,
                    object : ShowStoryCallback {
                        override fun showStory(
                            storyData: StoryData?,
                            action: ShowStoryAction?
                        ) {
                            showStory.invoke(storyData, action)
                        }
                    }
                );
            }
        })
    }


}