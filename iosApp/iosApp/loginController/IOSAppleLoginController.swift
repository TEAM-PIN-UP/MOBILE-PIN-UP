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

    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        if let window = presentingViewController?.view.window {
            return window
        }
        
        return UIApplication.shared
            .connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow } ?? ASPresentationAnchor()
    }

    func authorizationController(controller: ASAuthorizationController,
                                 didCompleteWithAuthorization authorization: ASAuthorization) {
        guard let credential = authorization.credential as? ASAuthorizationAppleIDCredential else { return }

        let userId = credential.user
        let email: String? = credential.email
        let fullName: String? = credential.fullName?.formatted()

        resultListener?.onSuccess(snsLoginInfo: SNSUserInfo(
                            socialId: credential.user,
                            snsType: SNSType.apple,
                            email: email,
                            name: fullName,
                            nickname: fullName
                        ))
    }

    func authorizationController(controller: ASAuthorizationController,
                                 didCompleteWithError error: Error) {
        resultListener?.onFail(message: error.localizedDescription)
    }
}

