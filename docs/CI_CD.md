# 스토어 배포 CI/CD

GitHub Actions + fastlane 으로 브랜치에 따라 자동 배포한다.

| 브랜치 | Android (`release-android.yml`) | iOS (`release-ios.yml`) |
|---|---|---|
| `develop` | 푸시(머지)마다 Play Console **내부 테스트(internal)** | 푸시(머지)마다 **TestFlight** |
| `master` | 푸시(머지)마다 **프로덕션 전체 출시** (Google 검토 후 공개) | 푸시(머지)마다 **App Store 심사 제출**, 승인되면 자동 출시 |
| 수동 | Actions → Release Android → Run workflow (트랙 선택) | Actions → Release iOS → Run workflow (`beta` / `release` 선택) |

- 저장소가 public 이라 GitHub 호스팅 표준 러너(macOS 포함)를 무료로 쓴다. private 으로 되돌리면 macOS 러너가 Linux 의 약 10배로 차감되므로 iOS 트리거를 다시 줄여야 한다.
- 문서(`*.md`, `docs/`)만 바뀐 푸시는 배포하지 않는다. `iosApp/` 만 바뀌면 Android 는, `composeApp/src/androidMain/` 만 바뀌면 iOS 는 건너뛴다.
- develop 에 연달아 푸시하면 진행 중인 이전 빌드를 취소하고 최신 커밋만 올린다. master 는 취소하지 않는다.
- 기존 `pinup-ci-for-test.yml`(모든 푸시에 debug 빌드)은 그대로다.

## 버전 규칙 (중요)

빌드 번호(versionCode / CFBundleVersion)는 CI 가 스토어 최신 번호 + 1 로 자동으로 올린다. **버전 이름은 사람이 올린다.**

1. 출시할 내용이 develop 에 모이면, develop 에서 버전 이름을 올린다.
   - Android: `composeApp/build.gradle.kts` 의 `versionName`
   - iOS: `iosApp/iosApp.xcodeproj` 의 `MARKETING_VERSION` (Debug/Release 둘 다) 와 `iosApp/iosApp/Info.plist` 의 `CFBundleShortVersionString`
2. `fastlane/metadata/ios/ko/release_notes.txt` 에 이번 버전의 '새로운 기능' 문구를 쓴다. (App Store 업데이트 심사에 필수)
3. develop 을 master 에 머지한다.

안전장치:
- **Android**: 프로덕션에 이미 같은 versionName 이 있으면 빌드 전에 실패한다.
- **iOS**: 앱 버전이 App Store 출시 버전 이하이면 빌드 전에 실패한다. 한 버전이 출시되고 나면 같은 버전으로는 TestFlight 에도 못 올리므로, 출시 직후 develop 의 버전을 다음 버전으로 올려 둔다.

## 최초 1회 설정

### Android

1. **Google Cloud** (Firebase 프로젝트 `pinup-dc3c1`)
   1. API 및 서비스 → 라이브러리 → `Google Play Android Developer API` 사용 설정
   2. IAM 및 관리자 → 서비스 계정 → `play-deploy` 생성 (역할 없음) → 키 → JSON 키 발급
2. **Play Console** → 사용자 및 권한 → 서비스 계정 이메일 초대 → 앱 권한에 PinUp 추가 → 앱 정보 보기, 테스트 트랙 출시·관리, 프로덕션 출시 권한
   - 권한 반영까지 최대 하루. 그 전에는 `versionCode 를 읽지 못했습니다` 오류가 난다.
3. **업로드 키스토어**: Play Console → 앱 무결성 → 앱 서명의 업로드 키 SHA-1 과 `keytool -list -v -keystore <jks> -alias <alias>` 의 SHA1 이 같은지 확인

### iOS

1. **App Store Connect** → 사용자 및 액세스 → 통합 → App Store Connect API → 팀 키 생성, 역할 **관리(Admin)**
   - 프로젝트가 자동 서명이라 CI 에서 Xcode 가 배포 인증서·프로파일을 받아오는데, 이 권한이 Admin 키에만 있다.
   - `.p8` 은 한 번만 다운로드된다. Key ID, Issuer ID 도 적어 둔다.
2. `Info.plist` 에 `ITSAppUsesNonExemptEncryption = NO` (표준 암호화만 사용) 가 선언돼 있어 빌드마다 수출 규정 질문에 답하지 않아도 된다. 앱이 자체 암호화를 쓰게 되면 이 값을 다시 검토한다.

