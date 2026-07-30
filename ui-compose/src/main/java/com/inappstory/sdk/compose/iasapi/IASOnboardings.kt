package com.inappstory.sdk.compose.iasapi

import android.content.Context
import com.inappstory.sdk.AppearanceManager
import com.inappstory.sdk.CancellationToken
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.core.CancellationTokenImpl
import com.inappstory.sdk.core.CancellationTokenWithStatus
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.stories.outercallbacks.common.onboarding.OnboardingLoadCallback


class IASOnboardings {
    fun show(
        context: Context,
        feed: String = "onboardings",
        appearanceManager: AppearanceManager = AppearanceManager(),
        tags: MutableList<String>? = arrayListOf(),
        limit: Int = 100
    ): CancellationToken {
        val token: CancellationTokenWithStatus =
            CancellationTokenImpl("External Onboardings feed: $feed")
        InAppStoryManager.useCoreInSeparateThread(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.cancellationTokenPool().addToken(token)
                core.onboardingsAPI().show(
                    token,
                    context,
                    feed,
                    appearanceManager,
                    tags,
                    limit
                )
            }
        })
        return token
    }

    fun callback(
        success: (count: Int, feed: String?) -> Unit = { _, _ -> },
        error: (feed: String?, reason: String?) -> Unit = { _, _ -> }
    ) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.onboardingsAPI().loadCallback(object : OnboardingLoadCallback {
                    override fun onboardingLoadSuccess(count: Int, feed: String?) {
                        success.invoke(count, feed)
                    }

                    override fun onboardingLoadError(feed: String?, reason: String?) {
                        error.invoke(feed, reason)
                    }
                })
            }
        })
    }
}