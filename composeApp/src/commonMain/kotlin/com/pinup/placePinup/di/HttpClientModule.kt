package com.pinup.placePinup.di

import com.pinup.placePinup.data.local.MembersLocalDataSource
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.domain.model.FailState
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.TokenInfo
import com.pinup.placePinup.domain.model.getSuccessOrNull
import com.pinup.placePinup.domain.model.mapSuccessData
import com.pinup.placePinup.event.LogoutEventBus
import com.pinup.placePinup.platform.hLog
import com.pinup.placePinup.remote.api.AuthApi
import com.pinup.placePinup.remote.api.createAuthApi
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.withCharset
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.charsets.Charsets
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import org.koin.dsl.module
import kotlin.reflect.typeOf

val httpClientModule = module {
    // HttpClient를 별도 정의로 노출한다 — 로그아웃 시 Auth 플러그인의 토큰 캐시를
    // 비우려면(clearToken) 클라이언트 인스턴스 자체에 접근할 수 있어야 한다.
    single<HttpClient> {
        HttpClient {
            val membersLocalDataSource: MembersLocalDataSource = get()
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        isLenient = true
                        encodeDefaults = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                 logger = object : Logger {
                    override fun log(message: String) {
                        hLog(message)
                    }
                 }
                level = LogLevel.HEADERS
            }

            install(Auth) {
                bearer {
                    // 토큰 적재는 Auth 플러그인이 담당한다(suspend 컨텍스트라 DataStore를 정상 await).
                    // 예전엔 defaultRequest에서 runBlocking으로 읽어 붙였는데, defaultRequest는
                    // 요청을 시작한 코루틴(대개 Main)에서 실행되므로 모든 API 호출이 메인 스레드를
                    // 디스크 I/O만큼 정지시켰다. 게다가 Auth 플러그인이 그 헤더를 지우고 자기
                    // 캐시로 덮어쓰기 때문에 로그아웃 후에도 이전 계정 토큰이 나가는 버그가 있었다.
                    loadTokens {
                        val access = membersLocalDataSource.getAccessToken()
                        if (access.isBlank()) null
                        else BearerTokens(access, membersLocalDataSource.getRefreshToken())
                    }
                    refreshTokens {
                        val refreshToken = membersLocalDataSource.getRefreshToken()
                        val authApi: AuthApi = getKtorfit().createAuthApi()
                        val response =
                            authApi.refreshToken("Bearer $refreshToken").mapSuccessData().getSuccessOrNull()
                        if (response != null) {
                            membersLocalDataSource.saveToken(
                                TokenInfo(
                                    accessToken = response.accessToken,
                                    refreshToken = response.refreshToken
                                )
                            )
                            BearerTokens(response?.accessToken!!, response.refreshToken)
                        } else {
                            LogoutEventBus.sendEvent()
                            return@refreshTokens null
                        }
                    }
                }
            }
            defaultRequest {
                // Authorization은 Auth 플러그인이 붙인다(위 loadTokens/refreshTokens).
                // 여기서 append하면 Auth가 어차피 remove 후 덮어쓰므로 토큰 소스만 이원화된다.
                contentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))
            }
        }

    }

    single {
        val json = Json {
            ignoreUnknownKeys = true
        }
        Ktorfit.Builder()
            .baseUrl("http://101.79.30.115:8080/")
            .httpClient(get<HttpClient>())
            .converterFactories(PResultConverterFactory(json))
            .build()
    }
}

fun getKtorfit(): Ktorfit {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(
                json = Json {
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    hLog(message)
                }
            }
            level = LogLevel.HEADERS
        }
        defaultRequest {
            contentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))
        }
    }
    val json = Json {
        ignoreUnknownKeys = true
    }
    return Ktorfit.Builder()
        .baseUrl("http://101.79.30.115:8080/")
        .httpClient(client)
        .converterFactories(PResultConverterFactory(json))
        .build()
}

class PResultConverterFactory(
    private val json: Json
) : Converter.Factory {
    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        if (typeData.typeInfo.type == PResult::class) {
            return object : Converter.SuspendResponseConverter<HttpResponse, Any> {
                override suspend fun convert(result: KtorfitResult): Any {
                    return when (result) {
                        is KtorfitResult.Failure -> {
                            PResult.Fail(
                                FailState.default.copy(
                                    message = result.throwable.message ?: "error"
                                )
                            )
                        }

                        is KtorfitResult.Success -> {
                            val bodyText = result.response.bodyAsText()
                            kLog(bodyText)
                            if (result.response.status.value == 200 || result.response.status.value == 201) {
                                if (typeData.typeArgs.first().typeInfo.kotlinType!! == typeOf<PResponse<Unit>>()) {
                                    PResult.Success(
                                        PResponse(
                                            status = 200,
                                            code = "",
                                            message = "",
                                            data = Unit
                                        )
                                    )
                                } else {
                                    val deserializer = json.serializersModule.serializer(typeData.typeArgs.first().typeInfo.kotlinType!!)
                                    PResult.Success(json.decodeFromString(deserializer, bodyText))
                                }
                            } else {
                                if (result.response.status.value == 403) {
                                    PResult.Fail(
                                        FailState(
                                            status = 403,
                                            code = "",
                                            message = "엑세스 토큰 필요"
                                        )
                                    )
                                } else if (result.response.status.value == 401) {
                                    PResult.Fail(
                                        FailState(
                                            status = 401,
                                            code = "",
                                            message = "엑세스 토큰 만료 갱신 요청 필요"
                                        )
                                    )
                                } else {
                                    val response =
                                        json.decodeFromString<PResponse<Nothing>>(bodyText)
                                    PResult.Fail(
                                        FailState(
                                            status = response.status,
                                            code = response.code,
                                            message = response.message
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        return null
    }
}

private fun kLog(log : String){
    val prettyPrinter = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
        isLenient = true
        ignoreUnknownKeys = true
    }

    val prettyText = runCatching {
        val element = prettyPrinter.parseToJsonElement(log)
        prettyPrinter.encodeToString(JsonElement.serializer(), element)
    }.getOrDefault(log)

    hLog(prettyText)
}