### GitHub Secrets

저장소 Settings → Secrets and variables → Actions. 비밀번호·키 파일은 `gh secret set` 으로 직접 넣는다. (값을 채팅/문서에 붙여 넣지 말 것)

| 이름 | 내용 | 사용처 |
|---|---|---|
| `LOCAL_PROPERTIES` | `local.properties` 에서 `sdk.dir`, `kotlin.apple.cocoapods.bin` 줄을 뺀 내용 | Android, iOS |
| `GOOGLE_SERVICES_JSON` | `google-services.json` 의 base64 | Android |
| `ANDROID_KEYSTORE_BASE64` | 업로드 키스토어 `.jks` 의 base64 | Android |
| `ANDROID_KEYSTORE_PASSWORD` | 키스토어 비밀번호 | Android |
| `ANDROID_KEY_ALIAS` | 키 alias | Android |
| `ANDROID_KEY_PASSWORD` | 키 비밀번호 | Android |
| `PLAY_STORE_JSON_KEY` | 서비스 계정 JSON 키의 base64 | Android |
| `ASC_KEY_P8_BASE64` | App Store Connect API 키 `.p8` 의 base64 | iOS |
| `ASC_KEY_ID` | API 키 Key ID | iOS |
| `ASC_ISSUER_ID` | API 키 Issuer ID | iOS |

```bash
grep -vE '^(sdk\.dir|kotlin\.apple\.cocoapods\.bin)=' local.properties | gh secret set LOCAL_PROPERTIES
base64 -i /경로/upload.jks | gh secret set ANDROID_KEYSTORE_BASE64
gh secret set ANDROID_KEYSTORE_PASSWORD   # 프롬프트에 입력
base64 -i /경로/play-deploy.json | gh secret set PLAY_STORE_JSON_KEY
base64 -i /경로/AuthKey_XXXXXXXXXX.p8 | gh secret set ASC_KEY_P8_BASE64
```

## 로컬에서 같은 레인 실행하기

```bash
bundle install
export LC_ALL=en_US.UTF-8 LANG=en_US.UTF-8

# Android
export PLAY_STORE_JSON_KEY_PATH=/경로/play-deploy.json
export ANDROID_KEYSTORE_PATH=/경로/upload.jks ANDROID_KEYSTORE_PASSWORD=... ANDROID_KEY_ALIAS=... ANDROID_KEY_PASSWORD=...
bundle exec fastlane android deploy track:internal

# iOS
export ASC_KEY_PATH=/경로/AuthKey_XXXXXXXXXX.p8 ASC_KEY_ID=... ASC_ISSUER_ID=...
bundle exec fastlane ios beta      # TestFlight
bundle exec fastlane ios release   # App Store 심사 제출
```

`ANDROID_KEYSTORE_PATH` 가 없으면 Gradle 은 release 서명 설정을 붙이지 않는다. Android Studio 의 Generate Signed Bundle 흐름은 예전과 똑같이 쓸 수 있다.

## 문제 해결

- **`versionCode 를 읽지 못했습니다`**: 서비스 계정이 Play Console 에 초대되지 않았거나 권한이 아직 반영되지 않았다.
- **`이미 프로덕션에 출시돼 있습니다` / `App Store 출시 버전 이하입니다`**: 버전 이름을 안 올리고 배포했다. 버전을 올려 다시 푸시한다.
- **iOS `No signing certificate "iOS Distribution" found` / 프로파일 오류**: API 키 역할이 Admin 이 아닌 경우가 대부분이다.
- **iOS 심사 제출 단계 실패**: `release_notes.txt` 가 비었거나, 이전 버전이 아직 심사 중인 경우가 많다. App Store Connect 에서 상태를 확인한다.
- **`Xcode_26.2.app` 을 찾을 수 없음**: GitHub 러너에서 해당 Xcode 가 빠졌다. `release-ios.yml` 의 경로를 러너에 있는 버전으로 올린다.
- **iOS 빌드가 Gradle 단계에서 `NullPointerException`**: `LOCAL_PROPERTIES` 에 kakao/naver/google 키가 빠졌다.
- **한쪽 플랫폼만 실패**: 두 워크플로는 독립이다. 실패한 쪽만 Actions 에서 Re-run 하면 된다. 빌드 번호는 스토어 조회 기준이라 재실행해도 겹치지 않는다.
