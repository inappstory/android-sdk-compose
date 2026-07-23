package com.inappstory.sdk.compose.views

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.network.JsonParser
import com.inappstory.sdk.share.IASShareData
import com.inappstory.sdk.share.IASShareManager
import com.inappstory.sdk.stories.callbacks.OverlappingContainerActions
import com.inappstory.sdk.stories.callbacks.ShareCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.HashMap

class CustomShare(
    private val shareContent: @Composable (
        shareData: HashMap<String, Any>,
        actions: OverlappingContainerActions
    ) -> Unit
) : ShareCallback {
    private var actions: OverlappingContainerActions? = null

    init {
        instance = this
        InAppStoryManager.getInstance()?.setShareCallback(this)
    }

    private var _viewIsVisible = MutableStateFlow(false);
    private val viewIsVisible = _viewIsVisible.asStateFlow()

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
                    packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
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
            _viewIsVisible.update { true }
        }
    }

    override fun onDestroyView(p0: View?) {

    }

    fun hideView(success: Boolean) {
        coreScope.launch {
            _viewIsVisible.update { false }
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