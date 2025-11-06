package com.pinup.placePinup

import com.pinup.placePinup.platform.KakaoDeepLinkStore

class KakaoLinkBridge {
    fun onOpenFromKakao(userId: String) {
        KakaoDeepLinkStore.onNewParams(userId)
    }
}