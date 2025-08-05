package com.pinup.pinup.extentions

import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.json.Json

inline fun <reified T> NavBackStackEntry.jsonToArg(key: String): T? =
    arguments?.getString(key)?.let { Json.decodeFromString<T>(it) }