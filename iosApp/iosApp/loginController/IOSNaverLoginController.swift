//
//  IOSNaverLoginController.swift
//  iosApp
//
//  Created by 정상훈 on 4/3/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import ComposeApp
import NidThirdPartyLogin

class IOSNaverLoginController: NaverLoginController {
    func doLogin(resultListener: SNSLoginResultListener) {
        if let accessToken = NidOAuth.shared.accessToken,
        !accessToken.isExpired {
            // 접근 토큰이 유효하다면 바로 프로필 API 호출
            fetchUserProfile(accessToken: accessToken, resultListener: resultListener)
        } else {
        // AccessToken이 없거나 유효하지 않은 경우
        NidOAuth.shared.requestLogin { result in
            switch result {
            case .success(let loginResult):
                self.fetchUserProfile(
                    accessToken: loginResult.accessToken,
                    resultListener: resultListener
                )
            case .failure(let error):
                resultListener.onFail(message: error.localizedDescription)
            }
        }
      }
    }

    func fetchUserProfile(accessToken: AccessToken, resultListener: SNSLoginResultListener) {
        NidOAuth.shared.getUserProfile(accessToken: accessToken.tokenString) { result in
        switch result {
          case .success(let profileResult):
            print("User: ", profileResult)
            resultListener.onSuccess(snsLoginInfo: SNSUserInfo(
                socialId: profileResult["id"] ?? "",
                snsType: SNSType.naver,
                email: profileResult["email"],
                name: profileResult["nickname"],
                nickname: profileResult["nickname"])
            )
          case .failure(let error):
            resultListener.onFail(message: error.localizedDescription)
        }
      }
    }
}
