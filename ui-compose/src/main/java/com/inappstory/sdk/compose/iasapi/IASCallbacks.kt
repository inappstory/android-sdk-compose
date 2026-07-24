package com.inappstory.sdk.compose.iasapi

import android.content.Context
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.core.api.IASCallbackType
import com.inappstory.sdk.inappmessage.CloseInAppMessageCallback
import com.inappstory.sdk.inappmessage.InAppMessageData
import com.inappstory.sdk.inappmessage.InAppMessageWidgetCallback
import com.inappstory.sdk.inappmessage.ShowInAppMessageCallback
import com.inappstory.sdk.stories.outercallbacks.common.errors.ErrorCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.CallToActionCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.ClickAction
import com.inappstory.sdk.stories.outercallbacks.common.reader.ClickOnShareStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.CloseReader
import com.inappstory.sdk.stories.outercallbacks.common.reader.CloseStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.ContentData
import com.inappstory.sdk.stories.outercallbacks.common.reader.FavoriteStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.LikeDislikeStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.ShowSlideCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.ShowStoryAction
import com.inappstory.sdk.stories.outercallbacks.common.reader.ShowStoryCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.SlideData
import com.inappstory.sdk.stories.outercallbacks.common.reader.StoryData
import com.inappstory.sdk.stories.outercallbacks.common.reader.StoryWidgetCallback


class IASCallbacks {
    private fun useCore(callback: UseIASCoreCallback) {
        InAppStoryManager.useCore(callback)
    }

    fun onError(
        loadListError: (feed: String?) -> Unit = {},
        cacheError: () -> Unit = {},
        emptyLinkError: () -> Unit = {},
        sessionError: () -> Unit = {},
        noConnection: () -> Unit = {},
    ) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.ERROR,
                    object : ErrorCallback {
                        override fun loadListError(feed: String?) {
                            loadListError.invoke(feed)
                        }

                        override fun cacheError() {
                            cacheError.invoke()
                        }

                        override fun emptyLinkError() {
                            emptyLinkError.invoke()
                        }

                        override fun sessionError() {
                            sessionError.invoke()
                        }

                        override fun noConnection() {
                            noConnection.invoke()
                        }
                    }
                )
            }
        })
    }

    fun onCallToAction(
        cta: (
            context: Context?,
            slideData: ContentData?,
            link: String?,
            action: ClickAction?
        ) -> Unit
    ) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI()
                    .setCallback(
                        IASCallbackType.CALL_TO_ACTION,
                        object : CallToActionCallback {
                            override fun callToAction(
                                context: Context?,
                                slideData: ContentData?,
                                link: String?,
                                action: ClickAction?
                            ) {
                                cta.invoke(
                                    context,
                                    slideData,
                                    link,
                                    action
                                )
                            }
                        }
                    );
            }
        })
    }

}