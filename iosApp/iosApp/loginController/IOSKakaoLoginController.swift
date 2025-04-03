//
//  IOSKakaoLoginController.swift
//  iosApp
//
//  Created by 정상훈 on 4/3/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import ComposeApp
import KakaoSDKAuth
import KakaoSDKCommon
import KakaoSDKUser

class IOSKaKaoLoginController: KaKaoLoginController {
    func doLogin(resultListener: SNSLoginResultListener) {
        print("IOSKaKaoLoginController call")
        // 카카오톡 실행 가능 여부 확인
        if (UserApi.isKakaoTalkLoginAvailable()) {
            UserApi.shared.loginWithKakaoTalk {(oauthToken, error) in
                if let error = error {
                    resultListener.onFail(message: error.localizedDescription)
                    print(error)
                }
                else {
                    print("loginWithKakaoTalk() success.")

                    // 성공 시 동작 구현
                    self.getKakaoUserInfo(resultListener: resultListener)
                }
            }
        } else {
            // 카카오톡 없어서 계정 로그인
            UserApi.shared.loginWithKakaoAccount {(oauthToken, error) in
                    if let error = error {
                        resultListener.onFail(message: error.localizedDescription)
                        print(error)
                    }
                    else {
                        print("loginWithKakaoAccount() success.")

                        // 성공 시 동작 구현
                        self.getKakaoUserInfo(resultListener: resultListener)
                    }
                }
        }
    }
    
    func getKakaoUserInfo(resultListener: SNSLoginResultListener) {
        UserApi.shared.me() {(user, error) in
            if let error = error {
                resultListener.onFail(message: error.localizedDescription)
                print(error)
            }
            else {
                print("me() success.")
                resultListener.onSuccess(snsLoginInfo: SNSUserInfo(
                    socialId: String(user?.id ?? 0),
                    snsType: SNSType.kakao,
                    email: user?.kakaoAccount?.email,
                    name: user?.kakaoAccount?.name,
                    nickname: user?.kakaoAccount?.profile?.nickname
                ))
            }
        }
    }
}
