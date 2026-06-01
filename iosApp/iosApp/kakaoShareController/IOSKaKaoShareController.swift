//
//  KakaoShareIos.swift
//  iosApp
//
//  Created by seok on 10/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import UIKit
import ComposeApp
import KakaoSDKTemplate
import KakaoSDKShare

/// Kotlin/Native가 노출한 프로토콜(KaKaoShareController)에 맞춰 구현
/// (필요 시 NSObject 상속을 권장: Obj‑C 런타임과 상호운용 안정)
class IOSKaKaoShareController: NSObject, KaKaoShareController {

  /// Kotlin 인터페이스와 정확히 동일한 시그니처
  func kakaoShare(
      context: Any,
      memberId: Int32,
      memberName: String,
      shareTitle: String,
      shareContent: String,
      shareButton: String
  ) {
    print("IOSKaKaoShareController call")

    let title = shareTitle
    let desc = shareContent
    let buttonTitle = shareButton

    let userIdParamKey = "userId"

    // ---------- 2) 템플릿 구성 (Android getFeed와 동등) ----------
    // Android 코드의 imageUrl과 동일
    let imageUrl = URL(string: "https://lh3.googleusercontent.com/d/1ui1iK7vFLd1wj8KuiCgXMQo3YFGMd4w-")!

    let content = Content(
        title: title,
        imageUrl: imageUrl,
        description: desc,
        link: Link(
            androidExecutionParams: [userIdParamKey: String(memberId)],
            iosExecutionParams:     [userIdParamKey: String(memberId)]
        )
    )

    // 버튼: 실행 파라미터 + 폴백 웹 링크 (Android와 동일 키/링크)
    let buttonLink = Link(
        androidExecutionParams: [userIdParamKey: String(memberId)],
        iosExecutionParams:     [userIdParamKey: String(memberId)]
    )
    let button = Button(title: buttonTitle, link: buttonLink)

    let template = FeedTemplate(
      content: content,
      buttons: [button]
    )

    // ---------- 3) 공유 동작 ----------
    if ShareApi.isKakaoTalkSharingAvailable() {
      // 톡 설치됨 → 인앱으로 공유
      ShareApi.shared.shareDefault(templatable: template) { result, error in
        if let error = error {
          print("카카오톡 공유 실패: \(error)")
          // 실패 시 바로 웹 공유 폴백
          if let url = ShareApi.shared.makeDefaultUrl(templatable: template) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
          }
          return
        }
        if let linkResult = result {
          // 카카오톡 인앱 브라우저의 URL 열기
          DispatchQueue.main.async {
            UIApplication.shared.open(linkResult.url, options: [:], completionHandler: nil)
          }
        }
      }
    } else {
      // 톡 미설치 → 웹 공유 URL
      if let url = ShareApi.shared.makeDefaultUrl(templatable: template) {
        UIApplication.shared.open(url, options: [:], completionHandler: nil)
      } else {
        print("공유 URL 생성 실패")
      }
    }
  }
}
