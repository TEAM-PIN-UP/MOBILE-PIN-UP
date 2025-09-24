package com.pinup.pinup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.platform.hLog


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contextFactory = ContextFactory(this)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            )
        )
        val data: Uri? = intent?.data
        data?.let {
            val userId = it.getQueryParameter("key")
            hLog("userId1 = $userId")
        }
        setContent {
            PinUpApp(
                userId = data?.getQueryParameter("key")?.toInt() ?: -1,
                contextFactory = contextFactory
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        data?.let {
            val userId = it.getQueryParameter("key")
            hLog("userId2 = $userId")
        }
    }
}