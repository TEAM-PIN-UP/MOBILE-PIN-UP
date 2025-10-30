//
//  KakaoShareIos.swift
//  iosApp
//
//  Created by seok on 10/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import ComposeApp
import KakaoSDKTemplate
import KakaoSDKShare

class IOSKaKaoShareController: KaKaoShareController {
  func kakaoShare(
      context: Any,
      memberId: Int32,
      memberName: String
  ) {
    // 컨텐츠 생성
    print("IOSKaKaoLoginController call")
    let content = Content(
        title: "공유 커스텀 테스트",
        description: "정상적으로 공유가 잘되나?",
        link: Link(
            webUrl: URL(string:"hhttps://green1229.tistory.com"),
            mobileWebUrl: URL(string:"hhttps://green1229.tistory.com/m")
        )
    )

    // 버튼 생성
    let buttons = [
      Button(
          title: "웹으로 보기",
          link: Link(
              webUrl: URL(string:"https://green1229.tistory.com"),
              mobileWebUrl: URL(string:"https://green1229.tistory.com/m")
          )
      ),
      Button(
          title: "앱으로 보기",
          link: Link(
              webUrl: URL(string:"hhttps://green1229.tistory.com"),
              mobileWebUrl: URL(string:"hhttps://green1229.tistory.com/m"),
              iosExecutionParams: ["key1": "value1"]
          )
      )
    ]

    // 템플릿 생성
    let template = FeedTemplate(
        content: content,
        social: Social(
            likeCount: 999,
            commentCount: 232,
            sharedCount: 777
        ),
        buttons: buttons
    )

    // 카카오톡 공유하기
    if ShareApi.isKakaoTalkSharingAvailable() {
      ShareApi.shared.shareDefault(templatable: template) { result, error in
        if let error = error {
          print("공유 실패: \(error)")
          return
        }

        // 성공 시 URL 열기
        if let url = result {
          DispatchQueue.main.async {
            UIApplication.shared.open(url.url, options: [:], completionHandler: nil)
          }
        }
      }
    } else {
      // 카카오톡 미설치시 웹 공유 띄우기
      if let url = ShareApi.shared.makeDefaultUrl(templatable: template) {
        UIApplication.shared.open(url, options: [:], completionHandler: nil)
      } else {
          print("공유실패")
      }
    }
  }
}
