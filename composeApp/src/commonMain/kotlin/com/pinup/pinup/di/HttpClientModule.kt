package com.pinup.pinup.di

import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.hLog
import com.pinup.pinup.remote.api.AuthApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.withCharset
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.charsets.Charsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val httpClientModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        isLenient = true
                        encodeDefaults = true
                    }
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        hLog(message)
                    }
                }
                level = LogLevel.BODY
            }
            install(Auth) {
                val membersLocalDataSource: MembersLocalDataSource = get()
                val accessToken = runBlocking { membersLocalDataSource.getAccessToken() }
                val refreshToken = runBlocking { membersLocalDataSource.getRefreshToken() }
                bearer {
                    loadTokens {
                        BearerTokens(accessToken, refreshToken)
                    }

                    refreshTokens {
                        val authApi: AuthApi = get()
                        val response = authApi.refreshToken(refreshToken).mapSuccessData().getSuccessOrNull()
                        if (response != null) {
                            membersLocalDataSource.saveToken(
                                TokenInfo(
                                    accessToken = response.accessToken,
                                    refreshToken = response.refreshToken
                                )
                            )
                        }
                        BearerTokens(response?.accessToken!!, response.refreshToken)
                    }
                }
            }
            defaultRequest {
                contentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))
                url("https://api.kwonyonghyun.p-e.kr/")
            }
        }
    }
}