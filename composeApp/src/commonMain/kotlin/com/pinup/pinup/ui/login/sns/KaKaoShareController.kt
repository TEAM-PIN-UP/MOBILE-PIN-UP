package com.pinup.pinup.ui.login.sns

interface KaKaoShareController {
    fun kakaoShare(
        context: Any,
        memberId: Int,
        memberName: String,
    )
}