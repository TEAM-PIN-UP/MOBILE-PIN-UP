//
//  LoginBridge.swift
//  iosApp
//
//  Created by 정상훈 on 4/2/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

@objc public class LoginBridge: NSObject {
    
    @objc static func kakaoLogin(completion: @escaping (String) -> Void) {
        print("Swift에서 버튼 클릭 이벤트 감지됨")
    }
}
