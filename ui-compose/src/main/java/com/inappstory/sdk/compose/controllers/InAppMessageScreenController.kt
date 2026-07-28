package com.inappstory.sdk.compose.controllers

import android.widget.FrameLayout
import com.inappstory.sdk.CancellationToken
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.inappmessage.InAppMessageContainerProvider
import com.inappstory.sdk.inappmessage.InAppMessageContainerSettings
import com.inappstory.sdk.inappmessage.InAppMessageData
import com.inappstory.sdk.inappmessage.InAppMessageOpenSettings
import com.inappstory.sdk.inappmessage.InAppMessageScreenActions
import com.inappstory.sdk.inappmessage.InAppMessageViewController
import com.inappstory.sdk.inappmessage.domain.reader.IAMViewController

class InAppMessageScreenController {
    internal var getLayout: (() -> FrameLayout?)? = null
    private var viewController: InAppMessageViewController = InAppMessageViewController()

    fun pauseView() {
        viewController.pauseView()
    }

    fun closeView() {
        viewController.closeView()
    }

    fun resumeView() {
        viewController.resumeView()
    }

    fun openInAppMessage(
        openSettings: InAppMessageOpenSettings,
        readerOpened: () -> Unit = {},
        readerClosed: () -> Unit = {},
        readerOpenErr: () -> Unit = {},
    ): CancellationToken? {
        val token = getLayout?.invoke()?.let { layout ->
            InAppStoryManager.getInstance()?.showInAppMessage(
                openSettings,
                object : InAppMessageContainerProvider {
                    override fun provideContainer(messageData: InAppMessageData?):
                            InAppMessageContainerSettings? =
                        InAppMessageContainerSettings().layout(
                            layout
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