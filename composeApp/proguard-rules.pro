# ── Ktor + kotlinx.serialization ────────────────────────────────────────────
# Ktor ContentNegotiation은 TypeInfo의 제네릭 시그니처로 역직렬화 타입을 결정한다.
# (HttpClientModule이 kotlin.reflect.typeOf를 직접 사용)
# 이 속성이 깎이면 응답 파싱이 런타임에 전부 깨진다.
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes *Annotation*, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes RuntimeVisibleTypeAnnotations

# @Serializable 클래스의 생성된 serializer 유지
-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
    *** serializer(...);
}
-keepclasseswithmembers class ** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.pinup.placePinup.**$$serializer { *; }
-keepclassmembers class com.pinup.placePinup.** {
    *** Companion;
}

# API 응답/요청 DTO는 이름 기반 매핑이므로 필드를 보존
-keep class com.pinup.placePinup.data.response.** { *; }
-keep class com.pinup.placePinup.data.request.** { *; }
-keep class com.pinup.placePinup.domain.model.** { *; }

# ── 네이버 로그인 SDK ───────────────────────────────────────────────────────
# AAR에 consumer proguard 규칙이 비어 있고 moshi-kotlin/gson 리플렉션을 쓴다.
-keep class com.navercorp.nid.** { *; }
-keep class com.nhn.android.naverlogin.** { *; }
-dontwarn com.navercorp.nid.**

# Moshi (네이버 SDK 전이 의존)
-keep @com.squareup.moshi.JsonClass class * { *; }
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}
-dontwarn com.squareup.moshi.**

# Gson (전이 의존) — TypeToken 제네릭 정보 보존
-keep class * extends com.google.gson.TypeAdapter
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-dontwarn com.google.gson.**

# ── 카카오 SDK ─────────────────────────────────────────────────────────────
# v2-* AAR도 consumer 규칙이 비어 있고 Gson+Retrofit 리플렉션 기반이다.
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.kakao.sdk.auth.model.** { *; }
-dontwarn com.kakao.sdk.**

# ── Retrofit 2.9 (카카오·네이버 SDK 내부 전이 의존) ─────────────────────────
# retrofit 2.9.0의 consumer 규칙에는 R8 full mode(AGP 8 기본) 대응이 없다.
# full mode는 인터페이스 메서드의 제네릭 시그니처를 소거해 카카오 로그인의
# 토큰 교환(AuthApiClient) 시점에 "Call return type must be parameterized as
# Call<Foo>"로 크래시한다. retrofit 2.11이 자체 탑재한 규칙을 그대로 가져온다.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep,allowobfuscation,allowshrinking class retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-dontwarn retrofit2.**

# Gson TypeToken 제네릭 (R8 full mode에서 소거 방지 — 카카오 SDK 파싱 경로)
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# ── 네이버 지도 ────────────────────────────────────────────────────────────
# (map-sdk AAR은 자체 proguard.txt를 제공하지만 방어적으로 유지)
-dontwarn com.naver.maps.**

# ── Coroutines / Ktor 내부 ─────────────────────────────────────────────────
-dontwarn kotlinx.coroutines.**
-dontwarn io.ktor.**
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# ── R8이 참조를 찾지 못하는 선택적 의존성들 ───────────────────────────────
# (컴파일에는 없지만 라이브러리가 조건부로 참조하는 클래스들)
-dontwarn org.apache.http.**
-dontwarn org.apache.commons.**
-dontwarn com.google.api.**
-dontwarn com.google.common.**
-dontwarn io.opencensus.**
-dontwarn io.grpc.**
-dontwarn org.slf4j.**
-dontwarn java.lang.management.**
-dontwarn javax.naming.**

# 크래시 스택트레이스를 읽을 수 있도록 라인 정보 보존
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
