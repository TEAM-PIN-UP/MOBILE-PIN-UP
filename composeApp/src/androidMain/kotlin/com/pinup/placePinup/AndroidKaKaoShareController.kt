package com.pinup.placePinup

import android.content.ActivityNotFoundException
import android.content.Context
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.pinup.placePinup.platform.hLog
import com.pinup.placePinup.ui.login.sns.KaKaoShareController
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.util.Const

class AndroidKaKaoShareController: KaKaoShareController {
    override fun kakaoShare(
        context: Any,
        memberId: Int,
        memberName: String,
    ) {
        val feedTemplate = getFeed(memberId, memberName)
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context as Context)) {
            ShareClient.instance.shareDefault(context, feedTemplate) { sharingResult, error ->
                if (error != null) {
                    hLog("카카오톡 공유 실패" + error)
                } else if (sharingResult != null) {
                    hLog("카카오톡 공유 성공${sharingResult.intent}")
                    context.startActivity(sharingResult.intent)
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(feedTemplate)
            // 1. CustomTabsServiceConnection 지원 브라우저 열기
            try {
                KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
            } catch (e: UnsupportedOperationException) {
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

    private fun getFeed(memberId: Int, memberName: String): FeedTemplate {
        return FeedTemplate(
            content =
                Content(
                    title = Texts.Kakao.getProfileShareTitle(memberName),
                    description = Texts.Kakao.getProfileShareContent(memberName),
                    imageUrl = "https://lh3.googleusercontent.com/d/1ui1iK7vFLd1wj8KuiCgXMQo3YFGMd4w-",
                    link = Link(),
                ),
            buttons =
                listOf(
                    Button(
                        Texts.Kakao.PROFILE_SHARE_BUTTON,
                        Link(
                            androidExecutionParams = mapOf(Const.ShareKey.KAKAO_USER_ID to memberId.toString()),
                            mobileWebUrl = Const.ShareKey.WEB_LINK,
                            webUrl = Const.ShareKey.WEB_LINK
                        ),
                    ),
                ),
        )
    }
}