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

class IOSAppleLoginController: NSObject,
                               AppleLoginController,
                               ASAuthorizationControllerDelegate,
                               ASAuthorizationControllerPresentationContextProviding {

    private var resultListener: SNSLoginResultListener?
    private weak var presentingViewController: UIViewController?

    func doLogin(resultListener: SNSLoginResultListener, context: Any) {
        self.resultListener = resultListener
        self.presentingViewController = context as? UIViewController
        
        let appleIDProvider = ASAuthorizationAppleIDProvider()
        let request = appleIDProvider.createRequest()
        request.requestedScopes = [.fullName, .email]

        let authorizationController = ASAuthorizationController(authorizationRequests: [request])
        authorizationController.delegate = self
        authorizationController.presentationContextProvider = self
        authorizationController.performRequests()
    }

    // ✅ 필수: 어떤 윈도우 위에 애플 로그인 창 띄울지
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        if let window = presentingViewController?.view.window {
            return window
        }
        // fallback (키 윈도우 찾기)
        return UIApplication.shared
            .connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow } ?? ASPresentationAnchor()
    }

    // ✅ 로그인 성공
    func authorizationController(controller: ASAuthorizationController,
                                 didCompleteWithAuthorization authorization: ASAuthorization) {
        guard let credential = authorization.credential as? ASAuthorizationAppleIDCredential else { return }

        let userId = credential.user
        let email = credential.email
        let fullName = credential.fullName

        // TODO: 여기서 resultListener로 성공 전달
        print("\(userId) \(email ?? "") \(fullName?.description ?? "")")

    }

    // ✅ 로그인 실패
    func authorizationController(controller: ASAuthorizationController,
                                 didCompleteWithError error: Error) {
        // TODO: 여기서 resultListener로 실패 전달
        // resultListener.onFailure(error: error)
        print("실패")
    }
}

