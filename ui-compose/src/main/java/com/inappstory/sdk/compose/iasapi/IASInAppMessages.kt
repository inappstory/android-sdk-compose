package com.inappstory.sdk.compose.iasapi

import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.core.api.IASCallbackType
import com.inappstory.sdk.inappmessage.CloseInAppMessageCallback
import com.inappstory.sdk.inappmessage.InAppMessageData
import com.inappstory.sdk.inappmessage.InAppMessageLoadCallback
import com.inappstory.sdk.inappmessage.InAppMessagePreloadSettings
import com.inappstory.sdk.inappmessage.InAppMessageSlideData
import com.inappstory.sdk.inappmessage.InAppMessageWidgetCallback
import com.inappstory.sdk.inappmessage.ShowInAppMessageCallback
import com.inappstory.sdk.inappmessage.ShowInAppMessageSlideCallback


class IASInAppMessages {
    private fun useCore(callback: UseIASCoreCallback) {
        InAppStoryManager.useCore(callback)//d
    }

    fun onShowInAppMessageSlide(showSlide: (iamSlideData: InAppMessageSlideData?) -> Unit) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.SHOW_IN_APP_MESSAGE,
                    object : ShowInAppMessageSlideCallback {
                        override fun showSlide(iamSlideData: InAppMessageSlideData?) {
                            showSlide.invoke(iamSlideData)
                        }
                    }
                );
            }
        })
    }

    fun onShowInAppMessage(showIAM: (iamData: InAppMessageData?) -> Unit) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.SHOW_IN_APP_MESSAGE,
                    object : ShowInAppMessageCallback {
                        override fun showInAppMessage(iamData: InAppMessageData?) {
                            showIAM.invoke(iamData)
                        }
                    }
                );
            }
        })
    }

    fun onCloseInAppMessage(closeIAM: (iamData: InAppMessageData?) -> Unit) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.CLOSE_IN_APP_MESSAGE,
                    object : CloseInAppMessageCallback {
                        override fun closeInAppMessage(iamData: InAppMessageData?) {
                            closeIAM.invoke(iamData)
                        }
                    }
                );
            }
        })
    }

    fun onInAppMessageWidget(
        widgetEvent: (
            inAppMessageData: InAppMessageData?,
            widgetEventName: String?,
            widgetData: Map<String?, String?>?
        ) -> Unit
    ) {
        useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.callbacksAPI().setCallback(
                    IASCallbackType.IN_APP_MESSAGE_WIDGET,
                    object : InAppMessageWidgetCallback {
                        override fun inAppMessageWidget(
                            inAppMessageData: InAppMessageData?,
                            widgetEventName: String?,
                            widgetData: Map<String?, String?>?
                        ) {
                            widgetEvent.invoke(
                                inAppMessageData,
                                widgetEventName,
                                widgetData
                            )
                        }

                    }
                );
            }
        })
    }


    fun preload(
        inAppMessagePreloadSettings: InAppMessagePreloadSettings?,
        iamLoaded: (id: Int) -> Unit = {},
        allIamLoaded: () -> Unit = {},
        iamLoadError: (id: Int) -> Unit = {},
        allIamLoadError: () -> Unit = {},
        isEmpty: () -> Unit = {},
    ) {
        InAppStoryManager.useCoreInSeparateThread(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.inAppMessageAPI().preload(
                    inAppMessagePreloadSettings,
                    object : InAppMessageLoadCallback {
                        override fun loaded(id: Int) {
                            iamLoaded.invoke(id)
                        }

                        override fun allLoaded() {
                            allIamLoaded.invoke()
                        }

                        override fun loadError(id: Int) {
                            iamLoadError.invoke(id)
                        }

                        override fun loadError() {
                            allIamLoadError.invoke()
                        }

                        override fun isEmpty() {
                            isEmpty.invoke()
                        }
                    }
                )
            }
        })
    }
}