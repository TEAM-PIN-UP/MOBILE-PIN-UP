package com.pinup.pinup

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import coil3.PlatformContext
import org.koin.java.KoinJavaComponent

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun openBrowser(url: String, context: PlatformContext) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
actual fun getRealPathFromUri(contentUri: String): String? {
    val context: Context = KoinJavaComponent.getKoin().get()
    var cursor: Cursor? = null
    try {
        val proj = arrayOf(MediaStore.Video.Media.DATA)
        cursor = context.contentResolver.query(contentUri.toUri(), proj, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(columnIndex!!)
        }
    } finally {
        cursor?.close()
    }
    return null
}

actual fun hLog(message: String) {
    Log.d("jsh", message)
}

actual fun dataStorePreferences(): DataStore<Preferences> {
    val context: Context = KoinJavaComponent.getKoin().get()
    return createDataStore(
        producePath = { context.filesDir.resolve(DATA_STORE_PREFERENCE).absolutePath }
    )
}

actual fun pxToDp(px: Float): Float {
    val context: Context = KoinJavaComponent.getKoin().get()
    val metric = context.resources.displayMetrics
    return (px / (metric.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}
