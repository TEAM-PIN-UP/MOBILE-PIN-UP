//
//  IOSAppleLoginController.swift
//  iosApp
//
//  Created by seok on 12/2/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import AuthenticationServices
import ComposeApp

class IOSAppleLoginController: AppleLoginController {
    func doLogin(resultListener: SNSLoginResultListener, context: Any) {
        // 애플 ID Provider 생성 (애플로그인 요청을 생성하기 위한 ASAuthorizationAppleIDProvider객체임)
        let appleIDProvider = ASAuthorizationAppleIDProvider()
        // 애플 로그인에 필요한 request 객체 생성하기
        let request = appleIDProvider.createRequest()
        // 사용자에게 얻고자 하는 정보 지정하기 (이름, 메일)
        request.requestedScopes = [.fullName, .email]

        // 애플로그인 절차를 관리하는 컨트롤러이다.
        let authorizationController = ASAuthorizationController(authorizationRequests: [request])
        // 로그인 과정의 성공/실패 결과를 처리한다.
        //authorizationController.delegate = self
        // 인증 컨트롤러가 어떤 화면에 표시될지 결정하는 부분. 보통 현재 화면을 지정한다.
        //authorizationController.presentationContextProvider = self
        // 애플로그인 request 시작 !
        authorizationController.performRequests()
    }
}
    
