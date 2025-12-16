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
    
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
            let sceneConfig = UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
            sceneConfig.delegateClass = SceneDelegate.self // 여기서 연결
            return sceneConfig
        }

    func application(
        _ app: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool {
        if GIDSignIn.sharedInstance.handle(url) {
            return true
        }

        // 2) 카카오 로그인
        if AuthApi.isKakaoTalkLoginUrl(url) {
            _ = AuthController.handleOpenUrl(url: url)
            return true
        }

        // 3) 네이버 로그인
        if NidOAuth.shared.handleURL(url) {
            return true
        }

        // 4) 카카오 “공유” 딥링크 (iosExecutionParams 쿼리 파싱)
        if handleKakaoShareUrl(url) {
            return true
        }

        // 처리하지 않은 URL
        return false
    }
    
    func handleKakaoShareUrl(_ url: URL) -> Bool {
        guard let components = URLComponents(url: url, resolvingAgainstBaseURL: false),
        let queryItems = components.queryItems else { return false }

        // query 파라미터 → [String:String] 딕셔너리
        var params: [String:String] = [:]
        for item in queryItems {
            params[item.name] = item.value ?? ""
        }

        // KAKAO_USER_ID 값 꺼내보기
        if let userId = params["userId"] {
            print("예아 userId")
            KakaoLinkBridge().onOpenFromKakao(userId: userId)
            return true
        } else {
            print("⚠️ 카카오 공유 URL이지만 userId 없음")
            return false
        }
    }
}

class SceneDelegate: NSObject, UIWindowSceneDelegate {

    var window: UIWindow?

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        window = UIWindow(frame: windowScene.coordinateSpace.bounds)
        window?.windowScene = windowScene


        window?.rootViewController = UIHostingController(rootView: ContentView())
        window?.makeKeyAndVisible()
        
        if let url = connectionOptions.urlContexts.first?.url {
            handleKakaoShareUrl(url)
        }
    }
    
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
    
    func handleKakaoShareUrl(_ url: URL) {
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
            naverLoginController: IOSNaverLoginController(),
            appleLoginController: IOSAppleLoginController(),
        )
    }
    
    var body: some Scene {
        WindowGroup {
            //ContentView()
        }
    }
}
