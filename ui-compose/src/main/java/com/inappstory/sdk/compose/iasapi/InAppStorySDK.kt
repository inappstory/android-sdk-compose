package com.inappstory.sdk.compose.iasapi

import android.content.Context
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback

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
    }

    fun clearCache() {
        InAppStoryManager.useCoreInSeparateThread(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.contentLoader().clearCache()
            }
        })
    }

    fun handleBackPress(handle: ()-> Unit, skip: ()->Unit) {

    }
}