package com.inappstory.sdk.compose.iasapi

import android.content.Context
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.UseManagerInstanceCallback
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.externalapi.ExternalPlatforms

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