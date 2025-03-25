package com.pinup.pinup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PinUpApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    PinUpApp()
}