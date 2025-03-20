package com.pinup.pinup

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import coil3.PlatformContext

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun openBrowser(url: String, context: PlatformContext) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
actual fun getRealPathFromUri(contentUri: String, context: PlatformContext): String? {
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