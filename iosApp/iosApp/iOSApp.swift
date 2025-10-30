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
