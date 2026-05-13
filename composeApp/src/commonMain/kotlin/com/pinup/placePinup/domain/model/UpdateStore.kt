package com.pinup.placePinup.domain.model

data class UpdateStore(
    val url: String,
    val market: String,
    val packageNameOrBundleId: String
)
