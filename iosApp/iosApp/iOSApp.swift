import SwiftUI
import GoogleSignIn
import FirebaseCore
import ComposeApp
import KakaoSDKAuth
import KakaoSDKCommon
import NidThirdPartyLogin
import NMapsMap

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      FirebaseApp.configure()
      KakaoSDK.initSDK(appKey: "25ad8729a58b6d767f7e48a2d359d54b")
      NidOAuth.shared.initialize()
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
            if (NidOAuth.shared.handleURL(url) == true) { // 네이버앱에서 전달된 Url인 경우
                  return
            }
            handleKakaoShareUrl(url)
        }
    }
    
    private func handleKakaoShareUrl(_ url: URL) {
            guard let components = URLComponents(url: url, resolvingAgainstBaseURL: false),
                  let queryItems = components.queryItems else { return }

            // query 파라미터 → [String:String] 딕셔너리
            var params: [String:String] = [:]
            for item in queryItems {
                params[item.name] = item.value ?? ""
            }

            // KAKAO_USER_ID 값 꺼내보기
            if let userId = params["userId"] {
                print("✅ 카카오 공유 링크 userId: \(userId)")
                // 👉 Kotlin으로 전달 (Compose에서 쓰기 위해)
                KakaoLinkBridge().onOpenFromKakao(userId: userId)
            } else {
                print("⚠️ 카카오 공유 URL이지만 userId 없음")
            }
        }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        KoinInitializerKt.doInit(
            kaKaoLoginController: IOSKaKaoLoginController(),
            kaKaoShareController: IOSKaKaoShareController(),
            naverLoginController: IOSNaverLoginController()
        )
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
