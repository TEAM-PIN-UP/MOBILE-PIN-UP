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

  /// Kotlin expect/actual과 정확히 동일한 시그니처(Int32!!).
  func kakaoShare(
      context: Any,
      memberId: Int32,
      memberName: String
  ) {
    print("IOSKaKaoShareController call")

    let title = "\(memberName) 님의 핀업 계정"
    let desc = "핀업에서 \(memberName) 님의 핀들을 구경해 보세요."
    let buttonTitle = "핀업으로 이동하기"

    let webLinkString = "https://www.youtube.com/watch?v=yWP--1gsr20&list=RDyWP--1gsr20&start_radio=1"
    let userIdParamKey = "userId"

    // ---------- 2) 템플릿 구성 (Android getFeed와 동등) ----------
    // Android 코드의 imageUrl과 동일
    let imageUrl = URL(string: "https://lh3.googleusercontent.com/d/1ui1iK7vFLd1wj8KuiCgXMQo3YFGMd4w-")!

    // 안드로이드의 Content(link = Link())와 유사하게, iOS는 빈 Link()도 가능
    // 만약 컴파일러가 빈 생성자를 허용하지 않으면 아래 한 줄을
    //   Link(webUrl: URL(string: webLinkString), mobileWebUrl: URL(string: webLinkString))
    // 로 바꿔도 됩니다.
    let content = Content(
        title: title,
        imageUrl: imageUrl,
        description: desc,
        link: Link()
    )

    // 버튼: 실행 파라미터 + 폴백 웹 링크 (Android와 동일 키/링크)
    let buttonLink = Link(
        webUrl: URL(string: webLinkString),
        mobileWebUrl: URL(string: webLinkString),
        androidExecutionParams: [userIdParamKey: String(memberId)],
        iosExecutionParams:     [userIdParamKey: String(memberId)],
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
