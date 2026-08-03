package com.inappstory.sdk.compose.views

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.share.IASShareData
import com.inappstory.sdk.share.IASShareManager
import com.inappstory.sdk.stories.callbacks.OverlappingContainerActions
import com.inappstory.sdk.stories.callbacks.ShareCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.HashMap

class CustomShare(
    private val shareContent: @Composable (
        shareData: HashMap<String, Any>,
        actions: OverlappingContainerActions
    ) -> Unit,
    private val updateViewVisibility: (Boolean) -> Unit,
    private val getViewActions: (data: Map<String, Any?>?) -> Unit = {},
) : ShareCallback {
    private var actions: OverlappingContainerActions? = null

    init {
        instance = this
        InAppStoryManager.getInstance()?.setShareCallback(this)
    }

    private var coreScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        var instance: CustomShare? = null

        fun share(
            context: Context,
            data: IASShareData,
            packageName: String?
        ) {
            val shareManager = IASShareManager()
            if (packageName != null) shareManager.shareToSpecificApp(
                ShareBroadcastReceiver::class.java,
                context as Activity,
                data,
                packageName
            ) else shareManager.shareDefault(
                ShareBroadcastReceiver::class.java,
                context as Activity,
                data
            )
        }

        @Suppress("DEPRECATION")
        fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.getPackageInfo(
                        packageName,
                        PackageManager.PackageInfoFlags.of(0)
                    )
                } else {
                    packageManager.getPackageInfo(packageName, 0)
                }
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }
    }

    override fun getView(
        context: Context,
        shareData: HashMap<String, Any>,
        actions: OverlappingContainerActions
    ): View {
        this.actions = actions
        getViewActions.invoke(shareData)
        return ComposeView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setContent {
                shareContent(shareData, actions)
            }
        }

    }

    private fun shareNotSuccess() {
        val data = HashMap<String, Any>()
        data["shared"] = false
        actions?.closeView(data)
        actions = null
    }

    private fun shareSuccess() {
        val data = HashMap<String, Any>()
        data["shared"] = true
        actions?.closeView(data)
        actions = null
    }

    override fun viewIsVisible(view: View?) {
        coreScope.launch {
            delay(200)
            updateViewVisibility(true)
        }
    }

    override fun onDestroyView(p0: View?) {

    }

    fun hideView(success: Boolean) {
        coreScope.launch {
            updateViewVisibility(false)
            delay(500)
            if (success) {
                shareSuccess()
            } else {
                shareNotSuccess()
            }
        }
    }

    override fun onBackPress(view: View?, actions: OverlappingContainerActions): Boolean {
        hideView(false)
        return true
    }
}