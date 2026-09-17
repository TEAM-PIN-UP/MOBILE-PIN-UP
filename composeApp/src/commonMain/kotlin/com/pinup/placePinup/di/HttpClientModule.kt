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
import com.pinup.placePinup.platform.isDebugBuild
import com.pinup.placePinup.remote.api.AuthApi
import com.pinup.placePinup.remote.api.createAuthApi
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
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
    single {
        val client = HttpClient {
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
                    refreshTokens {
                        val refreshToken = runBlocking { membersLocalDataSource.getRefreshToken() }
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
                val accessToken = runBlocking { membersLocalDataSource.getAccessToken() }
                headers {
                    append("Authorization", "Bearer $accessToken")
                }
                contentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))
            }
        }
        if (isDebugBuild()) logRequestBody(client)

        val json = Json {
            ignoreUnknownKeys = true
        }
        Ktorfit.Builder()
            .baseUrl("http://101.79.30.115:8080/")
            .httpClient(client)
            .converterFactories(PResultConverterFactory(json))
            .build()
    }
}

// 디버그 빌드에서만 요청 body 를 남긴다. (응답 body 는 PResultConverterFactory 의 kLog 가 남김)
// Ktor Logging 의 BODY/ALL 레벨은 3.0.3 에서 응답 body 관찰 중 호출이 끝나지 않아 앱이 스플래시에서 멈추므로 쓰지 않는다.
private fun logRequestBody(client: HttpClient) {
    client.plugin(HttpSend).intercept { request ->
        (request.body as? TextContent)?.let {
            hLog("REQUEST BODY: ${request.method.value} ${request.url.buildString()}\n${it.text}")
        }
        execute(request)
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