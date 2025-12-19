package com.pinup.placePinup

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.util.Const


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contextFactory = ContextFactory(this)
        Firebase.initialize(this)
        val data: Uri? = intent?.data
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            )
        )

        setContent {
            PinUpApp(
                userId = data?.getQueryParameter(Const.ShareKey.KAKAO_USER_ID)?.toInt() ?: -1,
                contextFactory = contextFactory
            )
        }
    }
}