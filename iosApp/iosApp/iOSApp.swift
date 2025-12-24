import SwiftUI
import GoogleSignIn
import FirebaseCore
import ComposeApp
import KakaoSDKAuth
import KakaoSDKCommon
import NidThirdPartyLogin
import NMapsMap
import FirebaseMessaging
import UserNotifications

class AppDelegate: NSObject, UIApplicationDelegate, MessagingDelegate, UNUserNotificationCenterDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      FirebaseApp.configure()
      KakaoSDK.initSDK(appKey: "25ad8729a58b6d767f7e48a2d359d54b")
      Messaging.messaging().delegate = self
      
      UNUserNotificationCenter.current().delegate = self
      UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
          if let error = error {
              print("Notification permission error:", error)
              return
          }
          print("Notification permission granted:", granted)

          
          DispatchQueue.main.async {
              UIApplication.shared.registerForRemoteNotifications()
          }
      }

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
    
    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        guard let fcmToken else { return }
        FCMLinkBridge().setFcmIos(token: fcmToken)
        print("FCM Token:", fcmToken)
        if let tokenData = Messaging.messaging().apnsToken {
            let hex = tokenData.map { String(format: "%02x", $0) }.joined()
            print("✅ APNs token (hex): \(hex)")
        } else {
            print("⚠️ APNs token is nil")
        }

    }

    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        print("Receive silent push>", userInfo)
        completionHandler(.newData)
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification) async -> UNNotificationPresentationOptions {
        let userInfo = notification.request.content.userInfo
        print(userInfo)
        
        
        if #available(iOS 14.0, *) {
            return [.sound, .banner, .list]
        } else {
            return []
        }
    }
    
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo
        print("🔔 notification tapped userInfo:", userInfo)

        // 1) custom 딕셔너리 꺼내기
        if let custom = userInfo["custom"] as? [String: Any] {
            // 2) 내부 키 파싱
            let targetId = custom["targetId"] as? Int
            let type = custom["type"] as? String
        } else {
            print("⚠️ custom 없음 (payload 구조 확인 필요)")
        }

        FCMLinkBridge().updateTargetId(id: targetId)
        FCMLinkBridge().updateType(type: type)

        completionHandler()
    }


    
    func handleKakaoShareUrl(_ url: URL) -> Bool {
        guard let components = URLComponents(url: url, resolvingAgainstBaseURL: false),
        let queryItems = components.queryItems else { return false }

        // query 파라미터 → [String:String] 딕셔너리
        var params: [String:String] = [:]
        for item in queryItems {
            params[item.name] = item.value ?? ""
        }

        if let userId = params["userId"] {
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
