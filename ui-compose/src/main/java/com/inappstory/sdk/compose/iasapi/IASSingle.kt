package com.inappstory.sdk.compose.iasapi

import android.content.Context
import com.inappstory.sdk.AppearanceManager
import com.inappstory.sdk.CancellationToken
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.core.CancellationTokenImpl
import com.inappstory.sdk.core.CancellationTokenWithStatus
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.stories.callbacks.IShowStoryCallback
import com.inappstory.sdk.stories.callbacks.IShowStoryOnceCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.StoryData
import com.inappstory.sdk.stories.outercallbacks.common.single.SingleLoadCallback


class IASSingle {

    fun showOnce(
        context: Context,
        storyId: String,
        appearanceManager: AppearanceManager = AppearanceManager(),
        show: () -> Unit = {},
        error: () -> Unit = {},
        alreadyShown: () -> Unit = {}
    ): CancellationToken {
        val token: CancellationTokenWithStatus =
            CancellationTokenImpl("External Single once id: $storyId")
        val callback = object : IShowStoryOnceCallback {
            override fun onShow() {
                show.invoke()
            }

            override fun onError() {
                error.invoke()
            }

            override fun alreadyShown() {
                alreadyShown.invoke()
            }
        }
        InAppStoryManager.useCoreInSeparateThread(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.cancellationTokenPool().addToken(token)
                core.singleStoryAPI().showOnce(
                    token,
                    context,
                    storyId,
                    appearanceManager,
                    callback
                )
            }
        })
        return token
    }

    fun show(
        context: Context,
        storyId: String,
        appearanceManager: AppearanceManager = AppearanceManager(),
        show: () -> Unit = {},
        error: () -> Unit = {},
        slide: Int = 0
    ): CancellationToken {
        val token: CancellationTokenWithStatus =
            CancellationTokenImpl("External Single id: $storyId")
        val callback = object : IShowStoryCallback {
            override fun onShow() {
                show.invoke()
            }

            override fun onError() {
                error.invoke()
            }
        }
        InAppStoryManager.useCoreInSeparateThread(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.cancellationTokenPool().addToken(token)
                core.singleStoryAPI()
                    .show(
                        token,
                        context,
                        storyId,
                        appearanceManager,
                        callback,
                        slide
                    )
            }
        })
        return token
    }

    fun callback(
        success: (storyData: StoryData?) -> Unit = {},
        error: (storyId: String?, reason: String?) -> Unit
    ) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.singleStoryAPI().loadCallback(object : SingleLoadCallback {
                    override fun singleLoadSuccess(storyData: StoryData?) {
                        success.invoke(storyData)
                    }

                    override fun singleLoadError(storyId: String?, reason: String?) {
                        error.invoke(storyId, reason)
                    }
                })
            }
        })
    }
}