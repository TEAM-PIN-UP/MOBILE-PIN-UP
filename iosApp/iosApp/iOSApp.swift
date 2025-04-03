import SwiftUI
import KakaoSDKUser
import GoogleSignIn
import FirebaseCore
import ComposeApp
import KakaoSDKAuth
import KakaoSDKCommon

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      FirebaseApp.configure()
      KakaoSDK.initSDK(appKey: "16861d355e1d003cba9138948b8a11f5")
      
    return true
  }
    
    func application(
      _ app: UIApplication,
      open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool {
      var handled: Bool

      handled = GIDSignIn.sharedInstance.handle(url)
      if handled {
        return true
      }

      // If not handled by this app, return false.
      return false
    }
}

class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    func scene(_ scene: UIScene, openURLContexts URLContexts: Set<UIOpenURLContext>) {
        if let url = URLContexts.first?.url {
            if (AuthApi.isKakaoTalkLoginUrl(url)) {
                _ = AuthController.handleOpenUrl(url: url)
            }
        }
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        KoinInitializerKt.doInit(kaKaoLoginController: IOSKaKaoLoginController())
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

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
