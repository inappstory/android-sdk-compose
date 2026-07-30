package com.inappstory.sdk.compose.iasapi

import com.inappstory.sdk.AppearanceManager
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.core.data.IAppVersion
import com.inappstory.sdk.core.data.models.InAppStoryUserSettings
import com.inappstory.sdk.stories.api.models.ImagePlaceholderValue
import java.util.Locale


class IASSettings {

    fun inAppStorySettings(settings: InAppStoryUserSettings?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().inAppStorySettings(settings)
            }
        })
    }

    fun deviceId(deviceId: String?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().deviceId(deviceId)
            }
        })
    }

    fun userId(userId: String?, sign: String?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().setUserId(userId, sign)
            }
        })
    }
    fun userId(userId: String?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().setUserId(userId, null)
            }
        })
    }

    fun externalAppVersion(externalAppVersion: IAppVersion) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().setExternalAppVersion(externalAppVersion)
            }
        })
    }

    fun gameDemoMode(gameDemoMode: Boolean) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().gameDemoMode(gameDemoMode)
            }
        })
    }

    fun lang(lang: Locale?, changeLayoutDirection: Boolean) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().setLang(lang, changeLayoutDirection)
            }
        })
    }

    fun placeholder(key: String?, value: String?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().setPlaceholder(key, value)
            }
        })
    }

    fun imagePlaceholder(key: String?, value: ImagePlaceholderValue?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().setImagePlaceholder(key, value)
            }
        })
    }

    fun placeholders(newPlaceholders: MutableMap<String?, String?>) {}

    fun imagePlaceholders(newPlaceholders: MutableMap<String?, ImagePlaceholderValue?>) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().setImagePlaceholders(newPlaceholders)
            }
        })
    }

    fun commonAppearanceManager(appearanceManager: AppearanceManager?) {
        AppearanceManager.setCommonInstance(appearanceManager);
    }

    fun tags(tags: MutableList<String?>?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().setTags(tags)
            }
        })
    }

    fun addTags(tags: MutableList<String?>?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().addTags(tags)
            }
        })
    }

    fun options(extraOptions: MutableMap<String?, String?>?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().options(extraOptions)
            }
        })
    }

    fun removeTags(tags: MutableList<String?>?) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.settingsAPI().removeTags(tags)
            }
        })
    }
}