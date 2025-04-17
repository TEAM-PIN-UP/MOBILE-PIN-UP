package com.pinup.pinup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pinup.pinup.platform.ContextFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contextFactory = ContextFactory(this)
        setContent {
            PinUpApp(
                contextFactory = contextFactory
            )
        }
    }
}