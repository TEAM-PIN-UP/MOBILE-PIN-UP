package com.pinup.placePinup.ui.login.sns

interface KaKaoShareController {
    fun kakaoShare(
        context: Any,
        memberId: Int,
        memberName: String,
        shareTitle: String,
        shareContent: String,
        shareButton: String,
    )
}