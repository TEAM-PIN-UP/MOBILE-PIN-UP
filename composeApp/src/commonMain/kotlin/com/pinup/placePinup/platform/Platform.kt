package com.pinup.placePinup.platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import coil3.PlatformContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath

interface Platform {
    val name: String
}

expect fun openBrowser(url: String, context: PlatformContext)
expect fun getPlatformName(): String
expect fun getRealPathFromUri(contentUri: String): String?
expect fun hLog(message: String)
expect fun dataStorePreferences(): DataStore<Preferences>
expect fun pxToDp(px: Float): Float

fun createDataStore(
    producePath: () -> String,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        scope = coroutineScope,
        produceFile = { producePath().toPath() }
    )

const val DATA_STORE_PREFERENCE = "pinup.preferences_pb"
const val PLATFORM_ANDROID = "Android"
const val PLATFORM_IOS = "iOS"