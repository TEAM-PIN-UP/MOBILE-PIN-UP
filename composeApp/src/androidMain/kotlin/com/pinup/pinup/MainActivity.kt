package com.pinup.pinup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.pinup.pinup.platform.ContextFactory


class MainActivity : FragmentActivity() {
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