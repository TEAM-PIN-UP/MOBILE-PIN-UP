package com.pinup.pinup.platform

import androidx.fragment.app.FragmentActivity

actual class ContextFactory(private val activity: FragmentActivity) {
    actual fun getContext(): Any = activity.baseContext
    actual fun getApplication(): Any = activity.application
    actual fun getActivity(): Any = activity
}