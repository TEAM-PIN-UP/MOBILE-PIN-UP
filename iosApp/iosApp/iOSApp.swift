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
      KakaoSDK.initSDK(appKey: "16861d355e1d003cba9138948b8a11f5")
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
        }
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        KoinInitializerKt.doInit(
            kaKaoLoginController: IOSKaKaoLoginController(),
            naverLoginController: IOSNaverLoginController()
        )
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
