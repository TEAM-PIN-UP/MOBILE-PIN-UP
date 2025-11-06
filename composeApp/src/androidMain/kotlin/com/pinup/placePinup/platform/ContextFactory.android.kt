package com.pinup.placePinup.platform

import android.app.Activity

actual class ContextFactory(private val activity: Activity) {
    actual fun getContext(): Any = activity.baseContext
    actual fun getApplication(): Any = activity.application
    actual fun getActivity(): Any = activity
}