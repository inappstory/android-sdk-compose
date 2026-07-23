package com.inappstory.sdk.compose.controllers

import androidx.fragment.app.FragmentManager
import com.inappstory.sdk.CancellationToken
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.inappmessage.InAppMessageContainerProvider
import com.inappstory.sdk.inappmessage.InAppMessageContainerSettings
import com.inappstory.sdk.inappmessage.InAppMessageData
import com.inappstory.sdk.inappmessage.InAppMessageOpenSettings
import com.inappstory.sdk.inappmessage.InAppMessageScreenActions
import com.inappstory.sdk.inappmessage.InAppMessageViewController
import com.inappstory.sdk.inappmessage.domain.reader.IAMViewController

internal class FragmentSettings(
    val fragmentId: Int,
    val fragmentManager: FragmentManager
)

class InAppMessageFragmentScreenController {
    internal var getSettings: (() -> FragmentSettings?)? = null

    fun openInAppMessage(
        openSettings: InAppMessageOpenSettings,
        viewController: InAppMessageViewController? = null,
        readerOpened: () -> Unit = {},
        readerClosed: () -> Unit = {},
        readerOpenErr: () -> Unit = {},
    ): CancellationToken? {
        val token = getSettings?.invoke()?.let { settings ->
            InAppStoryManager.getInstance()?.showInAppMessage(
                openSettings,
                object : InAppMessageContainerProvider {
                    override fun provideContainer(messageData: InAppMessageData?):
                            InAppMessageContainerSettings? =
                        InAppMessageContainerSettings().fragment(
                            settings.fragmentManager,
                            settings.fragmentId
                        )

                    override fun layoutController(): IAMViewController? =
                        viewController
                },
                object : InAppMessageScreenActions {
                    override fun readerIsOpened() {
                        readerOpened.invoke()
                    }

                    override fun readerOpenError(error: String?) {
                        readerClosed.invoke()
                    }

                    override fun readerIsClosed() {
                        readerOpenErr.invoke()
                    }
                }
            )
        }
        return token
    }
}