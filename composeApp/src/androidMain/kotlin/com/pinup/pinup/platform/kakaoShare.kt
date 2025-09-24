package com.pinup.pinup.platform

import android.content.ActivityNotFoundException
import android.content.Context
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient

actual fun kakaoShare(
    context: Any
) {
    val templateId = 124510.toLong()
    // 카카오톡 설치여부 확인
    if (ShareClient.instance.isKakaoTalkSharingAvailable(context as Context)) {
        ShareClient.instance.shareCustom(context, templateId) { sharingResult, error ->
            if (error != null) {
                hLog("카카오톡 공유 실패" + error)
            }
            else if (sharingResult != null) {
                hLog("카카오톡 공유 성공${sharingResult.intent}")
                context.startActivity(sharingResult.intent)
            }
        }
    } else {
        // 카카오톡 미설치: 웹 공유 사용 권장
        val sharerUrl = WebSharerClient.instance.makeCustomUrl(templateId)
        // 1. CustomTabsServiceConnection 지원 브라우저 열기
        try {
            KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
        } catch(e: UnsupportedOperationException) {
            hLog("CustomTabsServiceConnection 지원 브라우저가 없음")
        }

        // 2. CustomTabsServiceConnection 미지원 브라우저 열기
        try {
            KakaoCustomTabsClient.open(context, sharerUrl)
        } catch (e: ActivityNotFoundException) {
            hLog("디바이스에 설치된 인터넷 브라우저가 없음")
        }
    }
}