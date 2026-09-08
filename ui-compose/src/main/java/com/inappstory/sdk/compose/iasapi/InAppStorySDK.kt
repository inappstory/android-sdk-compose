package com.inappstory.sdk.compose.iasapi

import android.content.Context
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.UseManagerInstanceCallback
import com.inappstory.sdk.compose.views.customlistitem.StoryListItemViewModelsHolder
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.core.api.IASCallbackType
import com.inappstory.sdk.core.api.UseIASCallback
import com.inappstory.sdk.externalapi.ExternalPlatforms
import com.inappstory.sdk.stories.callbacks.SessionIsOpenedCallback

object InAppStorySDK {
    val inAppStoryManager = IASManager()
    val stories = IASStories()
    val callbacks = IASCallbacks()
    val settings = IASSettings()
    val games = IASGames()
    val inAppMessages = IASInAppMessages()
    val banners = IASBanners()

    fun initSdk(context: Context) {
        InAppStoryManager.initSDK(context)
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().agentPrefix(ExternalPlatforms.COMPOSE_SDK.prefix)
                core.callbacksAPI().setCallback(
                    IASCallbackType.SESSION_IS_OPENED,
                    object : SessionIsOpenedCallback {
                        override fun isOpened() {
                            StoryListItemViewModelsHolder.clear()
                        }
                    }
                )
            }
        })
    }

    fun clearCache() {
        InAppStoryManager.useCoreInSeparateThread(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.contentLoader().clearCache()
            }
        })
    }

    fun handleBackPress(): Boolean {
        return InAppStoryManager.getInstance()?.onBackPressed() ?: false
    }
}