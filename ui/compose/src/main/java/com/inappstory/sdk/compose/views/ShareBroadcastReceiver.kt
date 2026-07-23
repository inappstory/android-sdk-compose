package com.inappstory.sdk.compose.views

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ShareBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        CustomShare.instance?.hideView(true)
    }
}