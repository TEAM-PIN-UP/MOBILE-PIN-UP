package com.pinup.placePinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ReportType(name: String) {
    COMMENT("댓글"), PINLOG("핀로그"), USER("계정")
}