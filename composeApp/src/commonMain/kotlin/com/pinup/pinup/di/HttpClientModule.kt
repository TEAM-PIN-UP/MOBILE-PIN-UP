package com.pinup.pinup.di

import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.FailState
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.event.LogoutEventBus
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.remote.api.AuthApi
import com.pinup.pinup.remote.api.createAuthApi
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

            install(Auth) {
                bearer {
                    refreshTokens {
                        val refreshToken = runBlocking { membersLocalDataSource.getRefreshToken() }
                        val authApi: AuthApi = getKtorfit().createAuthApi()
                        val response =
                            authApi.refreshToken(refreshToken).mapSuccessData().getSuccessOrNull()
                        if (response != null) {
                            membersLocalDataSource.saveToken(
                                TokenInfo(
                                    accessToken = response.accessToken,
                                    refreshToken = response.refreshToken
                                )
                            )
                        } else {
                            LogoutEventBus.sendEvent()
                        }
                        BearerTokens(response?.accessToken!!, response.refreshToken)
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

        val json = Json {
            ignoreUnknownKeys = true
        }
        Ktorfit.Builder()
            .baseUrl("http://43.200.161.96:8080/")
            .httpClient(client)
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
//        install(Logging) {
//            logger = object : Logger {
//                override fun log(message: String) {
//                    hLog(message)
//                }
//            }
//            level = LogLevel.ALL
//        }
        defaultRequest {
            contentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))
        }
    }
    val json = Json {
        ignoreUnknownKeys = true
    }
    return Ktorfit.Builder()
        .baseUrl("https://api.kwonyonghyun.p-e.kr/")
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
                            if (result.response.status.value == 200) {
                                val deserializer = json.serializersModule.serializer(typeData.typeArgs.first().typeInfo.kotlinType!!)
                                PResult.Success(json.decodeFromString(deserializer, bodyText))
                            } else {
                                if (result.response.status.value == 403) {
                                    PResult.Fail(
                                        FailState(
                                            status = 403,
                                            code = "",
                                            message = "엑세스 토큰 필요"
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

fun kLog(log : String){
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