package com.pinup.placePinup.data.request.fcm

import kotlinx.serialization.Serializable

@Serializable
data class SetDeviceTokenRequest(
    val token: String = "",
    val platform: String = "",
)