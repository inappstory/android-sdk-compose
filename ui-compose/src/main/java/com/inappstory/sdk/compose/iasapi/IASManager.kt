package com.inappstory.sdk.compose.iasapi

import android.content.Context
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.stories.api.models.ImagePlaceholderValue
import java.util.Locale


class IASManager {



    fun closeReaders(complete: () -> Unit) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.screensManager().forceCloseAllReaders { complete() }
            }
        })
    }

    fun create(
        apiKey: String,
        userId: String? = null,
        userSign: String? = null,
        lang: Locale = Locale.getDefault(),
        tags: ArrayList<String?>? = null,
        placeholders: MutableMap<String?, String?>? = null,
        imagePlaceholders: MutableMap<String?, ImagePlaceholderValue?>? = null,
        extraOptions: MutableMap<String?, String?>? = null,
        testKey: String? = null,
        gameDemoMode: Boolean = false,
        deviceIdEnabled: Boolean = true,
        sandbox: Boolean = false
    ): InAppStoryManager? {
        var builder = InAppStoryManager.Builder()
            .apiKey(apiKey)
            .isDeviceIDEnabled(deviceIdEnabled)
            .lang(lang)
            .options(extraOptions)
            .sandbox(sandbox)
            .gameDemoMode(gameDemoMode)
            .testKey(testKey)
            .tags(tags)
            .placeholders(placeholders)
            .imagePlaceholders(imagePlaceholders)
        if (userId != null) builder = builder.userId(userId, userSign)
        return builder
            .create()
    }
}