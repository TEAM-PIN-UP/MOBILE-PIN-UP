# 스토어 배포 CI/CD

GitHub Actions + fastlane 으로 Play Console(Android)과 TestFlight(iOS)에 자동 배포한다.

| | Android | iOS |
|---|---|---|
| 워크플로 | `.github/workflows/release-android.yml` | `.github/workflows/release-ios.yml` |
| fastlane 레인 | `fastlane android deploy track:<트랙>` | `fastlane ios beta` |
| 러너 | `ubuntu-latest` | `macos-26` (Xcode 26.2) |
| 올라가는 곳 | 기본 internal 트랙 (production 은 초안) | TestFlight |
| 빌드 번호 | Play Console 전체 트랙 최대 versionCode + 1 | TestFlight 최신 빌드 번호 + 1 |

기존 `pinup-ci-for-test.yml`(push 마다 debug 빌드)은 그대로 둔다.

## 배포하는 방법

1. 버전 이름을 올리고 커밋한다. 버전 이름은 자동으로 바꾸지 않는다.
   - Android: `composeApp/build.gradle.kts` 의 `versionName`
   - iOS: Xcode `iosApp` 타깃의 Version (`MARKETING_VERSION`)
   - 빌드 번호(versionCode / CFBundleVersion)는 CI 가 알아서 올리므로 건드리지 않아도 된다.
2. 태그를 올리면 두 스토어에 동시에 배포된다.
   ```bash
   git tag v1.1.3
   git push origin v1.1.3
   ```
3. 한쪽만 올리거나 Android 트랙을 고르려면 GitHub → Actions → `Release Android` / `Release iOS` → **Run workflow**.
4. 프로덕션 출시는 사람이 한다.
   - Android: production 트랙은 초안으로만 올라가니 Play Console 에서 검토 후 출시.
   - iOS: TestFlight 처리 완료 후 App Store Connect 에서 심사 제출.

## 최초 1회 설정

### Android

1. **업로드 키스토어 확인**: Play App Signing 을 쓰고 있다면 지금까지 AAB 를 서명하던 업로드 키(.jks)와 alias/비밀번호를 준비한다.
2. **서비스 계정 만들기**
   1. Google Cloud Console → IAM 및 관리자 → 서비스 계정 → 만들기 (역할은 주지 않아도 됨)
   2. 해당 서비스 계정 → 키 → 새 키 만들기 → JSON 다운로드
   3. Play Console → 사용자 및 권한 → 새 사용자 초대 → 서비스 계정 이메일 입력
   4. 앱 권한에서 PinUp 을 추가하고 **앱 정보 보기**, **테스트 트랙 출시 관리**, (프로덕션도 쓰면) **프로덕션 출시 관리** 권한을 준다.
   - 권한이 반영되기까지 최대 하루 걸릴 수 있다. 그 전에는 `versionCode 를 읽지 못했습니다` 오류가 난다.

### iOS

1. **App Store Connect API 키 만들기**: App Store Connect → 사용자 및 액세스 → 통합 → App Store Connect API → 팀 키 생성
   - 역할은 **관리(Admin)** 로 만든다. 프로젝트가 자동 서명이라 CI 에서 Xcode 가 배포 인증서·프로파일을 클라우드 관리 방식으로 받아오는데, 이 권한이 Admin 키에만 있다.
   - `.p8` 파일은 한 번만 다운로드된다. Key ID 와 Issuer ID 도 같이 적어 둔다.
2. 알림 확장(`PinUpNotificationService`)도 같은 팀 자동 서명이라 추가 작업은 없다.

### GitHub Secrets 등록

저장소 Settings → Secrets and variables → Actions 에 등록하거나, 아래처럼 `gh` 로 파일에서 바로 넣는다. (값을 채팅/문서에 붙여 넣지 말 것)

