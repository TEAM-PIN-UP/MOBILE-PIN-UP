import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.googleServices)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")

        pod("NMapsMap")
        pod("GoogleSignIn")
        pod("KakaoSDKCommon")
        pod("KakaoSDKShare")
        pod("KakaoSDKTemplate")
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.splashscreen)
            // Koin support for Android
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            // ktor
            implementation(libs.ktor.client.okhttp)
            // naver map
            implementation(libs.naver.map.compose)
            // google login
            implementation(libs.androidx.credentials)
            implementation(libs.google.api)
            implementation(libs.googleid)
            // kakao login
            implementation(libs.kakao.user)
            // naver login
            implementation(libs.naver.oauth)
            // firebase
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.messaging)

            implementation(libs.camera.camera2)
            implementation(libs.camera.lifecycle)
            implementation(libs.camera.view)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.compose.constraintlayout)

            // image, camera
            implementation(libs.peekaboo.image.picker)
            implementation(libs.peekaboo.ui)

            // ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.negotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.logging)
            implementation(libs.ktorfit)
            //kotlinx
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            // coil
            implementation(libs.coil)
            implementation(libs.coil.network)
            // koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)
            // backHandler
            //implementation(libs.ui.backhandler)
            // datastore
            implementation(libs.androidx.data.store.core)
            // moko library(geo, permission, media)
            implementation(libs.permissions)
            implementation(libs.permissions.compose)
            implementation(libs.media.compose)
            implementation(libs.geo.compose)
            implementation(libs.file.picker)

            implementation(libs.web.view)

            // image zoomable
            implementation(libs.zoomable)
        }
        iosMain.dependencies {
            // ktor
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.pinup.placePinup"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.pinup.placePinup"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        // CI(fastlane)는 Play Console 에 올라간 가장 큰 versionCode + 1 을 -Ppinup.versionCode 로 넘긴다.
        // 로컬 빌드는 아래 기본값을 쓴다.
        versionCode = providers.gradleProperty("pinup.versionCode").orNull?.toInt() ?: 26
        versionName = "1.1.3"
        multiDexEnabled = true

        buildConfigField(
            "String",
            "NAVER_CLIENT_ID",
            getApiKey("naver.client.id")
        )

        buildConfigField(
            "String",
            "NAVER_CLIENT_SECRET",
            getApiKey("naver.client.secret")
        )

        buildConfigField(
            "String",
            "GOOGLE_CLIENT_ID",
            getApiKey("google.client.id")
        )

        buildConfigField(
            "String",
            "KAKAO_APP_KEY",
            getApiKey("kakao.app.key")
        )
        manifestPlaceholders["KAKAO_LOGIN_KEY"] = getApiKey("kakao.login.key")
        manifestPlaceholders["NATIVE_APP_KEY"] = getApiKey("kakao.app.key")
        manifestPlaceholders["NAVER_MAP_CLIENT_ID"] = getApiKey("naver.map.client.id")
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }
    // 업로드 키 서명. CI 에서만 환경 변수로 키스토어를 넘기고,
    // 값이 없으면(로컬) 서명 설정을 붙이지 않아 기존처럼 Android Studio 에서 직접 서명한다.
    val releaseKeystorePath = System.getenv("ANDROID_KEYSTORE_PATH")
    signingConfigs {
        if (releaseKeystorePath != null) {
            create("release") {
                storeFile = file(releaseKeystorePath)
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("ANDROID_KEY_ALIAS")
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        getByName("release") {
            if (releaseKeystorePath != null) {
                signingConfig = signingConfigs.getByName("release")
            }
            isDebuggable = false
            // R8 코드 축소·난독화. 켜기 전에는 dex 266,458 메서드가 무손실로 실려 나갔다
            // (참조하던 proguard-rules.pro 파일 자체가 존재하지 않아 규칙도 비어 있었다).
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

dependencies {
    debugImplementation(compose.uiTooling)
}

