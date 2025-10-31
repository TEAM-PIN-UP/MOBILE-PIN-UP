package com.pinup.pinup

import com.pinup.pinup.platform.KakaoDeepLinkStore
import com.pinup.pinup.platform.hLog

class KakaoLinkBridge {
    fun onOpenFromKakao(userId: String) {
        KakaoDeepLinkStore.onNewParams(userId)
    }
}