| 이름 | 내용 | 사용처 |
|---|---|---|
| `LOCAL_PROPERTIES` | `local.properties` 내용 그대로. **`sdk.dir` 줄은 빼고** kakao/naver/google 키만 | Android, iOS |
| `GOOGLE_SERVICES_JSON` | `google-services.json` 의 base64 (이미 등록돼 있으면 그대로) | Android |
| `ANDROID_KEYSTORE_BASE64` | 업로드 키스토어 `.jks` 의 base64 | Android |
| `ANDROID_KEYSTORE_PASSWORD` | 키스토어 비밀번호 | Android |
| `ANDROID_KEY_ALIAS` | 키 alias | Android |
| `ANDROID_KEY_PASSWORD` | 키 비밀번호 | Android |
| `PLAY_STORE_JSON_KEY` | 서비스 계정 JSON 키의 base64 | Android |
| `ASC_KEY_P8_BASE64` | App Store Connect API 키 `.p8` 의 base64 | iOS |
| `ASC_KEY_ID` | API 키 Key ID | iOS |
| `ASC_ISSUER_ID` | API 키 Issuer ID | iOS |

```bash
grep -v '^sdk.dir' local.properties | grep -v '^kotlin.apple.cocoapods.bin' | gh secret set LOCAL_PROPERTIES
base64 -i composeApp/google-services.json | gh secret set GOOGLE_SERVICES_JSON
base64 -i /경로/upload.jks | gh secret set ANDROID_KEYSTORE_BASE64
gh secret set ANDROID_KEYSTORE_PASSWORD
gh secret set ANDROID_KEY_ALIAS
gh secret set ANDROID_KEY_PASSWORD
base64 -i /경로/play-service-account.json | gh secret set PLAY_STORE_JSON_KEY
base64 -i /경로/AuthKey_XXXXXXXXXX.p8 | gh secret set ASC_KEY_P8_BASE64
gh secret set ASC_KEY_ID
gh secret set ASC_ISSUER_ID
```

값 없이 `gh secret set 이름` 만 치면 입력 프롬프트가 뜬다.

## 로컬에서 같은 레인 실행하기

```bash
bundle install
export LC_ALL=en_US.UTF-8 LANG=en_US.UTF-8

# Android
export PLAY_STORE_JSON_KEY_PATH=/경로/play-service-account.json
export ANDROID_KEYSTORE_PATH=/경로/upload.jks ANDROID_KEYSTORE_PASSWORD=... ANDROID_KEY_ALIAS=... ANDROID_KEY_PASSWORD=...
bundle exec fastlane android deploy track:internal

# iOS
export ASC_KEY_PATH=/경로/AuthKey_XXXXXXXXXX.p8 ASC_KEY_ID=... ASC_ISSUER_ID=...
bundle exec fastlane ios beta
```

`ANDROID_KEYSTORE_PATH` 가 없으면 Gradle 은 release 서명 설정을 붙이지 않는다. 그래서 Android Studio 의 Generate Signed Bundle 흐름은 예전과 똑같이 쓸 수 있다.

## 문제 해결

- **`versionCode 를 읽지 못했습니다`**: 서비스 계정이 Play Console 에 초대되지 않았거나 권한이 아직 반영되지 않았다.
- **iOS `No signing certificate "iOS Distribution" found` / 프로파일 오류**: API 키 역할이 Admin 이 아닌 경우가 대부분이다. 조직 정책상 Admin 키를 못 쓰면 fastlane `match` 로 인증서를 별도 private 저장소에 두는 수동 서명 방식으로 바꿔야 한다.
- **`Xcode_26.2.app` 을 찾을 수 없음**: GitHub 러너에서 해당 Xcode 가 빠진 것이다. `release-ios.yml` 의 경로를 러너에 있는 버전으로 올린다. (목록: actions/runner-images 의 macos-26 Readme)
- **iOS 빌드가 Gradle 단계에서 `NullPointerException`**: `LOCAL_PROPERTIES` 에 kakao/naver/google 키가 빠졌다. `composeApp/build.gradle.kts` 의 `getApiKey()` 가 구성 단계에서 읽는다.
- **태그 배포가 한쪽만 실패**: 두 워크플로는 서로 독립이다. 실패한 쪽만 Actions 에서 Re-run 하면 된다. 빌드 번호는 스토어 조회 기준이라 재실행해도 겹치지 않는다.
